package net.flintmc.framework.generation.internal.parsing.creator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.framework.generation.internal.parsing.DefaultDataField;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.creator.DataCreatorMethod;

/** {@inheritDoc} */
public class DefaultDataCreatorMethod implements DataCreatorMethod {

  private final CtMethod interfaceMethod;

  private final Collection<DataField> targetDataFields;

  public DefaultDataCreatorMethod(CtMethod interfaceMethod)
      throws ClassNotFoundException, NotFoundException {
    this.interfaceMethod = interfaceMethod;
    this.targetDataFields = this.parseDataFields();
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod generateImplementation(CtClass implementationClass, String dataImplementationName)
      throws CannotCompileException, NotFoundException {
    String rawMethodSource =
        "public %s %s (%s){%s dataInstance = new %s(); %s return dataInstance;}";

    String parameters =
        this.targetDataFields.stream()
            .map(dataField -> dataField.getType().getName() + " " + dataField.getName())
            .collect(Collectors.joining(","));

    String fieldInitialization =
        this.targetDataFields.stream()
            .map(
                dataField ->
                    String.format(
                        "dataInstance.%s = %s;", dataField.getName(), dataField.getName()))
            .collect(Collectors.joining());

    return CtNewMethod.make(
        String.format(
            rawMethodSource,
            this.interfaceMethod.getReturnType().getName(),
            this.interfaceMethod.getName(),
            parameters,
            dataImplementationName,
            dataImplementationName,
            fieldInitialization),
        implementationClass);
  }

  /** {@inheritDoc} */
  @Override
  public Collection<DataField> getTargetDataFields() {
    return this.targetDataFields;
  }

  private Collection<DataField> parseDataFields() throws ClassNotFoundException, NotFoundException {
    Collection<DataField> dataFields = new ArrayList<>();

    for (int parameterIndex = 0;
        parameterIndex < this.interfaceMethod.getParameterAnnotations().length;
        parameterIndex++) {
      Object[] parameterAnnotations =
          this.interfaceMethod.getParameterAnnotations()[parameterIndex];

      for (Object parameterAnnotation : parameterAnnotations) {
        if (parameterAnnotation instanceof TargetDataField) {
          TargetDataField targetDataField = (TargetDataField) parameterAnnotation;
          CtClass parameterType = this.interfaceMethod.getParameterTypes()[parameterIndex];

          dataFields.add(new DefaultDataField(targetDataField.value(), parameterType));
        }
      }
    }

    return dataFields;
  }
}
