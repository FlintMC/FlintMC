package net.labyfy.component.gui.mcjfxgl.labymod;

import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import net.labyfy.base.structure.identifier.IgnoreInitialization;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.mcjfxgl.McJfxGLApplication;
import net.labyfy.component.gui.mcjfxgl.component.labeled.button.Button;
import net.labyfy.component.inject.InjectionHolder;

@IgnoreInitialization
public class MinecraftMainMenuSkin extends SkinBase<MainMenu.Handle> implements GuiComponent {

  protected final StackPane stackPane;
  protected final MainMenu mainMenu;
  protected final Button.Factory buttonFactory;
  protected final McJfxGLApplication application;
  protected final MinecraftWindow minecraftWindow;
  protected final Button multiPlayerButton;
  protected final Button singlePlayerButton;
  protected final Button optionsButton;
  protected final Button quitButton;

  /**
   * At the time it is required to not use AssistedFactories, because they cannot handle javafx
   * classes. This might get fixed in the future.
   */
  protected MinecraftMainMenuSkin(MainMenu.Handle control) {
    super(control);
    if (this.getClass().equals(MinecraftMainMenuSkin.class)) {
      System.out.println("Create Minecraft main menu");
    }
    this.stackPane = new StackPane();
    this.stackPane.setAlignment(Pos.TOP_LEFT);

    this.mainMenu = (MainMenu) control.getComponent();
    this.buttonFactory = InjectionHolder.getInjectedInstance(Button.Factory.class);
    this.minecraftWindow = InjectionHolder.getInjectedInstance(MinecraftWindow.class);
    this.application = InjectionHolder.getInjectedInstance(McJfxGLApplication.class);

    this.multiPlayerButton =
        buttonFactory.create().setText("Multiplayer").setWidth(800).setHeight(80);
    this.singlePlayerButton =
        buttonFactory.create().setText("Singleplayer").setWidth(800).setHeight(80);
    this.optionsButton = buttonFactory.create().setText("Options").setWidth(392).setHeight(80);
    this.quitButton = buttonFactory.create().setText("Quit Game").setWidth(392).setHeight(80);

    this.getChildren().clear();
    this.stackPane
        .getChildren()
        .addAll(
            this.multiPlayerButton.getControl(),
            this.singlePlayerButton.getControl(),
            this.optionsButton.getControl(),
            this.quitButton.getControl());
    this.getChildren().add(this.stackPane);
  }

  public void render(GuiAdapter adapter) {
    this.stackPane.setMinSize(
        this.minecraftWindow.getScaledWidth() * 4, this.minecraftWindow.getScaledHeight() * 4);
    this.stackPane.setMaxSize(
        this.minecraftWindow.getScaledWidth() * 4, this.minecraftWindow.getScaledHeight() * 4);

    this.singlePlayerButton.setTranslateX((this.minecraftWindow.getScaledWidth() / 2f - 100) * 4);
    this.singlePlayerButton.setTranslateY((this.minecraftWindow.getScaledHeight() / 4 + 53) * 4);

    this.multiPlayerButton.setTranslateX((this.minecraftWindow.getScaledWidth() / 2f - 100) * 4);
    this.multiPlayerButton.setTranslateY((this.minecraftWindow.getScaledHeight() / 4 + 77) * 4);

    this.optionsButton.setTranslateX((this.minecraftWindow.getScaledWidth() / 2f - 100) * 4f);
    this.optionsButton.setTranslateY((this.minecraftWindow.getScaledHeight() / 4 + 101) * 4);

    this.quitButton.setTranslateX((this.minecraftWindow.getScaledWidth() / 2f + 2) * 4f);
    this.quitButton.setTranslateY((this.minecraftWindow.getScaledHeight() / 4 + 101) * 4);
  }
}
