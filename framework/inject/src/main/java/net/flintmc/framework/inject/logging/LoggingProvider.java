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

package net.flintmc.framework.inject.logging;

import java.util.function.Function;
import org.apache.logging.log4j.Logger;

/** Provides Log4J Logger instances. */
public interface LoggingProvider {

  /**
   * Retrieves the logger that should be injected into a given class.
   *
   * @param clazz owner of the Logger
   * @return a Log4J Logger
   */
  Logger getLogger(Class<?> clazz);

  /**
   * Sets the package name provide callback which will be used by the logger to resolve the prefix
   * for a specific class.
   *
   * @param prefixProvider the prefix provider to be used
   */
  void setPrefixProvider(Function<Class<?>, String> prefixProvider);
}
