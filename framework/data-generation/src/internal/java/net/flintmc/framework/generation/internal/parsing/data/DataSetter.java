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
