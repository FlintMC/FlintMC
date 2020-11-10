package net.flintmc.mcapi.internal.world.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.storage.WorldConfiguration;
import net.flintmc.mcapi.world.type.WorldType;

/**
 * Default implementation of the {@link WorldConfiguration}.
 */
@Implement(WorldConfiguration.class)
public class DefaultWorldConfiguration implements WorldConfiguration {

  private final long seed;
  private final GameMode gameMode;
  private final boolean mapFeaturesEnabled;
  private final boolean hardcoreMode;
  private final WorldType terrainType;
  private final boolean commandsAllowed;
  private final boolean bonusChest;
  private final JsonElement generationOptions;

  @AssistedInject
  private DefaultWorldConfiguration(
          @Assisted("seed") long seed,
          @Assisted("gameMode") GameMode gameMode,
          @Assisted("mapFeaturesEnabled") boolean mapFeaturesEnabled,
          @Assisted("hardcoreMode") boolean hardcoreMode,
          @Assisted("terrainType") WorldType terrainType) {
    this(seed, gameMode, mapFeaturesEnabled, hardcoreMode, terrainType, false, false, new JsonObject());
  }

  @AssistedInject
  private DefaultWorldConfiguration(
          @Assisted("seed") long seed,
          @Assisted("gameMode") GameMode gameMode,
          @Assisted("mapFeaturesEnabled") boolean mapFeaturesEnabled,
          @Assisted("hardcoreMode") boolean hardcoreMode,
          @Assisted("terrainType") WorldType terrainType,
          @Assisted("commandsAllowed") boolean commandsAllowed,
          @Assisted("bonusChest") boolean bonusChest,
          @Assisted("generationOptions") JsonElement generationOptions) {
    this.seed = seed;
    this.gameMode = gameMode;
    this.mapFeaturesEnabled = mapFeaturesEnabled;
    this.hardcoreMode = hardcoreMode;
    this.terrainType = terrainType;
    this.commandsAllowed = commandsAllowed;
    this.bonusChest = bonusChest;
    this.generationOptions = generationOptions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBonusChest() {
    return this.bonusChest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getSeed() {
    return this.seed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameMode getGameMode() {
    return this.gameMode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHardcoreMode() {
    return this.hardcoreMode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMapFeaturesEnabled() {
    return this.mapFeaturesEnabled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean areCommandsAllowed() {
    return this.commandsAllowed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WorldType getWorldType() {
    return this.terrainType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonElement getGeneratorOptions() {
    return this.generationOptions;
  }
}
