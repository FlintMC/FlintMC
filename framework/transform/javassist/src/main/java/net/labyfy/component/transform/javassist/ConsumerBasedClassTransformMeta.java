package net.labyfy.component.transform.javassist;

import com.google.inject.assistedinject.Assisted;
import javassist.CtClass;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.util.function.Consumer;

public interface ConsumerBasedClassTransformMeta extends ClassTransformMeta {

  @AssistedFactory(ConsumerBasedClassTransformMeta.class)
  interface Factory {
    ConsumerBasedClassTransformMeta create(
        @Assisted CtClass ctClass,
        @Assisted int priority,
        @Assisted Consumer<ClassTransformContext> execution);
  }
}
