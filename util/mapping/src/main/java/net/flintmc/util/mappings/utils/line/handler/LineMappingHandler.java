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

import net.flintmc.util.mappings.utils.line.MappingLine;
import net.flintmc.util.mappings.utils.line.MappingLineParser;

/**
 * Handler for the {@link MappingLineParser} to parse specific variables like <b>$c{CLASS}</b>.
 *
 * @see LineObfuscationMapper
 * @see MappingLineParser#translateMappings(String)
 */
public interface LineMappingHandler {

  /**
   * Maps the given value to its obfuscated one (if running in an obfuscated environment, otherwise
   * to the deobfuscated value). The exact result depends on the implementation.
   *
   * @param line  The non-null line that is currently being parsed
   * @param value The non-null value to be parsed and mapped to the obfuscated one by this handler
   * @return The non-null obfuscated value
   */
  String mapToObfuscated(MappingLine line, String value);

}
