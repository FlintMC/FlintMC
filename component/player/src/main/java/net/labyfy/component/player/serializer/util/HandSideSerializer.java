package net.labyfy.component.player.serializer.util;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.util.Hand;

/**
 * Represents a {@link Hand.Side} serializer.
 *
 * @param <T> The type of serialize of deserialize
 */
public interface HandSideSerializer<T> extends Serializer<T, Hand.Side> {
}
