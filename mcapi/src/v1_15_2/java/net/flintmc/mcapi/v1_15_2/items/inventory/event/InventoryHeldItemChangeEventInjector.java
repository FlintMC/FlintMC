package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.items.inventory.event.InventoryHeldItemChangeEvent;
import net.flintmc.mcapi.items.inventory.event.InventoryHeldItemChangeEvent.Factory;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.client.Minecraft;

@Singleton
public class InventoryHeldItemChangeEventInjector {

  private final EventBus eventBus;
  private final InventoryHeldItemChangeEvent.Factory eventFactory;

  @Inject
  private InventoryHeldItemChangeEventInjector(EventBus eventBus, Factory eventFactory) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
  }

  @Hook(
      className = "net.minecraft.client.multiplayer.PlayerController",
      methodName = "syncCurrentPlayItem",
      executionTime = ExecutionTime.BEFORE)
  public HookResult fireHeldItemChangeEvent(
      @Named("instance") Object instance, ExecutionTime executionTime) {
    int knownSlot = ((AccessiblePlayerController) instance).getCurrentPlayerItem();
    int changedSlot = Minecraft.getInstance().player.inventory.currentItem;
    if (knownSlot != changedSlot) {
      boolean cancelled =
          this.eventBus
              .fireEvent(this.eventFactory.create(changedSlot), executionTime)
              .isCancelled();
      return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
    }
    return HookResult.CONTINUE;
  }
}
