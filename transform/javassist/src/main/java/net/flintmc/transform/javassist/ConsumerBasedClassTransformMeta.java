package net.flintmc.transform.javassist;

import net.flintmc.framework.inject.assisted.Assisted;
import javassist.CtClass;
import net.flintmc.framework.inject.assisted.AssistedFactory;

import java.util.function.Consumer;

public interface ConsumerBasedClassTransformMeta extends ClassTransformMeta {

  //@AssistedFactory(ConsumerBasedClassTransformMeta.class)
  interface Factory {
    ConsumerBasedClassTransformMeta create(
        CtClass ctClass,
        int priority,
        Consumer<ClassTransformContext> execution);
  }
}
