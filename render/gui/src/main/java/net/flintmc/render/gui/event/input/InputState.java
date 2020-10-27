package net.flintmc.render.gui.event.input;

/**
 * Simple representation of possible key/button states
 */
public enum InputState {
  /**
   * The key/button has been pressed
   */
  PRESS,

  /**
   * The key/button has been released
   */
  RELEASE,

  /**
   * The key/button is being hold for a long duration
   */
  REPEAT
}
