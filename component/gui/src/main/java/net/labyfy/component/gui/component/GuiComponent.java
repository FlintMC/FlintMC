package net.labyfy.component.gui.component;

import net.labyfy.component.gui.adapter.GuiAdapter;

public interface GuiComponent {

  default void init(GuiAdapter adapter) {
  }

  default void render(GuiAdapter adapter) {
  }

}
