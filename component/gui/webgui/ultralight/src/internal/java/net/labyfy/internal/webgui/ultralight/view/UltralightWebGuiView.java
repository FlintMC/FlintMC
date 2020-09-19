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
   * Notifies the view that the data that needs to be drawn is available on the surface.
   */
  void dataReadyOnSurface();

  /**
   * Notifies the view that the data that needs to be drawn is available in an OpenGL texture.
   *
   * @param textureId The id of the OpenGL texture
   */
  void dataReadyOnOpenGLTexture(int textureId);
}
