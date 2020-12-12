package net.flintmc.framework.generation.internal.parsing;

import java.util.Objects;
import javassist.CtClass;
import net.flintmc.framework.generation.parsing.DataField;

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
  public String getName() {
    return this.name;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass getType() {
    return this.type;
  }
}
