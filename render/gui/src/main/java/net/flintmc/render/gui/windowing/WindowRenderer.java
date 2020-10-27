package net.flintmc.render.gui.windowing;

/**
 * Renderer for windows.
 */
public interface WindowRenderer {
  /**
   * Called when the renderer is added to a window. The OpenGL context in this method is the one used for the window.
   */
  void initialize();

  /**
   * Determines if this renderer is taking full control of the content of the window. If this setting is {@code true} on
   * a renderer attached to the main window, the default render logic of minecraft will be disabled.
   * <p>
   * This setting can't change while a renderer is attached! To change it, re-attach the renderer with this method
   * returning another value.
   *
   * @return {@code true} to mark the renderer as intrusive, {@code false} to mark it as cooperative
   */
  boolean isIntrusive();

  /**
   * Called when window needs to be rendered. The OpenGL context in this method is the one used for the window.
   */
  void render();

  /**
   * Called when the renderer is removed from a window. The OpenGL context in this method is the one used for the
   * window.
   */
  void cleanup();
}
