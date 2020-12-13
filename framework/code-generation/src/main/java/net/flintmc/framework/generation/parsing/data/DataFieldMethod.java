package net.flintmc.framework.generation.parsing.data;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.framework.generation.parsing.DataField;

/**
 * A DataFieldMethod stands for a not yet generated method bound to a specific {@link DataField} in
 * a data class which is defined by the {@link TargetDataField} annotation. It is based on a method
 * in the data interface and might be a getter or setter for example.
 */
public interface DataFieldMethod {

  /**
   * Generates an implementation of the method defined in the data interface.
   *
   * @param implementationClass The data implementation class
   * @return The generated method implementation
   */
  CtMethod generateImplementation(CtClass implementationClass)
      throws CannotCompileException, NotFoundException;

  /** @return The method in the data interface which should be implemented */
  CtMethod getInterfaceMethod();

  /** @return The data field this method is bound to */
  DataField getTargetDataField();
}
