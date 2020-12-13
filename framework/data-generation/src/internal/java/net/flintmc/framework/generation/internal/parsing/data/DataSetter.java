package net.flintmc.framework.generation.internal.parsing.data;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.parsing.DataField;
import net.flintmc.framework.generation.parsing.data.DataFieldMethod;

/** {@inheritDoc} */
public class DataSetter implements DataFieldMethod {

  private final CtMethod interfaceMethod;

  private final DataField targetDataField;

  private final boolean returnSelf;

  public DataSetter(CtMethod interfaceMethod, DataField targetDataField, boolean returnSelf) {
    this.interfaceMethod = interfaceMethod;
    this.targetDataField = targetDataField;
    this.returnSelf = returnSelf;
  }

  /** {@inheritDoc} */
  @Override
  public CtMethod generateImplementation(CtClass implementationClass)
      throws CannotCompileException, NotFoundException {
    return CtMethod.make(
        String.format(
            "public %s %s(%s data) {this.%s = data; %s}",
            this.interfaceMethod.getReturnType().getName(),
            this.interfaceMethod.getName(),
            this.targetDataField.getType().getName(),
            this.targetDataField.getName(),
            this.returnSelf ? "return this;" : ""),
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
