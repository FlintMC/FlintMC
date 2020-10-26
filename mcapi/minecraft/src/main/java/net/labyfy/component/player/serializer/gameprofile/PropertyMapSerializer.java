package net.labyfy.component.player.serializer.gameprofile;

import net.labyfy.component.player.gameprofile.property.PropertyMap;
import net.labyfy.component.player.serializer.Serializer;

/**
 * Represents a {@link PropertyMap} serializer
 *
 * @param <T> The type to serialize or deserialize
 */
public interface PropertyMapSerializer<T> extends Serializer<T, PropertyMap> {

}
