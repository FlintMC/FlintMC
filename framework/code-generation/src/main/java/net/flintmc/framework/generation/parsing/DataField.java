package net.flintmc.framework.generation.parsing;

import javassist.CtClass;

public interface DataField {

  String getName();

  CtClass getType();
}
