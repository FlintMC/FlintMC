package net.labyfy.internal.component.world.v1_16_3;

import com.google.inject.Inject;
import net.labyfy.component.world.World;
import net.labyfy.component.world.border.WorldBorder;
import net.labyfy.component.world.difficult.Difficulty;
import net.labyfy.component.world.difficult.DifficultyLocal;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.util.BlockPosition;
import net.labyfy.component.world.util.Dimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;

import java.util.Random;

/**
 * 1.16.3 implementation of {@link World}.
 */
public class VersionedWorld implements World {

  private final BlockPosition.Factory blockPositionFactory;
  private final DifficultyLocal.Factory difficultyLocalFactory;
  private final WorldBorder worldBorder;

  @Inject
  public VersionedWorld(
          BlockPosition.Factory blockPositionFactory,
          DifficultyLocal.Factory difficultyLocalFactory,
          WorldBorder worldBorder
  ) {
    this.blockPositionFactory = blockPositionFactory;
    this.difficultyLocalFactory = difficultyLocalFactory;
    this.worldBorder = worldBorder;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public long getSeed() {
    return Minecraft.getInstance().getIntegratedServer() == null ? 0L : Minecraft.getInstance().getIntegratedServer()
            .getWorld(Minecraft.getInstance().world.func_234923_W_())
            .getSeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getSpawnPoint() {
    return this.fromMinecraftBlockPos(
            new BlockPos(
                    Minecraft.getInstance().world.getWorldInfo().getSpawnX(),
                    Minecraft.getInstance().world.getWorldInfo().getSpawnY(),
                    Minecraft.getInstance().world.getWorldInfo().getSpawnZ()
            )
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCurrentMoonPhaseFactor() {
    return Minecraft.getInstance().world.func_242413_ae();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCelestialAngle(float partialTicks) {
    return Minecraft.getInstance().world.func_242415_f(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMoonPhase() {
    return Minecraft.getInstance().world.func_242414_af();
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
            this.getDifficulty(),
            this.getDayTime(),
            chunkInhabitedTime,
            moonPhaseFactory
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Difficulty getDifficulty() {
    net.minecraft.world.Difficulty difficulty = Minecraft.getInstance().world.getDifficulty();

    switch (difficulty) {
      case PEACEFUL:
        return Difficulty.PEACEFUL;
      case EASY:
        return Difficulty.EASY;
      case NORMAL:
        return Difficulty.NORMAL;
      case HARD:
        return Difficulty.HARD;
      default:
        throw new IllegalStateException("Unexpected value: " + difficulty);
    }
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
    return Minecraft.getInstance().world.isRainingAt(
            (BlockPos) this.toMinecraftBlockPos(position)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getActualHeight() {
    return Minecraft.getInstance().world.func_234938_ad_();
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
    return Minecraft.getInstance().world.getLightSubtracted(
            (BlockPos) this.toMinecraftBlockPos(position),
            amount
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLightValue(BlockPosition position) {
    return Minecraft.getInstance().world.getLightValue(
            (BlockPos) this.toMinecraftBlockPos(position)
    );
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
    return Minecraft.getInstance().world.getNeighborAwareLightSubtracted(
            (BlockPos) this.toMinecraftBlockPos(position),
            amount
    );
  }

  @Override
  public Dimension getDimension() {
    return this.fromMinecraftDimension(Minecraft.getInstance().world.func_234923_W_().func_240901_a_());
  }

  @Override
  public Scoreboard getScoreboard() {
    return null;
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
    if (!(handle instanceof BlockPos)) return null;

    BlockPos blockPos = (BlockPos) handle;

    return this.blockPositionFactory.create(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }

  /**
   * {@inheritDoc}
   *
   * @throws IllegalStateException When an unexpected value is received.
   */
  @Override
  public Object toMinecraftDimension(Dimension dimension) {
    switch (dimension) {
      case OVERWORLD:
        return DimensionType.field_242710_a;
      case NETHER:
        return DimensionType.field_242711_b;
      case THE_END:
        return DimensionType.field_242712_c;
      default:
        throw new IllegalStateException("Unexpected value: " + dimension);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension fromMinecraftDimension(Object handle) {
    if (!(handle instanceof ResourceLocation)) return null;

    ResourceLocation dimensionType = (ResourceLocation) handle;

    switch (dimensionType.getPath()) {
      case "the_nether":
        return Dimension.NETHER;
      case "overworld":
        return Dimension.OVERWORLD;
      case "the_end":
        return Dimension.THE_END;
      default:
        throw new IllegalStateException("Unexpected value: " + dimensionType.getPath());
    }
  }
}
