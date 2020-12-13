package net.flintmc.framework.generation.parsing;

import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;

import java.util.Collection;

/**
 * Parses interface methods of the data- and factory interface to gain information about the wanted
 * {@link DataField}s and methods which should be implemented.
 */
public interface DataMethodParser {

  /**
   * Parses all {@link DataFactoryMethod}s, which define the data fields, out of the factory
   * interface.
   *
   * @param factoryInterface The factory interface
   * @param dataInterface The data interface
   * @return All data factory methods parsed out of the factory interface
   */
  Collection<DataFactoryMethod> parseFactoryMethods(CtClass factoryInterface, CtClass dataInterface)
      throws NotFoundException, ClassNotFoundException;

  /**
   * Parses all {@link DataFieldMethod}s which should be implemented out of the data interface.
   *
   * @param dataInterface The data interface
   * @param targetDataFields The data fields which should be created in the data class
   *     implementation
   * @return All data field methods parsed out of the data interface
   */
  Collection<DataFieldMethod> parseDataMethods(
      CtClass dataInterface, Collection<DataField> targetDataFields)
      throws NotFoundException, ClassNotFoundException;
}
