package net.labyfy.component.gui.mcjfxgl;

import cuchaz.jfxgl.JFXGL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javassist.CtBehavior;
import javassist.CtField;
import javassist.Modifier;
import net.labyfy.component.gui.MinecraftWindow;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CountDownLatch;

@Singleton
public class McJfxGLApplication extends Application {

  private final MinecraftWindow minecraftWindow;
  private Stage stage;

  @Inject
  private McJfxGLApplication(MinecraftWindow minecraftWindow) {
    this.minecraftWindow = minecraftWindow;
  }

  public void start(Stage stage) {
    this.stage = stage;
    this.stage.initStyle(StageStyle.TRANSPARENT);
  }

  protected void setParent(Parent parent) {
    Platform.runLater(
        () -> {
          Scene scene = new Scene(parent, 1, 1, false, SceneAntialiasing.DISABLED);
          // TODO force Antialiasing -.- it is not supported by this stupid library
          scene.setFill(null);
          stage.setScene(scene);
          stage.show();
        });
  }

  public Stage getStage() {
    return stage;
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  @TaskBody
  public void initialize() {
    JFXGL.start(this.minecraftWindow.getHandle(), new String[] {}, this);
  }

  @ClassTransform({
    "com.sun.javafx.tk.quantum.QuantumToolkit",
    "com.sun.javafx.tk.quantum.QuantumRenderer",
    "com.sun.javafx.tk.quantum.PaintCollector",
    "com.sun.prism.es2.GLFactory"
  })
  public void fixJfxGL(ClassTransformContext context) {
    context.getCtClass().setModifiers(this.modify(context.getCtClass().getModifiers()));

    for (CtBehavior behavior : context.getCtClass().getDeclaredBehaviors()) {
      behavior.setModifiers(this.modify(behavior.getModifiers()));
    }
    for (CtField field : context.getCtClass().getDeclaredFields()) {
      field.setModifiers(this.modify(field.getModifiers()));
    }
  }

  private int modify(int modifiers) {
    modifiers &= ~Modifier.PRIVATE;
    modifiers &= ~Modifier.PROTECTED;
    modifiers &= ~Modifier.FINAL;
    modifiers |= Modifier.PUBLIC;
    return modifiers;
  }

  public static void runAndWait(Runnable action) {
    if (action == null) throw new NullPointerException("action");

    // run synchronously on JavaFX thread
    if (Platform.isFxApplicationThread()) {
      action.run();
      return;
    }

    // queue on JavaFX thread and wait for completion
    final CountDownLatch doneLatch = new CountDownLatch(1);
    Platform.runLater(
        () -> {
          try {
            action.run();
          } finally {
            doneLatch.countDown();
          }
        });

    JFXGL.render();
    try {
      doneLatch.await();
    } catch (InterruptedException e) {
      // ignore exception
    }
  }
}
