package net.labyfy.component.player.gameprofile.property;

import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.util.Map;
import java.util.Set;

/**
 * Represents the properties of a game profile.
 */
public interface PropertyMap extends Multimap<String, Property> {

    /**
     * A json serializer and deserializer of {@link PropertyMap}
     */
    interface Serializer extends JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {

    }

}
