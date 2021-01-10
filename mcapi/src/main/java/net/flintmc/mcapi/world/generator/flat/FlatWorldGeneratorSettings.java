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

import net.flintmc.mcapi.world.biome.Biome;
import net.flintmc.mcapi.world.generator.WorldGeneratorBuilder;
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;

/**
 * Settings for the flat world generator with the {@link WorldType} flat.
 *
 * @see WorldGeneratorBuilder
 * @see WorldTypeRegistry#getFlatType()
 */
public interface FlatWorldGeneratorSettings {

  /**
   * Retrieves the biome that has been set for this generator settings. If not set, the default
   * biome of Minecraft will be used.
   *
   * @return The biome or {@code null} if no biome is set
   */
  Biome getBiome();

  /**
   * Sets the biome of this generator settings. If not set, the default biome of Minecraft will be
   * used.
   *
   * @param biome The new biome to be used or {@code null} to use the default biome of Minecraft
   * @return this settings object for chaining
   */
  FlatWorldGeneratorSettings setBiome(Biome biome);

  /**
   * Retrieves an array of all structures of this settings object.
   *
   * @return The non-null array of structures of this settings object
   * @see #addStructure(FlatWorldStructure)
   * @see #removeStructure(FlatWorldStructure)
   */
  FlatWorldStructure[] getStructures();

  /**
   * Retrieves whether this settings object contains this structure. Similar to checking if {@link
   * #getStructures()} contains the given structure.
   *
   * @param structure The non-null structure to check for
   * @return this settings object for chaining
   */
  boolean hasStructure(FlatWorldStructure structure);

  /**
   * Adds a new structure to this settings object. If the structure is already added, nothing will
   * happen.
   *
   * @param structure The non-null structure to be added
   * @return this settings object for chaining
   */
  FlatWorldGeneratorSettings addStructure(FlatWorldStructure structure);

  /**
   * Removes a structure and all its options from this settings object. If the structure is not
   * added, nothing will happen.
   *
   * @param structure The non-null structure to be added
   * @return this settings object for chaining
   */
  FlatWorldGeneratorSettings removeStructure(FlatWorldStructure structure);

  /**
   * Checks whether the given structure has the given option set.
   *
   * @param structure The non-null structure to check for
   * @param option    The non-null option to check for
   * @return {@code true} if it is set, {@code false} otherwise
   * @see #getStructureOption(FlatWorldStructure, StructureOption)
   * @see #setStructureOption(FlatWorldStructure, StructureOption, int)
   */
  boolean hasStructureOption(FlatWorldStructure structure, StructureOption option);

  /**
   * Retrieves the value of the given option for the given structure.
   *
   * @param structure The non-null structure to get the options from
   * @param option    The non-null option to get the value from
   * @return The value of the option for the structure or {@code 0} if {@link
   * #hasStructureOption(FlatWorldStructure, StructureOption) this option is not set}
   * @see #hasStructureOption(FlatWorldStructure, StructureOption)
   * @see #setStructureOption(FlatWorldStructure, StructureOption, int)
   */
  int getStructureOption(FlatWorldStructure structure, StructureOption option);

  /**
   * Changes the value of the given option for the given structure to the given value. If the given
   * structure is not {@link #addStructure(FlatWorldStructure) added yet}, it will automatically be
   * added.
   *
   * @param structure The non-null structure to change the option for
   * @param option    The non-null option to be changed
   * @param value     The new value of the given option
   * @return this settings object for chaining
   * @see #hasStructureOption(FlatWorldStructure, StructureOption)
   * @see #getStructureOption(FlatWorldStructure, StructureOption)
   */
  FlatWorldGeneratorSettings setStructureOption(
      FlatWorldStructure structure, StructureOption option, int value);

  /**
   * Retrieves the layers of this settings object. The lowest value in this array (index 0) will be
   * the lowest layer.
   *
   * @return The non-null array of layers in this settings object
   * @see #addLayer(FlatWorldLayer)
   * @see #setLayer(int, FlatWorldLayer)
   * @see #removeLayer(int)
   */
  FlatWorldLayer[] getLayers();

  /**
   * Adds a new layer to the top of the already present layers.
   *
   * @param layer The non-null layer to be added
   * @return this settings object for chaining
   * @see #getLayers()
   * @see #setLayer(int, FlatWorldLayer)
   * @see #removeLayer(int)
   */
  FlatWorldGeneratorSettings addLayer(FlatWorldLayer layer);

  /**
   * Replaces the layer at the given index with the given layer.
   *
   * @param index The index of the layer to be replaced
   * @param layer The new non-null layer to replace the old one with
   * @return this settings object for chaining
   * @throws IndexOutOfBoundsException If the given index is out of range (<tt>index &lt; 0 || index
   *                                   &gt;= size()</tt>)
   */
  FlatWorldGeneratorSettings setLayer(int index, FlatWorldLayer layer);

  /**
   * Removes the layer at the given index.
   *
   * @param index The index of the layer to be removed
   * @return this settings object for chaining
   * @throws IndexOutOfBoundsException If the given index is out of range (<tt>index &lt; 0 || index
   *                                   &gt;= size()</tt>)
   * @see #clearLayers()
   */
  FlatWorldGeneratorSettings removeLayer(int index);

  FlatWorldGeneratorSettings clearLayers();

  /**
   * Validates if this settings object is correct and can be used to generate a new flat world.
   *
   * @return this settings object for chaining
   * @throws IllegalArgumentException If no {@link #getLayers() layer} is set
   */
  FlatWorldGeneratorSettings validate();

  /**
   * Serializes this settings object to be deserialized by {@link Factory#parseString(String)}.
   *
   * @return The new non-null string containing the serialized version of this settings object
   * @see Factory#parseString(String)
   */
  String serialize();

  /**
   * Sets an option to this settings object. If the given key is already set for another option, the
   * old one will be overridden. This is independent from Minecraft stuff and only intended to be
   * used internally.
   *
   * @param key   The non-null key for the option
   * @param value The non-null value for the option
   * @see #getOption(String)
   */
  FlatWorldGeneratorSettings setOption(String key, Object value);

  /**
   * Retrieves an option that has been set for this settings object.
   *
   * @param key The non-null key to get the option for
   * @return The value of the option or {@code null} if the given key has no option
   * @see #setOption(String, Object)
   */
  Object getOption(String key);

  /**
   * Factory for the {@link FlatWorldGeneratorSettings}.
   */
  interface Factory {

    /**
     * Creates the default Minecraft flat world settings which are 3 layers:
     * <ul>
     *   <li>1 * grass</li>
     *   <li>2 * dirt</li>
     *   <li>1 * bedrock</li>
     * </ul>
     *
     * @return The new non-null settings with the default values
     */
    FlatWorldGeneratorSettings createDefault();

    /**
     * Creates new flat world settings without any layers, structures and no biome.
     *
     * @return The new non-null flat world settings that need to be filled
     */
    FlatWorldGeneratorSettings createEmpty();

    /**
     * Parses the given string in the version-specific format provided by Minecraft. This format
     * looks like the following:
     *
     * <pre>
     *   X*BLOCK,X*OTHER BLOCK,...;BIOME;STRUCTURE1,STRUCTURE2,...
     *   1*minecraft:bedrock,10*minecraft:stone;minecraft:mountains;decoration,stronghold
     * </pre>
     * {@code X*} is always optional, if not provided 1 will be used.
     *
     * <ul>
     *   <li>X represents the height of this layer</li>
     *   <li>The BIOME is the biome of the whole map</li>
     *   <li>The STRUCTURES are structures that should be generated in this world</li>
     * </ul>
     * <p>
     * <p>
     * In general, first there are all layers separated by a comma. (If set) followed by a
     * semicolon the biome needs to be set. (If set) followed by another semicolon the
     * structures (separated by a comma) can be set. If the structures are set, the biome
     * also needs to be set.
     *
     * @param serialized The non-null serialized string in the described format
     * @return The new non-null world generator settings
     */
    FlatWorldGeneratorSettings parseString(String serialized);

  }

}
