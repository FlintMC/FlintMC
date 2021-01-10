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

package net.flintmc.render.gui.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.DoubleBuffer;
import javassist.CannotCompileException;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.InputInterceptor;
import net.flintmc.render.gui.v1_15_2.glfw.VersionedGLFWCallbacks;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharModsCallbackI;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.system.MemoryStack;

/**
 * 1.15.2 implementation of the input interceptor
 */
@Singleton
@Implement(value = InputInterceptor.class, version = "1.15.2")
public class VersionedInputInterceptor implements InputInterceptor {

  private final InjectedFieldBuilder.Factory fieldBuilderFactory;
  private final VersionedGLFWCallbacks callbacks;
  private GLFWCursorPosCallbackI cursorPosCallback;

  @Inject
  private VersionedInputInterceptor(
      InjectedFieldBuilder.Factory fieldBuilderFactory, VersionedGLFWCallbacks callbacks) {
    this.fieldBuilderFactory = fieldBuilderFactory;
    this.callbacks = callbacks;
  }

  /**
   * Called from injected code, see below. The parameters match the hooked function
   */
  public void interceptKeyboardCallbacks(
      long minecraftWindowHandle,
      GLFWKeyCallbackI keyCallback,
      GLFWCharModsCallbackI charModsCallback) {
    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetKeyCallback,
        minecraftWindowHandle,
        (window, key, scancode, action, mods) -> {
          if (!this.callbacks.keyCallback(window, key, scancode, action, mods)) {
            // The window manager has not handled the event, pass it on to the original callback
            keyCallback.invoke(window, key, scancode, action, mods);
          }
        });

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetCharModsCallback,
        minecraftWindowHandle,
        (window, codepoint, mods) -> {
          if (!this.callbacks.charModsCallback(window, codepoint, mods)) {
            // The window manager has not handled the event, pass it on to the original callback
            charModsCallback.invoke(window, codepoint, mods);
          }
        });
  }

  /**
   * Called from injected code, see below. The parameters match the hooked function.
   */
  public void interceptMouseCallbacks(
      long minecraftWindowHandle,
      GLFWCursorPosCallbackI cursorPosCallback,
      GLFWMouseButtonCallbackI mouseButtonCallback,
      GLFWScrollCallbackI scrollCallback) {
    this.cursorPosCallback =
        (window, x, y) -> {
          if (!this.callbacks.cursorPosCallback(window, x, y)) {
            // The window manager has not handled the event, pass it on to the original callback
            cursorPosCallback.invoke(window, x, y);
          }
        };

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetCursorPosCallback, minecraftWindowHandle, this.cursorPosCallback);

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetMouseButtonCallback,
        minecraftWindowHandle,
        (window, button, action, mods) -> {
          if (!this.callbacks.mouseButtonCallback(window, button, action, mods)) {
            // The window manager has not handled the event, pass it on to the original callback
            mouseButtonCallback.invoke(window, button, action, mods);
          }
        });

    VersionedGLFWCallbacks.overrideCallback(
        GLFW::glfwSetScrollCallback,
        minecraftWindowHandle,
        (window, x, y) -> {
          if (!this.callbacks.scrollCallback(window, x, y)) {
            // The window manager has not handled the event, pass it on to the original callback
            scrollCallback.invoke(window, x, y);
          }
        });
  }

  @ClassTransform(version = "1.15.2", value = "net.minecraft.client.util.InputMappings")
  public void transformInputMappings(ClassTransformContext context)
      throws CannotCompileException, NotFoundException {
    CtField injectedField =
        this.fieldBuilderFactory
            .create()
            .target(context.getCtClass())
            .inject(super.getClass())
            .generate();

    String fieldName = injectedField.getName();

    CtMethod setKeyCallbacksMethod =
        context.getDeclaredMethod(
            "setKeyCallbacks", long.class, GLFWKeyCallbackI.class, GLFWCharModsCallbackI.class);
    setKeyCallbacksMethod.setBody(String.format("%s.interceptKeyboardCallbacks($$);", fieldName));

    CtMethod setMouseCallbacksMethod =
        context.getDeclaredMethod(
            "setMouseCallbacks",
            long.class,
            GLFWCursorPosCallbackI.class,
            GLFWMouseButtonCallbackI.class,
            GLFWScrollCallbackI.class);
    setMouseCallbacksMethod.setBody(String.format("%s.interceptMouseCallbacks($$);", fieldName));
  }

  @ClassTransform(value = "net.minecraft.client.MainWindow", version = "1.15.2")
  public void hookMainWindowConstructor(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    CtField injectedField =
        this.fieldBuilderFactory
            .create()
            .target(context.getCtClass())
            .inject(VersionedGLFWCallbacks.class)
            .generate();
    String fieldName = injectedField.getName();

    context
        .getDeclaredMethod("onFramebufferSizeUpdate", long.class, int.class, int.class)
        .insertBefore(String.format("if (%s.framebufferSizeCallback($$)) { return; }", fieldName));

    context
        .getDeclaredMethod("onWindowPosUpdate", long.class, int.class, int.class)
        .insertBefore(String.format("if (%s.windowPosCallback($$)) { return; }", fieldName));

    context
        .getDeclaredMethod("onWindowSizeUpdate", long.class, int.class, int.class)
        .insertBefore(String.format("if (%s.windowSizeCallback($$)) { return; }", fieldName));

    context
        .getDeclaredMethod("onWindowFocusUpdate", long.class, boolean.class)
        .insertBefore(String.format("if (%s.windowFocusCallback($$)) { return; }", fieldName));
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

    this.cursorPosCallback.invoke(window.getHandle(), cursorX, cursorY);
  }
}
