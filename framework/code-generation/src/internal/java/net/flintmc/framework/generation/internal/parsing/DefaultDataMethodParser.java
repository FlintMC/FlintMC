package net.flintmc.framework.generation.internal.parsing;

import com.google.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.framework.generation.internal.parsing.creator.DefaultDataCreatorMethod;
import net.flintmc.framework.generation.internal.parsing.data.DataGetter;
import net.flintmc.framework.generation.internal.parsing.data.DataSetter;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.DataMethodParser;
import net.flintmc.framework.generation.parsing.creator.DataCreatorMethod;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;
import net.flintmc.framework.inject.implement.Implement;

/** {@inheritDoc} */
@Singleton
@Implement(DataMethodParser.class)
public class DefaultDataMethodParser implements DataMethodParser {

  /** {@inheritDoc} */
  @Override
  public Collection<DataCreatorMethod> parseCreatorMethods(
      CtClass creatorInterface, CtClass dataInterface)
      throws NotFoundException, ClassNotFoundException {
    this.checkIsInterface(creatorInterface, dataInterface);

    Collection<DataCreatorMethod> methods = new HashSet<>();

    for (CtMethod interfaceMethod : creatorInterface.getDeclaredMethods()) {
      if (!interfaceMethod.isEmpty()) {
        // default method in interface
        continue;
      }

      // all methods in the creator should return an instance of the data interface
      if (!interfaceMethod.getReturnType().equals(dataInterface)) {
        throw new IllegalStateException(
            String.format(
                "Method %s in data creator interface %s does not return an instance of the data interface!",
                interfaceMethod.getName(), creatorInterface.getName()));
      }

      methods.add(new DefaultDataCreatorMethod(interfaceMethod));
    }

    return methods;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<DataFieldMethod> parseDataMethods(
      CtClass dataInterface, Collection<DataField> targetDataFieldsFields)
      throws NotFoundException, ClassNotFoundException {
    this.checkIsInterface(dataInterface);

    Collection<DataFieldMethod> methods = new HashSet<>();

    for (CtMethod interfaceMethod : dataInterface.getDeclaredMethods()) {
      if (!interfaceMethod.isEmpty()) {
        // default method in interface
        continue;
      }

      DataField targetDataField = this.getTargetDataField(interfaceMethod, targetDataFieldsFields);

      CtClass returnType = interfaceMethod.getReturnType();

      if (interfaceMethod.getParameterTypes().length == 1
          && (returnType.equals(dataInterface)
              || returnType
                  .getName()
                  .equals("void"))) { // a setter can return the instance of the data interface too
        CtClass firstParameterType = interfaceMethod.getParameterTypes()[0];

        this.checkDataFieldMethodType(
            "setter", firstParameterType, interfaceMethod, dataInterface, targetDataField);

        methods.add(
            new DataSetter(interfaceMethod, targetDataField, returnType.equals(dataInterface)));
      } else if (interfaceMethod.getParameterTypes().length == 0
          && !returnType.getName().equals("void")) {
        this.checkDataFieldMethodType(
            "getter", returnType, interfaceMethod, dataInterface, targetDataField);
        methods.add(new DataGetter(interfaceMethod, targetDataField));
      } else {
        throw new IllegalStateException(
            String.format(
                "Method %s in data interface %s is neither a setter nor a getter but targets a data field!",
                interfaceMethod.getName(), dataInterface.getName()));
      }
    }

    return methods;
  }

  private DataField getTargetDataField(
      CtMethod interfaceMethod, Collection<DataField> targetDataFields)
      throws ClassNotFoundException {
    if (!interfaceMethod.hasAnnotation(TargetDataField.class)) {
      throw new IllegalStateException(
          String.format(
              "Method %s in data interface %s does not target a valid data field!",
              interfaceMethod.getName(), interfaceMethod.getDeclaringClass().getName()));
    }

    String fieldName =
        ((TargetDataField) interfaceMethod.getAnnotation(TargetDataField.class)).value();

    DataField targetDataField = null;

    for (DataField dataClassField : targetDataFields) {
      if (dataClassField.getName().equals(fieldName)) {
        targetDataField = dataClassField;
        break;
      }
    }

    if (targetDataField == null) {
      throw new IllegalStateException(
          String.format(
              "Method %s in data interface %s does not target a valid data field!",
              interfaceMethod.getName(), interfaceMethod.getDeclaringClass().getName()));
    }

    return targetDataField;
  }

  private void checkDataFieldMethodType(
      String methodDescription,
      CtClass methodType,
      CtMethod interfaceMethod,
      CtClass dataInterface,
      DataField targetDataField) {
    if (!methodType.equals(targetDataField.getType())) {
      throw new IllegalStateException(
          String.format(
              "The type (%s) of %s %s in data interface %s is not compatible with the type of the target data field %s (%s)!",
              methodType.getName(),
              methodDescription,
              interfaceMethod.getName(),
              dataInterface.getName(),
              targetDataField.getName(),
              targetDataField.getType().getName()));
    }
  }

  private void checkIsInterface(CtClass... interfaces) {
    for (CtClass anInterface : interfaces) {
      if (!anInterface.isInterface()) {
        throw new IllegalStateException(
            String.format(
                "Data class or creator data class %s is not an interface!", anInterface.getName()));
      }
    }
  }
}
