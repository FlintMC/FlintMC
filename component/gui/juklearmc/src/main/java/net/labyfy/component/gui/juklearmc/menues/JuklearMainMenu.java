package net.labyfy.component.gui.juklearmc.menues;

import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.layout.JuklearPanelFlags;
import net.janrupf.juklear.layout.component.JuklearWindow;
import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.juklearmc.JuklearMC;
import net.labyfy.component.transform.hook.Hook;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Gui(Guis.GUI_MAIN_MENU)
public class JuklearMainMenu {
  private final MinecraftWindow minecraftWindow;
  private final JuklearMC juklearMC;
  private final JuklearWindow window;

  @Inject
  private JuklearMainMenu(MinecraftWindow minecraftWindow, JuklearMC juklearMC) {
    this.minecraftWindow = minecraftWindow;
    this.juklearMC = juklearMC;
    JuklearContext context = juklearMC.getContext();

    this.window = new JuklearWindow("Hello, here is Juklear in Minecraft!", 0, 0, 600, 400);
    this.window.addFlag(JuklearPanelFlags.TITLE);
    this.window.addFlag(JuklearPanelFlags.BORDER);
    context.addTopLevel(window);
  }

  @GuiRenderState(value = GuiRenderState.Type.RENDER, executionTime = Hook.ExecutionTime.AFTER)
  public void render() {
    juklearMC.draw((int) minecraftWindow.getWidth(), (int) minecraftWindow.getHeight());
  }
}
