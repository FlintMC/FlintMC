package net.flintmc.framework.generation.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewConstructor;
import javassist.NotFoundException;
import net.flintmc.framework.generation.DataImplementationGenerator;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.generation.parsing.factory.DataFactoryMethod;
import net.flintmc.framework.inject.implement.Implement;

/** {@inheritDoc} */
@Singleton
@Implement(DataImplementationGenerator.class)
public class DefaultDataImplementationGenerator implements DataImplementationGenerator {

  private final ClassPool classPool;

  @Inject
  public DefaultDataImplementationGenerator(ClassPool classPool) {
    this.classPool = classPool;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass generateDataImplementationClass(
      CtClass dataInterface,
      String dataImplementationName,
      Collection<DataField> targetDataFields,
      Collection<DataFieldMethod> dataFieldMethods)
      throws CannotCompileException, NotFoundException {
    CtClass dataImplementationClass = this.classPool.makeClass(dataImplementationName);
    dataImplementationClass.addInterface(dataInterface);
    dataImplementationClass.addConstructor(
        CtNewConstructor.defaultConstructor(dataImplementationClass));

    // adding all parsed data fields
    for (DataField targetDataField : targetDataFields) {
      dataImplementationClass.addField(targetDataField.generate(dataImplementationClass));
    }

    // implementing all methods in the data interface
    for (DataFieldMethod dataFieldMethod : dataFieldMethods) {
      dataImplementationClass.addMethod(
          dataFieldMethod.generateImplementation(dataImplementationClass));
    }

    return dataImplementationClass;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass generateFactoryImplementationClass(
      CtClass factoryInterface,
      Collection<DataFactoryMethod> dataFactoryMethods,
      String dataImplementationName)
      throws CannotCompileException, NotFoundException {
    CtClass factoryImplementationClass =
        this.classPool.makeClass(
            "Default"
                + factoryInterface.getSimpleName()
                + UUID.randomUUID().toString().replaceAll("-", ""));
    factoryImplementationClass.addInterface(factoryInterface);
    factoryImplementationClass.addConstructor(
        CtNewConstructor.defaultConstructor(factoryImplementationClass));

    for (DataFactoryMethod dataFactoryMethod : dataFactoryMethods) {
      factoryImplementationClass.addMethod(
          dataFactoryMethod.generateImplementation(
              factoryImplementationClass, dataImplementationName));
    }

    return factoryImplementationClass;
  }
}
