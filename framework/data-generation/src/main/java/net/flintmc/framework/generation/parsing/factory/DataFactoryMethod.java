package net.flintmc.framework.generation.parsing.factory;

import java.util.Collection;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.framework.generation.parsing.DataField;

/**
 * A DataFactoryMethod stands for a not yet generated method which creates an instance of a data
 * interface. It is based on a create method in the factory interface and defines the {@link
 * DataField}s of a data class by its method parameters annotated with {@link TargetDataField}.
 */
public interface DataFactoryMethod {

  /**
   * Generates an implementation of the method defined in the data interface which creates an
   * instance of the data interface.
   *
   * @param implementationClass The factory implementation class
   * @param dataImplementationName The name of the generated data implementation class
   * @return The generated method implementation
   */
  CtMethod generateImplementation(CtClass implementationClass, String dataImplementationName)
      throws CannotCompileException, NotFoundException;

  /** @return The method in the factory interface which should be implemented */
  CtMethod getInterfaceMethod();

  /** @return The data fields of the data class defined by the parameters of the interface method */
  Collection<DataField> getTargetDataFields();
}
