package net.labyfy.internal.component.config.transform;

import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import net.labyfy.component.config.generator.GeneratingConfig;
import net.labyfy.component.config.generator.method.ConfigMethod;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.javassist.ClassTransform;
import net.labyfy.component.transform.javassist.ClassTransformContext;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.MINECRAFT_TRANSFORMER_SERVICE_PRIORITY;
import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.MINECRAFT_TRANSFORMER_SERVICE_ROUND;

@Singleton
@AutoLoad(round = MINECRAFT_TRANSFORMER_SERVICE_ROUND, priority = MINECRAFT_TRANSFORMER_SERVICE_PRIORITY)
public class ConfigTransformer {

  private final Collection<PendingTransform> pendingTransforms;

  public ConfigTransformer() {
    this.pendingTransforms = new CopyOnWriteArrayList<>();
  }

  public void addToTransformations(GeneratingConfig config, ConfigMethod method) {
    this.pendingTransforms.add(new PendingTransform(config, method));
  }

  @ClassTransform
  public void transformConfigs(ClassTransformContext context) throws CannotCompileException, NotFoundException {
    // TODO this doesn't work yet because of the current implementation/service system

    for (PendingTransform transform : this.pendingTransforms) {
      ConfigMethod method = transform.getMethod();
      if (!method.getDeclaringClass().subclassOf(context.getCtClass())) {
        continue;
      }

      method.implementExistingMethods(context.getCtClass(), transform.getConfig());
    }

  }

}
