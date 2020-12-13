package net.flintmc.framework.generation.internal.parsing;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import net.flintmc.framework.generation.parsing.DataField;

import java.util.Objects;

/** {@inheritDoc} */
public class DefaultDataField implements DataField {

  private final String name;

  private final CtClass type;

  public DefaultDataField(String name, CtClass type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DefaultDataField dataField = (DefaultDataField) o;
    return this.name.equals(dataField.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  /** {@inheritDoc} */
  @Override
  public CtField generate(CtClass implementationClass) throws CannotCompileException {
    return CtField.make(
        String.format("public %s %s;", this.getType().getName(), this.getName()),
        implementationClass);
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass getType() {
    return this.type;
  }
}
