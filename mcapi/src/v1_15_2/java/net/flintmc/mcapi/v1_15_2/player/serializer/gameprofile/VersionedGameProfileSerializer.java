package net.flintmc.mcapi.v1_15_2.player.serializer.gameprofile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.player.serializer.gameprofile.PropertyMapSerializer;

import java.util.Map;

/** 1.15.2 implementation of {@link GameProfileSerializer} */
@Singleton
@Implement(GameProfileSerializer.class)
public class VersionedGameProfileSerializer
    implements GameProfileSerializer<com.mojang.authlib.GameProfile> {

  private final GameProfile.Builder profileBuilder;
  private final PropertyMapSerializer<PropertyMap> propertyMapSerializer;

  @Inject
  public VersionedGameProfileSerializer(
      GameProfile.Builder profileBuilder, PropertyMapSerializer propertyMapSerializer) {
    this.profileBuilder = profileBuilder;
    this.propertyMapSerializer = propertyMapSerializer;
  }

  /**
   * Deserializes the Mojang {@link com.mojang.authlib.GameProfile} to the Flint {@link GameProfile}
   *
   * @param profile The game profile to deserialize
   * @return A deserialized {@link GameProfile}
   */
  @Override
  public GameProfile deserialize(com.mojang.authlib.GameProfile profile) {
      return this.profileBuilder
              .setName(profile.getName())
              .setUniqueId(profile.getId())
              .setProperties(this.propertyMapSerializer.deserialize(profile.getProperties()))
              .build();
  }

    /**
     * Serializes the Flint {@link GameProfile} to the Mojang {@link com.mojang.authlib.GameProfile}.
     *
     * @param profile The profile to serialize
     * @return A serialized game profile
     */
    @Override
    public com.mojang.authlib.GameProfile serialize(GameProfile profile) {
        com.mojang.authlib.GameProfile gameProfile =
                new com.mojang.authlib.GameProfile(profile.getUniqueId(), profile.getName());

        PropertyMap properties = this.propertyMapSerializer.serialize(profile.getProperties());

        for (Map.Entry<String, Property> entry : properties.entries()) {
            gameProfile.getProperties().put(entry.getKey(), entry.getValue());
    }
    return gameProfile;
  }
}
