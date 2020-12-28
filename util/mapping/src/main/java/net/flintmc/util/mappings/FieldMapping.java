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

package net.flintmc.util.mappings;

import java.lang.reflect.Field;

public final class FieldMapping extends BaseMapping {

  private final ClassMapping classMapping;
  private Field field;

  /**
   * Construct a field mapping.
   *
   * @param obfuscated       Whether the current environment is encrypted.
   * @param classMapping     The class mapping the field belongs to.
   * @param obfuscatedName   An obfuscated name.
   * @param deobfuscatedName A deobfuscated name.
   */
  public FieldMapping(
      final boolean obfuscated,
      final ClassMapping classMapping,
      final String obfuscatedName,
      final String deobfuscatedName) {
    super(obfuscated, obfuscatedName, deobfuscatedName);
    this.classMapping = classMapping;
  }

  /**
   * Get the field value.
   *
   * @param instance The instance or null if you want to get the static value.
   * @param <T>      The type of the field.
   * @return The value of the field from instance.
   * @throws NoSuchFieldException   If the field could not be found.
   * @throws ClassNotFoundException If the class could not be found.
   * @throws IllegalAccessException If the field definition could not be found.
   */
  public <T> T getValue(final Object instance)
      throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
    return (T) getField().get(instance);
  }

  /**
   * Get the field.
   *
   * @return The the field.
   * @throws NoSuchFieldException   If the field could not be found.
   * @throws ClassNotFoundException If the class could not be found.
   */
  public Field getField() throws ClassNotFoundException, NoSuchFieldException {
    if (field == null) {
      field = classMapping.get().getDeclaredField(name);
      field.setAccessible(true);
    }
    return field;
  }

  /**
   * Get the class mapping the field belongs to.
   *
   * @return A class mapping.
   */
  public ClassMapping getClassMapping() {
    return classMapping;
  }
}
