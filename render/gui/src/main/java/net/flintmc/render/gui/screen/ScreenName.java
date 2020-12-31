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

package net.flintmc.render.gui.screen;

import java.util.Objects;

/** Abstract representation of a screen class */
public final class ScreenName {
  public static final String MAIN_MENU = "main_menu";
  public static final String RESOURCE_LOAD = "resource_load";
  public static final String OPTIONS = "options";
  public static final String MULTIPLAYER = "multiplayer";
  public static final String SINGLEPLAYER = "singleplayer";
  public static final String DUMMY = "dummy";

  // Where the screen is from
  private final Type type;

  // Identifier, must be unique in each type. See the documentation of Type
  // to see what identifier is used for which type.
  private final String identifier;

  private ScreenName(Type type, String identifier) {
    this.type = type;
    this.identifier = identifier;
  }

  /**
   * Constructs a screen name with the given type and identifier
   *
   * @param type The type indicating where the screen is from
   * @param identifier The unique identifier within the type of this screen
   * @return The constructed screen name
   */
  public static ScreenName typed(Type type, String identifier) {
    return new ScreenName(type, identifier);
  }

  /**
   * Constructs a screen name which contains a screen identifier from minecraft. The identifier is
   * assumed to be one of the constants defined on this class.
   *
   * @param identifier The identifier which is one of the constants of this class
   * @return The constructed screen name
   */
  public static ScreenName minecraft(String identifier) {
    return new ScreenName(Type.FROM_MINECRAFT, identifier);
  }

  /**
   * Constructs a screen name with an unknown origin (possibly a package). The identifier is assumed
   * to be the full qualified name of the class the screen.
   *
   * @param clazz The full qualified name of the class of the screen
   * @return The constructed screen name
   */
  public static ScreenName unknown(String clazz) {
    return new ScreenName(Type.UNKNOWN, clazz);
  }

  /**
   * Retrieves the within screen names of the same type unique identifier. This also means that the
   * return value is only meaningful when matched with the type.
   *
   * @return The unique identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Retrieves the type of this screen name
   *
   * @return The type of this screen name
   */
  public Type getType() {
    return type;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScreenName that = (ScreenName) o;
    return type == that.type && Objects.equals(identifier, that.identifier);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(type, identifier);
  }

  /** Type indicating where a screen name is from */
  public enum Type {
    /**
     * The screen name is from minecraft. This means the unique identifier is one of the constants
     * defined in the {@link ScreenName} class.
     */
    FROM_MINECRAFT,

    /**
     * The screen name has an unknown origin. This means the unique identifier is the fully
     * qualified class name of the screen class.
     */
    UNKNOWN
  }
}
