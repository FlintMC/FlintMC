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
import net.flintmc.mcapi.world.type.WorldType;
import net.flintmc.mcapi.world.type.WorldTypeRegistry;
import net.flintmc.mcapi.world.type.difficulty.Difficulty;

/**
 * More extended settings for the {@link WorldGeneratorBuilder}.
 * <p>
 * To be used no specific value needs to be set, but no value must be set to {@code null}.
 *
 * @see WorldGeneratorBuilder
 */
public interface ExtendedWorldGeneratorSettings {

  /**
   * Sets the seed of this settings object. If the given seed is a valid long, the seed will be the
   * string parsed as a long, otherwise it will be the {@link String#hashCode() hashCode} of the
   * string.
   *
   * @param seed The non-null seed to be used
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings seed(String seed);

  /**
   * Sets the seed of this settings object.
   *
   * @param seed The seed to be used
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings seed(long seed);

  /**
   * Retrieves the seed of this settings object. If none is set, a random one will be generated.
   *
   * @return The seed of this settings object
   */
  long seed();

  /**
   * Sets the game mode of this settings object.
   *
   * @param mode The new non-null game mode to be used
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings mode(WorldGameMode mode);

  /**
   * Retrieves the game mode of this settings object, defaults to {@link WorldGameMode#SURVIVAL}.
   *
   * @return This settings object for chaining
   */
  WorldGameMode mode();

  /**
   * Sets the world type of this settings object. Some world types need more configuration in the
   * {@link WorldGeneratorBuilder} like {@link WorldGeneratorBuilder#flatSettings()} for the {@link
   * WorldTypeRegistry#getFlatType() flat type}.
   *
   * @param type The non-null world type to be used
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings type(WorldType type);

  /**
   * Retrieves the world type of this settings object, defaults to {@link
   * WorldTypeRegistry#getDefaultType()}.
   *
   * @return The non-null world type
   */
  WorldType type();

  /**
   * Sets whether the generation of structures is enabled or disabled in this settings object.
   *
   * @param generateStructures {@code true} if it should be enabled, {@code false} otherwise
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings generateStructures(boolean generateStructures);

  /**
   * Retrieves whether the generation of structures is enabled or disabled in this settings object,
   * defaults to {@code true}.
   *
   * @return {@code true} if it is enabled, {@code false} otherwise
   */
  boolean generateStructures();

  /**
   * Sets whether cheats (= usage of commands) is enabled or disabled in this settings object.
   *
   * @param allowCheats {@code true} if it should be enabled, {@code false} otherwise
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings allowCheats(boolean allowCheats);

  /**
   * Retrieves whether cheats (= usage of commands) is enabled or disabled in this settings object,
   * defaults to {@code false}.
   *
   * @return {@code true} if it is enabled, {@code false} otherwise
   */
  boolean allowCheats();

  /**
   * Sets whether the bonus chest is enabled or disabled in this settings object.
   *
   * @param bonusChest {@code true} if it should be enabled, {@code false} otherwise
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings bonusChest(boolean bonusChest);

  /**
   * Retrieves whether the bonus chest is enabled or disabled in this settings object, defaults to
   * {@code false}.
   *
   * @return {@code true} if it is enabled, {@code false} otherwise
   */
  boolean bonusChest();

  /**
   * Sets the difficulty of this settings object.
   *
   * @param difficulty The new non-null difficulty to be used
   * @return This settings object for chaining
   */
  ExtendedWorldGeneratorSettings difficulty(Difficulty difficulty);

  /**
   * Retrieves the difficulty of the generator, defaults to {@link Difficulty#NORMAL}.
   *
   * @return The non-null difficulty of this settings object
   */
  Difficulty difficulty();

  /**
   * Validates whether this settings object can be used to generate a world.
   *
   * @return This settings object for chaining
   * @throws NullPointerException If {@link #mode(WorldGameMode)} has been invoked with {@code null}
   *                              as the parameter
   * @throws NullPointerException If {@link #type(WorldType)} has been invoked with {@code null} as
   *                              the parameter
   * @throws NullPointerException If {@link #difficulty(Difficulty)} has been invoked with {@code
   *                              null} as the parameter
   */
  ExtendedWorldGeneratorSettings validate();

  /**
   * Factory for the {@link ExtendedWorldGeneratorSettings}.
   */
  @AssistedFactory(ExtendedWorldGeneratorSettings.class)
  interface Factory {

    /**
     * Creates a new {@link ExtendedWorldGeneratorSettings} object with the default values for the
     * specific fields, see their getters for more information about the default values.
     *
     * @return The new non-null {@link ExtendedWorldGeneratorSettings}
     */
    ExtendedWorldGeneratorSettings create();

  }

}
