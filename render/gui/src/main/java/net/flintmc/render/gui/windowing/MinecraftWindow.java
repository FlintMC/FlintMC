package net.flintmc.render.gui.windowing;

/** Interface representing the minecraft main window. */
public interface MinecraftWindow extends Window {
  /**
   * Retrieves the scale factor currently applied to the minecraft window. This will usually be 1 to
   * 4, but could be changed by mods.
   *
   * @return The scale factor of the minecraft window
   */
  int getScaleFactor();

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
   * Retrieves the width of the framebuffer. This will usually be the same as {@link #getWidth()},
   * but may vary for for example upscaled screenshots. If you need the width for rendering, this
   * method should be used instead of {@link #getWidth()}
   *
   * @return The current framebuffer width
   */
  int getFramebufferWidth();

  /**
   * Retrieves the height of the framebuffer. This will usually be the same as {@link #getHeight()},
   * but may vary for for example upscaled screenshots. If you need the height for rendering, this
   * method should be used instead of {@link #getHeight()}
   *
   * @return The current framebuffer height
   */
  int getFramebufferHeight();

  /**
   * Retrieves the FPS counter of the minecraft window.
   *
   * @return The current FPS of the minecraft window
   * @throws IllegalAccessException If the field definition could not be accessed.
   * @throws NoSuchFieldException If the field could not be found.
   * @throws ClassNotFoundException If the class could not be found.
   */
  int getFPS() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException;

  /**
   * Retrieves whether the game is currently ingame or not. Ingame means that a world is loaded, for
   * example being in a SinglePlayer world or on a MultiPlayer server.
   *
   * @return {@code true} if the client is ingame, {@code false} otherwise
   */
  boolean isIngame();
}
