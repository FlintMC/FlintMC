package net.labyfy.internal.webgui.ultralight.view;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.transform.hook.Hook;
import net.labyfy.webgui.WebGuiView;

/**
 * Main view of the Ultralight web content.
 */
@Singleton
public class UltralightMainWebGuiView implements GuiComponent, WebGuiView {
  @Inject
  private UltralightMainWebGuiView() {}

  @Override
  public void screenChanged(ScreenName newScreen) {

  }

  @Override
  public boolean shouldRender(Hook.ExecutionTime executionTime, RenderExecution execution) {
    // TODO: Maybe allow the Javascript running in the view to signal whether transparency is required, and if
    //       it is not required, cancel the rendering of other stuff and render in the BEFORE ExecutionTime?
    return executionTime == Hook.ExecutionTime.AFTER;
  }

  @Override
  public void render(RenderExecution execution) {

  }

  @Override
  public void close() {
    // TODO: Maybe allow this?
    throw new UnsupportedOperationException("Can't close the main view, exit Minecraft via other means instead");
  }
}
