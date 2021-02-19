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

package net.flintmc.framework.config.generator.method;

import javassist.NotFoundException;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.GeneratingConfig;

/**
 * Parses all {@link ConfigMethod}s in an interface and all sub interfaces.
 */
public interface ConfigMethodResolver {

  /**
   * Parses all {@link ConfigMethod}s from all interfaces that are referenced in the interface of
   * {@link GeneratingConfig#getBaseClass()}. The methods will then be added to {@link
   * GeneratingConfig#getAllMethods()}.
   *
   * @param config The non-null config to parse the methods for
   * @throws NotFoundException If an internal error occurred when trying to find a CtClass/CtMethod
   * @see Config
   */
  void resolveMethods(GeneratingConfig config) throws NotFoundException;

}
