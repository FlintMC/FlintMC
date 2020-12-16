package net.flintmc.mcapi.world.scoreboad.type;

/**
 * An enumeration of all available render types.
 */
public enum RenderType {

  /**
   * The type renders integers.
   */
  INTEGER,
  /**
   * The type renders hearts.
   */
  HEARTS;

  /**
   * Retrieves the name in lower case.
   *
   * @return The name in lower case.
   */
  public String getLowerCaseName() {
    return this.name().toLowerCase();
  }
}
