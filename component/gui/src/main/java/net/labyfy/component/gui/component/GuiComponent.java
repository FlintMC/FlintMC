package net.labyfy.component.gui.component;

import net.labyfy.component.gui.adapter.GuiAdapter;

public interface GuiComponent {

  void init(GuiAdapter adapter);

  void render(GuiAdapter adapter);

}
