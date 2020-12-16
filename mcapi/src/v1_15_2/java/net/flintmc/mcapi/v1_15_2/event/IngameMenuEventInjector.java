package net.flintmc.mcapi.v1_15_2.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.event.IngameMenuCloseEvent;
import net.flintmc.mcapi.event.IngameMenuOpenEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;

@Singleton
public class IngameMenuEventInjector {

  private final EventBus eventBus;
  private final IngameMenuOpenEvent openEvent;
  private final IngameMenuCloseEvent closeEvent;

  @Inject
  private IngameMenuEventInjector(
      EventBus eventBus,
      IngameMenuOpenEvent.Factory openEventFactory,
      IngameMenuCloseEvent.Factory closeEventFactory) {
    this.eventBus = eventBus;
    this.openEvent = openEventFactory.create();
    this.closeEvent = closeEventFactory.create();
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayInGameMenu",
      parameters = @Type(reference = boolean.class),
      executionTime = Hook.ExecutionTime.BEFORE)
  public HookResult displayInGameMenu(Hook.ExecutionTime executionTime) {
    if (Minecraft.getInstance().currentScreen != null) {
      return HookResult.CONTINUE;
    }

    this.openEvent.setCancelled(false);
    return this.eventBus.fireEvent(this.openEvent, executionTime).isCancelled()
        ? HookResult.BREAK
        : HookResult.CONTINUE;
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "displayGuiScreen",
      parameters = @Type(typeName = "net.minecraft.client.gui.screen.Screen"),
      executionTime = Hook.ExecutionTime.BEFORE)
  public void displayGuiScreen(@Named("args") Object[] args, Hook.ExecutionTime executionTime) {
    Object screen = args[0];
    if (screen != null) {
      return;
    }
    if (!(Minecraft.getInstance().currentScreen instanceof IngameMenuScreen)) {
      return;
    }

    this.eventBus.fireEvent(this.closeEvent, executionTime);
  }
}
