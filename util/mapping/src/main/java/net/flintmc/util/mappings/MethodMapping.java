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

import javassist.ClassPool;
import javassist.CtMethod;
import javassist.NotFoundException;

public final class MethodMapping extends BaseMapping {

  final ClassMapping classMapping;
  final String obfuscatedDescriptor;
  final String obfuscatedIdentifier;
  /* final */ String deobfuscatedDescriptor;
  /* final */ String deobfuscatedIdentifier;

  /**
   * Construct a method mapping.
   *
   * @param obfuscated           Whether the current environment is encrypted.
   * @param classMapping         The class mapping the method belongs to.
   * @param obfuscatedDescriptor An obfuscated method descriptor.
   * @param obfuscatedIdentifier An obfuscated method identifier.
   * @param obfuscatedName       An obfuscated name.
   * @param deobfuscatedName     A deobfuscated name.
   */
  public MethodMapping(
      final boolean obfuscated,
      final ClassMapping classMapping,
      final String obfuscatedDescriptor,
      final String obfuscatedIdentifier,
      final String obfuscatedName,
      final String deobfuscatedName) {
    super(obfuscated, obfuscatedName, deobfuscatedName);
    this.classMapping = classMapping;
    this.obfuscatedDescriptor = obfuscatedDescriptor;
    this.obfuscatedIdentifier = obfuscatedIdentifier;
  }

  /**
   * Get the class mapping the method belongs to.
   *
   * @return A class mapping.
   */
  public ClassMapping getClassMapping() {
    return classMapping;
  }

  /**
   * Get obfuscated method descriptor.
   *
   * @return An obfuscated method descriptor.
   */
  public String getObfuscatedDescriptor() {
    return obfuscatedDescriptor;
  }

  /**
   * Get the obfuscated or deobfuscated method descriptor, depending on {@link #isObfuscated()}.
   *
   * @return An obfuscated or deobfuscated method descriptor
   */
  public String getDescriptor() {
    return this.isObfuscated() ? this.obfuscatedDescriptor : this.deobfuscatedDescriptor;
  }

  /**
   * Get deobfuscated method descriptor.
   *
   * @return A deobfuscated method descriptor.
   */
  public String getDeobfuscatedDescriptor() {
    return deobfuscatedDescriptor;
  }

  /**
   * Get obfuscated method identifier.
   *
   * @return An obfuscated method identifier.
   */
  public String getObfuscatedIdentifier() {
    return obfuscatedIdentifier;
  }

  /**
   * Get deobfuscated method identifier.
   *
   * @return A deobfuscated method identifier.
   */
  public String getDeobfuscatedIdentifier() {
    return deobfuscatedIdentifier;
  }

  /**
   * Retrieves the javassist method of this mapping.
   *
   * @return The non-null javassist method
   * @throws NotFoundException If this method doesn't exist which should basically never happen
   */
  public CtMethod getMethod() throws NotFoundException {
    return this.getMethod(ClassPool.getDefault());
  }

  /**
   * Retrieves the javassist method of this mapping from the given pool.
   *
   * @param pool The non-null pool to get the method and its declaring class from
   * @return The non-null javassist method
   * @throws NotFoundException If this method doesn't exist which should basically never happen
   */
  public CtMethod getMethod(ClassPool pool) throws NotFoundException {
    return pool.get(this.classMapping.getName()).getMethod(this.getName(), this.getDescriptor());
  }
}
