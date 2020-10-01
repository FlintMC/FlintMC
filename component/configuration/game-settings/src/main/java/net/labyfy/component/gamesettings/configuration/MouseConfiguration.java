package net.labyfy.component.gamesettings.configuration;

/**
 * Represents the mouse configuration.
 */
public interface MouseConfiguration {

  /**
   * Retrieves the sensitivity of the mouse.
   *
   * @return The mouse sensitivity.
   */
  double getMouseSensitivity();

  /**
   * Changes the sensitivity of the mouse.
   *
   * @param mouseSensitivity The new mouse sensitivity.
   */
  void setMouseSensitivity(double mouseSensitivity);

  /**
   * Retrieves the sensitivity of the mouse wheel.
   *
   * @return The mouse wheel sensitivity.
   */
  double getMouseWheelSensitivity();

  /**
   * Changes the sensitivity of the mouse wheel.
   *
   * @param mouseWheelSensitivity The new mouse wheel sensitivity.
   */
  void setMouseWheelSensitivity(double mouseWheelSensitivity);

  /**
   * Whether it is the raw mouse input.
   *
   * @return {@code true} if is the raw mouse input, otherwise {@code false}.
   */
  boolean isRawMouseInput();

  /**
   * Changes the state of the raw mouse input.
   *
   * @param rawMouseInput The new state for the raw mouse input.
   */
  void setRawMouseInput(boolean rawMouseInput);

  /**
   * Whether the mouse inverted.
   *
   * @return {@code true} if the mouse is inverted, otherwise {@code false}.
   */
  boolean isInvertMouse();

  /**
   * Changes the state of the inverted mouse.
   *
   * @param invertMouse The new state for the inverted mouse.
   */
  void setInvertMouse(boolean invertMouse);

  /**
   * Whether it is a discreet scrolling with the mouse.
   *
   * @return {@code true} if it is discreet scrolling with the mouse, otherwise {@code false}.
   */
  boolean isDiscreteMouseScroll();

  /**
   * Changes the state of the discreet scrolling with the mouse.
   *
   * @param discreteMouseScroll The new state of for the discrete mouse scrolling.
   */
  void setDiscreteMouseScroll(boolean discreteMouseScroll);

  /**
   * Whether the touchscreen mode should be used.
   *
   * @return {@code true} if the touchscreen mode is used, otherwise {@code false}.
   */
  boolean isTouchscreen();

  /**
   * Changes the touchscreen mode.
   *
   * @param touchscreen The new state for the touchscreen mode.
   */
  void setTouchscreen(boolean touchscreen);

}
