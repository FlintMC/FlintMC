package net.labyfy.internal.webgui.ultralight.view;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.gui.event.*;
import net.labyfy.component.gui.event.input.InputState;
import net.labyfy.component.gui.event.input.Key;
import net.labyfy.component.gui.windowing.WindowRenderer;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.render.shader.ShaderException;
import net.labyfy.component.render.shader.ShaderProgram;
import net.labyfy.component.render.shader.ShaderUniform;
import net.labyfy.component.render.vbo.*;
import net.labyfy.internal.webgui.ultralight.UltralightWebGuiController;
import net.labyfy.internal.webgui.ultralight.event.interop.UltralightWebGuiViewEventInterop;
import net.labyfy.internal.webgui.ultralight.util.UltralightLabyfyBridge;
import net.labymedia.ultralight.Databind;
import net.labymedia.ultralight.DatabindConfiguration;
import net.labymedia.ultralight.UltralightSurface;
import net.labymedia.ultralight.UltralightView;
import net.labymedia.ultralight.input.*;
import net.labymedia.ultralight.javascript.*;
import net.labymedia.ultralight.math.IntRect;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class UltralightWindowWebView
    implements UltralightWebGuiView, WindowRenderer, GuiEventListener {
  private final UltralightView view;
  private final boolean gpuRenderer;

  private int openGLTexture;
  private boolean transparent;

  private final VertexArrayObject.Factory vaoFactory;
  private final VertexBufferObject.Factory vboFactory;
  private final VertexIndexObject.Factory eboFactory;
  private final UltralightWebGuiViewEventInterop eventInterop;

  private VertexArrayObject vao;
  private VertexBufferObject vbo;
  private VertexIndexObject ebo;

  private float scale; // TODO: make configurable

  // Both are cached to prevent JNI calls
  private int width;
  private int height;

  private final ShaderProgram shader;
  private final Databind databind;

  @AssistedInject
  protected UltralightWindowWebView(
      ShaderProgram.Factory shaderFactory,
      ShaderUniform.Factory uniformFactory,
      VertexArrayObject.Factory vaoFactory,
      VertexBufferObject.Factory vboFactory,
      VertexIndexObject.Factory eboFactory,
      UltralightWebGuiViewEventInterop.Factory interopFactory,
      UltralightWebGuiController controller,
      @InjectLogger Logger logger,
      @Assisted("initialWidth") int initialWidth,
      @Assisted("initialHeight") int initialHeight,
      @Assisted("transparent") boolean transparent,
      @Assisted("autoBridgeJava") boolean autoBridgeJava) {
    this.view = controller.getRenderer().createView(initialWidth, initialHeight, transparent);
    this.gpuRenderer = controller.isUsingGPURenderer();
    this.openGLTexture = -1;

    this.scale = 1.0f;

    this.width = initialWidth;
    this.height = initialHeight;

    this.vaoFactory = vaoFactory;
    this.vboFactory = vboFactory;
    this.eboFactory = eboFactory;
    this.eventInterop = interopFactory.create(this);

    this.shader = shaderFactory.create();

    try {
      this.shader.addVertexShader(
          getClass().getResourceAsStream("/shader/ultralight/mainMenu.vsh"));
      this.shader.addFragmentShader(
          getClass().getResourceAsStream("/shader/ultralight/mainMenu.fsh"));
      this.shader.link();
    } catch(ShaderException e) {
      logger.error("Failed load shaders:", e);
    }

    this.databind = new Databind(DatabindConfiguration.builder()
        .automaticPrototype(autoBridgeJava)
        .build());
  }

  @Override
  public void dataReadyOnSurface() {
    // Get the surface
    UltralightSurface surface = view.surface();

    // Retrieve the dirty bounds to check if the data needs to be updated, and if so, what needs to
    // be
    // updated
    IntRect dirtyBounds = surface.dirtyBounds();

    // Bind the OpenGL texture
    glBindTexture(GL_TEXTURE_2D, openGLTexture);

    if(dirtyBounds.isValid()) {
      try {
        // Retrieve the raw image data in BGRA format
        ByteBuffer imageData = surface.lockPixels();
        glPixelStorei(GL_UNPACK_ROW_LENGTH, (int) surface.rowBytes() / 4);
        // Needs updating
        if(dirtyBounds.width() >= width && dirtyBounds.height() >= height) {
          // Perform full upload as the entire image has been invalidated
          glTexImage2D(
              GL_TEXTURE_2D,
              0,
              GL_RGBA8,
              width,
              height,
              0,
              GL_BGRA,
              GL_UNSIGNED_INT_8_8_8_8_REV,
              imageData);

        } else {
          // Partially update the image (improved performance compared to full upload)
          int dirtyX = dirtyBounds.x();
          int dirtyY = dirtyBounds.y();
          int dirtyWidth = dirtyBounds.width();
          int dirtyHeight = dirtyBounds.height();

          // Calculate the offset where the dirty data starts in the buffer
          int startOffset = (int) ((dirtyY * surface.rowBytes()) + dirtyX * 4);

          // Upload the partial data
          glTexSubImage2D(
              GL_TEXTURE_2D, // Upload to our bound texture
              0, // No mipmapping
              dirtyX,
              dirtyY,
              dirtyWidth,
              dirtyHeight,
              GL_BGRA,
              GL_UNSIGNED_INT_8_8_8_8_REV,
              (ByteBuffer) imageData.position(startOffset) // Offset the data pointer
          );
        }
        // Reset the pixels per row value to auto detection, else we cause weird crashes
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
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
    eventInterop.notifyClose(Subscribe.Phase.PRE);
    this.view.setLoadListener(null);
    this.view.setViewListener(null);
    eventInterop.notifyClose(Subscribe.Phase.POST);
  }

  @Override
  public void setTransparent(boolean transparent) {
    this.transparent = transparent;
  }

  @Override
  public void setURL(String url) {
    this.view.loadURL(url);
  }

  @Override
  public String getURL() {
    return this.view.url();
  }

  @Override
  public void initialize() {
    if(gpuRenderer) {
      // If we are already rendering to a OpenGL texture in the renderer, ignore the initialization
      return;
    }

    if(openGLTexture != -1) {
      throw new IllegalStateException("Renderer has been initialized and not been destructed yet");
    }

    openGLTexture = glGenTextures();

    this.vbo = this.vboFactory.create(VertexFormats.POS3F_UV);
    this.ebo = this.eboFactory.create(VboDrawMode.QUADS);

    this.vbo
        .addVertex()
        .position(-1, -1, 0)
        .texture(0, 1)
        .next()
        .position(-1, 1, 0)
        .texture(0, 0)
        .next()
        .position(1, 1, 0)
        .texture(1, 0)
        .next()
        .position(1, -1, 0)
        .texture(1, 1);

    this.ebo.addIndices(0, 1, 2, 3);

    this.vao =
        this.vaoFactory.create(
            this.vbo,
            () -> {
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
            });
  }

  @Override
  public boolean isIntrusive() {
    return true;
  }

  @Override
  public void render() {
    glViewport(0, 0, width, height);
    glScissor(0, 0, width, height);
    glDisable(GL_CULL_FACE);
    glMatrixMode(GL_MODELVIEW);
    glPushMatrix();
    this.shader.useShader();
    glBindTexture(GL_TEXTURE_2D, openGLTexture);
    this.vao.draw(this.ebo);
    glBindTexture(GL_TEXTURE_2D, 0);
    this.shader.stopShader();

    glPopMatrix();
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
              .y((int) evt.getY()));
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
      view.fireKeyEvent(
          new UltralightKeyEvent()
              .type(
                  state == InputState.PRESS || state == InputState.REPEAT
                      ? UltralightKeyEventType.RAW_DOWN
                      : UltralightKeyEventType.UP)
              .virtualKeyCode(ultralightKey)
              .nativeKeyCode(evt.getScancode())
              .keyIdentifier(UltralightKeyEvent.getKeyIdentifierFromVirtualKeyCode(ultralightKey))
              .modifiers(
                  UltralightLabyfyBridge.labyfyToUltralightModifierKeys(evt.getModifierKeys())));

      // Some keys need special treatment, namely <ENTER> and <TAB>
      if((state == InputState.PRESS || state == InputState.REPEAT)
          && (key == Key.ENTER || key == Key.TAB)) {
        // Translate them into their string versions
        String text = key == Key.ENTER ? "\n" : "\t";

        // Fire the extra event
        view.fireKeyEvent(
            new UltralightKeyEvent()
                .type(UltralightKeyEventType.CHAR)
                .text(text)
                .unmodifiedText(text));
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

      view.fireMouseEvent(
          new UltralightMouseEvent()
              .x((int) evt.getX())
              .y((int) evt.getY())
              .button(button)
              .type(
                  evt.getState() == InputState.PRESS
                      ? UltralightMouseEventType.DOWN
                      : UltralightMouseEventType.UP));
    } else if(event instanceof MouseScrolledEvent) {
      // Translate mouse scrolling
      MouseScrolledEvent evt = (MouseScrolledEvent) event;

      view.fireScrollEvent(
          new UltralightScrollEvent()
              .type(UltralightScrollEventType.BY_PIXEL)
              .deltaX((int) evt.getXOffset() * 20)
              .deltaY((int) evt.getYOffset() * 20));
    } else if(event instanceof UnicodeTypedEvent) {
      // Translate normal char input
      UnicodeTypedEvent evt = (UnicodeTypedEvent) event;

      // Convert the codepoint to an UTF-16 Java string
      String text = new String(Character.toChars(evt.getCodepoint()));

      view.fireKeyEvent(
          new UltralightKeyEvent()
              .type(UltralightKeyEventType.CHAR)
              .text(text)
              .unmodifiedText(text));
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

  @Override
  public <T> void setGlobalJavascriptObject(String key, T value, Class<? extends T> clazz) {
    try(JavascriptContextLock lock = this.view.lockJavascriptContext()) {
      JavascriptContext context = lock.getContext();
      JavascriptObject globalObject = context.getGlobalObject();

      JavascriptValue converted = value == null ? context.makeUndefined() :
          databind.getConversionUtils().toJavascript(context, value, clazz);
      globalObject.setProperty(key, converted, JavascriptPropertyAttributes.READ_ONLY);
    }
  }

  @AssistedFactory(UltralightWindowWebView.class)
  public interface Factory {
    /**
     * Creates a new {@link UltralightWindowWebView} instance with the specified initial width and height.
     *
     * @param initialWidth   The initial with of the view
     * @param initialHeight  The initial height of the view
     * @param transparent    If the view should be transparent
     * @param autoBridgeJava If {@code true}, unknown Java objects will be automatically translated when called from
     *                       Javascript (set to {@code false} for externally loaded views!)
     * @return The created view
     */
    UltralightWindowWebView create(
        @Assisted("initialWidth") int initialWidth,
        @Assisted("initialHeight") int initialHeight,
        @Assisted("transparent") boolean transparent,
        @Assisted("autoBridgeJava") boolean autoBridgeJava
    );
  }
}
