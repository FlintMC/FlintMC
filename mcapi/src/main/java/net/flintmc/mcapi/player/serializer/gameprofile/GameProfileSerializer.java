package net.flintmc.mcapi.player.serializer.gameprofile;

import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.serializer.Serializer;

/**
 * Represents a {@link GameProfile} serializer
 *
 * @param <T> The type to serialize or deserialize
 */
public interface GameProfileSerializer<T> extends Serializer<T, GameProfile> {}
