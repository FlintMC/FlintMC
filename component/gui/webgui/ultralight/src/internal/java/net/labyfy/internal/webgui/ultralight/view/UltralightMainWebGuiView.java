package net.labyfy.internal.webgui.ultralight.view;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.event.FramebufferSizeEvent;
import net.labyfy.component.gui.event.GuiEvent;
import net.labyfy.component.gui.event.GuiEventListener;
import net.labyfy.component.gui.event.WindowFocusEvent;
import net.labyfy.component.gui.windowing.MinecraftWindow;
import net.labyfy.component.gui.windowing.WindowRenderer;
import net.labyfy.webgui.MainWebGuiView;

/** Extension of a windowed Ultralight view for the main window. */
@Singleton
public class UltralightMainWebGuiView
    implements MainWebGuiView, UltralightWebGuiView, WindowRenderer, GuiEventListener {
  private final MinecraftWindow minecraftWindow;

  private final UltralightWindowWebView windowWebView;

  private boolean transparent;
  private boolean allowFocus;
  private boolean visible;

  @Inject
  protected UltralightMainWebGuiView(
      UltralightWindowWebView.Factory windowWebViewFactory, MinecraftWindow minecraftWindow) {
    this.windowWebView =
        windowWebViewFactory.create(
            minecraftWindow.getFramebufferWidth(),
            minecraftWindow.getFramebufferHeight(),
            true,
            true);
    this.minecraftWindow = minecraftWindow;
    this.transparent = true;
    this.allowFocus = true;
    this.visible = true;
  }

  /** {@inheritDoc} */
  @Override
  public void initialize() {
    this.windowWebView.initialize();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isIntrusive() {
    return this.windowWebView.isIntrusive();
  }

  /** {@inheritDoc} */
  @Override
  public void render() {
    this.windowWebView.render();
  }

  /** {@inheritDoc} */
  @Override
  public void cleanup() {
    this.windowWebView.cleanup();
  }

  /** {@inheritDoc} */
  @Override
  public boolean handle(GuiEvent event) {
    if (event instanceof FramebufferSizeEvent) {
      // The framebuffer size event needs to be handled always (or the content will look weird)
      this.windowWebView.handle(event);
      return false;
    }

    if (!allowFocus || !visible) {
      return false; // Pass through events
    }

    return this.windowWebView.handle(event);
  }

  /** {@inheritDoc} */
  @Override
  public void setAllowFocus(boolean allow) {
    this.allowFocus = allow;

    handle(new WindowFocusEvent(allowFocus && minecraftWindow.isFocused()));
  }

  /** {@inheritDoc} */
  @Override
  public void setVisible(boolean visible) {
    if (this.visible == visible) {
      return;
    }

    this.visible = visible;
    if (this.visible) {
      this.minecraftWindow.addRenderer(this);
      this.minecraftWindow.addEventListener(this);
    } else {
      this.minecraftWindow.removeRenderer(this);
      this.minecraftWindow.removeEventListener(this);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void dataReadyOnSurface() {
    this.windowWebView.dataReadyOnSurface();
  }

  /** {@inheritDoc} */
  @Override
  public void dataReadyOnOpenGLTexture(int textureId) {
    this.windowWebView.dataReadyOnOpenGLTexture(textureId);
  }

  /** {@inheritDoc} */
  @Override
  public void close() {
    this.windowWebView.close();
  }

  /** {@inheritDoc} */
  @Override
  public void setTransparent(boolean transparent) {
    if (this.transparent == transparent) {
      return;
    }

    this.windowWebView.setTransparent(transparent);
    if (visible) {
      // Re-add the renderer to notify about the transparency change
      this.minecraftWindow.removeRenderer(this);
      this.minecraftWindow.addRenderer(this);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setURL(String url) {
    this.windowWebView.setURL(url);
  }

  /** {@inheritDoc} */
  @Override
  public String getURL() {
    return this.windowWebView.getURL();
  }

  /** {@inheritDoc} */
  @Override
  public void setScale(float scale) {
    this.windowWebView.setScale(scale);
  }

  /** {@inheritDoc} */
  @Override
  public <T> void setGlobalJavascriptObject(String key, T value, Class<? extends T> clazz) {
    this.windowWebView.setGlobalJavascriptObject(key, value, clazz);
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (o instanceof UltralightMainWebGuiView) {
      return ((UltralightMainWebGuiView) o).windowWebView.equals(windowWebView);
    }

    return windowWebView.equals(o);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return this.windowWebView.hashCode();
  }

  public UltralightWindowWebView getWindowWebView() {
    return windowWebView;
  }
}
