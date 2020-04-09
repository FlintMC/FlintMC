package net.labyfy.component.gui;

import net.labyfy.component.transform.hook.Hook;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class GuiInterceptor {

  @Inject private GuiService guiService;

  public final void notifyGuis(
      Hook.ExecutionTime executionTime, GuiRenderState.Type guiRenderState, Object screen, Map<String, Object> args) {
    this.guiService.notify(executionTime, guiRenderState, screen, args);
  }
}
