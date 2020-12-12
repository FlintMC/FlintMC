package net.flintmc.framework.generation.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Collection;
import java.util.UUID;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.NotFoundException;
import net.flintmc.framework.generation.DataImplementationGenerator;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.creator.DataCreatorMethod;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.inject.implement.Implement;

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
  public CtClass generateCreatorImplementationClass(
      CtClass creatorInterface,
      Collection<DataCreatorMethod> dataCreatorMethods,
      String dataImplementationName)
      throws CannotCompileException, NotFoundException {
    CtClass creatorImplementationClass =
        this.classPool.makeClass(
            "Default"
                + creatorInterface.getSimpleName()
                + UUID.randomUUID().toString().replaceAll("-", ""));
    creatorImplementationClass.addInterface(creatorInterface);
    creatorImplementationClass.addConstructor(
        CtNewConstructor.defaultConstructor(creatorImplementationClass));

    for (DataCreatorMethod dataCreatorMethod : dataCreatorMethods) {
      creatorImplementationClass.addMethod(
          dataCreatorMethod.generateImplementation(
              creatorImplementationClass, dataImplementationName));
    }

    return creatorImplementationClass;
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

    for (DataField targetDataField : targetDataFields) {
      dataImplementationClass.addField(
          CtField.make(
              String.format(
                  "public %s %s;", targetDataField.getType().getName(), targetDataField.getName()),
              dataImplementationClass));
    }

    for (DataFieldMethod dataFieldMethod : dataFieldMethods) {
      dataImplementationClass.addMethod(
          dataFieldMethod.generateImplementation(dataImplementationClass));
    }

    return dataImplementationClass;
  }
}
