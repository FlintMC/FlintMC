package net.labyfy.internal.component.player.v1_15_2.serializer.gameprofile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.serializer.gameprofile.PropertyMapSerializer;

import java.util.Map;

/**
 * 1.15.2 implementation of {@link GameProfileSerializer}
 */
@Singleton
@Implement(GameProfileSerializer.class)
public class VersionedGameProfileSerializer implements GameProfileSerializer<com.mojang.authlib.GameProfile> {

    private final GameProfile.Builder profileBuilder;
    private final PropertyMapSerializer<PropertyMap> propertyMapSerializer;

    @Inject
    public VersionedGameProfileSerializer(GameProfile.Builder profileBuilder, PropertyMapSerializer propertyMapSerializer) {
        this.profileBuilder = profileBuilder;
        this.propertyMapSerializer = propertyMapSerializer;
    }

    /**
     * Deserializes the Mojang {@link com.mojang.authlib.GameProfile} to the Labyfy {@link GameProfile}
     *
     * @param profile The game profile to deserialize
     * @return A deserialized {@link GameProfile}
     */
    @Override
    public GameProfile deserialize(com.mojang.authlib.GameProfile profile) {
        return this.profileBuilder
                .setName(profile.getName())
                .setUniqueId(profile.getId())
                .setProperties(
                        this.propertyMapSerializer.deserialize(
                                profile.getProperties()
                        )
                ).build();
    }

    /**
     * Serializes the Labyfy {@link GameProfile} to the Mojang {@link com.mojang.authlib.GameProfile}.
     *
     * @param profile The profile to serialize
     * @return A serialized game profile
     */
    @Override
    public com.mojang.authlib.GameProfile serialize(GameProfile profile) {
        com.mojang.authlib.GameProfile gameProfile = new com.mojang.authlib.GameProfile(
                profile.getUniqueId(),
                profile.getName()
        );

        PropertyMap properties = this.propertyMapSerializer.serialize(profile.getProperties());

        for (Map.Entry<String, Property> entry : properties.entries()) {
            gameProfile.getProperties().put(
                    entry.getKey(),
                    entry.getValue()
            );
        }
        return gameProfile;
    }
}
