package net.labyfy.internal.webgui.ultralight.view;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.event.FramebufferSizeEvent;
import net.labyfy.component.gui.event.GuiEvent;
import net.labyfy.component.gui.event.WindowFocusEvent;
import net.labyfy.component.gui.windowing.MinecraftWindow;
import net.labyfy.component.render.shader.ShaderProgram;
import net.labyfy.component.render.shader.ShaderUniform;
import net.labyfy.component.render.vbo.VertexArrayObject;
import net.labyfy.component.render.vbo.VertexBufferObject;
import net.labyfy.component.render.vbo.VertexIndexObject;
import net.labyfy.internal.webgui.ultralight.UltralightWebGuiController;
import net.labyfy.webgui.MainWebGuiView;

/** Extension of a windowed Ultralight view for the main window. */
@Singleton
public class UltralightMainWebGuiView extends UltralightWindowWebView implements MainWebGuiView {
  private final UltralightWebGuiController controller;
  private final MinecraftWindow minecraftWindow;

  private boolean allowFocus;

  @Inject
  protected UltralightMainWebGuiView(
      ShaderProgram.Factory shaderFactory,
      ShaderUniform.Factory uniformFactory,
      VertexArrayObject.Factory vaoFactory,
      VertexBufferObject.Factory vboFactory,
      VertexIndexObject.Factory eboFactory,
      UltralightWebGuiController controller,
      MinecraftWindow minecraftWindow) {
    super(
        shaderFactory,
        uniformFactory,
        vaoFactory,
        vboFactory,
        eboFactory,
        controller,
        minecraftWindow.getFramebufferWidth(),
        minecraftWindow.getFramebufferHeight(),
        true);
    this.controller = controller;
    this.minecraftWindow = minecraftWindow;
    this.allowFocus = true;
  }

  @Override
  public void render() {
    // Trigger rendering of all views now
    controller.renderAll();
    super.render();
  }

  @Override
  public boolean handle(GuiEvent event) {
    if (event instanceof FramebufferSizeEvent) {
      // The framebuffer size event needs to be handled always (or the content will look weird)
      super.handle(event);
      return false;
    }

    if (!allowFocus) {
      return false; // Pass through events
    }

    return super.handle(event);
  }

  @Override
  public void setAllowFocus(boolean allow) {
    this.allowFocus = allow;

    handle(new WindowFocusEvent(allowFocus && minecraftWindow.isFocused()));
  }
}
