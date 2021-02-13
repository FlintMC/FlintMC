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

import java.util.Collection;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.config.storage.ConfigStorageProvider;

/**
 * Represents the implementation of an interface that has been marked with {@link Config} and been
 * marked as a config by the {@link ConfigGenerator}. This interface is not intended to be
 * implemented manually.
 */
public interface ParsedConfig {

  /**
   * Retrieves the name of this config. The name is just the name of the interface (with its
   * package) to be unique.
   *
   * @return The non-null name of this config
   */
  String getConfigName();

  /**
   * Retrieves a collection of all entries in this config. This may be modified to manually change
   * the entries, but it is not recommended.
   *
   * @return The non-null collection
   */
  Collection<ConfigObjectReference> getConfigReferences();

  /**
   * Retrieves the interface annotated with {@link Config} which this instance has been created
   * from. This instance will always be an instance of the retrieved class.
   *
   * @return The non-null interface annotated with {@link Config}
   */
  Class<?> getConfigClass();

  /**
   * Copies every {@link ConfigObjectReference} of this config to the given config.
   *
   * @param dst The config to copy the values of this config to
   * @see ConfigObjectReference#copyTo(ParsedConfig)
   */
  void copyTo(ParsedConfig dst);

  /**
   * Sets whether values changed in this config and every {@link SubConfig} of this config should
   * automatically be forwarded to {@link ConfigStorageProvider#write(ParsedConfig)} after changes
   * have been made by a setter.
   *
   * @param storeContent {@code true} if the changed values should automatically be forwarded to the
   *                     storage provider, {@code false} otherwise
   * @see #shouldStoreContent()
   */
  void setStoreContent(boolean storeContent);

  /**
   * Retrieves whether values changed in this config and every {@link SubConfig} of this config
   * should automatically be forwarded to {@link ConfigStorageProvider#write(ParsedConfig)} after
   * changes have been made by a setter.
   *
   * @return {@code true} if the changed values should automatically be forwarded to the storage
   * provider, {@code false} otherwise
   * @see #setStoreContent(boolean)
   */
  boolean shouldStoreContent();
}
