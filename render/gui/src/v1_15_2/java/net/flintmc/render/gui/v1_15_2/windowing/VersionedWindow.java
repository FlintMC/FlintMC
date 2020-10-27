package net.flintmc.render.gui.v1_15_2.windowing;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.event.GuiEvent;
import net.flintmc.render.gui.event.GuiEventListener;
import net.flintmc.render.gui.internal.windowing.DefaultWindowManager;
import net.flintmc.render.gui.internal.windowing.InternalWindow;
import net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks;
import net.flintmc.render.gui.windowing.MinecraftWindow;
import net.flintmc.render.gui.windowing.Window;
import net.flintmc.render.gui.windowing.WindowRenderer;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/** 1.15.2 implementation for {@link Window}. */
@Implement(Window.class)
public class VersionedWindow implements InternalWindow {
  protected final List<WindowRenderer> renderers;
  protected final List<GuiEventListener> listeners;
  protected final DefaultWindowManager windowManager;

  protected long handle;

  /**
   * Creates a new OpenGL window using GLFW. Constructor for assisted factory at {@link
   * Window.Factory#create(String, int, int)}.
   *
   * @param title The title of new the window
   * @param width The width of new the window
   * @param height The height of the new window
   * @param minecraftWindow The main minecraft window, used to derive the context
   * @param windowManager The window manager of this Labyfy instance
   * @param callbacks The callbacks to install on the window
   */
  @AssistedInject
  public VersionedWindow(
      @Assisted("title") String title,
      @Assisted("width") int width,
      @Assisted("height") int height,
      MinecraftWindow minecraftWindow,
      DefaultWindowManager windowManager,
      VersionedGLFWCallbacks callbacks) {
    this.renderers = new ArrayList<>();
    this.listeners = new ArrayList<>();
    this.windowManager = windowManager;
    this.handle = glfwCreateWindow(width, height, title, 0, minecraftWindow.getHandle());

    callbacks.install(handle);
    windowManager.registerWindow(this);
  }

  /**
   * Wraps an existing GLFW window handle.
   *
   * <p><b>Registration of the window needs to be done by the caller or by other means!</b>
   *
   * @param handle The GLFW handle to wrap
   * @param windowManager The window manager to unregister this window on when it is closed
   */
  protected VersionedWindow(long handle, DefaultWindowManager windowManager) {
    this.renderers = new ArrayList<>();
    this.listeners = new ArrayList<>();
    this.windowManager = windowManager;
    this.handle = handle;
  }

  @Override
  public void addRenderer(WindowRenderer renderer) {
    glfwMakeContextCurrent(ensureHandle());
    renderer.initialize();
  }

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

  @Override
  public void addEventListener(GuiEventListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public boolean removeEventListener(GuiEventListener listener) {
    return this.listeners.remove(listener);
  }

  @Override
  public boolean sendEvent(GuiEvent event) {
    for (GuiEventListener listener : listeners) {
      if (listener.handle(event)) {
        // Event has been handled, cancel chain
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isFocused() {
    return glfwGetWindowAttrib(ensureHandle(), GLFW_FOCUSED) == GLFW_TRUE;
  }

  @Override
  public long getHandle() {
    return handle;
  }

  @Override
  public float getWidth() {
    return getSize()[0];
  }

  @Override
  public float getHeight() {
    return getSize()[1];
  }

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

  @Override
  public void close() {
    close(true);
  }

  /**
   * Overloaded version of {@link #close()} allowing more control over the close process.
   *
   * @param destroyWindow If {@code true}, the GLFW window is destroyed, if {@code false}, only the
   *     {@link #handle} field is zeroed out
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

  @Override
  public boolean isRenderedIntrusively() {
    return false;
  }

  @Override
  public void render() {
    glfwMakeContextCurrent(ensureHandle());
    for (WindowRenderer renderer : renderers) {
      renderer.render();
    }
  }
}
