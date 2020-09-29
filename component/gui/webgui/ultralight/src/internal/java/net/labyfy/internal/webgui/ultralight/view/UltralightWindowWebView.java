package net.labyfy.internal.webgui.ultralight.view;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.gui.event.*;
import net.labyfy.component.gui.event.input.InputState;
import net.labyfy.component.gui.event.input.Key;
import net.labyfy.component.gui.windowing.WindowRenderer;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.render.shader.ShaderException;
import net.labyfy.component.render.shader.ShaderProgram;
import net.labyfy.component.render.shader.ShaderUniform;
import net.labyfy.internal.webgui.ultralight.UltralightWebGuiController;
import net.labyfy.internal.webgui.ultralight.util.UltralightLabyfyBridge;
import net.labymedia.ultralight.UltralightSurface;
import net.labymedia.ultralight.UltralightView;
import net.labymedia.ultralight.input.*;
import net.labymedia.ultralight.math.IntRect;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class UltralightWindowWebView implements UltralightWebGuiView, WindowRenderer, GuiEventListener {
  private final UltralightView view;
  private final boolean gpuRenderer;

  private int openGLTexture;
  private int vao, vbo, ebo;
  private boolean transparent;

  private float scale; // TODO: make configurable

  // Both are cached to prevent JNI calls
  private int width;
  private int height;

  private ShaderProgram shader;
  private ShaderUniform dimsUniform;
  private ShaderUniform textureUniform;

  @AssistedInject
  protected UltralightWindowWebView(
      ShaderProgram.Factory shaderFactory,
      ShaderUniform.Factory uniformFactory,
      UltralightWebGuiController controller,
      @Assisted("initialWidth") int initialWidth,
      @Assisted("initialHeight") int initialHeight,
      @Assisted("transparent") boolean transparent
  ) {
    this.view = controller.getRenderer().createView(
        initialWidth,
        initialHeight,
        transparent
    );
    this.gpuRenderer = controller.isUsingGPURenderer();
    this.openGLTexture = -1;

    this.scale = 1.0f;

    this.width = initialWidth;
    this.height = initialHeight;

    this.shader = shaderFactory.create();
    try {
      this.shader.addVertexShader("#version 330 core\n" +
          "\n" +
          "layout (location = 0) in vec3 inPos;\n" +
          "layout (location = 1) in vec3 inColor;\n" +
          "layout (location = 2) in vec2 inTexCoord;\n" +
          "\n" +
          "uniform vec2 Dims;\n" +
          "\n" +
          "out vec3 VertexColor;\n" +
          "out vec2 TexCoord;\n" +
          "\n" +
          "void main() \n" +
          "{\n" +
          "    VertexColor = inColor;\n" +
          "    TexCoord = inTexCoord;\n" +
          "    gl_Position = vec4((inPos.xy / Dims)*2. - 1., 0., 1.);\n" +
          "}");
      this.shader.addFragmentShader("#version 330 core\n" +
          "in vec3 VertexColor;\n" +
          "in vec2 TexCoord;\n" +
          "\n" +
          "out vec4 FragColor;\n" +
          "\n" +
          "uniform sampler2D Text;\n" +
          "\n" +
          "void" +
          " main()\n" +
          "{\n" +
          "    FragColor = texture(Text, TexCoord);\n" +
          "    //FragColor = vec4(TexCoord,0., 1.);\n" +
          "}");
      this.shader.link();

      this.dimsUniform = uniformFactory.create("Dims", this.shader);
      this.textureUniform = uniformFactory.create("Text", this.shader);
    } catch(ShaderException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void dataReadyOnSurface() {
    // Get the surface
    UltralightSurface surface = view.surface();

    // Retrieve the dirty bounds to check if the data needs to be updated, and if so, what needs to be
    // updated
    IntRect dirtyBounds = surface.dirtyBounds();

    // Bind the OpenGL texture
    //glBindTexture(GL_TEXTURE_2D, openGLTexture);

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

          // Reset the pixels per row value to auto detection, else we cause weird crashes
          glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
        }
      } finally {
        // Make the pixels available again to Ultralight
        surface.unlockPixels();
        surface.clearDirtyBounds();
      }
    }
  }

  @Override
  public void dataReadyOnOpenGLTexture(int textureId) {
    this.openGLTexture = textureId;
  }

  @Override
  public void close() {

  }

  @Override
  public void setTransparent(boolean transparent) {
    this.transparent = transparent;
  }

  @Override
  public void setURL(String url) {
    this.view.loadURL(url);
  }

  float ow = width, oh = height;

  @Override
  public void initialize() {
    if(gpuRenderer) {
      // If we are already rendering to a OpenGL texture in the renderer, ignore the initialization
      return;
    }

    int oldVertexArray = glGetInteger(GL_VERTEX_ARRAY_BINDING);
    int oldVertexBuffer = glGetInteger(GL_ARRAY_BUFFER_BINDING);
    int oldElementArrayBuffer = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING);

    /*if(openGLTexture != -1) {
      throw new IllegalStateException("Renderer has been initialized and not been destructed yet");
    }*/

    openGLTexture = glGenTextures();
    vao = glGenVertexArrays();
    vbo = glGenBuffers();
    ebo = glGenBuffers();
    float[] vertices = new float[]{
        width, height, 0,/**/ 0, 0, 1,/**/ 1, 1, // TOP RIGHT
        width, 0, 0,/**/ 0, 0, 1,/**/ 1, 0,  // BOTTOM RIGHT
        0, 0, 0,/**/ 1, 0, 0,/**/ 0, 0, // BOTTOM LEFT
        0, height, 0,/**/ 0, 1, 0,/**/ 0, 1 // TOP LEFT
    };
    int[] indices = new int[]{
        0, 3, 1,
        3, 2, 1
    };
    glBindVertexArray(vao);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 32, 0);
    glEnableVertexAttribArray(0);

    glVertexAttribPointer(1, 3, GL_FLOAT, false, 32, 12);
    glEnableVertexAttribArray(1);

    glVertexAttribPointer(2, 2, GL_FLOAT, false, 32, 24);
    glEnableVertexAttribArray(2);

    glEnableVertexAttribArray(2);
    glBindTexture(GL_TEXTURE_2D, openGLTexture);

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

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, oldElementArrayBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, oldVertexBuffer);
    glBindVertexArray(oldVertexArray);
  }

  @Override
  public boolean isIntrusive() {
    return false;
  }

  @Override
  public void render() {

    glViewport(0, 0, width, height);
    glScissor(0, 0, width, height);
    glMatrixMode(GL_MODELVIEW);
    glPushMatrix();

    glBindTexture(GL_TEXTURE_2D, openGLTexture);
    this.shader.useShader();
    this.dimsUniform.set2fv(new float[]{width, height});
    this.textureUniform.set1i(this.openGLTexture);

    int oldVertexArray = glGetInteger(GL_VERTEX_ARRAY_BINDING);
    int oldVertexBuffer = glGetInteger(GL_ARRAY_BUFFER_BINDING);
    int oldElementArrayBuffer = glGetInteger(GL_ELEMENT_ARRAY_BUFFER_BINDING);

    glBindVertexArray(vao);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, oldElementArrayBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, oldVertexBuffer);
    glBindVertexArray(oldVertexArray);

    this.shader.stopShader();
    glBindTexture(GL_TEXTURE_2D, 0);
    glPopMatrix();
   /* // Set up OpenGL state
    glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT | GL_TRANSFORM_BIT);

    // Set up matrix stack
    glViewport(0, 0, width, height);
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();
    glOrtho(0, width, height, 0, -100000, 100000);
    glMatrixMode(GL_MODELVIEW);
    glPushMatrix();
    glLoadIdentity();

    // Disable lighting and scissoring, we always render fullscreen
    glDisable(GL_LIGHTING);
    glDisable(GL_SCISSOR_TEST);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    // Bind the OpenGL texture
    //glBindTexture(GL_TEXTURE_2D, openGLTexture);

    // Draw with a neutral color
    glColor4f(1f, 0f, 0f, 1f);

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
    glPopAttrib();*/
  }

  @Override
  public void cleanup() {
    if(gpuRenderer) {
      // The renderer will clean up its own resources
      return;
    }

    if(openGLTexture == -1) {
      throw new IllegalStateException("Renderer has been destructed already");
    }

    glDeleteTextures(openGLTexture);
    openGLTexture = -1;
  }

  @Override
  public boolean handle(GuiEvent event) {
    if(event instanceof CursorPosChangedEvent) {
      // Cursor position changed, signal to Ultralight
      CursorPosChangedEvent evt = (CursorPosChangedEvent) event;

      view.fireMouseEvent(
          new UltralightMouseEvent()
              .type(UltralightMouseEventType.MOVED)
              .x((int) evt.getX())
              .y((int) evt.getY())
      );
    } else if(event instanceof FramebufferSizeEvent) {
      // Update the framebuffer size
      FramebufferSizeEvent evt = (FramebufferSizeEvent) event;

      this.width = (int) (evt.getWidth() * scale);
      this.height = (int) (evt.getHeight() * scale);

      view.resize(width, height);
    } else if(event instanceof KeyEvent) {
      // Raw key input
      KeyEvent evt = (KeyEvent) event;
      InputState state = evt.getState();
      Key key = evt.getKey();

      // Translate Labyfy to Ultralight key
      UltralightKey ultralightKey = UltralightLabyfyBridge.labyfyToUltralightKey(evt.getKey());

      // Fire the normal input event
      view.fireKeyEvent(new UltralightKeyEvent()
          .type(
              state == InputState.PRESS || state == InputState.REPEAT
                  ? UltralightKeyEventType.RAW_DOWN : UltralightKeyEventType.UP)
          .virtualKeyCode(ultralightKey)
          .nativeKeyCode(evt.getScancode())
          .keyIdentifier(UltralightKeyEvent.getKeyIdentifierFromVirtualKeyCode(ultralightKey))
          .modifiers(UltralightLabyfyBridge.labyfyToUltralightModifierKeys(evt.getModifierKeys()))
      );

      // Some keys need special treatment, namely <ENTER> and <TAB>
      if((state == InputState.PRESS || state == InputState.REPEAT) && (key == Key.ENTER || key == Key.TAB)) {
        // Translate them into their string versions
        String text = key == Key.ENTER ? "\n" : "\t";

        // Fire the extra event
        view.fireKeyEvent(new UltralightKeyEvent()
            .type(UltralightKeyEventType.CHAR)
            .text(text)
            .unmodifiedText(text)
        );
      }
    } else if(event instanceof MouseButtonEvent) {
      // Translate mouse clicks
      MouseButtonEvent evt = (MouseButtonEvent) event;

      UltralightMouseEventButton button = null;

      switch(evt.getButton()) {
        case LEFT:
          button = UltralightMouseEventButton.LEFT;
          break;

        case MIDDLE:
          button = UltralightMouseEventButton.MIDDLE;
          break;

        case RIGHT:
          button = UltralightMouseEventButton.RIGHT;
          break;

        case UNKNOWN:
          return false;
      }

      view.fireMouseEvent(new UltralightMouseEvent()
          .x((int) evt.getX())
          .y((int) evt.getY())
          .button(button)
          .type(evt.getState() == InputState.PRESS ? UltralightMouseEventType.DOWN : UltralightMouseEventType.UP)
      );
    } else if(event instanceof MouseScrolledEvent) {
      // Translate mouse scrolling
      MouseScrolledEvent evt = (MouseScrolledEvent) event;

      view.fireScrollEvent(new UltralightScrollEvent()
          .type(UltralightScrollEventType.BY_PIXEL)
          .deltaX((int) evt.getXOffset())
          .deltaY((int) evt.getYOffset())
      );
    } else if(event instanceof UnicodeTypedEvent) {
      // Translate normal char input
      UnicodeTypedEvent evt = (UnicodeTypedEvent) event;

      // Convert the codepoint to an UTF-16 Java string
      String text = new String(Character.toChars(evt.getCodepoint()));

      view.fireKeyEvent(new UltralightKeyEvent()
          .type(UltralightKeyEventType.CHAR)
          .text(text)
          .unmodifiedText(text)
      );
    } else if(event instanceof WindowFocusEvent) {
      // Translate focus events
      if(((WindowFocusEvent) event).isFocused()) {
        view.focus();
      } else {
        view.unfocus();
      }
    }

    return true;
  }

  @Override
  public void setScale(float newScale) {
    this.scale = newScale;

    // The window handler will now take care of rescaling
    handle(new FramebufferSizeEvent(width, height));
  }

  @AssistedFactory(UltralightWindowWebView.class)
  public interface Factory {
    /**
     * Creates a new {@link UltralightWindowWebView} instance with the specified initial width and height.
     *
     * @param initialWidth  The initial with of the view
     * @param initialHeight The initial height of the view
     * @param transparent   If the view should be transparent
     * @return The created view
     */
    UltralightWindowWebView create(
        @Assisted("initialWidth") int initialWidth,
        @Assisted("initialHeight") int initialHeight,
        @Assisted("transparent") boolean transparent
    );
  }
}
