package net.labyfy.component.world;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.player.AbstractClientPlayerEntity;
import net.labyfy.component.world.border.WorldBorder;
import net.labyfy.component.world.difficult.Difficulty;
import net.labyfy.component.world.difficult.DifficultyLocal;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.util.BlockPosition;
import net.labyfy.component.world.util.Dimension;

import java.util.Map;
import java.util.Random;
import java.util.Set;

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
   * @param partialTicks The period of time, in fractions of a tick,
   *                     that has passed since the last full tick
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
   * @param partialTicks The period of time, in fractions of a tick,
   *                     that has passed since the last full tick
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
   * @param partialTicks The period of time, in fractions of a tick,
   *                     that has passed since the last full tick
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

  Map<Integer, Entity> getEntities();

  Set<AbstractClientPlayerEntity> getPlayers();

  /**
   * Creates a new Minecraft block pos by using the given {@link BlockPosition} as the base.
   *
   * @param position The non-null {@link BlockPosition}.
   * @return The new Minecraft block pos or {@code null} if the given block position was invalid.
   */
  Object toMinecraftBlockPos(BlockPosition position);

  /**
   * Creates a new {@link BlockPosition} by using the given Minecraft block pos as the base.
   *
   * @param handle The non-null block pos.
   * @return The new {@link BlockPosition} or {@code null} if the given Minecraft block pos was invalid.
   */
  BlockPosition fromMinecraftBlockPos(Object handle);

  /**
   * Creates a new Minecraft dimension type by using the given {@link Dimension} as the base.
   *
   * @param dimension The non-null {@link Dimension}.
   * @return The new Minecraft dimension type or {@code null} if the given {@link Dimension} was invalid.
   */
  Object toMinecraftDimension(Dimension dimension);

  /**
   * Creates a new {@link Dimension} by using the given Minecraft dimension type as the base.
   *
   * @param handle The non-null minecraft dimension type.
   * @return The new {@link Dimension} or {@code null} if the given {@link Dimension} was invalid.
   * @throws IllegalStateException When an unexpected value is received.
   */
  Dimension fromMinecraftDimension(Object handle);

}
