package net.flintmc.transform.javassist.internal.factory;

import com.google.inject.Inject;
import javassist.CtClass;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.ConsumerBasedClassTransformMeta;
import net.flintmc.transform.javassist.internal.DefaultConsumerBasedClassTransformMeta;

import java.util.function.Consumer;

@Implement(ConsumerBasedClassTransformMeta.Factory.class)
public class DefaultConsumerBasedClassTransformMetaFactory implements ConsumerBasedClassTransformMeta.Factory {

  private final ClassTransformContext.Factory classTransformContextFactory;

  @Inject
  private DefaultConsumerBasedClassTransformMetaFactory(
          ClassTransformContext.Factory classTransformContextFactory) {
    this.classTransformContextFactory = classTransformContextFactory;
  }

  @Override
  public ConsumerBasedClassTransformMeta create(CtClass ctClass, int priority, Consumer<ClassTransformContext> execution) {
    return new DefaultConsumerBasedClassTransformMeta(
            this.classTransformContextFactory,
            ctClass,
            priority,
            execution
    );
  }
}
