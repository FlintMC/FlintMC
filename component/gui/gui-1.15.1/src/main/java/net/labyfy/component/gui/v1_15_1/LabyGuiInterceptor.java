package net.labyfy.component.gui.v1_15_1;

import com.google.common.collect.ImmutableMap;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.representation.Type;
import net.labyfy.component.gui.GuiInterceptor;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.hook.HookFilter;
import net.labyfy.component.transform.hook.HookFilters;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;

@Singleton
@AutoLoad
public class LabyGuiInterceptor extends GuiInterceptor {

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      methodName = "init",
      version = "1.15.1")
  @HookFilter(
          value = HookFilters.SUBCLASS_OF,
          type = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  public void hookInit(Hook.ExecutionTime executionTime, @Named("instance") Object screen) {
    this.notifyGuis(executionTime, GuiRenderState.Type.INIT, screen, Collections.emptyMap());
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      methodName = "render",
      parameters = {
        @Type(reference = int.class),
        @Type(reference = int.class),
        @Type(reference = float.class)
      },
      version = "1.15.1")
  @HookFilter(
          value = HookFilters.SUBCLASS_OF,
          type = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  public void hookRender(
      Hook.ExecutionTime executionTime,
      @Named("instance") Object screen,
      @Named("args") Object[] args) {
    this.notifyGuis(
        executionTime,
        GuiRenderState.Type.RENDER,
        screen,
        ImmutableMap.of("mouseX", args[0], "mouseY", args[1], "partialTick", args[2]));
  }
}
