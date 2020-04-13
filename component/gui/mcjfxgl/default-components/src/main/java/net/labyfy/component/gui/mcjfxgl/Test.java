package net.labyfy.component.gui.mcjfxgl;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.mcjfxgl.component.labeled.button.Button;
import net.labyfy.component.resources.ResourceLocationProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Gui(Guis.GUI_MAIN_MENU)
public class Test {

  private final McJfxGLSceneRepository sceneRepository;
  private final Button.Factory buttonFactory;
  private final ResourceLocationProvider resourceLocationProvider;
  private Button multiPlayerButton;
  private ImageView background;

  @Inject
  private Test(McJfxGLSceneRepository sceneRepository, Button.Factory buttonFactory, ResourceLocationProvider resourceLocationProvider) {
    this.sceneRepository = sceneRepository;
    this.buttonFactory = buttonFactory;
    this.resourceLocationProvider = resourceLocationProvider;
  }

  private boolean skipped;

  @GuiRenderState(GuiRenderState.Type.INIT)
  public void init(GuiAdapter guiAdapter) {
    if (!skipped) {
      skipped = true;
      return;
    }
    guiAdapter.addComponent(
        sceneRepository.createOrGet("main_menu", factory -> factory.create(
            () -> {
              StackPane stackPane = new StackPane();
              stackPane.setBackground(Background.EMPTY);
              stackPane.setAlignment(Pos.TOP_CENTER);

              this.multiPlayerButton = this.buttonFactory.create();
              this.multiPlayerButton
                  .setText("Multiplayer")
                  .setWidth(800)
                  .setHeight(80);

              BorderPane borderPane = new BorderPane();
              borderPane.setBackground(new Background(new BackgroundFill(new Color(15f / 255f, 15f / 255f, 40f / 255f, 1), CornerRadii.EMPTY, Insets.EMPTY)));

              this.background = new ImageView(new Image(this.resourceLocationProvider.get("labymod/textures/gui/background.png").openInputStream()));
              this.background.setOpacity(0.18f);
              stackPane.getChildren().addAll(borderPane, this.background, this.multiPlayerButton.getControl());
              return stackPane;
            })));

    guiAdapter.initComponents();
  }

  @GuiRenderState(GuiRenderState.Type.RENDER)
  public void render(GuiAdapter guiAdapter, MinecraftWindow minecraftWindow, McJfxGLApplication application) {
    this.background.setPreserveRatio(true);
    this.multiPlayerButton.setTranslateY((minecraftWindow.getScaledHeight() / 4 + (48 + 23)) * 4);

    if (this.background.getImage().getHeight() / minecraftWindow.getScaledHeight() < this.background.getImage().getWidth() / minecraftWindow.getScaledWidth()) {
      this.background.setFitHeight(minecraftWindow.getScaledHeight() * 4);
      this.background.setFitWidth(0);
    } else {
      this.background.setFitHeight(0);
      this.background.setFitWidth(minecraftWindow.getScaledWidth() * 4);
    }

    guiAdapter.drawComponents();
  }
}
