package net.flintmc.mcapi.items.inventory;

/** An enumeration of all available slot types. */
public enum EquipmentSlotType {
  MAIN_HAND(Group.HAND, 0),
  OFF_HAND(Group.HAND, 1),
  FEET(Group.ARMOR, 0),
  LEGS(Group.ARMOR, 1),
  CHEST(Group.ARMOR, 2),
  HEAD(Group.ARMOR, 3);

  private final EquipmentSlotType.Group group;
  private final int index;

  EquipmentSlotType(Group group, int index) {
    this.group = group;
    this.index = index;
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

  /** An enumeration of all availables groups. */
  enum Group {
    ARMOR,
    HAND
  }
}
