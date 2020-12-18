package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryHeldItemChangeEvent;
import net.flintmc.mcapi.items.inventory.event.InventoryHeldItemChangeEvent.Factory;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.flintmc.transform.hook.HookResult;
import net.minecraft.client.Minecraft;

@Singleton
public class InventoryHeldItemChangeEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final InventoryHeldItemChangeEvent.Factory eventFactory;

  @Inject
  private InventoryHeldItemChangeEventInjector(
      EventBus eventBus, InventoryController controller, Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
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
      ItemStack item = this.controller.getPlayerInventory().getItem(changedSlot + 36);
      boolean cancelled =
          this.eventBus
              .fireEvent(this.eventFactory.create(changedSlot, item), executionTime)
              .isCancelled();

      return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
    }
    return HookResult.CONTINUE;
  }
}
