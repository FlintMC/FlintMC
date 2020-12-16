package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.Window;
import net.flintmc.render.gui.windowing.WindowRenderer;

/** Gets fired when a window renderer is being called (PRE and POST). */
public class WindowRenderEvent extends DefaultGuiEvent implements GuiEvent {

  private final WindowRenderer windowRenderer;

  /**
   * Constructs a new {@link WindowRenderEvent} for the given window and renderer.
   *
   * @param window The non-null window to be rendered
   * @param windowRenderer The non-null renderer that renders the window
   */
  public WindowRenderEvent(Window window, WindowRenderer windowRenderer) {
    super(window);
    this.windowRenderer = windowRenderer;
  }

  /** @return the {@link WindowRenderer} that is about to render (or just rendered). */
  public WindowRenderer getWindowRenderer() {
    return this.windowRenderer;
  }
}
