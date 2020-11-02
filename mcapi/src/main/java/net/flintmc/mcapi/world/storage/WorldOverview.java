package net.flintmc.mcapi.world.storage;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.type.GameMode;

/**
 * Represents an informative interface for the world.
 */
public interface WorldOverview extends Comparable<WorldOverview> {

  /**
   * Retrieves the file name of the world.
   *
   * @return The world's file name.
   */
  String getFileName();

  /**
   * Retrieves the display name of the world.
   *
   * @return The world's display name.
   */
  String getDisplayName();

  /**
   * Retrieves the size on the disk of the world.
   *
   * @return The world's size.
   */
  long getSizeOnDisk();

  /**
   * Whether the world required a conversion.
   *
   * @return {@code true} if the world required a conversion, otherwise {@code false}.
   */
  boolean requiresConversion();

  /**
   * Retrieves the last time played of the world.
   *
   * @return The world's last time played.
   */
  long getLastTimePlayed();

  /**
   * Retrieves the game mode of the world.
   *
   * @return The world's game mode.
   */
  GameMode getGameMode();

  /**
   * Whether the world is in hardcore mode.
   *
   * @return {@code true} if the world is in hardcore mode, otherwise {@code false}.
   */
  boolean isHardcoreMode();

  /**
   * Whether the cheats are enabled on the world.
   *
   * @return {@code true} if cheats are enabled on the world, otherwise {@code false}.
   */
  boolean isCheats();

  /**
   * Whether the world is marked in the list.
   *
   * @return {@code true} if the world is marked in the list, otherwise {@code false}.
   */
  boolean markWorldInList();

  /**
   * Whether the world should be asked at the opening.
   *
   * @return {@code true} if the world should be asked at the opening, otherwise {@code false}.
   */
  boolean askToOpenWorld();

  /**
   * Whether the is a future world.
   *
   * @return {@code true} if the is a future world, otherwise {@code false}.
   */
  boolean isFutureWorld();

  /**
   * Whether the world is a outdated world.
   *
   * @return {@code true} if the is a outdated world, otherwise {@code false}.
   */
  boolean isOutdatedWorld();

  /**
   * A factory class for creating {@link WorldOverview}'s.
   */
  @AssistedFactory(WorldOverview.class)
  interface Factory {

    /**
     * Creates a new {@link WorldOverview} with the given parameters.
     *
     * @param fileName           The file name of the world overview.
     * @param displayName        The display name of the world overview.
     * @param lastTimePlayed     The last time played timestamp of the world overview.
     * @param sizeOnDisk         The size on the disk of the world.
     * @param requiresConversion {@code true} if the world requires a conversion, otherwise {@code false}.
     * @param gameMode           The game mode of the world.
     * @param hardcoreMode       {@code true} if the hardcore mode is enabled in the world, otherwise {@code false}.
     * @param cheats             {@code true} if the cheats are enabled in the world, otherwise {@code false}.
     * @param askToOpenWorld     {@code true} asks a world to open, otherwise {@code false}.
     * @param markWorldInList    {@code true} marks a world in the list, otherwise {@code false}.
     * @param futureWorld        {@code true} if the world in the future, otherwise {@code false}.
     * @param outdatedWorld      {@code true} if the world is outdated, otherwise {@code false}.
     * @return A created world overview.
     */
    WorldOverview create(
            @Assisted("fileName") String fileName,
            @Assisted("displayName") String displayName,
            @Assisted("lastTimePlayed") long lastTimePlayed,
            @Assisted("sizeOnDisk") long sizeOnDisk,
            @Assisted("requiresConversion") boolean requiresConversion,
            @Assisted("gameMode") GameMode gameMode,
            @Assisted("hardcoreMode") boolean hardcoreMode,
            @Assisted("cheats") boolean cheats,
            @Assisted("askToOpenWorld") boolean askToOpenWorld,
            @Assisted("markWorldInList") boolean markWorldInList,
            @Assisted("futureWorld") boolean futureWorld,
            @Assisted("outdatedWorld") boolean outdatedWorld);

  }

}
