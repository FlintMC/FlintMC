package net.labyfy.component.player.serializer.util;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.util.PlayerClothing;

/**
 * A serializer for {@link PlayerClothing}
 *
 * @param <T> The type to serialize or deserialize
 */
public interface PlayerClothingSerializer<T> extends Serializer<T, PlayerClothing> {
}
