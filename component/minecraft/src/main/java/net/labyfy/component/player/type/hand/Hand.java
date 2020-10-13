package net.labyfy.component.player.type.hand;

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
    LEFT("options.mainHand.left"),
    RIGHT("options.mainHand.right");

    private final String translateKey;

    Side(String translateKey) {
      this.translateKey = translateKey;
    }

    /**
     * Retrieves the opposite hand.
     *
     * @return The opposite hand.
     */
    public Side opposite() {
      return this == LEFT ? RIGHT : LEFT;
    }

    /**
     * Retrieves the translation key of this hand side.
     *
     * @return The translation key of this hand side.
     */
    public String getTranslateKey() {
      return translateKey;
    }

  }
}
