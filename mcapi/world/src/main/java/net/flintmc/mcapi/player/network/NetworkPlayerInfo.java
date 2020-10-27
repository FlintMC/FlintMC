package net.flintmc.mcapi.player.network;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.PlayerSkinProfile;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.scoreboad.score.PlayerTeam;

/**
 * Represents the network information of a player.
 */
public interface NetworkPlayerInfo extends PlayerSkinProfile {

  /**
   * Retrieves the game profile form the network information.
   *
   * @return The game profile of a player
   */
  GameProfile getGameProfile();

  /**
   * Retrieves the response time from the network information.
   *
   * @return The response time form the network information
   */
  int getResponseTime();

  /**
   * Retrieves the player game mode from the network information.
   *
   * @return The player game mode
   */
  GameMode getGameMode();

  /**
   * Retrieves the player team of the network information.
   *
   * @return The player team.
   */
  PlayerTeam getPlayerTeam();

  /**
   * Retrieves the player last health.
   *
   * @return The player last health
   */
  int getLastHealth();

  /**
   * Retrieves the player display health.
   *
   * @return The player display health
   */
  int getDisplayHealth();

  /**
   * Retrieves the player last health time.
   *
   * @return The player last health time
   */
  long getLastHealthTime();

  /**
   * Retrieves the player health blink time.
   *
   * @return The player health blink time
   */
  long getHealthBlinkTime();

  /**
   * Retrieves the player render visibility identifier.
   *
   * @return The player render visibility identifier
   */
  long getRenderVisibilityId();

  /**
   * Retrieves the display name of the display name.
   *
   * @return The display name.
   */
  ChatComponent getDisplayName();

  /**
   * A factory class for the {@link NetworkPlayerInfo}.
   */
  @AssistedFactory(NetworkPlayerInfo.class)
  interface Factory {

    /**
     * Creates a new {@link NetworkPlayerInfo} with the given parameters.
     *
     * @param gameProfile            The game profile for the network information.
     * @return A created network information.
     */
    NetworkPlayerInfo create(@Assisted("gameProfile") GameProfile gameProfile);
  }

}
