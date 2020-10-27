package net.flintmc.render.gui.event;

import net.flintmc.render.gui.screen.ScreenName;

/**
 * Event indicating that the GUI screen has changed on a window.
 *
 * <p>This event is only fired on the main window by default.
 */
public class ScreenChangedEvent implements GuiEvent {
  private final ScreenName screenName;

  /**
   * Constructs a new {@link ScreenChangedEvent} with the given screen name.
   *
   * @param screenName The name of the new GUI screen, or {@code null}, if no screen is displayed
   *     anymore
   */
  public ScreenChangedEvent(ScreenName screenName) {
    this.screenName = screenName;
  }

  /**
   * Retrieves the newly displayed screen name.
   *
   * @return The name of the newly displayed screen, or {@code null}, if no screen is displayed
   *     anymore
   */
  public ScreenName getScreenName() {
    return screenName;
  }
}
