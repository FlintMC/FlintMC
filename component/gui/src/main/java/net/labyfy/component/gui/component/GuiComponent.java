package net.labyfy.component.gui.component;

import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.gui.name.ScreenName;
import net.labyfy.component.transform.hook.Hook;

public interface GuiComponent {
  void screenChanged(ScreenName newScreen);
  boolean shouldRender(Hook.ExecutionTime executionTime, RenderExecution execution);
  void render(RenderExecution execution);
  void frameDone();
}
