package net.labyfy.component.player.serializer.util;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.util.Hand;

/**
 * Represents a {@link Hand} serializer
 *
 * @param <T> The type of serialize or deserialize
 */
public interface HandSerializer<T> extends Serializer<T, Hand> {
}
