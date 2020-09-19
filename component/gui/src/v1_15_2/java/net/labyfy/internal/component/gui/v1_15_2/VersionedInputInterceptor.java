package net.labyfy.internal.component.gui.v1_15_2;

import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.component.gui.InputInterceptor;
import net.labyfy.component.gui.event.CursorPosChangedEvent;
import net.labyfy.component.gui.event.MouseButtonEvent;
import net.labyfy.component.gui.event.MouseScrolledEvent;
import net.labyfy.component.gui.event.UnicodeTypedEvent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.internal.component.gui.windowing.DefaultWindowManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryStack;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.DoubleBuffer;
import java.util.function.BiFunction;

/**
 * 1.15.2 implementation of the input interceptor
 */
@Singleton
@Implement(InputInterceptor.class)
public class VersionedInputInterceptor implements InputInterceptor {
  private GLFWCursorPosCallbackI cursorPosCallback;

  @Inject
  private VersionedInputInterceptor() {
  }

  /**
   * Called from injected code, see above. The parameters match the hooked function
   */
  public static void interceptKeyboardCallbacks(
      long windowHandle,
      GLFWKeyCallbackI keyCallback,
      GLFWCharModsCallbackI charModsCallback
  ) {
    DefaultWindowManager windowManager = InjectionHolder.getInjectedInstance(DefaultWindowManager.class);

    // We don't hook the key callback yet, possible that this will be necessary
    overrideCallback(GLFW::glfwSetKeyCallback, windowHandle, keyCallback);
    overrideCallback(GLFW::glfwSetCharModsCallback, windowHandle, (window, codepoint, mods) -> {
      if(!windowManager.fireEvent(windowHandle, new UnicodeTypedEvent(codepoint))) {
        // The window manager has not handled the event, pass it on to the original callback
        charModsCallback.invoke(window, codepoint, mods);
      }
    });
  }

  /**
   * Called from injected code, see above. The parameters match the hooked function.
   */
  public static void interceptMouseCallbacks(
      long windowHandle,
      GLFWCursorPosCallbackI cursorPosCallback,
      GLFWMouseButtonCallbackI mouseButtonCallback,
      GLFWScrollCallbackI scrollCallback
  ) {
    DefaultWindowManager windowManager = InjectionHolder.getInjectedInstance(DefaultWindowManager.class);
    VersionedInputInterceptor inputInterceptor = InjectionHolder.getInjectedInstance(VersionedInputInterceptor.class);

    inputInterceptor.cursorPosCallback = (window, xpos, ypos) -> {
      if(!windowManager.fireEvent(windowHandle, new CursorPosChangedEvent(xpos, ypos))) {
        // The window manager has not handled the event, pass it on to the original callback
        cursorPosCallback.invoke(window, xpos, ypos);
      }
    };

    overrideCallback(GLFW::glfwSetCursorPosCallback, windowHandle, inputInterceptor.cursorPosCallback);

    overrideCallback(GLFW::glfwSetMouseButtonCallback, windowHandle, (window, button, action, mods) -> {
      MouseButtonEvent event;

      switch(action) {
        case GLFW.GLFW_PRESS:
          event = new MouseButtonEvent(MouseButtonEvent.State.PRESS, button);
          break;

        case GLFW.GLFW_RELEASE:
          event = new MouseButtonEvent(MouseButtonEvent.State.RELEASE, button);
          break;

        case GLFW.GLFW_REPEAT:
          event = new MouseButtonEvent(MouseButtonEvent.State.REPEAT, button);
          break;

        default:
          event = null;
          break;
      }

      if(event != null && windowManager.fireEvent(windowHandle, event)) {
        // The window manager has handled the event, don't pass it on
        return;
      }

      mouseButtonCallback.invoke(window, button, action, mods);
    });

    overrideCallback(GLFW::glfwSetScrollCallback, windowHandle, (window, xoffset, yoffset) -> {
      if(!windowManager.fireEvent(windowHandle, new MouseScrolledEvent(xoffset, yoffset))) {
        // The window manager has not handled the event, pass it on to the original callback
        scrollCallback.invoke(window, xoffset, yoffset);
      }
    });
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
  private static <T extends Callback, C extends CallbackI> void overrideCallback(
      BiFunction<Long, C, T> setter, long windowHandle, C value) {
    T old = setter.apply(windowHandle, value);
    if(old != null) {
      old.free();
    }
  }

  @ClassTransform(version = "1.15.2", value = "net.minecraft.client.util.InputMappings")
  public void transformInputMappings(ClassTransformContext context) throws CannotCompileException, NotFoundException {
    CtMethod setKeyCallbacksMethod = context.getDeclaredMethod(
        "setKeyCallbacks", long.class, GLFWKeyCallbackI.class, GLFWCharModsCallbackI.class);
    setKeyCallbacksMethod.setBody(
        "net.labyfy.internal.component.gui.v1_15_2.VersionedInputInterceptor.interceptKeyboardCallbacks($$);");

    CtMethod setMouseCallbacksMethod = context.getDeclaredMethod(
        "setMouseCallbacks",
        long.class,
        GLFWCursorPosCallbackI.class,
        GLFWMouseButtonCallbackI.class,
        GLFWScrollCallbackI.class
    );
    setMouseCallbacksMethod.setBody(
        "net.labyfy.internal.component.gui.v1_15_2.VersionedInputInterceptor.interceptMouseCallbacks($$);");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void signalCurrentMousePosition() {
    MainWindow window = Minecraft.getInstance().getMainWindow();

    double cursorX;
    double cursorY;

    try(MemoryStack stack = MemoryStack.stackPush()) {
      DoubleBuffer cursorXPointer = stack.callocDouble(1);
      DoubleBuffer cursorYPointer = stack.callocDouble(1);

      GLFW.glfwGetCursorPos(window.getHandle(), cursorXPointer, cursorYPointer);

      cursorX = cursorXPointer.get(0);
      cursorY = cursorYPointer.get(0);
    }

    cursorPosCallback.invoke(window.getHandle(), cursorX, cursorY);
  }
}
