package net.labyfy.component.gui.mcjfxgl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import cuchaz.jfxgl.JFXGL;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

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

      this.init(adapter, this.parent);

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

    this.render(adapter, this.parent);
    JFXGL.render();
  }

  private void init(GuiAdapter adapter, Parent parent){
    for (Node node : parent.getChildrenUnmodifiable()) {
      if (node instanceof Control) {
        if (((Control) node).getSkin() instanceof GuiComponent) {
          ((GuiComponent) ((Control) node).getSkin()).init(adapter.getChild());
        }
      }
      if (node instanceof GuiComponent) {
        ((GuiComponent) node).init(adapter.getChild());
      }
      if (node instanceof Parent) {
        this.init(adapter.getChild(), (Parent) node);
      }
    }
  }

  private void render(GuiAdapter adapter, Parent parent) {

    for (Node node : parent.getChildrenUnmodifiable()) {
      if (node instanceof GuiComponent) {
        ((GuiComponent) node).render(adapter.getChild());
      }
      if (node instanceof Control) {
        if (((Control) node).getSkin() instanceof GuiComponent) {
          ((GuiComponent) ((Control) node).getSkin()).render(adapter.getChild());
        }
      }
      if (node instanceof Parent) {
        this.render(adapter.getChild(), (Parent) node);
      }
    }

  }


  public Parent getParent() {
    return parent;
  }
}
