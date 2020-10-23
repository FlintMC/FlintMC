package net.labyfy.internal.component.gui.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.labyfy.internal.component.gui.DefaultGuiController;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.glfw.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.util.function.BiFunction;

/**
 * 1.15.2 implementation of the input interceptor
 */
@Singleton
@Implement(InputInterceptor.class)
public class VersionedInputInterceptor implements InputInterceptor {
  private static double lastDrawTime = Double.MIN_VALUE;
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
    DefaultGuiController guiController = InjectionHolder.getInjectedInstance(DefaultGuiController.class);

    // We don't hook the key callback yet, possible that this will be necessary
    overrideCallback(GLFW::glfwSetKeyCallback, windowHandle, keyCallback);
    overrideCallback(GLFW::glfwSetCharModsCallback, windowHandle, (window, codepoint, mods) -> {
      guiController.safeBeginInput();
      if (!guiController.doInput(new UnicodeTypedEvent(codepoint))) {
        // The GUI controller has not handled the event, pass it on to the original callback
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
    DefaultGuiController guiController = InjectionHolder.getInjectedInstance(DefaultGuiController.class);
    VersionedInputInterceptor inputInterceptor = InjectionHolder.getInjectedInstance(VersionedInputInterceptor.class);

    inputInterceptor.cursorPosCallback = (window, xpos, ypos) -> {
      guiController.safeBeginInput();
      if (!guiController.doInput(new CursorPosChangedEvent(xpos, ypos))) {
        // The GUI controller has not handled the event, pass it on to the original callback
        cursorPosCallback.invoke(window, xpos, ypos);
      }
    };

    overrideCallback(GLFW::glfwSetCursorPosCallback, windowHandle, inputInterceptor.cursorPosCallback);

    overrideCallback(GLFW::glfwSetMouseButtonCallback, windowHandle, (window, button, action, mods) -> {
      MouseButtonEvent event;

      switch (action) {
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

      if (event != null) {
        guiController.safeBeginInput();

        if (guiController.doInput(event)) {
          // The GUI controller has handled the event, don't pass it on
          return;
        }
      }

      mouseButtonCallback.invoke(window, button, action, mods);
    });

    overrideCallback(GLFW::glfwSetScrollCallback, windowHandle, (window, xoffset, yoffset) -> {
      guiController.safeBeginInput();
      if (!guiController.doInput(new MouseScrolledEvent(xoffset, yoffset))) {
        // The GUI controller has not handled the event, pass it on to the original callback
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
    if (old != null) {
      old.free();
    }
  }

  public static void flipFrame(long windowHandle) {
    // This is a copy of the original flipFrame method, just that the glfwPollEvents() calls
    // are guarded by input event state toggles
    DefaultGuiController guiController = InjectionHolder.getInjectedInstance(DefaultGuiController.class);

    guiController.beginInput(); // Injected
    GLFW.glfwPollEvents();
    guiController.safeEndInput(); // Injected

    RenderSystem.replayQueue();
    Tessellator.getInstance().getBuffer().reset();
    GLFW.glfwSwapBuffers(windowHandle);

    guiController.beginInput(); // Injected
    GLFW.glfwPollEvents();
    guiController.safeEndInput(); // Injected
  }

  public static void limitDisplayFPS(int elapsedTicks) {
    // This is a copy of the original limitDisplayFPS method, just that the glfwWaitEventsTimeout(double) call
    // is guarded by input event state toggles
    DefaultGuiController guiController = InjectionHolder.getInjectedInstance(DefaultGuiController.class);
    double maxTime = lastDrawTime + 1.0d / (double) elapsedTicks;

    double currentTime;
    for (currentTime = GLFW.glfwGetTime(); currentTime < maxTime; currentTime = GLFW.glfwGetTime()) {
      guiController.beginInput(); // Injected
      guiController.updateMousePosition(); // Injected
      GLFW.glfwWaitEventsTimeout(maxTime - currentTime);
      guiController.safeEndInput(); // Injected
    }

    lastDrawTime = currentTime;
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

  @ClassTransform(version = "1.15.2", value = "com.mojang.blaze3d.systems.RenderSystem")
  public void transformRenderSystem(ClassTransformContext context) throws CannotCompileException, NotFoundException {
    // Overwrite the original methods with our slightly modified ones, see the next 2 functions below
    CtMethod flipFrameMethod = context.getDeclaredMethod("flipFrame", long.class);
    flipFrameMethod.setBody(
        "net.labyfy.internal.component.gui.v1_15_2.VersionedInputInterceptor.flipFrame($1);");

    CtMethod limitDisplayFPSMethod = context.getDeclaredMethod("limitDisplayFPS", int.class);
    limitDisplayFPSMethod.setBody(
        "net.labyfy.internal.component.gui.v1_15_2.VersionedInputInterceptor.limitDisplayFPS($1);");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void signalCurrentMousePosition() {
    MainWindow window = Minecraft.getInstance().getMainWindow();

    double cursorX;
    double cursorY;

    try (MemoryStack stack = MemoryStack.stackPush()) {
      DoubleBuffer cursorXPointer = stack.callocDouble(1);
      DoubleBuffer cursorYPointer = stack.callocDouble(1);

      GLFW.glfwGetCursorPos(window.getHandle(), cursorXPointer, cursorYPointer);

      cursorX = cursorXPointer.get(0);
      cursorY = cursorYPointer.get(0);
    }

    cursorPosCallback.invoke(window.getHandle(), cursorX, cursorY);
  }
}
