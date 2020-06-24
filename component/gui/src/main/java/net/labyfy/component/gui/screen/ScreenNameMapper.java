package net.labyfy.component.gui.screen;

/**
 * Service converting class names to screen names.
 */
public interface ScreenNameMapper {
  /**
   * Converts the given fully qualified class name to a screen name.
   *
   * @param className The fully qualified class name to convert
   * @return The converted screen name
   */
  ScreenName fromClass(String className);
}
