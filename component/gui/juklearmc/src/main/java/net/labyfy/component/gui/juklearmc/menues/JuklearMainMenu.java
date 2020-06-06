package net.labyfy.component.gui.juklearmc.menues;

import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.layout.JuklearPanelFlags;
import net.janrupf.juklear.layout.component.JuklearWindow;
import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.gui.juklearmc.JuklearMC;
import net.labyfy.component.gui.juklearmc.wrapper.ScreenSizeProvider;
import net.labyfy.component.transform.hook.Hook;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Gui(Guis.GUI_MAIN_MENU)
public class JuklearMainMenu {
  private final JuklearMC juklearMC;
  private final JuklearWindow window;

  @Inject
  private JuklearMainMenu(JuklearMC juklearMC) {
    this.juklearMC = juklearMC;
    JuklearContext context = juklearMC.getContext();

    this.window = new JuklearWindow("Hello, here is Juklear in Minecraft!");
    this.window.addFlag(JuklearPanelFlags.TITLE);
    this.window.addFlag(JuklearPanelFlags.BORDER);
    context.addTopLevel(window);
  }

  @GuiRenderState(value = GuiRenderState.Type.RENDER, executionTime = Hook.ExecutionTime.AFTER)
  public void render(@Named("instance") Object instance) {
    ScreenSizeProvider screenSizeProvider = (ScreenSizeProvider) instance;
    juklearMC.draw(screenSizeProvider.getWidth(), screenSizeProvider.getHeight());
  }
}
