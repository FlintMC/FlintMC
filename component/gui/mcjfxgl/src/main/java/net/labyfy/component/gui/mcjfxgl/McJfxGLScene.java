package net.labyfy.component.gui.mcjfxgl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import cuchaz.jfxgl.JFXGL;
import javafx.scene.Parent;
import net.labyfy.component.game.info.MinecraftWindow;
import net.labyfy.component.gui.adapter.GuiAdapter;
import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.concurrent.Callable;

public class McJfxGLScene implements GuiComponent {

  private final MinecraftWindow minecraftWindow;
  private final McJfxGLApplication mcJfxGLApplication;
  private final McJfxGLInitializer mcJfxGLInitializer;
  private final Callable<Parent> parentCallable;
  private Parent parent;

  @AssistedInject
  private McJfxGLScene(
      MinecraftWindow minecraftWindow,
      McJfxGLApplication mcJfxGLApplication,
      McJfxGLInitializer mcJfxGLInitializer,
      @Assisted Callable<Parent> parentCallable) {
    this.minecraftWindow = minecraftWindow;
    this.mcJfxGLApplication = mcJfxGLApplication;
    this.mcJfxGLInitializer = mcJfxGLInitializer;
    this.parentCallable = parentCallable;
  }

  @AssistedFactory(McJfxGLScene.class)
  public interface Factory {
    McJfxGLScene create(Callable<Parent> parentCallable);
  }

  public void init(GuiAdapter adapter) {
    try {
      this.mcJfxGLInitializer.initialize(
          InjectionHolder.getInjectedInstance(McJfxGLApplication.class));
      this.parent = this.parentCallable.call();
      this.mcJfxGLApplication.setParent(this.parent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void render(GuiAdapter adapter) {
        System.out.println(
            this.minecraftWindow.getWidth() + " " + this.minecraftWindow.getWidth());
    //    this.parent.resizeRelocate(
    //        0, 0, this.minecraftWindow.getWidth(), this.minecraftWindow.getHeight());
    //    this.parent.setTranslateX(
    //        this.parent.getLayoutBounds().getWidth() * (minecraftWindow.getScaleFactor() - 4) *
    // 0.25);
    //    this.parent.setTranslateY(
    //        this.parent.getLayoutBounds().getHeight() * (minecraftWindow.getScaleFactor() - 4) *
    // 0.25);
    //    GL11.glPushMatrix();
    //
    //    this.parent.setTranslateX(200);
    //    this.parent.setScaleX(minecraftWindow.getScaleFactor() * 0.25f);
    //    this.parent.setScaleY(minecraftWindow.getScaleFactor() * 0.25f);
    //        if (initialized) this.labyJavaFxInjector.render(this);
    JFXGL.render();
    //    GL11.glPopMatrix();
  }
}
