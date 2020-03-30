package net.labyfy.component.gui.mcjfxgl;

import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.labyfy.component.gui.Gui;
import net.labyfy.component.gui.GuiRenderState;
import net.labyfy.component.gui.Guis;
import net.labyfy.component.gui.adapter.GuiAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Gui(Guis.GUI_MAIN_MENU)
public class Test {

  private final McJfxGLScene scene;

  @Inject
  private Test(McJfxGLScene.Factory mcJfxGLSceneFactory) {
    this.scene =
        mcJfxGLSceneFactory.create(
            () -> {
              BorderPane borderPane = new BorderPane();
              borderPane.setCenter(new TestComponent());
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
