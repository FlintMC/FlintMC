package net.flintmc.render.gui.v1_16_4;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.render.gui.event.ScreenChangedEvent;
import net.flintmc.render.gui.internal.windowing.DefaultWindowManager;
import net.flintmc.render.gui.screen.ScreenNameMapper;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.CtClassFilter;
import net.flintmc.transform.javassist.CtClassFilters;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.MethodMapping;
import net.minecraft.client.Minecraft;

/** 1.16.4 Implementation of the gui interceptor */
@Singleton
public class VersionedGuiInterceptor {
  private final ClassMappingProvider mappingProvider;
  private final DefaultWindowManager windowManager;
  private final ScreenNameMapper nameMapper;

  @Inject
  private VersionedGuiInterceptor(
      ClassMappingProvider mappingProvider,
      DefaultWindowManager windowManager,
      ScreenNameMapper nameMapper) {
    this.mappingProvider = mappingProvider;
    this.windowManager = windowManager;
    this.nameMapper = nameMapper;
  }

  public static boolean preScreenRenderCallback() {
    DefaultWindowManager windowManager =
        InjectionHolder.getInjectedInstance(VersionedGuiInterceptor.class).windowManager;

    boolean intrusive = windowManager.isMinecraftWindowRenderedIntrusively();
    if (intrusive) {
      windowManager.renderMinecraftWindow();
    }

    return intrusive;
  }

  public static void postScreenRenderCallback() {
    DefaultWindowManager windowManager =
        InjectionHolder.getInjectedInstance(VersionedGuiInterceptor.class).windowManager;
    windowManager.renderMinecraftWindow();
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.AFTER, Hook.ExecutionTime.BEFORE},
      className = "net.minecraft.client.gui.IngameGui",
      methodName = "renderGameOverlay",
      parameters = @Type(reference = float.class))
  public void hookIngameRender(Hook.ExecutionTime executionTime) {
    if (executionTime == Hook.ExecutionTime.AFTER) {
      postScreenRenderCallback();
    }
  }

  @ClassTransform
  @CtClassFilter(
      className = "net.minecraft.client.gui.screen.Screen",
      value = CtClassFilters.SUBCLASS_OF)
  private void hookScreenRender(ClassTransformContext context) throws CannotCompileException {
    MethodMapping renderMapping =
        mappingProvider
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
          "if(net.flintmc.render.gui.v1_15_2.VersionedGuiInterceptor.preScreenRenderCallback()) {"
              + "   return;"
              + "}");

      method.insertAfter(
          "net.flintmc.render.gui.v1_15_2.VersionedGuiInterceptor.postScreenRenderCallback();");

      break;
    }
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.AFTER,
      version = "1.15.2")
  public void hookScreenChanged() {
    this.windowManager.fireEvent(
        -1,
        window ->
            new ScreenChangedEvent(
                window, nameMapper.fromObject(Minecraft.getInstance().currentScreen)));
  }
}
