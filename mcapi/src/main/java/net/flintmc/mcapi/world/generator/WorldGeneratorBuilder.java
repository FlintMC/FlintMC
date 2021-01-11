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

package net.flintmc.mcapi.world.generator;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.world.datapack.DatapackCodec;
import net.flintmc.mcapi.world.generator.buffet.BuffetWorldGeneratorSettings;
import net.flintmc.mcapi.world.generator.flat.FlatWorldGeneratorSettings;

/**
 * Builder for world generations.
 */
public interface WorldGeneratorBuilder {

  /**
   * Sets the name of the world to be generated.
   *
   * @param name The non-null name of the world
   * @return This builder for chaining
   */
  WorldGeneratorBuilder name(String name);

  /**
   * Retrieves the name of the world to be generated by this builder.
   *
   * @return The name of this builder or {@code null} if {@link #name(String) } hasn't been called
   * to set the name yet
   */
  String name();

  /**
   * Finds the name of the directory of the world. This requires a {@link #name(String) name} to be
   * set before.
   *
   * @return The non-null name of the directory
   */
  String findFileName();

  /**
   * Retrieves the extended settings of this builder. If not set, a new instance will be created.
   *
   * @return The non-null settings object
   * @see ExtendedWorldGeneratorSettings.Factory#create()
   */
  ExtendedWorldGeneratorSettings extended();

  /**
   * Sets the extended settings object of this builder.
   *
   * @param extended The extended settings or {@code null} to use the default values for the
   *                 contents of this object
   * @return This builder for chaining
   */
  WorldGeneratorBuilder extended(ExtendedWorldGeneratorSettings extended);

  /**
   * Retrieves whether or not this builder has flat settings. Those exist if either {@link
   * #flatSettings()} has been called, or {@link #flatSettings(FlatWorldGeneratorSettings)} has been
   * called with the parameter being non-null.
   *
   * @return {@code true} if this builder has flat settings, {@code false} otherwise
   */
  boolean hasFlatSettings();

  /**
   * Retrieves the flat settings of this builder. If not set, a new instance will be created.
   *
   * @return The non-null settings object
   * @see FlatWorldGeneratorSettings.Factory#createDefault()
   */
  FlatWorldGeneratorSettings flatSettings();

  /**
   * Sets the flat settings object of this builder.
   *
   * @param settings The flat settings or {@code null} to use the default values for the contents of
   *                 this object
   * @return This builder for chaining
   */
  WorldGeneratorBuilder flatSettings(FlatWorldGeneratorSettings settings);

  /**
   * Retrieves whether or not this builder has buffet settings. Those exist if either {@link
   * #buffetSettings()} has been called, or {@link #buffetSettings(BuffetWorldGeneratorSettings)}
   * has been called with the parameter being non-null.
   *
   * @return {@code true} if this builder has buffet settings, {@code false} otherwise
   */
  boolean hasBuffetSettings();

  /**
   * Retrieves the buffet settings of this builder. If not set, a new instance will be created.
   *
   * @return The non-null settings object
   * @see BuffetWorldGeneratorSettings.Factory#createDefault()
   */
  BuffetWorldGeneratorSettings buffetSettings();

  /**
   * Sets the buffet settings object of this builder.
   *
   * @param settings The buffet settings or {@code null} to use the default values for the contents
   *                 of this object
   * @return This builder for chaining
   */
  WorldGeneratorBuilder buffetSettings(BuffetWorldGeneratorSettings settings);

  /**
   * Sets the datapack codec of this builder. If this version doesn't support datapacks, this will
   * be ignored.
   *
   * @param codec The non-null datapack codec to be used
   * @return This builder for chaining
   */
  WorldGeneratorBuilder datapackCodec(DatapackCodec codec);

  /**
   * Retrieves the datapack codec of this builder.
   *
   * @return The non-null datapack codec of this builder
   */
  DatapackCodec datapackCodec();

  /**
   * Validates whether this builder can be used to generate a world. If set, {@link
   * ExtendedWorldGeneratorSettings#validate()}, {@link FlatWorldGeneratorSettings#validate()} and
   * {@link BuffetWorldGeneratorSettings#validate()} may also be fired to check if they are valid.
   *
   * @return This builder for chaining
   * @throws NullPointerException If no {@link #name(String) name} has been set
   */
  WorldGeneratorBuilder validate();

  /**
   * Generates the world and joins it as a player.
   * <p>
   * To be usable, at least a {@link #name(String) name} needs to be set.
   *
   * @see #validate()
   */
  void generateAndJoin();

  /**
   * Factory for the {@link WorldGeneratorBuilder}.
   */
  @AssistedFactory(WorldGeneratorBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link WorldGeneratorBuilder} with the default values so that only a {@link
     * #name(String) name} necessarily needs to be set.
     *
     * @return The new non-null {@link WorldGeneratorBuilder}
     */
    WorldGeneratorBuilder newBuilder();

  }

}
