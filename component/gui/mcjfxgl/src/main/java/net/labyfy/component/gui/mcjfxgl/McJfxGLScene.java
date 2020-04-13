package net.labyfy.component.gui.mcjfxgl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import cuchaz.jfxgl.JFXGL;
import javafx.scene.Node;
import javafx.scene.Parent;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.concurrent.Callable;

public class McJfxGLScene implements GuiComponent {

  private final MinecraftWindow minecraftWindow;
  private final McJfxGLApplication mcJfxGLApplication;
  private final Callable<Parent> parentCallable;
  private Parent parent;

  @AssistedInject
  private McJfxGLScene(
      MinecraftWindow minecraftWindow,
      McJfxGLApplication mcJfxGLApplication,
      @Assisted Callable<Parent> parentCallable) {
    this.minecraftWindow = minecraftWindow;
    this.mcJfxGLApplication = mcJfxGLApplication;
    this.parentCallable = parentCallable;
  }

  @AssistedFactory(McJfxGLScene.class)
  public interface Factory {
    McJfxGLScene create(Callable<Parent> parentCallable);
  }

  public void init(GuiAdapter adapter) {
    try {
      if (this.parent == null) {
        this.parent = this.parentCallable.call();
        this.mcJfxGLApplication.setParent(this.parent);
      }

      for (Node node : this.parent.getChildrenUnmodifiable()) {
        if (node instanceof GuiComponent) {
          ((GuiComponent) node).init(adapter.getChild());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void render(GuiAdapter adapter) {
    this.parent.resizeRelocate(
        0, 0, minecraftWindow.getScaledWidth() * 4, minecraftWindow.getScaledHeight() * 4);
    this.parent.setTranslateX(
        this.parent.getLayoutBounds().getWidth() * (minecraftWindow.getScaleFactor() - 4) * 0.125);
    this.parent.setTranslateY(
        this.parent.getLayoutBounds().getHeight() * (minecraftWindow.getScaleFactor() - 4) * 0.125);

    this.parent.setScaleX(minecraftWindow.getScaleFactor() * 0.25f);
    this.parent.setScaleY(minecraftWindow.getScaleFactor() * 0.25f);

    for (Node node : this.parent.getChildrenUnmodifiable()) {
      if (node instanceof GuiComponent) {
        ((GuiComponent) node).render(adapter.getChild());
      }
    }

    JFXGL.render();
  }

  public Parent getParent() {
    return parent;
  }
}
