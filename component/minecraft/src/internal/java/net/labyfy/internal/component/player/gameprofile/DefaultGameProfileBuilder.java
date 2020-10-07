package net.labyfy.internal.component.player.gameprofile;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.gameprofile.property.PropertyMap;

import java.util.UUID;

/**
 * An implementation of {@link GameProfile.Builder}
 */
@Implement(GameProfile.Builder.class)
public class DefaultGameProfileBuilder implements GameProfile.Builder {

    private UUID uniqueId;
    private String name;
    private PropertyMap properties;

    /**
     * Sets the unique identifier for this game profile
     *
     * @param uniqueId The unique identifier of this game profile
     * @return This builder, for chaining
     */
    @Override
    public GameProfile.Builder setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    /**
     * Sets the display name for this game profile
     *
     * @param name The display name of this game profile
     * @return This builder, for chaining
     */
    @Override
    public GameProfile.Builder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the properties for this game profile
     *
     * @param properties The game profile properties
     * @return This builder, for chaining
     */
    @Override
    public GameProfile.Builder setProperties(PropertyMap properties) {
        this.properties = properties;
        return this;
    }

    /**
     * Built the game profile
     *
     * @return The built game profile
     */
    @Override
    public GameProfile build() {
        return InjectionHolder
                .getInjectedInstance(GameProfile.class)
                .createProfile(this.uniqueId, this.name, this.properties);
    }
}
