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

package net.flintmc.util.mappings.utils.line;

/**
 * This exception will be thrown from the {@link MappingLineParser} or any {@link
 * net.flintmc.util.mappings.utils.line.handler.LineMappingHandler} if there is an error in the
 * string to be parsed.
 */
public class MappingLineParseException extends RuntimeException {

  /**
   * Constructs a new {@link MappingLineParseException} with the given detail message.
   *
   * @param message The non-null detail message of the new exception
   */
  public MappingLineParseException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@link MappingLineParseException} with the given detail message and specified
   * cause of this exception.
   *
   * @param message The non-null detail message of the new exception
   * @param cause   The non-null cause of the new exception
   */
  public MappingLineParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
