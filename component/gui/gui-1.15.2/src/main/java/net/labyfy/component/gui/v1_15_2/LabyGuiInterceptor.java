package net.labyfy.component.gui.v1_15_2;

import com.google.common.collect.ImmutableMap;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.representation.Type;
import net.labyfy.component.gui.GuiInterceptor;
import net.labyfy.component.gui.GuiRenderCancellation;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.mappings.MethodMapping;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.hook.HookFilter;
import net.labyfy.component.transform.hook.HookFilters;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;
import net.labyfy.component.transform.javassist.CtClassFilter;
import net.labyfy.component.transform.javassist.CtClassFilters;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;

@Singleton
@AutoLoad
public class LabyGuiInterceptor extends GuiInterceptor {
  private final ClassMappingProvider mappingProvider;

  @Inject
  private LabyGuiInterceptor(ClassMappingProvider mappingProvider) {
    this.mappingProvider = mappingProvider;
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      methodName = "init",
      version = "1.15.2")
  @HookFilter(
          value = HookFilters.SUBCLASS_OF,
          type = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  public void hookInit(Hook.ExecutionTime executionTime, @Named("instance") Object screen) {
    this.notifyGuis(executionTime, GuiRenderState.Type.INIT, screen, Collections.emptyMap());
  }

  @Hook(
      executionTime = Hook.ExecutionTime.AFTER,
      methodName = "render",
      parameters = {
        @Type(reference = int.class),
        @Type(reference = int.class),
        @Type(reference = float.class)
      },
      version = "1.15.2")
  @HookFilter(
          value = HookFilters.SUBCLASS_OF,
          type = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  public void postRenderHookCallback(
      Hook.ExecutionTime executionTime,
      @Named("instance") Object screen,
      @Named("args") Object[] args) {
    this.notifyGuis(
        executionTime,
        GuiRenderState.Type.RENDER,
        screen,
        ImmutableMap.of("mouseX", args[0], "mouseY", args[1], "partialTick", args[2]));
  }

  public static boolean preRenderHookCallback(int mouseX, int mouseY, float partialTick, Object screen) {
    GuiRenderCancellation cancellation = new GuiRenderCancellation();
    InjectionHolder.getInjectedInstance(LabyGuiInterceptor.class).notifyGuis(
        Hook.ExecutionTime.BEFORE,
        GuiRenderState.Type.RENDER,
        screen,
        ImmutableMap.of(
            "mouseX", mouseX,
            "mouseY", mouseY,
            "partialTick", partialTick,
            "cancellation", cancellation
        )
    );

    return cancellation.shouldCancel();
  }

  @ClassTransform
  @CtClassFilter(value = CtClassFilters.SUBCLASS_OF, className = "net.minecraft.client.gui.screen.Screen")
  public void hookPreRender(ClassTransformContext context) throws NotFoundException, CannotCompileException {
    CtClass screenClass = context.getCtClass();
    MethodMapping renderMapping = mappingProvider
        .get("net.minecraft.client.gui.screen.Screen")
        .getMethod("render", int.class, int.class, float.class);

    CtMethod renderMethod = screenClass.getMethod(renderMapping.getName(), "(IIF)V");
    renderMethod.insertBefore(
        "if(net.labyfy.component.gui.v1_15_1.LabyGuiInterceptor.preRenderHookCallback($$, $0)) { return; }");
  }
}
