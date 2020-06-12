package net.labyfy.component.gui.juklearmc.menues;

import net.janrupf.juklear.layout.component.JuklearWindow;
import net.janrupf.juklear.layout.component.base.JuklearTopLevelComponent;
import net.labyfy.component.gui.juklearmc.JuklearScreen;
import net.labyfy.component.gui.name.ScreenName;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;

@Singleton
@JuklearScreen(ScreenName.MAIN_MENU)
public class JuklearMainMenu implements JuklearMCScreen {
  private final JuklearWindow mainWindow;

  @Inject
  private JuklearMainMenu() {
    this.mainWindow = new JuklearWindow("Test");
  }

  @Override
  public Collection<JuklearTopLevelComponent> topLevelComponents() {
    return Collections.singleton(mainWindow);
  }

  @Override
  public void updateSize(int width, int height) {
    // OpenGL and width/height in Minecraft is a bit weird,
    // so overstretch the window by 1 pixel on each side to avoid visual artifacts
    this.mainWindow.setBounds(-1, -1, width + 2, height + 2);
  }
}
