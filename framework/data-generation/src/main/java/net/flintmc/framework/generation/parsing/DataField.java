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

package net.flintmc.framework.generation.parsing;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

/** A DataField stands for a not yet generated field in a data class. */
public interface DataField {

  /**
   * Generates a public field with the given type and name.
   *
   * @param implementationClass The data class in which this field should be added
   * @return The generated field
   */
  CtField generate(CtClass implementationClass) throws CannotCompileException;

  /** @return The name this field should have */
  String getName();

  /** @return The type this field should have */
  CtClass getType();
}
