package net.labyfy.component.gui.mcjfxgl;

import cuchaz.jfxgl.JFXGL;
import net.labyfy.component.game.info.MinecraftWindow;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class McJfxGLInitializer {

  private final MinecraftWindow minecraftWindow;
  private boolean initialized;

  @Inject
  private McJfxGLInitializer(MinecraftWindow minecraftWindow) {
    this.minecraftWindow = minecraftWindow;
  }

  public void initialize(McJfxGLApplication mcJfxGLApplication) {
    if (initialized) return;
    initialized = true;
    System.out.println("Initialized javafx");
    JFXGL.start(this.minecraftWindow.getHandle(), new String[] {}, mcJfxGLApplication);
  }
}
