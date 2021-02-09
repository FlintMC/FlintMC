/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
