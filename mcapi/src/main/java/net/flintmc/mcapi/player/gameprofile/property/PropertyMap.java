package net.flintmc.mcapi.player.gameprofile.property;

import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Represents the properties of a game profile. */
public interface PropertyMap extends Multimap<String, Property> {

  /** A json serializer and deserializer of {@link PropertyMap} */
  interface Serializer extends JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {}

  /** A factory class for the {@link PropertyMap}. */
  @AssistedFactory(PropertyMap.class)
  interface Factory {

    /**
     * Creates a new {@link PropertyMap}.
     *
     * @return A created property map.
     */
    PropertyMap create();

    /**
     * Creates a new {@link PropertyMap} with the given {@link Multimap}.
     *
     * @param properties The multimap for the property map.
     * @return A created property map.
     */
    PropertyMap create(@Assisted("properties") Multimap<String, Property> properties);
  }
}
