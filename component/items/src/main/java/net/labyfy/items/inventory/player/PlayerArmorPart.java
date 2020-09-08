package net.labyfy.items.inventory.player;

public enum PlayerArmorPart {

  HELMET(3),
  CHEST_PLATE(2),
  LEGGINGS(1),
  BOOTS(0);

  private final int index;

  PlayerArmorPart(int index) {
    this.index = index;
  }

  public int getIndex() {
    return this.index;
  }
}
