package net.labyfy.internal.component.gui.v1_15_2.glfw;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.event.*;
import net.labyfy.internal.component.gui.windowing.DefaultWindowManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Callback;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.util.function.BiFunction;

/**
 * Utility class holding GLFW callbacks.
 */
@Singleton
public class VersionedGLFWCallbacks {
  private final DefaultWindowManager windowManager;

  @Inject
  private VersionedGLFWCallbacks(DefaultWindowManager windowManager) {
    this.windowManager = windowManager;
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

  public boolean keyCallback(long window, int key, int scancode, int action, int mods) {
    KeyEvent event = new KeyEvent(
        VersionedGLFWInputConverter.glfwKeyToLabyfyKey(key),
        scancode,
        VersionedGLFWInputConverter.glfwActionToLabyfyInputState(action),
        VersionedGLFWInputConverter.glfwModifierToLabyfyModifier(mods)
    );

    return windowManager.fireEvent(window, event);
  }

  public boolean charModsCallback(long window, int codepoint, int mods) {
    UnicodeTypedEvent event = new UnicodeTypedEvent(
        codepoint,
        VersionedGLFWInputConverter.glfwModifierToLabyfyModifier(mods)
    );

    return windowManager.fireEvent(window, event);
  }

  public boolean cursorPosCallback(long window, double x, double y) {
    CursorPosChangedEvent event = new CursorPosChangedEvent(x, y);

    return windowManager.fireEvent(window, event);
  }

  public boolean mouseButtonCallback(long window, int button, int action, int mods) {
    // GLFW does not supply mouse coordinates for click events, but they tend to be very useful
    double mouseX;
    double mouseY;

    try(MemoryStack stack = MemoryStack.stackPush()) {
      DoubleBuffer buffer = stack.callocDouble(2);

      // Request mouse position
      GLFW.glfwGetCursorPos(window, (DoubleBuffer) buffer.slice().position(0), (DoubleBuffer) buffer.slice().position(1));

      // Extract x and y
      mouseX = buffer.get(0);
      mouseY = buffer.get(1);
    }

    MouseButtonEvent event = new MouseButtonEvent(
        VersionedGLFWInputConverter.glfwMouseButtonToLabyfyMouseButton(button),
        VersionedGLFWInputConverter.glfwActionToLabyfyInputState(action),
        mouseX,
        mouseY,
        VersionedGLFWInputConverter.glfwModifierToLabyfyModifier(mods)
    );

    return windowManager.fireEvent(window, event);
  }

  public boolean scrollCallback(long window, double x, double y) {
    MouseScrolledEvent event = new MouseScrolledEvent(x, y);

    return windowManager.fireEvent(window, event);
  }

  public boolean windowFocusCallback(long window, boolean isFocused) {
    WindowFocusEvent event = new WindowFocusEvent(isFocused);

    return windowManager.fireEvent(window, event);
  }

  public boolean framebufferSizeCallback(long window, int width, int height) {
    FramebufferSizeEvent event = new FramebufferSizeEvent(width, height);

    return windowManager.fireEvent(window, event);
  }

  public boolean windowPosCallback(long window, int x, int y) {
    WindowPosEvent event = new WindowPosEvent(x, y);

    return windowManager.fireEvent(window, event);
  }

  public boolean windowSizeCallback(long window, int width, int height) {
    WindowSizeEvent event = new WindowSizeEvent(width, height);

    return windowManager.fireEvent(window, event);
  }

  /**
   * Utility function to set a GLFW callback while also taking care of freeing it.
   *
   * @param setter       The function to call setting the callback
   * @param windowHandle The window handle to operator on
   * @param value        The new callback function
   * @param <T>          The old callback type
   * @param <C>          The new callback type
   */
  public static <T extends Callback, C extends CallbackI> void overrideCallback(
      BiFunction<Long, C, T> setter, long windowHandle, C value) {
    T old = setter.apply(windowHandle, value);
    if(old != null) {
      old.free();
    }
  }
}
