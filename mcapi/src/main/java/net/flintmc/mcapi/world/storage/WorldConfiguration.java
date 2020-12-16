package net.flintmc.mcapi.world.storage;

import com.google.gson.JsonElement;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.type.WorldType;

/**
 * Represents the world configuration.
 */
public interface WorldConfiguration {

  /**
   * Whether the bonus chest is enabled.
   *
   * @return {@code true} if the bonus chest is enabled, otherwise {@code false}.
   */
  boolean isBonusChest();

  /**
   * Retrieves the seed of the world.
   *
   * @return The world configuration's seed.
   */
  long getSeed();

  /**
   * Retrieves the game mode of the world configuration.
   *
   * @return The world configuration's game mode.
   */
  GameMode getGameMode();

  /**
   * Whether the hardcore mode is enabled in the world configuration.
   *
   * @return {@code true} if the hardcore mode is enabled, otherwise {@code false}.
   */
  boolean isHardcoreMode();

  /**
   * Whether the map features is enabled in the world configuration.
   *
   * @return {@code true} if the map features is enabled, otherwise {@code false}.
   */
  boolean isMapFeaturesEnabled();

  /**
   * Whether commands are allowed in the world configuration.
   *
   * @return {@code true} are commands allowed, otherwise {@code false}.
   */
  boolean areCommandsAllowed();

  /**
   * Retrieves the world type of the world configuration.
   *
   * @return The world configuration's world type.
   */
  WorldType getWorldType();

  /**
   * Retrieves a json of the generator options.
   *
   * @return A json of the generator options.
   */
  JsonElement getGeneratorOptions();

  /**
   * A factory class for creating {@link WorldConfiguration}'s.
   */
  @AssistedFactory(WorldConfiguration.class)
  interface Factory {

    /**
     * Creates a new {@link WorldConfiguration} with the given parameters.
     *
     * @param seed               The seed for the world configuration.
     * @param gameMode           The game mode for the world configuration.
     * @param mapFeaturesEnabled {@code true} are map features enabled, otherwise {@code false}.
     * @param hardcoreMode       {@code true} if hardcore mode enabled, otherwise {@code false}.
     * @param terrainType        The terrain type for the world configuration.
     * @return A created world configuration.
     */
    WorldConfiguration create(
        @Assisted("seed") long seed,
        @Assisted("gameMode") GameMode gameMode,
        @Assisted("mapFeaturesEnabled") boolean mapFeaturesEnabled,
        @Assisted("hardcoreMode") boolean hardcoreMode,
        @Assisted("terrainType") WorldType terrainType);

    /**
     * Creates a new {@link WorldConfiguration} with the given parameters.
     *
     * @param seed               The seed for the world configuration.
     * @param gameMode           The game mode for the world configuration.
     * @param mapFeaturesEnabled {@code true} are map features enabled, otherwise {@code false}.
     * @param hardcoreMode       {@code true} if hardcore mode enabled, otherwise {@code false}.
     * @param terrainType        The terrain type for the world configuration.
     * @param commandsAllowed    {@code true} are commands allowed, otherwise {@code false}.
     * @param bonusChest         {@code true} if the bonus chest is enabled, otherwise {@code
     *                           false}.
     * @param generationOptions  A json of the generator options.
     * @return A created world configuration.
     */
    WorldConfiguration create(
        @Assisted("seed") long seed,
        @Assisted("gameMode") GameMode gameMode,
        @Assisted("mapFeaturesEnabled") boolean mapFeaturesEnabled,
        @Assisted("hardcoreMode") boolean hardcoreMode,
        @Assisted("terrainType") WorldType terrainType,
        @Assisted("commandsAllowed") boolean commandsAllowed,
        @Assisted("bonusChest") boolean bonusChest,
        @Assisted("generationOptions") JsonElement generationOptions);

  }

}
