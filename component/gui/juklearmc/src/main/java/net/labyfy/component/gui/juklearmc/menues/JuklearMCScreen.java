package net.labyfy.component.gui.juklearmc.menues;

import net.janrupf.juklear.layout.component.base.JuklearTopLevelComponent;

import java.io.Closeable;
import java.util.Collection;

/**
 * Base interface for screens using JuklearMC. To get your class to work as a screen, you must
 * annotate it with {@link net.labyfy.component.gui.juklearmc.JuklearScreen}
 *
 * @see net.labyfy.component.gui.juklearmc.JuklearScreen
 */
public interface JuklearMCScreen extends Closeable {
  /**
   * Called by the Juklear service to determine which components should be applied
   * to the screen when rendering it.
   *
   * @return A fixed list of components to apply to the screen
   */
  Collection<JuklearTopLevelComponent> topLevelComponents();

  /**
   * Called by the Juklear service to notify you that the display width or height has changed.
   * The values passed in are always unscaled, the Juklear service manages scaling without
   * requiring attention from the screen implementor.
   *
   * @param width  The new viewport width
   * @param height The new viewport height
   */
  void updateSize(int width, int height);

  /**
   * Called by the Juklear screen service before Juklear renders. You can use this function to
   * draw a background and similar things.
   */
  default void preNuklearRender() {
  }

  /**
   * Called by the Juklear screen service after Juklear has rendered. You can use this function if you need
   * to draw something on top of your screen.
   */
  default void postNuklearRender() {
  }

  /**
   * Called by the Juklear screen service when your screen is about to be displayed.
   */
  default void open() {
  }

  /**
   * Called by the Juklear screen service when your screen is about to be closed.
   */
  @Override
  default void close() {
  }
}
