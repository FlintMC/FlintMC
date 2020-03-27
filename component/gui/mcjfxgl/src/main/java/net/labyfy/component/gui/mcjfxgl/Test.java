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
              Button hallo_welt = new Button("Hallo welt");
              hallo_welt.setMinWidth(minecraftWindow.getWidth());
              hallo_welt.setMaxWidth(minecraftWindow.getWidth());

              hallo_welt.setMinHeight(minecraftWindow.getHeight());
              hallo_welt.setMaxHeight(minecraftWindow.getHeight());
              vBox.getChildren().add(hallo_welt);
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
