package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.WindowRenderer;

/** Gets fired when a window renderer is being called (PRE and POST). */
public interface WindowRenderEvent extends GuiEvent {

  /** @return the {@link WindowRenderer} that is about to render (or just rendered). */
  WindowRenderer getWindowRenderer();
}
