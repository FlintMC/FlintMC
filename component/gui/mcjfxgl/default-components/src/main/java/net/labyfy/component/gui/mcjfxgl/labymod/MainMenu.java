package net.labyfy.component.gui.mcjfxgl.labymod;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import javafx.scene.control.Skin;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLComponent;
import net.labyfy.component.gui.mcjfxgl.component.McJfxGLControl;
import net.labyfy.component.gui.mcjfxgl.component.labeled.button.Button;
import net.labyfy.component.inject.assisted.AssistedFactory;

@IgnoreInitialization
public class MainMenu extends McJfxGLComponent<MainMenu> {

  private final Button.Factory buttonFactory;
  private final MinecraftWindow minecraftWindow;

  @Inject
  private MainMenu(MinecraftWindow minecraftWindow, Button.Factory buttonFactory) {
    this.minecraftWindow = minecraftWindow;
    this.buttonFactory = buttonFactory;
  }

  public McJfxGLControl createControl() {
    return new Handle(this);
  }

  @AssistedFactory(MainMenu.class)
  public interface Factory {
    MainMenu create();
  }

  @IgnoreInitialization
  public static class Handle extends McJfxGLControl implements GuiComponent {

    private final MainMenu mainMenu;

    protected Handle(MainMenu mainMenu) {
      super(mainMenu);
      this.mainMenu = mainMenu;
    }

    protected Skin<?> createDefaultSkin() {
      return new MinecraftMainMenuSkin(this);
    }

    public void render(GuiAdapter adapter) {
      super.render(adapter);
    }

    protected Class<? extends Skin<? extends McJfxGLControl>> getDefaultSkinClass() {
      return MinecraftMainMenuSkin.class;
    }
  }
}
