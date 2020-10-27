package net.flintmc.render.gui.screen;

/** Service converting class names to screen names. */
public interface ScreenNameMapper {
  /**
   * Converts the given fully qualified class name to a screen name.
   *
   * @param className The fully qualified class name to convert
   * @return The converted screen name
   */
  ScreenName fromClass(String className);

  /**
   * Converts the given object into a screen name.
   *
   * @param screen The screen object
   * @return The converted screen name, or {@code null}, if screen is {@code null}
   */
  default ScreenName fromObject(Object screen) {
    if (screen == null) {
      return null;
    }

    return fromClass(screen.getClass().getName());
  }
}
