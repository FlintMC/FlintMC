package net.labyfy.component.items.inventory.player;

/**
 * An enumeration of all available armor parts of the player.
 */
public enum PlayerArmorPart {

  /**
   * The helmet which protects the head of the player.
   */
  HELMET(3),
  /**
   * The chest plate which protects the body of the player.
   */
  CHEST_PLATE(2),
  /**
   * The leggings which protects the legs of the player.
   */
  LEGGINGS(1),
  /**
   * The boots which protects the feet of the player.
   */
  BOOTS(0);

  private final int index;

  PlayerArmorPart(int index) {
    this.index = index;
  }

  /**
   * Retrieves the index of this armor part.
   *
   * @return the index of this armor part.
   */
  public int getIndex() {
    return this.index;
  }
}
