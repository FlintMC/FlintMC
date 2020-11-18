package net.flintmc.render.gui.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.render.gui.InputInterceptor;
import net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

/** 1.15.2 implementation of the input interceptor */
@Singleton
@Implement(InputInterceptor.class)
public class VersionedInputInterceptor implements InputInterceptor {
  private GLFWCursorPosCallbackI cursorPosCallback;

  @Inject
  private VersionedInputInterceptor() {}

  /** Called from injected code, see above. The parameters match the hooked function */
  public static void interceptKeyboardCallbacks(
      long minecraftWindowHandle,
      GLFWKeyCallbackI keyCallback,
      GLFWCharModsCallbackI charModsCallback) {
    VersionedGLFWCallbacks callbacks =
        InjectionHolder.getInjectedInstance(VersionedGLFWCallbacks.class);

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetKeyCallback,
        minecraftWindowHandle,
        (window, key, scancode, action, mods) -> {
          if (!callbacks.keyCallback(window, key, scancode, action, mods)) {
            // The window manager has not handled the event, pass it on to the original callback
            keyCallback.invoke(window, key, scancode, action, mods);
          }
        });

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetCharModsCallback,
        minecraftWindowHandle,
        (window, codepoint, mods) -> {
          if (!callbacks.charModsCallback(window, codepoint, mods)) {
            // The window manager has not handled the event, pass it on to the original callback
            charModsCallback.invoke(window, codepoint, mods);
          }
        });
  }

  /** Called from injected code, see above. The parameters match the hooked function. */
  public static void interceptMouseCallbacks(
      long minecraftWindowHandle,
      GLFWCursorPosCallbackI cursorPosCallback,
      GLFWMouseButtonCallbackI mouseButtonCallback,
      GLFWScrollCallbackI scrollCallback) {
    VersionedGLFWCallbacks callbacks =
        InjectionHolder.getInjectedInstance(VersionedGLFWCallbacks.class);
    VersionedInputInterceptor inputInterceptor =
        InjectionHolder.getInjectedInstance(VersionedInputInterceptor.class);

    inputInterceptor.cursorPosCallback =
        (window, x, y) -> {
          if (!callbacks.cursorPosCallback(window, x, y)) {
            // The window manager has not handled the event, pass it on to the original callback
            cursorPosCallback.invoke(window, x, y);
          }
        };

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetCursorPosCallback, minecraftWindowHandle, inputInterceptor.cursorPosCallback);

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetMouseButtonCallback,
        minecraftWindowHandle,
        (window, button, action, mods) -> {
          if (!callbacks.mouseButtonCallback(window, button, action, mods)) {
            // The window manager has not handled the event, pass it on to the original callback
            mouseButtonCallback.invoke(window, button, action, mods);
          }
        });

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetScrollCallback,
        minecraftWindowHandle,
        (window, x, y) -> {
          if (!callbacks.scrollCallback(window, x, y)) {
            // The window manager has not handled the event, pass it on to the original callback
            scrollCallback.invoke(window, x, y);
          }
        });
  }

  @ClassTransform(version = "1.15.2", value = "net.minecraft.client.util.InputMappings")
  public void transformInputMappings(ClassTransformContext context)
      throws CannotCompileException, NotFoundException {
    CtMethod setKeyCallbacksMethod =
        context.getDeclaredMethod(
            "setKeyCallbacks", long.class, GLFWKeyCallbackI.class, GLFWCharModsCallbackI.class);
    setKeyCallbacksMethod.setBody(
        "net.flintmc.render.gui.v1_15_2.VersionedInputInterceptor.interceptKeyboardCallbacks($$);");

    CtMethod setMouseCallbacksMethod =
        context.getDeclaredMethod(
            "setMouseCallbacks",
            long.class,
            GLFWCursorPosCallbackI.class,
            GLFWMouseButtonCallbackI.class,
            GLFWScrollCallbackI.class);
    setMouseCallbacksMethod.setBody(
        "net.flintmc.render.gui.v1_15_2.VersionedInputInterceptor.interceptMouseCallbacks($$);");
  }

  @ClassTransform(value = "net.minecraft.client.MainWindow", version = "1.15.2")
  public void hookMainWindowConstructor(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    context
        .getDeclaredMethod("onFramebufferSizeUpdate", long.class, int.class, int.class)
        .insertBefore(
            "{"
                + "net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks callbacks = "
                + "   (net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks)"
                + "   net.flintmc.framework.inject.primitive.InjectionHolder.getInjectedInstance("
                + "     net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks.class"
                + "   );"
                + "if(callbacks.framebufferSizeCallback($$)) {"
                + "   return;"
                + "}"
                + "}");

    context
        .getDeclaredMethod("onWindowPosUpdate", long.class, int.class, int.class)
        .insertBefore(
            "{"
                + "net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks callbacks = "
                + "   (net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks)"
                + "   net.flintmc.framework.inject.primitive.InjectionHolder.getInjectedInstance("
                + "     net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks.class"
                + "   );"
                + "if(callbacks.windowPosCallback($$)) {"
                + "   return;"
                + "}"
                + "}");

    context
        .getDeclaredMethod("onWindowSizeUpdate", long.class, int.class, int.class)
        .insertBefore(
            "{"
                + "net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks callbacks = "
                + "   (net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks)"
                + "   net.flintmc.framework.inject.primitive.InjectionHolder.getInjectedInstance("
                + "     net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks.class"
                + "   );"
                + "if(callbacks.windowSizeCallback($$)) {"
                + "   return;"
                + "}"
                + "}");

    context
        .getDeclaredMethod("onWindowFocusUpdate", long.class, boolean.class)
        .insertBefore(
            "{"
                + "net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks callbacks = "
                + "   (net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks)"
                + "   net.flintmc.framework.inject.primitive.InjectionHolder.getInjectedInstance("
                + "     net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks.class"
                + "   );"
                + "if(callbacks.windowFocusCallback($$)) {"
                + "   return;"
                + "}"
                + "}");
  }

  /** {@inheritDoc} */
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