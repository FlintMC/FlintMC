package net.flintmc.mcapi.v1_15_2.items.inventory.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.mcapi.event.DirectionalEvent;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.event.InventoryUpdateSlotEvent;
import net.flintmc.mcapi.items.mapper.MinecraftItemMapper;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.minecraft.network.play.server.SSetSlotPacket;

@Singleton
public class InventoryUpdateSlotEventInjector {

  private final EventBus eventBus;
  private final InventoryController controller;
  private final MinecraftItemMapper itemMapper;
  private final InventoryUpdateSlotEvent.Factory eventFactory;

  @Inject
  private InventoryUpdateSlotEventInjector(
      EventBus eventBus,
      InventoryController controller,
      MinecraftItemMapper itemMapper,
      InventoryUpdateSlotEvent.Factory eventFactory) {
    this.eventBus = eventBus;
    this.controller = controller;
    this.itemMapper = itemMapper;
    this.eventFactory = eventFactory;
  }

  @Subscribe(phase = Phase.ANY)
  public void handleSetSlot(PacketEvent event, Phase phase) {
    if (event.getDirection() != DirectionalEvent.Direction.RECEIVE
        || !(event.getPacket() instanceof SSetSlotPacket)
        || !this.controller.canOpenInventories()) {
      return;
    }

    SSetSlotPacket packet = (SSetSlotPacket) event.getPacket();

    Inventory inventory =
        packet.getWindowId() == this.controller.getPlayerInventory().getWindowId()
            ? this.controller.getPlayerInventory()
            : packet.getWindowId() == this.controller.getOpenInventory().getWindowId()
                ? this.controller.getOpenInventory()
                : null;
    if (inventory == null) {
      return;
    }

    int slot = packet.getSlot();
    ItemStack previousItem = inventory.getItem(slot);
    ItemStack newItem = this.itemMapper.fromMinecraft(packet.getStack());

    this.eventBus.fireEvent(
        this.eventFactory.create(inventory, slot, previousItem, newItem), phase);
  }
}
