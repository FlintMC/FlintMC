package net.flintmc.mcapi.player.serializer.gameprofile;

import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;
import net.flintmc.mcapi.player.serializer.Serializer;

/**
 * Represents a {@link PropertyMap} serializer
 *
 * @param <T> The type to serialize or deserialize
 */
public interface PropertyMapSerializer<T> extends Serializer<T, PropertyMap> {

}
