package net.flintmc.framework.generation.parsing;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

/** A DataField stands for a not yet generated field in a data class. */
public interface DataField {

  /**
   * Generates a public field with the given type and name.
   *
   * @param implementationClass The data class in which this field should be added
   * @return The generated field
   */
  CtField generate(CtClass implementationClass) throws CannotCompileException;

  /** @return The name this field should have */
  String getName();

  /** @return The type this field should have */
  CtClass getType();
}
