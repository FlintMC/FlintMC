package net.labyfy.component.player.serializer.util;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.util.GameMode;

/**
 * Represents a {@link GameMode} serializer
 *
 * @param <T> The type to serialize or deserialize
 */
public interface GameModeSerializer<T> extends Serializer<T, GameMode> {

}
