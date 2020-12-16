package net.flintmc.transform.javassist.internal;

import com.google.inject.Inject;
import javassist.CtClass;
import net.flintmc.transform.exceptions.ClassTransformException;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.flintmc.transform.javassist.ConsumerBasedClassTransformMeta;

import java.util.function.Consumer;

public class DefaultConsumerBasedClassTransformMeta implements ConsumerBasedClassTransformMeta {

  private final ClassTransformContext.Factory classTransformContextFactory;
  private final CtClass ctClass;
  private final int priority;
  private final Consumer<ClassTransformContext> execution;

  @Inject
  public DefaultConsumerBasedClassTransformMeta(
          ClassTransformContext.Factory classTransformContextFactory,
          CtClass ctClass,
          int priority,
          Consumer<ClassTransformContext> execution) {
    this.classTransformContextFactory = classTransformContextFactory;
    this.ctClass = ctClass;
    this.priority = priority;
    this.execution = execution;
  }

  @Override
  public void execute(CtClass ctClass) throws ClassTransformException {
    this.execution.accept(this.classTransformContextFactory.create(ctClass));
  }

  @Override
  public boolean matches(CtClass ctClass) {
    return this.ctClass.getName().equals(ctClass.getName());
  }

  @Override
  public int getPriority() {
    return this.priority;
  }
}
