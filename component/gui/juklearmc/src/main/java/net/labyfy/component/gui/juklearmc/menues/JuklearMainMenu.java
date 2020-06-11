package net.labyfy.component.gui.juklearmc.menues;

import net.janrupf.juklear.JuklearContext;
import net.janrupf.juklear.layout.JuklearPanelFlags;
import net.janrupf.juklear.layout.component.JuklearWindow;
import net.labyfy.component.gui.*;
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

    this.window = new JuklearWindow("", 0, 0, minecraftWindow.getWidth(), minecraftWindow.getHeight());
    this.window.addOwnStyle(context.getStyle().getWindow().getFixedBackground().preparePush(0, 0, 0, 0));
    context.addTopLevel(window);
  }

  @GuiRenderState(value = GuiRenderState.Type.RENDER, executionTime = Hook.ExecutionTime.BEFORE)
  public void render(GuiRenderCancellation cancellation) {
    window.setBounds(0, 0, minecraftWindow.getWidth(), minecraftWindow.getHeight());
    juklearMC.draw((int) minecraftWindow.getWidth(), (int) minecraftWindow.getHeight());

    if(cancellation != null) {
      cancellation.cancel();
    }
  }
}
