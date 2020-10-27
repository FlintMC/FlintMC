package net.flintmc.util.session;

import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

import java.util.UUID;

/**
 * The ProfileLoader loads textures and player names for a specified UUID.
 */
public interface ProfileLoader {

  /**
   * Loads all properties that are available from the session server.
   *
   * @param uniqueId The non-null UUID of the player to load the textures for
   * @return The new non-null map with all properties (textures)
   */
  PropertyMap loadProfileProperties(UUID uniqueId);

  /**
   * Loads all properties that are available and the name of the player from the session server.
   *
   * @param uniqueId The non-null UUID of the player to load the textures and name for
   * @return The new non-null profile filled with the UUID, name and properties (textures)
   */
  GameProfile loadProfile(UUID uniqueId);

}
