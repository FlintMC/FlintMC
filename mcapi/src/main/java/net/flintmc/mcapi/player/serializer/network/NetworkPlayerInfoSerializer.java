package net.flintmc.mcapi.player.serializer.network;

import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.serializer.Serializer;

/**
 * Represents a {@link NetworkPlayerInfo} serializer.
 *
 * @param <T> The type to serialize or deserialize
 */
public interface NetworkPlayerInfoSerializer<T> extends Serializer<T, NetworkPlayerInfo> {}
