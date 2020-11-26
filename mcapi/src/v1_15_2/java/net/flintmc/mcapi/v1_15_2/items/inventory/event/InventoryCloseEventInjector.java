package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.mcapi.event.DirectionalEvent.Direction;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryCloseEvent;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.HookResult;

@Singleton
public class InventoryCloseEventInjector {

  private final EventBus eventBus;
  private final InventoryCloseEvent.Factory eventFactory;
  private final InventoryController controller;

  @Inject
  public InventoryCloseEventInjector(
      EventBus eventBus, InventoryCloseEvent.Factory eventFactory, InventoryController controller) {
    this.eventBus = eventBus;
    this.eventFactory = eventFactory;
    this.controller = controller;
  }

  @Subscribe(phase = Subscribe.Phase.PRE)
  public void handlePreIncomingClose(PacketEvent event) {
    this.fireServerClose(event, Subscribe.Phase.PRE);
  }

  @Subscribe(phase = Subscribe.Phase.POST)
  public void handlePostIncomingClose(PacketEvent event) {
    this.fireServerClose(event, Subscribe.Phase.POST);
  }

  private void fireServerClose(PacketEvent event, Subscribe.Phase phase) {
    if (event.getDirection() != Direction.RECEIVE
        || !(event.getPacket() instanceof AccessibleSCloseWindowPacket)) {
      return;
    }

    AccessibleSCloseWindowPacket packet = (AccessibleSCloseWindowPacket) event.getPacket();
    Inventory inventory = this.controller.getOpenInventory();
    if (inventory == null || inventory.getWindowId() != packet.getWindowId()) {
      return;
    }

    this.eventBus.fireEvent(this.eventFactory.create(inventory, Direction.RECEIVE), phase);
  }

  @Hook(
      executionTime = Hook.ExecutionTime.BEFORE,
      className = "net.minecraft.client.entity.player.ClientPlayerEntity",
      methodName = "closeScreen")
  public HookResult closeScreen() {
    Inventory inventory = this.controller.getOpenInventory();
    if (inventory == null) {
      inventory = this.controller.getPlayerInventory();
    }

    boolean cancelled =
        this.eventBus
            .fireEvent(this.eventFactory.create(inventory, Direction.SEND), Subscribe.Phase.PRE)
            .isCancelled();
    return cancelled ? HookResult.BREAK : HookResult.CONTINUE;
  }
}
