package net.labyfy.component.gui.mcjfxgl;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import net.labyfy.component.game.info.MinecraftWindow;
import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.gui.adapter.GuiAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Test {

  private final McJfxGLScene.Factory mcJfxGLSceneFactory;

  @Inject
  private Test(McJfxGLScene.Factory mcJfxGLSceneFactory) {
    this.mcJfxGLSceneFactory = mcJfxGLSceneFactory;
  }

  @Gui(Guis.GUI_MAIN_MENU)
  @GuiRenderState(GuiRenderState.Type.INIT)
  public void init(GuiAdapter guiAdapter, MinecraftWindow minecraftWindow) {
    System.out.println("Init");
    guiAdapter.addComponent(
        mcJfxGLSceneFactory.create(
            () -> {
              VBox vBox = new VBox();
              vBox.setBackground(Background.EMPTY);
              vBox.setMinWidth(minecraftWindow.getWidth());
              vBox.setMaxWidth(minecraftWindow.getWidth());

              vBox.setMinHeight(minecraftWindow.getHeight());
              vBox.setMaxHeight(minecraftWindow.getHeight());
              vBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                      + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                      + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
              return vBox;
            }));
    guiAdapter.initComponents();
  }

  @Gui(Guis.GUI_MAIN_MENU)
  @GuiRenderState(GuiRenderState.Type.RENDER)
  public void render(GuiAdapter guiAdapter) {
    guiAdapter.drawComponents();
  }
}
