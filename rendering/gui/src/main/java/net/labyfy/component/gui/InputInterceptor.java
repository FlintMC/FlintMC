package net.labyfy.component.gui;

import net.labyfy.component.gui.event.CursorPosChangedEvent;

/**
 * Service interface for updating the mouse position.
 *
 * @implNote The default implementation also uses an implementation of this class
 * to install hooks into minecraft
 */
public interface InputInterceptor {
  /**
   * Invokes the internal mouse position callback with the current mouse position and causes
   * a {@link CursorPosChangedEvent} to be fired.
   */
  void signalCurrentMousePosition();
}
