package net.labyfy.internal.webgui.ultralight.view;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.event.FramebufferSizeEvent;
import net.labyfy.component.gui.event.GuiEvent;
import net.labyfy.component.gui.event.GuiEventListener;
import net.labyfy.component.gui.event.WindowFocusEvent;
import net.labyfy.component.gui.windowing.MinecraftWindow;
import net.labyfy.component.gui.windowing.WindowRenderer;
import net.labyfy.internal.webgui.ultralight.UltralightWebGuiController;
import net.labyfy.webgui.MainWebGuiView;

/** Extension of a windowed Ultralight view for the main window. */
@Singleton
public class UltralightMainWebGuiView
    implements MainWebGuiView, UltralightWebGuiView, WindowRenderer, GuiEventListener {
  private final UltralightWebGuiController controller;
  private final MinecraftWindow minecraftWindow;

  private final UltralightWindowWebView windowWebView;

  private boolean allowFocus;

  @Inject
  protected UltralightMainWebGuiView(
      UltralightWindowWebView.Factory windowWebViewFactory,
      UltralightWebGuiController controller,
      MinecraftWindow minecraftWindow) {
    this.windowWebView =
        windowWebViewFactory.create(
            minecraftWindow.getFramebufferWidth(), minecraftWindow.getFramebufferHeight(), true);
    this.controller = controller;
    this.minecraftWindow = minecraftWindow;
    this.allowFocus = true;
  }

  @Override
  public void initialize() {
    this.windowWebView.initialize();
  }

  @Override
  public boolean isIntrusive() {
    return this.windowWebView.isIntrusive();
  }

  @Override
  public void render() {
    // Trigger rendering of all views now
    this.controller.renderAll();
    this.windowWebView.render();
  }

  @Override
  public void cleanup() {
    this.windowWebView.cleanup();
  }

  @Override
  public boolean handle(GuiEvent event) {
    if (event instanceof FramebufferSizeEvent) {
      // The framebuffer size event needs to be handled always (or the content will look weird)
      this.windowWebView.handle(event);
      return false;
    }

    if (!allowFocus) {
      return false; // Pass through events
    }

    return this.windowWebView.handle(event);
  }

  @Override
  public void setAllowFocus(boolean allow) {
    this.allowFocus = allow;

    handle(new WindowFocusEvent(allowFocus && minecraftWindow.isFocused()));
  }

  @Override
  public void dataReadyOnSurface() {
    this.windowWebView.dataReadyOnSurface();
  }

  @Override
  public void dataReadyOnOpenGLTexture(int textureId) {
    this.windowWebView.dataReadyOnOpenGLTexture(textureId);
  }

  @Override
  public void close() {
    this.windowWebView.close();
  }

  @Override
  public void setTransparent(boolean transparent) {
    this.windowWebView.setTransparent(transparent);
  }

  @Override
  public void setURL(String url) {
    this.windowWebView.setURL(url);
  }

  @Override
  public void setScale(float scale) {
    this.windowWebView.setScale(scale);
  }
}
