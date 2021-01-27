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

import net.flintmc.util.mappings.utils.line.handler.LineMappingHandler;

/**
 * Class that holds values for {@link LineMappingHandler}s while parsing a line through {@link
 * MappingLineParser#translateMappings(String)}.
 */
public final class MappingLine {

  private final String fullLine;

  private String currentClassName;

  /**
   * Constructs a new {@link MappingLine} with the given line that is currently being parsed by
   * {@link MappingLineParser#translateMappings(String)}.
   *
   * @param fullLine The non-null line that is currently being parsed by {@link
   *                 MappingLineParser#translateMappings(String)}
   */
  protected MappingLine(String fullLine) {
    this.fullLine = fullLine;
  }

  /**
   * Retrieves the line that is currently being parsed by {@link MappingLineParser#translateMappings(String)}.
   *
   * @return The non-null line that is currently being parsed by {@link MappingLineParser#translateMappings(String)}.
   */
  public String getFullLine() {
    return this.fullLine;
  }

  /**
   * Retrieves the current class name that should be used by the next handlers (for example the
   * class where a field is declared) to get their values from.
   *
   * @return The name of the current class or {@code null} if there is no class currently
   * @see #setCurrentClassName(String)
   */
  public String getCurrentClassName() {
    return this.currentClassName;
  }

  /**
   * Changes the current class name that should be used by the next handlers (for example the class
   * where a field is declared) to get their values from.
   *
   * @param currentClassName The new name of the current class or {@code null} to reset the current
   *                         class to none
   * @see #getCurrentClassName()
   */
  public void setCurrentClassName(String currentClassName) {
    this.currentClassName = currentClassName;
  }
}
