package net.labyfy.component.tasks.v1_15_1;

import javassist.CannotCompileException;
import net.labyfy.component.inject.InjectionHolder;
import net.labyfy.component.tasks.TaskExecutor;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import javax.inject.Singleton;

@Singleton
public class DefaultTasks {

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

  public static void notify(String task) {
    InjectionHolder.getInjectedInstance(TaskExecutor.class).execute(task);
  }
}
