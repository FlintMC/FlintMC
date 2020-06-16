package net.labyfy.component.gui.v1_15_1;

import com.mojang.blaze3d.systems.RenderSystem;
import javassist.CannotCompileException;
import javassist.CtMethod;
import net.labyfy.component.gui.GuiController;
import net.labyfy.component.gui.InputInterceptor;
import net.labyfy.component.gui.event.CursorPosChanged;
import net.labyfy.component.gui.event.MouseButton;
import net.labyfy.component.gui.event.MouseScrolled;
import net.labyfy.component.gui.event.UnicodeTyped;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.glfw.*;
import org.lwjgl.system.Callback;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryStack;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.DoubleBuffer;
import java.util.function.BiFunction;

@Singleton
@Implement(InputInterceptor.class)
public class LabyInputInterceptor implements InputInterceptor {
  private static double lastDrawTime = Double.MIN_VALUE;
  private GLFWCursorPosCallbackI cursorPosCallback;

  @Inject
  private LabyInputInterceptor() {
  }

  @ClassTransform(version = "1.15.1", value = "net.minecraft.client.util.InputMappings")
  public void transformInputMappings(ClassTransformContext context) throws CannotCompileException {
    CtMethod setKeyCallbacksMethod = context.getDeclaredMethod(
        "setKeyCallbacks", long.class, GLFWKeyCallbackI.class, GLFWCharModsCallbackI.class);
    setKeyCallbacksMethod.setBody(
        "net.labyfy.component.gui.v1_15_1.LabyInputInterceptor.interceptKeyboardCallbacks($$);");

    CtMethod setMouseCallbacksMethod = context.getDeclaredMethod(
        "setMouseCallbacks",
        long.class,
        GLFWCursorPosCallbackI.class,
        GLFWMouseButtonCallbackI.class,
        GLFWScrollCallbackI.class
    );
    setMouseCallbacksMethod.setBody(
        "net.labyfy.component.gui.v1_15_1.LabyInputInterceptor.interceptMouseCallbacks($$);");
  }

  public static void interceptKeyboardCallbacks(
      long windowHandle,
      GLFWKeyCallbackI keyCallback,
      GLFWCharModsCallbackI charModsCallback
  ) {
    GuiController guiController = InjectionHolder.getInjectedInstance(GuiController.class);

    overrideCallback(GLFW::glfwSetKeyCallback, windowHandle, keyCallback);
    overrideCallback(GLFW::glfwSetCharModsCallback, windowHandle, (window, codepoint, mods) -> {
      if (!guiController.doInput(new UnicodeTyped(codepoint))) {
        charModsCallback.invoke(window, codepoint, mods);
      }
    });
  }

  public static void interceptMouseCallbacks(
      long windowHandle,
      GLFWCursorPosCallbackI cursorPosCallback,
      GLFWMouseButtonCallbackI mouseButtonCallback,
      GLFWScrollCallbackI scrollCallback
  ) {
    GuiController guiController = InjectionHolder.getInjectedInstance(GuiController.class);
    LabyInputInterceptor inputInterceptor = InjectionHolder.getInjectedInstance(LabyInputInterceptor.class);

    inputInterceptor.cursorPosCallback = (window, xpos, ypos) -> {
      if (!guiController.doInput(new CursorPosChanged(xpos, ypos))) {
        cursorPosCallback.invoke(window, xpos, ypos);
      }
    };

    overrideCallback(GLFW::glfwSetCursorPosCallback, windowHandle, inputInterceptor.cursorPosCallback);

    overrideCallback(GLFW::glfwSetMouseButtonCallback, windowHandle, (window, button, action, mods) -> {
      MouseButton event;

      switch (action) {
        case GLFW.GLFW_PRESS:
          event = new MouseButton(MouseButton.State.PRESS, button);
          break;

        case GLFW.GLFW_RELEASE:
          event = new MouseButton(MouseButton.State.RELEASE, button);
          break;

        case GLFW.GLFW_REPEAT:
          event = new MouseButton(MouseButton.State.REPEAT, button);
          break;

        default:
          event = null;
          break;
      }

      if (event != null && guiController.doInput(event)) {
        return;
      }

      mouseButtonCallback.invoke(window, button, action, mods);
    });

    overrideCallback(GLFW::glfwSetScrollCallback, windowHandle, (window, xoffset, yoffset) -> {
      if (!guiController.doInput(new MouseScrolled(xoffset, yoffset))) {
        scrollCallback.invoke(window, xoffset, yoffset);
      }
    });
  }

  private static <T extends Callback, C extends CallbackI> void overrideCallback(
      BiFunction<Long, C, T> setter, long windowHandle, C value) {
    T old = setter.apply(windowHandle, value);
    if (old != null) {
      old.free();
    }
  }

  @ClassTransform(version = "1.15.1", value = "com.mojang.blaze3d.systems.RenderSystem")
  public void transformRenderSystem(ClassTransformContext context) throws CannotCompileException {
    CtMethod flipFrameMethod = context.getDeclaredMethod("flipFrame", long.class);
    flipFrameMethod.setBody(
        "net.labyfy.component.gui.v1_15_1.LabyInputInterceptor.flipFrame($1);");

    CtMethod limitDisplayFPSMethod = context.getDeclaredMethod("limitDisplayFPS", int.class);
    limitDisplayFPSMethod.setBody(
        "net.labyfy.component.gui.v1_15_1.LabyInputInterceptor.limitDisplayFPS($1);");
  }

  public static void flipFrame(long windowHandle) {
    GuiController guiController = InjectionHolder.getInjectedInstance(GuiController.class);

    guiController.beginInput();
    GLFW.glfwPollEvents();
    guiController.endInput();

    RenderSystem.replayQueue();
    Tessellator.getInstance().getBuffer().reset();
    GLFW.glfwSwapBuffers(windowHandle);

    guiController.beginInput();
    GLFW.glfwPollEvents();
    guiController.endInput();
    guiController.inputOnlyIterationDone();
  }

  public static void limitDisplayFPS(int elapsedTicks) {
    GuiController guiController = InjectionHolder.getInjectedInstance(GuiController.class);
    double maxTime = lastDrawTime + 1.0d / (double) elapsedTicks;

    double currentTime;
    for (currentTime = GLFW.glfwGetTime(); currentTime < maxTime; currentTime = GLFW.glfwGetTime()) {
      guiController.beginInput();
      guiController.updateMousePosition();
      GLFW.glfwWaitEventsTimeout(maxTime - currentTime);
      guiController.endInput();
      guiController.inputOnlyIterationDone();
    }

    lastDrawTime = currentTime;
  }

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
