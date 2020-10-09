package net.labyfy.component.player.serializer.world;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.world.Dimension;

/**
 * Represents a {@link Dimension} serializer
 *
 * @param <T> The type to serialize or deserialize
 */
public interface DimensionSerializer<T> extends Serializer<T, Dimension> {
}
