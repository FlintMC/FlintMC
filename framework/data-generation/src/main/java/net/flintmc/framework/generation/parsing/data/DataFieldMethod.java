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

package net.flintmc.framework.generation.parsing.data;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.generation.annotation.TargetDataField;
import net.flintmc.framework.generation.parsing.DataField;

/**
 * A DataFieldMethod stands for a not yet generated method bound to a specific {@link DataField} in
 * a data class which is defined by the {@link TargetDataField} annotation. It is based on a method
 * in the data interface and might be a getter or setter for example.
 */
public interface DataFieldMethod {

  /**
   * Generates an implementation of the method defined in the data interface.
   *
   * @param implementationClass The data implementation class
   * @return The generated method implementation
   */
  CtMethod generateImplementation(CtClass implementationClass)
      throws CannotCompileException, NotFoundException;

  /** @return The method in the data interface which should be implemented */
  CtMethod getInterfaceMethod();

  /** @return The data field this method is bound to */
  DataField getTargetDataField();
}
