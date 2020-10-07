package net.labyfy.component.player.serializer.util;

import net.labyfy.component.player.serializer.Serializer;
import net.labyfy.component.player.util.EntityPose;

/**
 * Represents a {@link EntityPose} serializer
 *
 * @param <T> The type to serialize or deserialize
 */
public interface PoseSerializer<T> extends Serializer<T, EntityPose> {

}
