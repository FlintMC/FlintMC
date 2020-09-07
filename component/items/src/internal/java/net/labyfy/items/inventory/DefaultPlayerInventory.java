package net.labyfy.items.inventory;

import net.labyfy.chat.builder.ComponentBuilder;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.items.ItemRegistry;
import net.labyfy.items.ItemStack;
import net.labyfy.items.inventory.player.PlayerHand;
import net.labyfy.items.inventory.player.PlayerInventory;

public abstract class DefaultPlayerInventory extends DefaultInventory implements PlayerInventory {

  private final ChatComponent title;

  public DefaultPlayerInventory(ItemRegistry registry, InventoryType type, InventoryDimension dimension, ComponentBuilder.Factory componentFactory) {
    super(registry, 0, type, dimension);
    this.title = componentFactory.text().text("Player").build();
  }

  protected abstract ItemStack getArmorSlot(int slot);

  protected abstract ItemStack getMainSlot(int slot);

  protected abstract ItemStack getOffHandSlot();

  @Override
  public ItemStack getItemInHand(PlayerHand hand) {
    int slot = this.getHandSlot(hand);
    return slot == -1 ? super.registry.getAirType().createStack() : this.getItem(slot);
  }

  @Override
  public ItemStack getItem(int slot) throws IndexOutOfBoundsException {
    this.validateSlot(slot);

    if (slot >= 0 && slot <= 35) {
      return this.getMainSlot(slot);
    }
    if (slot >= 36 && slot <= 39) {
      return this.getArmorSlot(slot - 36);
    }
    if (this.hasHand(PlayerHand.OFF_HAND) && this.getHandSlot(PlayerHand.OFF_HAND) == slot) {
      return this.getOffHandSlot();
    }
    throw new IndexOutOfBoundsException("Invalid slot provided: " + slot);
  }

  @Override
  public ChatComponent getTitle() {
    return this.title;
  }

  @Override
  public void setTitle(ChatComponent component) {
  }

}
