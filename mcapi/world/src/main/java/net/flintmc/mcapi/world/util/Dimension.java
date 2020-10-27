package net.flintmc.mcapi.world.util;

/**
 * An enumeration of all available dimensions.
 */
public enum Dimension {

  /**
   * The overworld is the dimension in which all player begin their.
   */
  OVERWORLD(0),
  /**
   * The nether is a dangerous hell-like dimension.
   */
  NETHER(-1),
  /**
   * The end is a dark, space-like dimension.
   */
  THE_END(1);

  private final int id;

  /**
   * Initializes a new dimension with an identifier
   *
   * @param id The dimension identifier
   */
  Dimension(int id) {
    this.id = id;
  }

  /**
   * Retrieves the identifier of this dimension.
   *
   * @return The identifier of this dimension.
   */
  public int getId() {
    return id;
  }
}
