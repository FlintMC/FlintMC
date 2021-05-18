/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.render.gui.v1_16_5.windowing;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.event.WindowRenderEvent;
import net.flintmc.render.gui.input.InputState;
import net.flintmc.render.gui.input.Key;
import net.flintmc.render.gui.input.ModifierKey;
import net.flintmc.render.gui.internal.windowing.DefaultWindowManager;
import net.flintmc.render.gui.v1_16_5.VersionedInputInterceptor;
import net.flintmc.render.gui.v1_16_5.glfw.VersionedGLFWCallbacks;
import net.flintmc.render.gui.v1_16_5.glfw.VersionedGLFWInputConverter;
import net.flintmc.render.gui.windowing.MinecraftWindow;
import net.flintmc.render.gui.windowing.WindowRenderer;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

@Singleton
@Implement(MinecraftWindow.class)
public class VersionedMinecraftWindow extends VersionedWindow implements MinecraftWindow {

  private final VersionedInputInterceptor inputInterceptor;
  private final VersionedGLFWCallbacks callbacks;
  private final List<WindowRenderer> intrusiveRenderers;

  @Inject
  private VersionedMinecraftWindow(
      DefaultWindowManager windowManager,
      EventBus eventBus,
      VersionedInputInterceptor inputInterceptor,
      VersionedGLFWCallbacks callbacks) {
    super(Minecraft.getInstance().getMainWindow().getHandle(), windowManager, eventBus);

    this.inputInterceptor = inputInterceptor;
    this.callbacks = callbacks;

    this.intrusiveRenderers = new ArrayList<>();
  }

  @Override
  public void close() {
    // The implementation actually destroys the window, for the Minecraft window we only want to
    // mark
    // to signal the close operation and let the game itself handle the close
    glfwSetWindowShouldClose(ensureHandle(), true);

    // Clean up intrusive renderers, the others will be cleaned up by the close method
    glfwMakeContextCurrent(ensureHandle());
    for (WindowRenderer renderer : intrusiveRenderers) {
      renderer.cleanup();
    }
    close(false);
  }

  @Override
  public void addRenderer(WindowRenderer renderer) {
    if (renderer.isIntrusive()) {
      intrusiveRenderers.add(renderer);
    } else {
      renderers.add(renderer);
    }

    glfwMakeContextCurrent(ensureHandle());
    renderer.initialize();
  }

  @Override
  public boolean removeRenderer(WindowRenderer renderer) {
    List<WindowRenderer> lookupList = renderer.isIntrusive() ? intrusiveRenderers : renderers;

    if (!lookupList.remove(renderer)) {
      // Slow case: renderer is not found in the correct state list, maybe the intrusive state has
      // changed,
      //            check the other list
      lookupList = renderer.isIntrusive() ? renderers : intrusiveRenderers;
      if (!lookupList.remove(renderer)) {
        // Renderer is not attached
        return false;
      }
    }

    // Perform detach
    glfwMakeContextCurrent(ensureHandle());
    renderer.cleanup();
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScaleFactor() {
    return (int) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWidth() {
    return Minecraft.getInstance().getMainWindow().getWidth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHeight() {
    return Minecraft.getInstance().getMainWindow().getHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getScaledWidth() {
    return Minecraft.getInstance().getMainWindow().getScaledWidth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getScaledHeight() {
    return Minecraft.getInstance().getMainWindow().getScaledHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFramebufferWidth() {
    return Minecraft.getInstance().getFramebuffer().framebufferWidth;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFramebufferHeight() {
    return Minecraft.getInstance().getFramebuffer().framebufferHeight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFPS() {
    return ((MinecraftFpsShadow) Minecraft.getInstance()).getFPS();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isIngame() {
    return Minecraft.getInstance().world != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireKeyEvent(Key key, InputState state) {
    this.fireKeyEvent(key, state, EnumSet.noneOf(ModifierKey.class));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void fireKeyEvent(Key key, InputState state, Set<ModifierKey> modifierKeys) {
    long window = this.ensureHandle();
    int action = VersionedGLFWInputConverter.flintInputStateToGlfwAction(state);
    int modifiers = VersionedGLFWInputConverter.flintModifierToGlfwModifier(modifierKeys);

    if (key.isMouse()) {
      GLFWMouseButtonCallbackI minecraftMouseButtonCallback =
          this.inputInterceptor.getMinecraftMouseButtonCallback();

      if (minecraftMouseButtonCallback == null) {
        throw new IllegalStateException(
            "Cannot fire mouse event before Minecraft has been initialized");
      }

      if (this.callbacks.mouseButtonCallback(window, key.getKey(), action, modifiers)) {
        // The window manager has handled the event and it has been cancelled,
        // don't pass it on to the original callback
        return;
      }

      minecraftMouseButtonCallback.invoke(this.ensureHandle(), key.getKey(), action, modifiers);
    } else {
      GLFWKeyCallbackI minecraftKeyCallback = this.inputInterceptor.getMinecraftKeyCallback();
      if (minecraftKeyCallback == null) {
        throw new IllegalStateException(
            "Cannot fire key event before Minecraft has been initialized");
      }

      if (this.callbacks.keyCallback(window, key.getKey(), key.getScanCode(), action, modifiers)) {
        // The window manager has handled the event and it has been cancelled,
        // don't pass it on to the original callback
        return;
      }

      minecraftKeyCallback.invoke(window, key.getKey(), key.getScanCode(), action, modifiers);
    }
  }

  /**
   * Determines if the window is currently rendered intrusively.
   *
   * @return {@code true} if the window is rendered intrusively, {@code false} otherwise
   */
  @Override
  public boolean isRenderedIntrusively() {
    return !intrusiveRenderers.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void render() {
    if (handle == 0) {
      // Minecraft might call this method once more time even after the window has been closed
      // already
      return;
    }

    glfwMakeContextCurrent(ensureHandle());

    // Render all intrusive renderers first
    for (WindowRenderer renderer : intrusiveRenderers) {
      WindowRenderEvent windowRenderEvent = new WindowRenderEvent(this, renderer);
      this.eventBus.fireEvent(windowRenderEvent, Subscribe.Phase.PRE);
      renderer.render();
      this.eventBus.fireEvent(windowRenderEvent, Subscribe.Phase.POST);
    }

    // Follow with other renderers
    for (WindowRenderer renderer : renderers) {
      WindowRenderEvent windowRenderEvent = new WindowRenderEvent(this, renderer);
      this.eventBus.fireEvent(windowRenderEvent, Subscribe.Phase.PRE);
      renderer.render();
      this.eventBus.fireEvent(windowRenderEvent, Subscribe.Phase.POST);
    }
  }
}
