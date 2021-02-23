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

import net.flintmc.framework.config.annotation.Config;

/**
 * Represents the implementation of an interface inside of an interface that has been marked with
 * {@link Config} and been marked as a config by the {@link ConfigGenerator}. This interface is not
 * intended to be implemented manually.
 */
public interface SubConfig {

  /**
   * Retrieves the config where this interface has been discovered.
   *
   * @return The non-null parent config of this interface
   */
  ParsedConfig getParentConfig();

}
