package net.flintmc.framework.generation.parsing.data;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public interface DataFieldMethod {

  CtMethod generateImplementation(CtClass implementationClass)
      throws CannotCompileException, NotFoundException;
}
