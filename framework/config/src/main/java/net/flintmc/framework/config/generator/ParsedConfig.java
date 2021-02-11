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

  Class<?> getConfigClass();

  void copyTo(ParsedConfig dst);

  void setStoreContent(boolean storeContent);

  boolean shouldStoreContent();
}
