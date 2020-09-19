package net.labyfy.internal.component.gui.v1_15_2;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.gui.event.ScreenChangedEvent;
import net.labyfy.component.gui.screen.ScreenNameMapper;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.MethodMapping;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.type.Type;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.javassist.CtClassFilters;
import net.labyfy.internal.component.gui.windowing.DefaultWindowManager;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 1.15.2 Implementation of the gui interceptor
 */
@Singleton
@AutoLoad
public class VersionedGuiInterceptor {
  private final ClassMappingProvider mappingProvider;
  private final DefaultWindowManager windowManager;
  private final ScreenNameMapper nameMapper;

  @Inject
  private VersionedGuiInterceptor(
      ClassMappingProvider mappingProvider, DefaultWindowManager windowManager, ScreenNameMapper nameMapper) {
    this.mappingProvider = mappingProvider;
    this.windowManager = windowManager;
    this.nameMapper = nameMapper;
  }

  public static boolean preScreenRenderCallback() {
    DefaultWindowManager windowManager = InjectionHolder.getInjectedInstance(VersionedGuiInterceptor.class).windowManager;

    boolean intrusive = windowManager.isMinecraftWindowRenderedIntrusively();
    if(intrusive) {
      windowManager.renderMinecraftWindow();
    }

    return intrusive;
  }

  public static void postScreenRenderCallback() {
    DefaultWindowManager windowManager = InjectionHolder.getInjectedInstance(VersionedGuiInterceptor.class).windowManager;
    windowManager.renderMinecraftWindow();
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.AFTER, Hook.ExecutionTime.BEFORE},
      className = "net.minecraft.client.gui.IngameGui",
      methodName = "renderGameOverlay",
      parameters = @Type(reference = float.class)
  )
  public void hookIngameRender(Hook.ExecutionTime executionTime) {
    if (executionTime == Hook.ExecutionTime.AFTER) {
      postScreenRenderCallback();
    }
  }

  @ClassTransform
  @CtClassFilter(className = "net.minecraft.client.gui.screen.Screen", value = CtClassFilters.SUBCLASS_OF)
  private void hookScreenRender(ClassTransformContext context) throws CannotCompileException {
    MethodMapping renderMapping = mappingProvider
        .get("net.minecraft.client.gui.IRenderable")
        .getMethod("render", int.class, int.class, float.class);

    CtClass screenClass = context.getCtClass();
    for (CtMethod method : screenClass.getDeclaredMethods()) {
      if (!method.getName().equals(renderMapping.getName())) {
        continue;
      }

      /*
       * Adjustment of the render method:
       *
       * if(preRenderCallback() {
       *   return;
       * }
       * [... original method ...]
       * postRenderCallback();
       */

      method.insertBefore(
          "if(net.labyfy.internal.component.gui.v1_15_2.VersionedGuiInterceptor.preScreenRenderCallback()) {" +
              "   return;" +
              "}"
      );

      method.insertAfter(
          "net.labyfy.internal.component.gui.v1_15_2.VersionedGuiInterceptor.postScreenRenderCallback();");

      break;
    }
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.AFTER,
      version = "1.15.2"
  )
  public void hookScreenChanged() {
    windowManager.fireEvent(
        -1, new ScreenChangedEvent(nameMapper.fromObject(Minecraft.getInstance().currentScreen)));
  }
}
