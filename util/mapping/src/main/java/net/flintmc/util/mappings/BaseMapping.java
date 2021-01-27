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

/**
 * A mapping base.
 */
class BaseMapping {

  private boolean obfuscated;
  final String obfuscatedName;
  final String deobfuscatedName;
  final String name;

  /**
   * Construct a base mapping.
   *
   * @param obfuscated       Whether the current environment is encrypted.
   * @param obfuscatedName   An obfuscated name.
   * @param deobfuscatedName A deobfuscated name.
   */
  public BaseMapping(
      final boolean obfuscated, final String obfuscatedName, final String deobfuscatedName) {
    this.obfuscated = obfuscated;
    this.obfuscatedName = obfuscatedName;
    this.deobfuscatedName = deobfuscatedName;
    this.name = obfuscatedName;//obfuscated ? obfuscatedName : deobfuscatedName;
  }

  /**
   * Get the name based on current environment.
   *
   * @return The obfuscated name if the environment is obfuscated. Deobfuscated name otherwise.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the obfuscated name.
   *
   * @return The obfuscated name.
   */
  public String getObfuscatedName() {
    return obfuscatedName;
  }

  /**
   * Get the deobfuscated name.
   *
   * @return The deobfuscated name.
   */
  public String getDeobfuscatedName() {
    return deobfuscatedName;
  }

  public boolean isDefault() {
    return obfuscatedName.equals(deobfuscatedName);
  }

  public boolean isObfuscated() {
    return obfuscated;
  }
}
