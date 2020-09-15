package net.labyfy.component.player.serializer.gameprofile;

import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.serializer.Serializer;

/**
 * Represents a {@link GameProfile} serializer
 *
 * @param <T> The type to serialize or deserialize
 */
public interface GameProfileSerializer<T> extends Serializer<T, GameProfile> {

}
