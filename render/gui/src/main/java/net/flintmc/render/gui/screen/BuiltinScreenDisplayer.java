package net.flintmc.render.gui.screen;

/** Interface abstracting the displaying of Minecraft GUI screens */
public interface BuiltinScreenDisplayer {
  /**
   * Tests whether this screen displayer is capable of displaying the given screen
   *
   * @param screenName The name of the screen to display
   * @return {@code true} if the displayer supports the screen, {@code false} otherwise
   */
  boolean supports(ScreenName screenName);

  /**
   * Changes the currently active GUI screen to the given one. This method may only be called with a
   * screen supported. To test for support, use the {@link #supports(ScreenName)} method.
   *
   * @param screenName The name of the screen to display
   * @param args Parameters to pass to the screen
   * @throws UnsupportedOperationException If the screen given is not supported by this displayer
   * @throws IllegalArgumentException If the arguments given are not acceptable for the given screen
   */
  void display(ScreenName screenName, Object... args);

  /**
   * Retrieves the currently active GUI screen, this will never return a screen name of the type
   * {@link ScreenName.Type#FROM_MINECRAFT} and may not be {@link #supports(ScreenName) supported by
   * this displayer}.
   *
   * @return The currently open screen or {@code null} if there is currently no screen opened (e.g.
   *     the ingame screen without the chat opened)
   */
  ScreenName getOpenScreen();
}
