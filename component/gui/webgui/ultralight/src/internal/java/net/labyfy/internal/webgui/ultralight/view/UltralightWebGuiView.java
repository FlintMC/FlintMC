package net.labyfy.internal.webgui.ultralight.view;

import net.labyfy.webgui.WebGuiView;

/**
 * Abstraction over views created by this package.
 */
public interface UltralightWebGuiView extends WebGuiView {
  /**
   * Updates information about the view, such as width and height.
   */
  void update();

  /**
   * Draws this view using the surface API. Called when the CPU renderer is active.
   */
  void drawUsingSurface();

  /**
   * Draws this view using an OpenGL texture.
   *
   * @param textureId The id of the OpenGL texture
   */
  void drawUsingOpenGLTexture(int textureId);
}
