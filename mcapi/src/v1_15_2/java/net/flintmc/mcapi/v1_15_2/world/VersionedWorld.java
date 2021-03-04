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

package net.flintmc.mcapi.v1_15_2.world;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import java.util.Random;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.tileentity.TileEntity;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.block.Block;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.block.BlockState.Factory;
import net.flintmc.mcapi.world.border.WorldBorder;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.type.Dimension;
import net.flintmc.mcapi.world.type.difficulty.Difficulty;
import net.flintmc.mcapi.world.type.difficulty.DifficultyLocal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 1.15.2 implementation of {@link World}.
 */
@Singleton
@Implement(value = World.class)
public class VersionedWorld implements World {

  private final BlockState.Factory stateFactory;
  private final Block.Factory blockFactory;

  private final BlockPosition.Factory blockPositionFactory;
  private final DifficultyLocal.Factory difficultyLocalFactory;
  private final WorldBorder worldBorder;
  private final Scoreboard scoreboard;

  private final List<TileEntity> loadedTileEntities;

  private final Map<net.minecraft.block.BlockState, BlockState> cachedStates;

  @Inject
  public VersionedWorld(
      Factory stateFactory,
      Block.Factory blockFactory,
      BlockPosition.Factory blockPositionFactory,
      DifficultyLocal.Factory difficultyLocalFactory,
      WorldBorder worldBorder,
      Scoreboard scoreboard) {
    this.stateFactory = stateFactory;
    this.blockFactory = blockFactory;
    this.blockPositionFactory = blockPositionFactory;
    this.difficultyLocalFactory = difficultyLocalFactory;
    this.worldBorder = worldBorder;
    this.scoreboard = scoreboard;

    this.loadedTileEntities = Lists.newArrayList();

    this.cachedStates = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getSeed() {
    return Minecraft.getInstance().world.getSeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getSpawnPoint() {
    return this.fromMinecraftBlockPos(Minecraft.getInstance().world.getSpawnPoint());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCurrentMoonPhaseFactor() {
    return Minecraft.getInstance().world.getCurrentMoonPhaseFactor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCelestialAngle(float partialTicks) {
    return Minecraft.getInstance().world.getCelestialAngle(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMoonPhase() {
    return Minecraft.getInstance().world.getMoonPhase();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DifficultyLocal getDifficulty(BlockPosition position) {
    ClientWorld world = Minecraft.getInstance().world;
    long chunkInhabitedTime = 0L;
    float moonPhaseFactory = 0.0F;

    BlockPos blockPos = (BlockPos) this.toMinecraftBlockPos(position);
    if (world.isBlockLoaded(blockPos)) {
      chunkInhabitedTime = world.getChunk(blockPos).getInhabitedTime();
      moonPhaseFactory = this.getCurrentMoonPhaseFactor();
    }

    return this.difficultyLocalFactory.create(
        this.getDifficulty(), this.getDayTime(), chunkInhabitedTime, moonPhaseFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Difficulty getDifficulty() {
    return this.fromMinecraftDifficulty(Minecraft.getInstance().world.getDifficulty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getDayTime() {
    return Minecraft.getInstance().world.getDayTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldBorder getWorldBorder() {
    return this.worldBorder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Random getRandom() {
    return Minecraft.getInstance().world.getRandom();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDayTime() {
    return Minecraft.getInstance().world.isDaytime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNightTime() {
    return Minecraft.getInstance().world.isNightTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getThunderStrength(float partialTicks) {
    return Minecraft.getInstance().world.getThunderStrength(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setThunderStrength(float strength) {
    Minecraft.getInstance().world.setThunderStrength(strength);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRainStrength(float partialTicks) {
    return Minecraft.getInstance().world.getRainStrength(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRainStrength(float strength) {
    Minecraft.getInstance().world.setRainStrength(strength);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isThundering() {
    return Minecraft.getInstance().world.isThundering();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRainingAt(BlockPosition position) {
    return Minecraft.getInstance().world.isRainingAt((BlockPos) this.toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getActualHeight() {
    return Minecraft.getInstance().world.getActualHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLight(BlockPosition position) {
    return Minecraft.getInstance().world.getLight((BlockPos) this.toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLightSubtracted(BlockPosition position, int amount) {
    return Minecraft.getInstance()
        .world
        .getLightSubtracted((BlockPos) this.toMinecraftBlockPos(position), amount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLightValue(BlockPosition position) {
    return Minecraft.getInstance()
        .world
        .getLightValue((BlockPos) this.toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxLightLevel() {
    return Minecraft.getInstance().world.getMaxLightLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getNeighborAwareLightSubtracted(BlockPosition position, int amount) {
    return Minecraft.getInstance()
        .world
        .getNeighborAwareLightSubtracted((BlockPos) this.toMinecraftBlockPos(position), amount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getDimension() {
    return this.fromMinecraftDimension(Minecraft.getInstance().world.getDimension().getType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TileEntity getTileEntity(BlockPosition blockPosition) {
    for (TileEntity loadedTileEntity : this.loadedTileEntities) {
      if (loadedTileEntity.getPosition().equals(blockPosition)) {
        return loadedTileEntity;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTileEntity(TileEntity tileEntity) {
    if (tileEntity != null) {
      this.loadedTileEntities.add(tileEntity);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTileEntity(TileEntity tileEntity) {
    this.loadedTileEntities.remove(tileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TileEntity> getLoadedTileEntities() {
    return this.loadedTileEntities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockState getBlockState(int x, int y, int z) {
    net.minecraft.block.BlockState stateHandle =
        Minecraft.getInstance().world.getBlockState(new BlockPos(x, y, z));
    if (this.cachedStates.containsKey(stateHandle)) {
      return this.cachedStates.get(stateHandle);
    }

    BlockState state = this.stateFactory.create(stateHandle);
    this.cachedStates.put(stateHandle, state);

    return state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockState getBlockState(BlockPosition position) {
    return this.getBlockState(position.getX(), position.getY(), position.getZ());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Block getBlock(int x, int y, int z) {
    return this.getBlock(this.blockPositionFactory.create(x, y, z));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Block getBlock(BlockPosition position) {
    return this.blockFactory.create(position);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftBlockPos(BlockPosition position) {
    return new BlockPos(position.getX(), position.getY(), position.getZ());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition fromMinecraftBlockPos(Object handle) {
    if (!(handle instanceof BlockPos)) {
      return null;
    }

    BlockPos blockPos = (BlockPos) handle;

    return this.blockPositionFactory.create(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftDimension(Dimension dimension) {
    return DimensionType.getById(dimension.getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension fromMinecraftDimension(Object handle) {
    if (!(handle instanceof DimensionType)) {
      return null;
    }

    DimensionType dimensionType = (DimensionType) handle;

    switch (dimensionType.getId()) {
      case -1:
        return Dimension.NETHER;
      case 0:
        return Dimension.OVERWORLD;
      case 1:
        return Dimension.THE_END;
      default:
        throw new IllegalStateException("Unexpected value: " + dimensionType.getId());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftDifficulty(Difficulty difficulty) {
    switch (difficulty) {
      case PEACEFUL:
        return net.minecraft.world.Difficulty.PEACEFUL;
      case EASY:
        return net.minecraft.world.Difficulty.EASY;
      case NORMAL:
        return net.minecraft.world.Difficulty.NORMAL;
      case HARD:
        return net.minecraft.world.Difficulty.HARD;
      default:
        throw new IllegalStateException("Unexpected value: " + difficulty);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Difficulty fromMinecraftDifficulty(Object handle) {
    if (!(handle instanceof net.minecraft.world.Difficulty)) {
      return null;
    }

    switch ((net.minecraft.world.Difficulty) handle) {
      case PEACEFUL:
        return Difficulty.PEACEFUL;
      case EASY:
        return Difficulty.EASY;
      case NORMAL:
        return Difficulty.NORMAL;
      case HARD:
        return Difficulty.HARD;
      default:
        throw new IllegalStateException("Unexpected value: " + handle);
    }
  }
}
