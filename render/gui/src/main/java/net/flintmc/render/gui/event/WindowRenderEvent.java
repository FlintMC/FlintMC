package net.flintmc.render.gui.event;

import net.flintmc.render.gui.windowing.WindowRenderer;

public interface WindowRenderEvent extends GuiEvent {

  WindowRenderer getWindowRenderer();
}
