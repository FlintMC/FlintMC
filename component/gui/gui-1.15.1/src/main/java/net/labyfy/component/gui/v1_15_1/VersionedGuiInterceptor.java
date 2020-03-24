package net.labyfy.component.gui.v1_15_1;

import net.labyfy.base.structure.representation.Type;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.component.transform.hook.HookFilter;
import net.labyfy.component.transform.hook.HookFilters;
import net.minecraft.client.gui.screen.Screen;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class VersionedGuiInterceptor {

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      methodName = "init")
  @HookFilter(
      value = HookFilters.SUBCLASS_OF,
      type = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  public void hookInit(Hook.ExecutionTime executionTime, @Named("instance") Object screen) {
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      methodName = "render",
      parameters = {
        @Type(reference = int.class),
        @Type(reference = int.class),
        @Type(reference = float.class)
      })
  @HookFilter(
      value = HookFilters.SUBCLASS_OF,
      type = @Type(typeName = "net.minecraft.client.gui.screen.Screen"))
  public void hookRender(Hook.ExecutionTime executionTime) {

  }
}
