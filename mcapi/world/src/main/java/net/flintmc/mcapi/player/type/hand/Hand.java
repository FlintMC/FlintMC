package net.flintmc.mcapi.player.type.hand;

/**
 * An enumeration of all available hands.
 */
public enum Hand {
  /**
   * The main-hand of a player
   */
  MAIN_HAND,
  /**
   * The off-hand of a player
   */
  OFF_HAND;

  /**
   * An enumeration of all available hand sides.
   */
  public enum Side {
    LEFT,
    RIGHT;

    /**
     * Retrieves the opposite hand.
     *
     * @return The opposite hand.
     */
    public Side opposite() {
      return this == LEFT ? RIGHT : LEFT;
    }

  }
}
