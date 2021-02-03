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

package net.flintmc.util.mojang;

import java.util.UUID;

/**
 * Parser for {@link UUID}s and strings without dashes.
 */
public class UUIDParser {

  /**
   * Retrieves the given {@link UUID} as a string as described in {@link UUID#toString()} but
   * without the dashes in between.
   *
   * @param value The non-null UUID to be mapped into a string
   * @return The non-null string value of the given UUID without dashes
   */
  public static String toUndashedString(final UUID value) {
    return value.toString().replace("-", "");
  }

  /**
   * Parses the given value as a {@link UUID} just like {@link UUID#fromString(String)} but without
   * the dashes in between.
   *
   * @param input The non-null input to be parsed, needs to have a length of 32 characters
   * @return The non-null UUID parsed from the given string
   * @throws IllegalArgumentException If an invalid input has been provided
   */
  public static UUID fromUndashedString(final String input) {
    return UUID.fromString(
        input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
  }

}
