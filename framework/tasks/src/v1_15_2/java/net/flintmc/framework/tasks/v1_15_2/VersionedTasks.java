package net.flintmc.framework.tasks.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.tasks.TaskExecutionException;
import net.flintmc.framework.tasks.TaskExecutor;
import net.flintmc.framework.tasks.Tasks;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.util.mappings.ClassMappingProvider;

@Singleton
public class VersionedTasks {

  private final ClassMappingProvider classMappingProvider;

  @Inject
  private VersionedTasks(ClassMappingProvider classMappingProvider) {
    this.classMappingProvider = classMappingProvider;
  }

  public static void notify(Tasks task) throws TaskExecutionException {
    InjectionHolder.getInjectedInstance(TaskExecutor.class).execute(task);
  }

  @ClassTransform(value = "net.minecraft.client.Minecraft", version = "1.15.2")
  public void transform(ClassTransformContext classTransformContext) throws CannotCompileException {
    classTransformContext
        .getCtClass()
        .getDeclaredConstructors()[0]
        .insertAfter(
            "net.flintmc.framework.tasks.v1_15_2.VersionedTasks.notify(net.flintmc.framework.tasks.Tasks.POST_MINECRAFT_INITIALIZE);");
  }

  @ClassTransform(value = "net.minecraft.client.MainWindow", version = "1.15.2")
  public void transformOpenGlInitialize(ClassTransformContext classTransformContext)
      throws CannotCompileException, NotFoundException {
    classTransformContext
        .getCtClass()
        .getDeclaredMethod(
            classMappingProvider
                .get("net.minecraft.client.MainWindow")
                .getMethod("setLogOnGlError")
                .getName())
        .insertAfter(
            "net.flintmc.framework.tasks.v1_15_2.VersionedTasks.notify(net.flintmc.framework.tasks.Tasks.POST_OPEN_GL_INITIALIZE);");
  }
}
