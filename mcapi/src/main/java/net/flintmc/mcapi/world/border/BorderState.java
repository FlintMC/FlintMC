package net.flintmc.mcapi.world.border;

import java.awt.*;

/** An enumeration of all available border states. */
public enum BorderState {

  /** Represents the state of when the world border is growing. */
  GROWING(new Color(64, 255, 128)),
  /** Represents the state of when the world border is shrinking. */
  SHRINKING(new Color(255, 48, 48)),
  /** Represents the state of when teh world border is stationary. */
  STATIONARY(new Color(32, 160, 255));

  private final Color color;

  BorderState(Color color) {
    this.color = color;
  }

  /**
   * Retrieves the world border color.
   *
   * @return The world border color.
   */
  public Color getColor() {
    return color;
  }
}
