package net.labyfy.component.player.serializer.util.sound;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.util.sound.SoundCategory;

/**
 * Represents a {@link SoundCategory} serializer.
 *
 * @param <T> The type serialize or deserialize
 */
public interface SoundCategorySerializer<T> extends Serializer<T, SoundCategory> {
}
