package net.labyfy.component.player.serializer.util.sound;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.util.sound.Sound;

/**
 * Represents a {@link Sound} serializer.
 *
 * @param <T> The type serialize or deserialize
 */
public interface SoundSerializer<T> extends Serializer<T, Sound> {

}
