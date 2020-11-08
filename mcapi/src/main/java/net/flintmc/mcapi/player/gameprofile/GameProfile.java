package net.flintmc.mcapi.player.gameprofile;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

import java.util.UUID;

/** Represents the game profile of a player */
public interface GameProfile {

  /**
   * Retrieves the unique identifier of this game profile. This may be null for partial profile data
   * if constructed manually.
   *
   * @return The unique identifier of the profile
   */
  UUID getUniqueId();

  /**
   * Retrieves the display name of this game profile. This may be null for partial profile data if
   * constructed manually.
   *
   * @return The name of the profile
   */
  String getName();

  /**
   * Retrieves any known properties about this game profile.
   *
   * @return A modifiable map of profile properties.
   */
  PropertyMap getProperties();

  /**
   * Whether this profile is complete. A complete profile has no empty fields. Partial profiles my
   * be constructed manually and used as input to methods.
   *
   * @return {@code true} if this profile is complete (as opposed to partial), otherwise {@code
   *     false}
   */
  boolean isComplete();

  /**
   * Whether this profile is legacy.
   *
   * @return {@code true} if this profile is legacy, otherwise {@code false}
   */
  boolean isLegacy();

  /** A factory class for the {@link GameProfile}. */
  @AssistedFactory(GameProfile.class)
  interface Factory {

    /**
     * Creates a new {@link GameProfile} with the given parameters.
     *
     * @param uniqueId The unique identifier for this game profile.
     * @param name The username for this game profile.
     * @return A created game profile.
     */
    GameProfile create(@Assisted("uniqueId") UUID uniqueId, @Assisted("name") String name);

    /**
     * Creates a new {@link GameProfile} with the given parameters.
     *
     * @param uniqueId The unique identifier for this game profile.
     * @param name The username for this game profile.
     * @param properties The properties for this game profile.
     * @return A created game profile.
     */
    GameProfile create(
        @Assisted("uniqueId") UUID uniqueId,
        @Assisted("name") String name,
        @Assisted("properties") PropertyMap properties);
  }

  /** Builder class for {@link GameProfile} */
  interface Builder {

    /**
     * Sets the unique identifier for this game profile
     *
     * @param uniqueId The unique identifier of this game profile
     * @return This builder, for chaining
     */
    Builder setUniqueId(UUID uniqueId);

    /**
     * Sets the display name for this game profile
     *
     * @param name The display name of this game profile
     * @return This builder, for chaining
     */
    Builder setName(String name);

    /**
     * Sets the properties for this game profile
     *
     * @param properties The game profile properies
     * @return This builder, for chaining
     */
    Builder setProperties(PropertyMap properties);

    /**
     * Built the game profile
     *
     * @return The built game profile
     */
    GameProfile build();
  }
}
