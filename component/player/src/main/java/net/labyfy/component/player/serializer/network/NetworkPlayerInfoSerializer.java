package net.labyfy.component.player.serializer.network;

import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.serializer.Serializer;

/**
 * Represents a {@link NetworkPlayerInfo} serializer.
 *
 * @param <T> The type to serialize or deserialize
 */
public interface NetworkPlayerInfoSerializer<T> extends Serializer<T, NetworkPlayerInfo> {
}
