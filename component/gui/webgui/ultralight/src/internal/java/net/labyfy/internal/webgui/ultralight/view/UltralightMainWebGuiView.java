package net.labyfy.internal.webgui.ultralight.view;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.internal.webgui.ultralight.UltralightWebGuiController;
import net.labymedia.ultralight.UltralightSurface;
import net.labymedia.ultralight.UltralightView;
import net.labymedia.ultralight.math.IntRect;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL20.*;

/**
 * Main view of the Ultralight web content.
 */
@Singleton
public class UltralightMainWebGuiView implements GuiComponent, UltralightWebGuiView {
  private final UltralightWebGuiController controller;
  private final MinecraftWindow minecraftWindow;
  private final UltralightView view;

  private int openglTextureId;

  @Inject
  private UltralightMainWebGuiView(UltralightWebGuiController controller, MinecraftWindow minecraftWindow) {
    this.controller = controller;
    this.minecraftWindow = minecraftWindow;
    this.view = controller.getRenderer().createView(
        minecraftWindow.getFramebufferWidth(),
        minecraftWindow.getFramebufferHeight(),
        true
    );
  }

  @Override
  public void screenChanged(ScreenName newScreen) {

  }

  @Override
  public boolean shouldRender(Hook.ExecutionTime executionTime, RenderExecution execution) {
    // TODO: Maybe allow the Javascript running in the view to signal whether transparency is required, and if
    //       it is not required, cancel the rendering of other stuff and render in the BEFORE ExecutionTime?
    return executionTime == Hook.ExecutionTime.AFTER;
  }

  @Override
  public void render(RenderExecution execution) {
    // Use the main render call only as a trigger for the controller to trigger a render,
    // real drawing is done depending on the used renderer in the draw* methods
    controller.renderAll();
  }

  @Override
  public void close() {
    // Perform cleanup
    if(openglTextureId != 0) {
      glDeleteTextures(openglTextureId);
    }
  }

  @Override
  public void setURL(String url) {
    view.loadURL(url);
  }

  @Override
  public void update() {
    long width = view.width();
    long height = view.height();

    if(width != minecraftWindow.getFramebufferWidth() || height != minecraftWindow.getFramebufferHeight()) {
      // Update the view only if required, prevents unnecessary performance impacts due to resizing
      view.resize(minecraftWindow.getFramebufferWidth(), minecraftWindow.getFramebufferHeight());
    }
  }

  @Override
  public void drawUsingSurface() {
    if(openglTextureId == 0) {
      // Create objects required for OpenGL upload
      initGL();
    }

    // Retrieve the view size
    int width = (int) view.width();
    int height = (int) view.height();

    // Get the surface
    UltralightSurface surface = view.surface();

    // Retrieve the dirty bounds to check if the data needs to be updated, and if so, what needs to be
    // updated
    IntRect dirtyBounds = surface.dirtyBounds();

    // Bind the OpenGL texture
    glBindTexture(GL_TEXTURE_2D, openglTextureId);

    if(dirtyBounds.isValid()) {
      try {
        // Retrieve the raw image data in BGRA format
        ByteBuffer imageData = surface.lockPixels();

        // Needs updating
        if(dirtyBounds.width() == width && dirtyBounds.height() == height) {
          // Perform full upload as the entire image has been invalidated
          glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, imageData);
        } else {
          // Partially update the image (improved performance compared to full upload)
          int dirtyX = dirtyBounds.x();
          int dirtyY = dirtyBounds.y();
          int dirtyWidth = dirtyBounds.width();
          int dirtyHeight = dirtyBounds.height();

          // Calculate the offset where the dirty data starts in the buffer
          int startOffset = (dirtyY * width * 4) + dirtyX * 4;

          // Tell OpenGL how much pixels to unpack per row
          glPixelStorei(GL_UNPACK_ROW_LENGTH, width);

          // Upload the partial data
          glTexSubImage2D(
              GL_TEXTURE_2D, // Upload to our bound texture
              0, // No mipmapping
              dirtyX, dirtyY, dirtyWidth, dirtyHeight,
              GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV,
              (ByteBuffer) imageData.position(startOffset) // Offset the data pointer
          );
        }
      } finally {
        // Make the pixels available again to Ultralight
        surface.unlockPixels();
        surface.clearDirtyBounds();
      }
    }

    // Dispatch draw using OpenGL
    drawUsingOpenGLTexture(openglTextureId);
  }

  @Override
  public void drawUsingOpenGLTexture(int textureId) {
    // Bind the OpenGL texture
    glBindTexture(GL_TEXTURE_2D, openglTextureId);

    // Set up OpenGL state
    glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT | GL_TRANSFORM_BIT);

    int width = (int) view.width();
    int height = (int) view.height();

    // Set up matrix stack
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();
    glOrtho(0, width, height, 0, -1, 1);
    glMatrixMode(GL_MODELVIEW);
    glPushMatrix();
    glLoadIdentity();

    // Disable lighting and scissoring, we always render fullscreen
    glDisable(GL_LIGHTING);
    glDisable(GL_SCISSOR_TEST);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    // Draw with a neutral color
    glColor4f(1f, 1f, 1f, 1f);

    // Draw the fullscreen quad
    glBegin(GL_QUADS);

    // Lower left corner, 0/0 on the screen space, 0/0 of the image UV
    glTexCoord2f(0, 0);
    glVertex2i(0, 0);

    // Upper left corner
    glTexCoord2f(0, 1);
    glVertex2i(0, height);

    // Upper right corner
    glTexCoord2f(1, 1);
    glVertex2i(width, height);

    // Lower right corner
    glTexCoord2f(1, 0);
    glVertex2i(width, 0);

    // Finish drawing and clean up
    glEnd();
    glBindTexture(GL_TEXTURE_2D, 0);

    // Restore OpenGL state
    glPopMatrix();
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    glMatrixMode(GL_MODELVIEW);
    glPopAttrib();
  }

  private void initGL() {
    // Create and bind the texture
    openglTextureId = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, openglTextureId);

    // Disable mipmapping, the texture is always directly user facing
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    // Clamp the texture, those settings are only here for clarity and
    // possibly bad OpenGL implementations, as the texture will always
    // be automatically adjusted to match the window size
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);

    // Clean up
    glBindTexture(GL_TEXTURE_2D, 0);
  }
}
