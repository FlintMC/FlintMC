package net.labyfy.component.gui.juklearmc;

import net.janrupf.juklear.backend.JuklearBackend;

/**
 * Interface for separating the provided backend from the implementation part.
 */
public interface JuklearMCBackendProvider {
  /**
   * Retrieves the backend Juklear should use for drawing.
   *
   * @return A {@link JuklearBackend} forwarded to the Juklear instance
   */
  JuklearBackend backend();
}
