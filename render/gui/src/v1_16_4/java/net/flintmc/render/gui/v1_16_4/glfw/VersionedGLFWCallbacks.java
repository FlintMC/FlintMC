package net.flintmc.render.gui.v1_16_4.glfw;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.DoubleBuffer;
import java.util.function.BiFunction;
import net.flintmc.render.gui.event.CursorPosChangedEvent;
import net.flintmc.render.gui.event.FramebufferSizeEvent;
import net.flintmc.render.gui.event.KeyEvent;
import net.flintmc.render.gui.event.MouseButtonEvent;
import net.flintmc.render.gui.event.MouseScrolledEvent;
import net.flintmc.render.gui.event.UnicodeTypedEvent;
import net.flintmc.render.gui.event.WindowFocusEvent;
import net.flintmc.render.gui.event.WindowPosEvent;
import net.flintmc.render.gui.event.WindowSizeEvent;
import net.flintmc.render.gui.internal.windowing.DefaultWindowManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Callback;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryStack;

/** Utility class holding GLFW callbacks. */
@Singleton
public class VersionedGLFWCallbacks {

  private final DefaultWindowManager windowManager;

  @Inject
  private VersionedGLFWCallbacks(DefaultWindowManager windowManager) {
    this.windowManager = windowManager;
  }

  /**
   * Utility function to set a GLFW callback while also taking care of freeing it.
   *
   * @param setter The function to call setting the callback
   * @param windowHandle The window handle to operator on
   * @param value The new callback function
   * @param <T> The old callback type
   * @param <C> The new callback type
   */
  public static <T extends Callback, C extends CallbackI> void overrideCallback(
      BiFunction<Long, C, T> setter, long windowHandle, C value) {
    T old = setter.apply(windowHandle, value);
    if (old != null) {
      old.free();
    }
  }

  /**
   * Installs the callbacks on the specified GLFW window.
   *
   * @param handle The handle of the window to install the callbacks on
   */
  public void install(long handle) {
    overrideCallback(GLFW::glfwSetKeyCallback, handle, this::keyCallback);
    overrideCallback(GLFW::glfwSetCharModsCallback, handle, this::charModsCallback);
    overrideCallback(GLFW::glfwSetCursorPosCallback, handle, this::cursorPosCallback);
    overrideCallback(GLFW::glfwSetMouseButtonCallback, handle, this::mouseButtonCallback);
    overrideCallback(GLFW::glfwSetScrollCallback, handle, this::scrollCallback);
    overrideCallback(GLFW::glfwSetWindowFocusCallback, handle, this::windowFocusCallback);
    overrideCallback(GLFW::glfwSetFramebufferSizeCallback, handle, this::framebufferSizeCallback);
    overrideCallback(GLFW::glfwSetWindowPosCallback, handle, this::windowPosCallback);
    overrideCallback(GLFW::glfwSetWindowSizeCallback, handle, this::windowSizeCallback);
  }

  public boolean keyCallback(long windowHandle, int key, int scancode, int action, int mods) {
    return this.windowManager.fireEvent(windowHandle, window -> new KeyEvent(
            window,
            VersionedGLFWInputConverter.glfwKeyToFlintKey(key),
            scancode,
            VersionedGLFWInputConverter.glfwActionToFlintInputState(action),
            VersionedGLFWInputConverter.glfwModifierToFlintModifier(mods)));
  }

  public boolean charModsCallback(long windowHandle, int codepoint, int mods) {
    return this.windowManager.fireEvent(
        windowHandle,
        window ->
            new UnicodeTypedEvent(
                window, codepoint, VersionedGLFWInputConverter.glfwModifierToFlintModifier(mods)));
  }

  public boolean cursorPosCallback(long windowHandle, double x, double y) {
    return this.windowManager.fireEvent(windowHandle, window -> new CursorPosChangedEvent(window, x, y));
  }

  public boolean mouseButtonCallback(long windowHandle, int button, int action, int mods) {
    return this.windowManager.fireEvent(windowHandle, window -> {
      // GLFW does not supply mouse coordinates for click events, but they tend to be very useful
      double mouseX;
      double mouseY;

      try (MemoryStack stack = MemoryStack.stackPush()) {
        DoubleBuffer buffer = stack.callocDouble(2);

        // Request mouse position
        GLFW.glfwGetCursorPos(
                windowHandle,
                (DoubleBuffer) buffer.slice().position(0),
                (DoubleBuffer) buffer.slice().position(1));

        // Extract x and y
        mouseX = buffer.get(0);
        mouseY = buffer.get(1);
      }

      return new MouseButtonEvent(
                      window,
                      VersionedGLFWInputConverter.glfwMouseButtonToFlintKey(button),
                      VersionedGLFWInputConverter.glfwActionToFlintInputState(action),
                      mouseX,
                      mouseY,
                      VersionedGLFWInputConverter.glfwModifierToFlintModifier(mods));
    });
  }

  public boolean scrollCallback(long windowHandle, double x, double y) {
    return this.windowManager.fireEvent(windowHandle, window -> new MouseScrolledEvent(window, x, y));
  }

  public boolean windowFocusCallback(long windowHandle, boolean isFocused) {
    return this.windowManager.fireEvent(windowHandle, window -> new WindowFocusEvent(window, isFocused));
  }

  public boolean framebufferSizeCallback(long windowHandle, int width, int height) {
    return this.windowManager.fireEvent(windowHandle, window -> new FramebufferSizeEvent(window, width, height));
  }

  public boolean windowPosCallback(long windowHandle, int x, int y) {
    return this.windowManager.fireEvent(windowHandle, window -> new WindowPosEvent(window, x, y));
  }

  public boolean windowSizeCallback(long windowHandle, int width, int height) {
    return this.windowManager.fireEvent(windowHandle, window -> new WindowSizeEvent(window, width, height));
  }


}
