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

package net.flintmc.mcapi.world;

import net.flintmc.mcapi.tileentity.TileEntity;
import net.flintmc.mcapi.world.block.Block;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.border.WorldBorder;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.type.Dimension;
import net.flintmc.mcapi.world.type.difficulty.Difficulty;
import net.flintmc.mcapi.world.type.difficulty.DifficultyLocal;
import java.util.List;
import java.util.Random;

/**
 * Represents the Minecraft world.
 */
public interface World {

  /**
   * Retrieves the world seed.
   *
   * @return The world seed.
   */
  long getSeed();

  /**
   * Retrieves the world spawn.
   *
   * @return The world spawn.
   */
  BlockPosition getSpawnPoint();

  /**
   * Retrieves the current factor of the moon phase.
   *
   * @return The factory of the moon phase.
   */
  float getCurrentMoonPhaseFactor();

  /**
   * Retrieves the calculated celestial angle.
   *
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *                     full tick
   * @return The calculated celestial angle.
   */
  float getCelestialAngle(float partialTicks);

  /**
   * Retrieves the moon phase identifier.
   *
   * @return The moon phase identifier.
   */
  int getMoonPhase();

  /**
   * Retrieves the local difficulty by the given block position.
   *
   * @param position The position for the local difficulty.
   * @return The local difficulty.
   */
  DifficultyLocal getDifficulty(BlockPosition position);

  /**
   * Retrieves the difficulty of this world.
   *
   * @return The world difficulty.
   */
  Difficulty getDifficulty();

  /**
   * Retrieves the day time of this world.
   *
   * @return The day time.
   */
  long getDayTime();

  /**
   * Retrieves the border of this world.
   *
   * @return The world border.
   */
  WorldBorder getWorldBorder();

  /**
   * Retrieves the random of this world.
   *
   * @return The random of this world.
   */
  Random getRandom();

  /**
   * Whether it is day time in the world.
   *
   * @return {@code true} if is day time in the world, otherwise {@code false}.
   */
  boolean isDayTime();

  /**
   * Whether it is night time in the world.
   *
   * @return {@code true} if is night time in the world, otherwise {@code false}.
   */
  boolean isNightTime();

  /**
   * Retrieves the thunder strength of this world.
   *
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *                     full tick
   * @return The strength of the thunder.
   */
  float getThunderStrength(float partialTicks);

  /**
   * Changes the strength of the thunder.
   *
   * @param strength The new strength of the thunder.
   */
  void setThunderStrength(float strength);

  /**
   * Retrieves the strength of the rain.
   *
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *                     full tick
   * @return The strength of the rain.
   */
  float getRainStrength(float partialTicks);

  /**
   * Changes the strength of the rain.
   *
   * @param strength The new strength.
   */
  void setRainStrength(float strength);

  /**
   * Whether it thunders.
   *
   * @return {@code true} it thunders, otherwise {@code false}.
   */
  boolean isThundering();

  /**
   * Whether it raining at the given block position.
   *
   * @param position The block position to be checked.
   * @return {@code true} it raining at the given position, otherwise {@code false}.
   */
  boolean isRainingAt(BlockPosition position);

  /**
   * Retrieves the actual height of this world.
   *
   * @return The world actual height.
   */
  int getActualHeight();

  /**
   * Retrieves the light at the given position.
   *
   * @param position The block position to get the light.
   * @return The light level of the block position.
   */
  int getLight(BlockPosition position);

  /**
   * Retrieves the subtracted light which is calculated by the given amount for the block position.
   *
   * @param position The block position to get the light.
   * @param amount   The amount to be subtracted.
   * @return The subtracted light level.
   */
  int getLightSubtracted(BlockPosition position, int amount);

  /**
   * Retrieves the light value by the given block position.
   *
   * @param position The block position to get the light value.
   * @return The light value by given block position.
   */
  int getLightValue(BlockPosition position);

  /**
   * Retrieves the maximum light level.
   *
   * @return The maximum light level.
   */
  int getMaxLightLevel();

  /**
   * Subtracts the neighborly light by the given {@code amount}.
   *
   * @param position The block position to get the coordinates.
   * @param amount   The amount to be subtracted
   * @return The subtracted neighborly light.
   */
  int getNeighborAwareLightSubtracted(BlockPosition position, int amount);

  /**
   * Retrieves the dimension of the world.
   *
   * @return The world dimension.
   */
  Dimension getDimension();

  /**
   * Retrieves the world scoreboard.
   *
   * @return The world scoreboard.
   */
  Scoreboard getScoreboard();

  /**
   * Retrieves a tile entity at the given block position.
   *
   * @param blockPosition The block position.
   * @return A tile entity at the given block position or {@code null}.
   */
  TileEntity getTileEntity(BlockPosition blockPosition);

  /**
   * Adds a tile entity to the {@link #getLoadedTileEntities()} collection.
   *
   * @param tileEntity The tile entity to be added.
   */
  void addTileEntity(TileEntity tileEntity);

  /**
   * Removes a tile entity from the {@link #getLoadedTileEntities()} collection.
   *
   * @param tileEntity The tile entity to be removed.
   */
  void removeTileEntity(TileEntity tileEntity);

  /**
   * Retrieves a collection with all loaded tile entities.
   *
   * @return A collection with all loaded tile entities.
   */
  List<TileEntity> getLoadedTileEntities();

  /**
   * Retrieves the block state at the given coordinates.
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @param z The z coordinate
   * @return The non-null block state at the given coordinates
   */
  BlockState getBlockState(int x, int y, int z);

  /**
   * Retrieves the block state at the given position.
   *
   * @param position The non-null position
   * @return The non-null block state at the given position
   */
  BlockState getBlockState(BlockPosition position);

  /**
   * Retrieves the block at the given coordinates.
   *
   * @param x The x coordinate
   * @param y The y coordinate
   * @param z The z coordinate
   * @return The non-null block at the given coordinates
   */
  Block getBlock(int x, int y, int z);

  /**
   * Retrieves the block at the given position.
   *
   * @param position The non-null position
   * @return The non-null block at the given position
   */
  Block getBlock(BlockPosition position);

  /**
   * Creates a new Minecraft block pos by using the given {@link BlockPosition} as the base.
   *
   * @param position The non-null {@link BlockPosition}.
   * @return The new Minecraft block pos or {@code null}, if the given block position was invalid.
   */
  Object toMinecraftBlockPos(BlockPosition position);

  /**
   * Creates a new {@link BlockPosition} by using the given Minecraft block pos as the base.
   *
   * @param handle The non-null block pos.
   * @return The new {@link BlockPosition} or {@code null}, if the given Minecraft block pos was
   * invalid.
   */
  BlockPosition fromMinecraftBlockPos(Object handle);

  /**
   * Creates a new Minecraft dimension type by using the given {@link Dimension} as the base.
   *
   * @param dimension The non-null {@link Dimension}.
   * @return The new Minecraft dimension type or {@code null}, if the given {@link Dimension} was
   * invalid.
   */
  Object toMinecraftDimension(Dimension dimension);

  /**
   * Creates a new {@link Dimension} by using the given Minecraft dimension type as the base.
   *
   * @param handle The non-null minecraft dimension type.
   * @return The new {@link Dimension} or {@code null}, if the given {@link Dimension} was invalid.
   * @throws IllegalStateException When an unexpected value is received.
   */
  Dimension fromMinecraftDimension(Object handle);

  /**
   * Converts the given Flint difficulty to the equivalent Minecraft difficulty.
   *
   * @param difficulty The non-null difficulty to be converted
   * @return The non-null Minecraft difficulty
   */
  Object toMinecraftDifficulty(Difficulty difficulty);

  /**
   * Converts the given Minecraft difficulty to the equivalent Flint difficulty.
   *
   * @param handle The non-null difficulty to be converted
   * @return The non-null Flint difficulty
   */
  Difficulty fromMinecraftDifficulty(Object handle);

}
