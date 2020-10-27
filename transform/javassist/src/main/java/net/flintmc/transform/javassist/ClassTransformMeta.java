package net.flintmc.transform.javassist;

import javassist.CtClass;
import net.flintmc.transform.exceptions.ClassTransformException;

public interface ClassTransformMeta {

  void execute(CtClass ctClass) throws ClassTransformException;

  boolean matches(CtClass ctClass);

  int getPriority();
}
