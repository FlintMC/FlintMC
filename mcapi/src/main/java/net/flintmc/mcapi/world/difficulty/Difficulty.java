package net.flintmc.mcapi.world.difficulty;

/** An enumeration of all available difficulties. */
public enum Difficulty {

  /** Everything lives in harmony and peace with each other and world hunger was defeated. */
  PEACEFUL,
  /**
   * Hostile mobs spawn, but they deal less damage than on {@link #NORMAL} difficulty. The hunger
   * bar can deplete damaging the player until they are left with 5 hearts if it drains completely.
   */
  EASY,
  /**
   * Hostile mobs spawn and deal standard damage. The hunger bar can deplete, damaging the player
   * until they are left with a half-heart if it drains completely.
   */
  NORMAL,
  /**
   * Hostile mobs deal more damage than on {@link #NORMAL} difficulty. The hunger bar can deplete,
   * damaging the player until they die if it drains completely.
   */
  HARD;

  /**
   * Retrieves the identifier of this difficulty.
   *
   * @return The identifier of this difficulty.
   */
  public int getId() {
    return this.ordinal();
  }
}
