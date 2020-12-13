package net.flintmc.framework.generation;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.DataMethodParser;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;

import java.util.Collection;

/**
 * Generates implementation classes for the factory- and data interfaces based on the information
 * gained from the {@link DataMethodParser}.
 */
public interface DataImplementationGenerator {

  /**
   * Generates an implementation for the data interface.
   *
   * @param dataInterface The data interface of the to be generated class
   * @param dataImplementationName The class name of the to be generated data implementation
   * @param targetDataFields All data fields which should be created in the data class
   * @param dataFieldMethods All data field methods which should be implemented
   * @return The class of the generated data implementation
   */
  CtClass generateDataImplementationClass(
      CtClass dataInterface,
      String dataImplementationName,
      Collection<DataField> targetDataFields,
      Collection<DataFieldMethod> dataFieldMethods)
      throws CannotCompileException, NotFoundException;

  /**
   * Generates an implementation for the factory interface.
   *
   * @param factoryInterface The factory interface of the to be generated class
   * @param dataFactoryMethods All data factory methods which should be implemented
   * @param dataImplementationName The class name of the generated data implementation
   * @return The class of the generated factory implementation
   */
  CtClass generateFactoryImplementationClass(
      CtClass factoryInterface,
      Collection<DataFactoryMethod> dataFactoryMethods,
      String dataImplementationName)
      throws CannotCompileException, NotFoundException;
}
