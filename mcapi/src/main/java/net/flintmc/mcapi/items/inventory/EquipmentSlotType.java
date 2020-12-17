package net.flintmc.mcapi.items.inventory;

/**
 * An enumeration of all available slot types.
 */
public enum EquipmentSlotType {
  MAIN_HAND(Group.HAND, 0, -1),
  OFF_HAND(Group.HAND, 1, 45),
  FEET(Group.ARMOR, 0, 8),
  LEGS(Group.ARMOR, 1, 7),
  CHEST(Group.ARMOR, 2, 6),
  HEAD(Group.ARMOR, 3, 5);

  private final EquipmentSlotType.Group group;
  private final int index;
  private final int slotIndex;

  EquipmentSlotType(Group group, int index, int slotIndex) {
    this.group = group;
    this.index = index;
    this.slotIndex = slotIndex;
  }

  /**
   * Retrieves the group of the slot type.
   *
   * @return The slot type group.
   */
  public Group getGroup() {
    return group;
  }

  /**
   * Retrieves the index of the slot type.
   *
   * @return The slot type index.
   */
  public int getIndex() {
    return index;
  }

  /**
   * Retrieves the slot index of the slot type.
   *
   * @return THe slot type slot index.
   */
  public int getSlotIndex() {
    return slotIndex;
  }

  /**
   * An enumeration of all availables groups.
   */
  enum Group {
    ARMOR,
    HAND
  }
}
