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

package net.flintmc.render.gui.v1_15_2.windowing;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.event.WindowRenderEvent;
import net.flintmc.render.gui.input.Key;
import net.flintmc.render.gui.internal.windowing.DefaultWindowManager;
import net.flintmc.render.gui.internal.windowing.InternalWindow;
import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks;
import net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWInputConverter;
import net.flintmc.render.gui.windowing.MinecraftWindow;
import net.flintmc.render.gui.windowing.Window;
import net.flintmc.render.gui.windowing.WindowRenderer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * 1.15.2 implementation for {@link Window}.
 */
@Implement(Window.class)
public class VersionedWindow implements InternalWindow {

  protected final EventBus eventBus;

  protected final List<WindowRenderer> renderers;
  protected final DefaultWindowManager windowManager;

  private final Set<Key> pressedKeys;

  protected long handle;

  /**
   * Creates a new OpenGL window using GLFW. Constructor for assisted factory at {@link
   * Window.Factory#create(String, int, int)}.
   *
   * @param title           The title of new the window
   * @param width           The width of new the window
   * @param height          The height of the new window
   * @param minecraftWindow The main minecraft window, used to derive the context
   * @param windowManager   The window manager of this Flint instance
   * @param callbacks       The callbacks to install on the window
   */
  @AssistedInject
  public VersionedWindow(
      @Assisted("title") String title,
      @Assisted("width") int width,
      @Assisted("height") int height,
      MinecraftWindow minecraftWindow,
      DefaultWindowManager windowManager,
      VersionedGLFWCallbacks callbacks,
      EventBus eventBus) {
    this.renderers = new ArrayList<>();
    this.pressedKeys = new HashSet<>();

    this.windowManager = windowManager;
    this.eventBus = eventBus;
    this.handle = glfwCreateWindow(width, height, title, 0, minecraftWindow.getHandle());

    callbacks.install(handle);
    windowManager.registerWindow(this);
  }


  /**
   * Consumes an existing GLFW OpenGL window. Constructor for assisted factory at {@link Window.Factory#create(long)}.
   *
   * @param windowHandle  the glfw window handle to use
   * @param windowManager The window manager of this Flint instance
   * @param callbacks     The callbacks to install on the window
   */
  @AssistedInject
  public VersionedWindow(
      @Assisted long windowHandle,
      DefaultWindowManager windowManager,
      EventBus eventBus,
      VersionedGLFWCallbacks callbacks) {

    this.renderers = new ArrayList<>();
    this.pressedKeys = new HashSet<>();

    this.eventBus = eventBus;
    this.handle = windowHandle;
    this.windowManager = windowManager;

    callbacks.install(handle);
    windowManager.registerWindow(this);
  }

  /**
   * Wraps an existing GLFW window handle.
   *
   * <p><b>Registration of the window needs to be done by the caller or by other means!</b>
   *
   * @param handle        The GLFW handle to wrap
   * @param windowManager The window manager to unregister this window on when it is closed
   */
  protected VersionedWindow(long handle, DefaultWindowManager windowManager, EventBus eventBus) {
    this.renderers = new ArrayList<>();
    this.pressedKeys = new HashSet<>();

    this.windowManager = windowManager;
    this.handle = handle;
    this.eventBus = eventBus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRenderer(WindowRenderer renderer) {
    glfwMakeContextCurrent(ensureHandle());
    renderer.initialize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeRenderer(WindowRenderer renderer) {
    if (!renderers.remove(renderer)) {
      // If the renderer has never been added to this window, we can't clean it up
      return false;
    }

    glfwMakeContextCurrent(ensureHandle());
    renderer.cleanup();
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFocused() {
    return glfwGetWindowAttrib(ensureHandle(), GLFW_FOCUSED) == GLFW_TRUE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isKeyPressed(Key key) {
    int glfwKey = VersionedGLFWInputConverter.flintKeyToGlfwKey(key);
    if (glfwKey == GLFW.GLFW_KEY_UNKNOWN) {
      return false;
    }
    int glfwAction =
        key.isMouse()
            ? GLFW.glfwGetMouseButton(this.ensureHandle(), glfwKey)
            : GLFW.glfwGetKey(this.ensureHandle(), glfwKey);
    return glfwAction == GLFW.GLFW_PRESS;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Key> getPressedKeys() {
    return Collections.unmodifiableSet(this.pressedKeys);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Key> getMutablePressedKeys() {
    return this.pressedKeys;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isClosed() {
    return this.handle == 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getHandle() {
    return handle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWidth() {
    return getSize()[0];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHeight() {
    return getSize()[1];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float[] getSize() {
    float[] out = new float[2];

    // Allocate a memory stack
    try (MemoryStack stack = MemoryStack.stackPush()) {
      // Reserve space for 2 ints on the stack, will be deallocated automatically when the
      // try-with-resources exits
      IntBuffer buffer = stack.mallocInt(2);

      // Write the width into index 0 and the height into position 1 of the buffer
      glfwGetWindowSize(
          ensureHandle(),
          (IntBuffer) buffer.slice().position(0),
          (IntBuffer) buffer.slice().position(1));

      // Copy into output, implicitly casting from int to float
      out[0] = buffer.get(0);
      out[1] = buffer.get(1);
    }

    return out;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void close() {
    close(true);
  }

  /**
   * Overloaded version of {@link #close()} allowing more control over the close process.
   *
   * @param destroyWindow If {@code true}, the GLFW window is destroyed, if {@code false}, only the
   *                      {@link #handle} field is zeroed out
   */
  protected final void close(boolean destroyWindow) {
    // Clean up all renderers
    glfwMakeContextCurrent(ensureHandle());
    for (WindowRenderer renderer : renderers) {
      renderer.cleanup();
    }

    windowManager.unregisterWindow(this);

    if (destroyWindow) {
      glfwDestroyWindow(handle);
    }

    handle = 0;
  }

  /**
   * Ensures the handle is still valid.
   *
   * @return The window handle
   * @throws IllegalStateException If the handle is not valid anymore
   */
  protected final long ensureHandle() {
    if (handle == 0) {
      throw new IllegalStateException("The window has been closed already");
    }

    return handle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRenderedIntrusively(ScreenName screen) {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void render() {
    glfwMakeContextCurrent(ensureHandle());
    for (WindowRenderer renderer : renderers) {
      WindowRenderEvent windowRenderEvent = new WindowRenderEvent(this, renderer);
      this.eventBus.fireEvent(windowRenderEvent, Subscribe.Phase.PRE);
      renderer.render();
      this.eventBus.fireEvent(windowRenderEvent, Subscribe.Phase.POST);
    }
  }
}
