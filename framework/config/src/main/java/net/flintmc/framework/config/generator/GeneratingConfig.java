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

package net.flintmc.framework.config.generator;

import javassist.CtClass;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import java.util.Collection;

/**
 * A config that is currently being implemented by a {@link ConfigGenerator}.
 */
public interface GeneratingConfig {

  /**
   * Retrieves the name of the config, unique per {@link ConfigGenerator} and usually the name (with
   * package) of the base interface that is annotated with {@link Config}.
   *
   * @return The non-null name of the generating config
   */
  String getName();

  /**
   * Retrieves the interface that is annotated with {@link Config} and used to generate the
   * implementation.
   *
   * @return The non-null base interface
   */
  CtClass getBaseClass();

  /**
   * Retrieves a mutable collection with all methods in this generating config.
   *
   * @return The mutable collection with all methods
   */
  Collection<ConfigMethod> getAllMethods();

  /**
   * Retrieves a collection of all interfaces that have been implemented in this config.
   *
   * @return The non-null immutable collection with all interfaces
   */
  Collection<CtClass> getImplementedInterfaces();

  /**
   * Retrieves a collection with all implementations that have been generated for this config. It
   * also contains interfaces within this config that are necessary for getters/setters to be
   * implemented.
   *
   * @return The non-null immutable collection with all implementations
   */
  Collection<CtClass> getGeneratedImplementations();

  /**
   * Retrieves an implementation for the given interface that has been generated for this config.
   *
   * @param baseName The name of the interface that has been implemented
   * @return The implementation class or {@code null}, if there is no implementation for an
   * interface with the given name
   * @see #bindGeneratedImplementation(CtClass, CtClass)
   */
  CtClass getGeneratedImplementation(String baseName);

  /**
   * Retrieves an implementation for the given interface that has been generated for this config.
   *
   * @param baseName The name of the interface that has been implemented
   * @param def      The value that should be returned if there is no generated implementation
   *                 available
   * @return The implementation class or {@code def} if there is no implementation for an interface
   * with the given name
   * @see #bindGeneratedImplementation(CtClass, CtClass)
   */
  CtClass getGeneratedImplementation(String baseName, CtClass def);

  /**
   * Binds an implementation for the name of a specific interface to be generated for this config.
   *
   * @param base           The non-null interface that has been implemented
   * @param implementation The non-null generated implementation to be bound to the given name
   */
  void bindGeneratedImplementation(CtClass base, CtClass implementation);

  /**
   * Factory for the {@link GeneratingConfig}.
   */
  @AssistedFactory(GeneratingConfig.class)
  interface Factory {

    /**
     * Creates a new {@link GeneratingConfig} with the given class.
     *
     * @param baseClass The non-null interface of the config, should be annotated with {@link
     *                  Config}
     * @return The new non-null generating config
     */
    GeneratingConfig create(@Assisted("baseClass") CtClass baseClass);

  }

}
