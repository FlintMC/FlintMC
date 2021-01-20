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

package net.flintmc.mcapi.world.generator.flat;

/**
 * Serializer for the {@link FlatWorldGeneratorSettings}.
 */
public interface FlatWorldGeneratorSettingsSerializer {

  /**
   * Serializes the given settings into a string.
   *
   * @param settings The non-null settings to be serialized
   * @return The new non-null string
   * @see FlatWorldGeneratorSettings#serialize()
   */
  String serialize(FlatWorldGeneratorSettings settings);

  /**
   * Deserializes the given string into a new settings object.
   *
   * @param serialized The non-null string to be deserialized
   * @return The new non-null settings
   * @see FlatWorldGeneratorSettings.Factory#parseString(String)
   */
  FlatWorldGeneratorSettings deserialize(String serialized);

}
