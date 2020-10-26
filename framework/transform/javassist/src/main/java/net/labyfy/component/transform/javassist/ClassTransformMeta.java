package net.labyfy.component.transform.javassist;

import javassist.CtClass;
import net.labyfy.component.transform.exceptions.ClassTransformException;

import java.util.function.Consumer;

public interface ClassTransformMeta {

  void execute(CtClass ctClass) throws ClassTransformException;

  boolean matches(CtClass ctClass);

  int getPriority();
}
