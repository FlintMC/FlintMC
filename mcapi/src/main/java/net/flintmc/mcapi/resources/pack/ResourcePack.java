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

package net.flintmc.mcapi.resources.pack;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Represents a minecraft resource pack.
 */
public interface ResourcePack {

  /**
   * Retrieves all namespaces contained in this resource pack.
   *
   * @return All namespaces this resource pack contains
   */
  Collection<String> getNameSpaces();

  /**
   * Retrieves the name of the resource pack.
   *
   * @return The name of the resource pack
   */
  String getName();

  /**
   * Opens an input stream to a specific resource location in this resource pack. Usually {@link
   * ResourceLocation#openInputStream()} should be preferred as it will respect the currently
   * selected resource pack, which this method does not.
   *
   * @param resourceLocation The resource location to open the stream to
   * @return A stream which points to the given resource location
   * @throws java.io.FileNotFoundException If the given resource location can't be found in the
   *                                       pack
   * @throws IOException                   If an I/O error occurs
   */
  InputStream getStream(ResourceLocation resourceLocation) throws IOException;

  /**
   * Retrieves the description of the resource pack.
   *
   * @return The description of the resource pack
   */
  String getDescription();

  /**
   * Retrieves the title of the resource pack.
   *
   * @return The title of the resource pack
   */
  String getTitle();
}
