package net.labyfy.component.gui;

/**
 * Interface representing the minecraft main window.
 */
public interface MinecraftWindow {
  /**
   * Retrieves the platform native handle.
   *
   * @return The platform native handle
   */
  long getHandle();

  /**
   * Retrieves the scale factor currently applied to the minecraft window. This will
   * usually be 1 to 4, but could be changed by mods.
   *
   * @return The scale factor of the minecraft window
   */
  int getScaleFactor();

  /**
   * Retrieves the width of the minecraft window.
   *
   * @return The current width of the window
   */
  float getWidth();

  /**
   * Retrieves the height of the minecraft window.
   *
   * @return The current height of the window
   */
  float getHeight();

  /**
   * Retrieves the scaled width of the minecraft window.
   *
   * @return The current scaled with of the window
   */
  float getScaledWidth();

  /**
   * Retrieves the scaled height of the minecraft window.
   *
   * @return The current scaled height of the window
   */
  float getScaledHeight();

  /**
   * Retrieves the width of the framebuffer. This will usually be the
   * same as {@link #getWidth()}, but may vary for for example upscaled screenshots.
   * If you need the width for rendering, this method should be used instead of {@link #getWidth()}
   *
   * @return The current framebuffer width
   */
  int getFramebufferWidth();

  /**
   * Retrieves the the height of the framebuffer. This will usually be the
   * same as {@link #getHeight()}, but may vary for for example upscaled screenshots.
   * If you need the height for rendering, this method should be used instead of {@link #getHeight()}
   *
   * @return The current framebuffer height
   */
  int getFramebufferHeight();

  /**
   * Retrieves the FPS counter of the minecraft window.
   *
   * @return The current FPS of the minecraft window
   * @throws IllegalAccessException if the field definition could not be accessed.
   * @throws NoSuchFieldException   if the field could not be found.
   * @throws ClassNotFoundException if the class could not be found.
   */
  int getFPS() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException;

  boolean isIngame();
}
