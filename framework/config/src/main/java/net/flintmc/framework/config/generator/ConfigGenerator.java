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

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.event.ConfigDiscoveredEvent;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.storage.ConfigStorageProvider;
import java.io.IOException;
import java.util.Collection;

/**
 * Generator for implementations of interfaces annotated with {@link Config}.
 */
public interface ConfigGenerator {

  /**
   * Generates a new implementation for the given interface as described in the {@link Config}
   * annotation.
   *
   * @param configInterface The non-null interface to create an implementation for
   * @return The new non-null implementation of the given interface
   * @throws NotFoundException            If any method is not found, should basically never occur
   * @throws CannotCompileException       If generated code cannot be compiled, should basically
   *                                      never occur
   * @throws IOException                  If an I/O error occurred while writing the bytecode
   * @throws ReflectiveOperationException If a reflective error occurred while creating a new
   *                                      instance of the new implementation
   */
  Class<? extends ParsedConfig> generateConfigImplementation(CtClass configInterface)
      throws NotFoundException, CannotCompileException, IOException, ReflectiveOperationException;

  /**
   * Constructs a new instance of the given config class and fills the {@link
   * ParsedConfig#getConfigReferences()} collection with the references in the given interface
   * parsed from the {@link ConfigObjectReference.Parser}.
   * <p>
   * {@link #generateConfigImplementation(CtClass)} needs to be called before for the given class.
   *
   * @param configClass The non-null class of the config that should be created
   * @return The new non-null config instance created from the given class
   */
  ParsedConfig createConfigInstance(Class<? extends ParsedConfig> configClass);

  /**
   * Retrieves a collection with all configs that have been parsed from {@link
   * #generateConfigImplementation(CtClass)} in this generator.
   *
   * @return An unmodifiable collection of all generated configs
   */
  Collection<Class<? extends ParsedConfig>> getDiscoveredConfigs();

  /**
   * Fills the {@link ParsedConfig#getConfigReferences()} references} with the references parsed
   * from the given {@link GeneratingConfig}. Additionally, the config filled with the references
   * will be forwarded to {@link ConfigStorageProvider#read(ParsedConfig)} to fill the config with
   * values from the storages and the {@link ConfigDiscoveredEvent} will be fired.
   *
   * @param generatingConfig The non-null generating config with all methods to parse the references
   *                         from to register the config
   * @param config           The non-null config to be registered
   * @throws IllegalStateException If a config with the {@link ParsedConfig#getConfigName() given
   *                               name} is already registered
   */
  void bindConfig(GeneratingConfig generatingConfig, ParsedConfig config)
      throws IllegalStateException;

  /**
   * Should only be called once per config instance, and is only intended to be used internally.
   * This method sets all default values to the config and reads it from the config.
   *
   * @param config The non-null config to be initialized
   */
  void initConfig(ParsedConfig config);

}
