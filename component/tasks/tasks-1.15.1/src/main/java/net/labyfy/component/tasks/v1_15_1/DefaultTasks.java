package net.labyfy.component.tasks.v1_15_1;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.tasks.TaskExecutor;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@AutoLoad
public class DefaultTasks {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private DefaultTasks(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  @ClassTransform(value = "net.minecraft.client.Minecraft", version = "1.15.1")
  public void transform(ClassTransformContext classTransformContext) throws CannotCompileException {
    classTransformContext
        .getCtClass()
        .getDeclaredConstructors()[0]
        .insertAfter(
            "net.labyfy.component.tasks.v1_15_1.DefaultTasks.notify(\""
                + Tasks.POST_MINECRAFT_INITIALIZE
                + "\");");
  }

  @ClassTransform(value = "net.minecraft.client.MainWindow", version = "1.15.1")
  public void transformOpenGlInitialize(ClassTransformContext classTransformContext)
      throws CannotCompileException {
    try {
      classTransformContext
          .getCtClass()
          .getDeclaredMethod(
              classMappingProvider
                  .get("net.minecraft.client.MainWindow")
                  .getMethod("setLogOnGlError")
                  .getName())
          .insertAfter(
              "net.labyfy.component.tasks.v1_15_1.DefaultTasks.notify(\""
                  + Tasks.POST_OPEN_GL_INITIALIZE
                  + "\");");
    } catch (NotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void notify(String task) {
    InjectionHolder.getInjectedInstance(TaskExecutor.class).execute(task);
  }
}
