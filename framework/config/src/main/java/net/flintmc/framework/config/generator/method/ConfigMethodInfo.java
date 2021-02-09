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

package net.flintmc.framework.config.generator.method;

import javassist.CtClass;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

public interface ConfigMethodInfo {

  /**
   * Retrieves the interface where this method has been discovered, this is not the class which is
   * being implemented.
   *
   * @return The non-null class where this method has been discovered
   */
  CtClass getDeclaringClass();

  /**
   * Retrieves the config where this method comes from.
   *
   * @return The non-null config
   */
  GeneratingConfig getConfig();

  /**
   * Retrieves the type that will be stored in the config, so for example for 'int getX()' or 'void
   * setX(int x)' this would be int.
   *
   * @return The non-null type to be serialized
   */
  CtClass getStoredType();

  /**
   * Retrieves the name as which this method will be stored in the config (only the ending of it).
   * The prefix to the entry in the config is defined with {@link #getPathPrefix()}. This is parsed
   * from the specific type of method in {@link ConfigMethodResolver#resolveMethods(GeneratingConfig)},
   * so for example 'getX' would be 'X', see {@link Config} for more information on how this is
   * parsed.
   *
   * @return The non-null suffix of the config
   */
  String getConfigName();

  /**
   * Retrieves the path prefix to be used in the config.
   *
   * @return The non-null (may be empty) array of prefixes
   */
  String[] getPathPrefix();

  /**
   * Changes the path prefix, this method can only be called once.
   *
   * @param pathPrefix The non-null prefix (may be empty)
   * @throws IllegalStateException If the method has been called twice or more
   */
  void setPathPrefix(String[] pathPrefix) throws IllegalStateException;

  /**
   * Retrieves the source code that should be used for the generated constructor of the class where
   * this method is declared. The code uses `this.config` to access the instance of the config.
   *
   * @return The non-null source code to be inserted to set the default values of all fields or an
   * empty string if no fields were generated that require a default value
   */
  String getFieldValuesCreator();

  /**
   * Adds a new field to the {@link #getFieldValuesCreator()}.
   *
   * @param fieldName  The non-null name of the field that should be declared
   * @param fieldValue The non-null value of the field that should be used
   * @see #getFieldValuesCreator()
   */
  void initializeField(String fieldName, String fieldValue);

  /**
   * Marks this method as it requires no implementation methods by {@link
   * ConfigMethod#implementExistingMethods(CtClass)}.
   */
  void requireNoImplementation();

  /**
   * Retrieves whether {@link ConfigMethod#implementExistingMethods(CtClass)} has already been
   * called.
   *
   * @return {@code true} if it has been called, {@code false} otherwise
   */
  boolean hasImplementedExistingMethods();

  /**
   * Sets {@link #hasImplementedExistingMethods()} to {@code true}.
   */
  void implementedExistingMethods();

  /**
   * Sets {@link #hasAddedInterfaceMethods()} to {@code true}.
   */
  void addedInterfaceMethods();

  /**
   * Retrieves whether {@link ConfigMethod#addInterfaceMethods(CtClass)} has already been called.
   *
   * @return {@code true} if it has been called, {@code false} otherwise
   */
  boolean hasAddedInterfaceMethods();

  /**
   * Factory for the {@link ConfigMethodInfo}.
   */
  @AssistedFactory(ConfigMethodInfo.class)
  interface Factory {

    /**
     * Creates a new {@link ConfigMethodInfo} with the given information about a {@link
     * ConfigMethod}.
     *
     * @param config         The non-null config where this method comes from
     * @param declaringClass The non-null class where this method is declared
     * @param configName     The non-null name of this method like in {@link ConfigObjectReference#getLastName()}
     * @param methodType     The non-null type that is stored by this method
     * @return The new non-null {@link ConfigMethodInfo}
     */
    ConfigMethodInfo create(
        @Assisted("config") GeneratingConfig config,
        @Assisted("declaringClass") CtClass declaringClass,
        @Assisted("configName") String configName,
        @Assisted("methodType") CtClass methodType);

  }
}
