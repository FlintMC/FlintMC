package net.flintmc.framework.generation.internal.parsing.data;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;

/** {@inheritDoc} */
public class DataGetter implements DataFieldMethod {

  private final CtMethod interfaceMethod;

  private final DataField targetDataField;

  public DataGetter(CtMethod interfaceMethod, DataField targetDataField) {
    this.interfaceMethod = interfaceMethod;
    this.targetDataField = targetDataField;
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod generateImplementation(CtClass implementationClass)
      throws NotFoundException, CannotCompileException {
    return CtMethod.make(
        String.format(
            "public %s %s() {return this.%s;}",
            this.interfaceMethod.getReturnType().getName(),
            this.interfaceMethod.getName(),
            this.targetDataField.getName()),
        implementationClass);
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod getInterfaceMethod() {
    return this.interfaceMethod;
  }

  /** {@inheritDoc} */
  @Override
  public DataField getTargetDataField() {
    return this.targetDataField;
  }
}
