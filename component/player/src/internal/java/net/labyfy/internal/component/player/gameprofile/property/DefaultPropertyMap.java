package net.labyfy.internal.component.player.gameprofile.property;

import com.google.inject.Inject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.property.Property;
import net.labyfy.component.player.gameprofile.property.PropertyMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of {@link PropertyMap}
 */
@Implement(PropertyMap.class)
public class DefaultPropertyMap implements PropertyMap {

    private final Map<String, Set<Property>> properties;

    @Inject
    public DefaultPropertyMap() {
        this.properties = new HashMap<>();
    }

    @Override
    public Map<String, Set<Property>> getProperties() {
        return this.properties;
    }

    @Override
    public Property put(String key, Property property) {
        Set<Property> properties = this.properties.get(key);

        if (properties == null) {
            properties = new HashSet<>();
        }

        properties.add(property);
        this.properties.put(key, properties);

        return property;
    }

    /**
     * Removes all  of this mappings from this map. The map will be empty after this call returns
     */
    @Override
    public void clear() {

    }
}
