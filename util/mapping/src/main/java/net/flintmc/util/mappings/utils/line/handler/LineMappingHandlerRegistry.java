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

package net.flintmc.util.mappings.utils.line.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.util.mappings.utils.line.MappingLineParser;

/**
 * Registry for all {@link LineMappingHandler}s which are used for {@link
 * MappingLineParser#translateMappings(String)}.
 */
@Singleton
public class LineMappingHandlerRegistry {

  private final Map<String, LineMappingHandler> mapper;

  @Inject
  private LineMappingHandlerRegistry() {
    this.mapper = new HashMap<>();
  }

  /**
   * Registers a handler for the given names.
   *
   * @param handler The non-null handler to be registered in this registry
   * @param names   The non-null names that should identify the given handler
   * @throws IllegalArgumentException If a handler with any of the given case-insensitive names is
   *                                  already present in this registry
   */
  public void registerHandler(LineMappingHandler handler, String... names) {
    for (String name : names) {
      String lowerName = name.toLowerCase();
      if (this.mapper.containsKey(lowerName)) {
        throw new IllegalArgumentException(
            "Cannot register line mapping handler with name '" + name + "' twice");
      }
      this.mapper.put(lowerName, handler);
    }
  }

  /**
   * Retrieves a handler out of this registry for the given case-insensitive name.
   *
   * @param name The non-null case-insensitive name to get the handler for
   * @return The non-null handler that matches the given name
   * @throws IllegalArgumentException If there is no handler that matches the given name present in
   *                                  this registry
   */
  public LineMappingHandler getHandler(String name) throws IllegalArgumentException {
    String lowerName = name.toLowerCase();
    if (!this.mapper.containsKey(lowerName)) {
      throw new IllegalArgumentException("No mapper with the name '" + name + "' found");
    }

    return this.mapper.get(lowerName);
  }

}
