package net.labyfy.component.items.inventory;

public enum EquipmentSlotType {

  MAIN_HAND(Group.HAND, 0, 0),
  OFF_HAND(Group.HAND, 1, 5),
  FEET(Group.ARMOR, 0, 1),
  LEGS(Group.ARMOR, 1, 2),
  CHEST(Group.ARMOR, 2, 3),
  HEAD(Group.ARMOR, 3, 4);

  private final EquipmentSlotType.Group group;
  private final int index;
  private final int slotIndex;

  EquipmentSlotType(Group group, int index, int slotIndex) {
    this.group = group;
    this.index = index;
    this.slotIndex = slotIndex;
  }

  public Group getGroup() {
    return group;
  }

  public int getIndex() {
    return index;
  }

  public int getSlotIndex() {
    return slotIndex;
  }

  enum Group {
    ARMOR,
    HAND
  }

}
