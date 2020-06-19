package net.labyfy.component.gui.v1_15_2;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.representation.Type;
import net.labyfy.component.gui.GuiController;
import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.MethodMapping;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.javassist.CtClassFilters;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@AutoLoad
public class LabyGuiInterceptor {
  private final ClassMappingProvider mappingProvider;
  private final GuiController controller;

  @Inject
  private LabyGuiInterceptor(ClassMappingProvider mappingProvider, GuiController controller) {
    this.mappingProvider = mappingProvider;
    this.controller = controller;
  }

  @ClassTransform
  @CtClassFilter(className = "net.minecraft.client.gui.screen.Screen", value = CtClassFilters.SUBCLASS_OF)
  private void hookScreenRender(ClassTransformContext context) throws CannotCompileException {
    MethodMapping renderMapping = mappingProvider
        .get("net.minecraft.client.gui.IRenderable")
        .getMethod("render", int.class, int.class, float.class);

    CtClass screenClass = context.getCtClass();
    for(CtMethod method : screenClass.getDeclaredMethods()) {
      if(!method.getName().equals(renderMapping.getName())) {
        continue;
      }

      method.insertBefore(
          "if(net.labyfy.component.gui.v1_15_2.LabyGuiInterceptor.preScreenRenderCallback($$)) {" +
              "   net.labyfy.component.gui.v1_15_2.LabyGuiInterceptor.postScreenRenderCallback(true, $$);" +
              "   return;" +
              "}"
      );

      method.insertAfter(
          "net.labyfy.component.gui.v1_15_2.LabyGuiInterceptor.postScreenRenderCallback(false, $$);");

      break;
    }
  }

  public static boolean preScreenRenderCallback(int mouseX, int mouseY, float partialTick) {
    GuiController controller = InjectionHolder.getInjectedInstance(LabyGuiInterceptor.class).controller;

    RenderExecution execution = new RenderExecution(
        mouseX,
        mouseY,
        partialTick
    );

    controller.screenRenderCalled(
        Hook.ExecutionTime.BEFORE,
        execution
    );

    return execution.getCancellation().isCancelled();
  }

  public static void postScreenRenderCallback(boolean isCancelled, int mouseX, int mouseY, float partialTick) {
    GuiController controller = InjectionHolder.getInjectedInstance(LabyGuiInterceptor.class).controller;

    RenderExecution execution = new RenderExecution(
        isCancelled,
        mouseX,
        mouseY,
        partialTick
    );

    controller.screenRenderCalled(
        Hook.ExecutionTime.AFTER,
        execution
    );
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.AFTER,
      version = "1.15.2"
  )
  public void hookScreenChanged() {
    controller.safeEndInput();
    controller.screenChanged(Minecraft.getInstance().currentScreen);
  }

  @Hook(
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "updateCameraAndRender",
      executionTime = Hook.ExecutionTime.AFTER,
      parameters = {
          @Type(reference = float.class),
          @Type(reference = long.class),
          @Type(reference = boolean.class)
      },
      version = "1.15.2"
  )
  public void hookRender() {
    controller.endFrame();
  }
}
