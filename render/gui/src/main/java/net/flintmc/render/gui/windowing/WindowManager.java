package net.flintmc.render.gui.windowing;

import java.util.Collection;

/** Manages windows controlled by this Labyfy instance. */
public interface WindowManager {
  /**
   * Retrieves a collection of all windows currently opened by this Labyfy instance.
   *
   * @return All windows of this Labyfy instance as an immutable collection
   */
  Collection<Window> allWindows();
}
