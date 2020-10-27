package net.flintmc.mcapi.items.meta;

/** Flags to be hidden on an item. */
public enum ItemHideFlag {

  /** Flag to hide all enchantments of an item. */
  ENCHANTMENTS(),

  /** Flag to hide all attributes like Damage, Armor points, etc. */
  ATTRIBUTES(),

  /** Flag to hide that an item is unbreakable. */
  UNBREAKABLE(),

  /** Flag to hide the blocks that can be destroyed with an item. */
  CAN_DESTROY(),

  /** Flag to hide the blocks that an item can be placed on. */
  CAN_PLACE_ON(),

  /** Flag to hide the potion effects of an item. */
  POTION_EFFECTS();

  private final byte modifier;

  ItemHideFlag() {
    this.modifier = (byte) (1 << super.ordinal());
  }

  /**
   * Retrieves the modifier which is used to generate and read the value in the NBT of the item.
   *
   * @return The modifier
   */
  public byte getModifier() {
    return this.modifier;
  }
}
