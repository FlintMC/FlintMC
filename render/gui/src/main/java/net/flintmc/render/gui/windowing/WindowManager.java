package net.flintmc.render.gui.windowing;

import java.util.Collection;

/** Manages windows controlled by this Flint instance. */
public interface WindowManager {
  /**
   * Retrieves a collection of all windows currently opened by this Flint instance.
   *
   * @return All windows of this Flint instance as an immutable collection
   */
  Collection<Window> allWindows();
}
