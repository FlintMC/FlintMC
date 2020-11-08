package net.flintmc.transform.javassist;

import javassist.CtClass;

import java.util.function.Consumer;

public interface ClassTransformService {

  ClassTransformService addClassTransformation(
      CtClass ctClass, Consumer<ClassTransformContext> consumer);

  ClassTransformService addClassTransformation(
      CtClass ctClass, int priority, Consumer<ClassTransformContext> consumer);

  ClassTransformService addClassTransformation(ClassTransformMeta classTransformMeta);
}
