package net.flintmc.render.gui.event;

import net.flintmc.render.gui.screen.ScreenName;
import net.flintmc.render.gui.windowing.Window;

/**
 * Event indicating that the GUI screen has changed on a window.
 *
 * <p>This event is only fired on the main window by default.
 */
public class ScreenChangedEvent extends DefaultGuiEvent implements GuiEvent {
  private final ScreenName screenName;

  /**
   * Constructs a new {@link ScreenChangedEvent} with the given screen name.
   *
   * @param window The non-null window where this event has happened
   * @param screenName The name of the new GUI screen, or {@code null}, if no screen is displayed
   *     anymore
   */
  public ScreenChangedEvent(Window window, ScreenName screenName) {
    super(window);
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
