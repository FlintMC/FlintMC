package net.flintmc.render.gui.internal.windowing;

import net.flintmc.render.gui.windowing.Window;

/** Interface for accessing internal components of windows from the internal implementation. */
public interface InternalWindow extends Window {
  /**
   * Determines whether the window is being rendered intrusively. This only has an effect for the
   * Minecraft window.
   *
   * @return {@code true} if the windows is renderer intrusively, {@code false} otherwise
   */
  boolean isRenderedIntrusively();

  /** Renders the window by executing the render chain. */
  void render();
}
