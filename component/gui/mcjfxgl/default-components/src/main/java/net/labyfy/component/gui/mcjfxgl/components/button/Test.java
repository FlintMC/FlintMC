package net.labyfy.component.gui.mcjfxgl.components.button;

import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.mcjfxgl.McJfxGLScene;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Gui(Guis.GUI_MAIN_MENU)
public class Test {

  private final McJfxGLScene scene;
  private final Button.Factory factory;

  @Inject
  private Test(McJfxGLScene.Factory mcJfxGLSceneFactory, Button.Factory factory) {
    this.factory = factory;
    this.scene =
            mcJfxGLSceneFactory.create(
                    () -> {
                      BorderPane borderPane = new BorderPane();
                      borderPane.setBackground(Background.EMPTY);
                      borderPane.setCenter(this.factory.create().getControl());
                      return borderPane;
                    });
  }

  @GuiRenderState(GuiRenderState.Type.INIT)
  public void init(GuiAdapter guiAdapter) {
    guiAdapter.addComponent(this.scene);
    guiAdapter.initComponents();
  }

  @GuiRenderState(GuiRenderState.Type.RENDER)
  public void render(GuiAdapter guiAdapter) {
    guiAdapter.drawComponents();
  }
}
