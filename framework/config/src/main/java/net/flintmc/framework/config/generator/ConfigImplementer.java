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

package net.flintmc.framework.config.generator;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * This interface can add the {@link ParsedConfig} as an interface to a class and implement its
 * methods.
 */
public interface ConfigImplementer {

  void preImplementParsedConfig(CtClass implementation, GeneratingConfig config)
      throws CannotCompileException;

  /**
   * Adds the {@link ParsedConfig} interface to the given class and implements all its methods.
   *
   * @param implementation The non-null class to add the interface to
   * @param config         The non-null config which contains information about the config
   * @throws NotFoundException      If the {@link ParsedConfig} class cannot be found in the {@link
   *                                javassist.ClassPool}
   * @throws CannotCompileException If the generated source code cannot be compiled, should
   *                                basically never happen
   */
  void implementParsedConfig(CtClass implementation, GeneratingConfig config)
      throws NotFoundException, CannotCompileException;

  void preImplementSubConfig(CtClass implementation, GeneratingConfig config)
      throws CannotCompileException;

  void implementSubConfig(CtClass implementation, GeneratingConfig config)
      throws NotFoundException, CannotCompileException;

}
