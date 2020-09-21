package net.labyfy.internal.component.player.v1_15_2.serializer.gameprofile;

import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.property.Property;
import net.labyfy.component.player.gameprofile.property.PropertyMap;
import net.labyfy.component.player.serializer.gameprofile.PropertyMapSerializer;
import net.labyfy.internal.component.player.gameprofile.property.DefaultProperty;
import net.labyfy.internal.component.player.gameprofile.property.DefaultPropertyMap;

import java.util.Map;
import java.util.Set;

/**
 * 1.15.2. implementation of {@link PropertyMapSerializer}
 */
@Singleton
@Implement(value = PropertyMapSerializer.class, version = "1.15.2")
public class VersionedPropertyMapSerializer implements PropertyMapSerializer<com.mojang.authlib.properties.PropertyMap> {

    /**
     * Deserializes the Mojang {@link com.mojang.authlib.properties.PropertyMap}
     * to the Labyfy {@link PropertyMap}
     *
     * @param value The properties being deserialized
     * @return A deserialized {@link PropertyMap}
     */
    @Override
    public PropertyMap deserialize(com.mojang.authlib.properties.PropertyMap value) {
        PropertyMap properties = new DefaultPropertyMap();

        for (Map.Entry<String, com.mojang.authlib.properties.Property> entry : value.entries()) {
            properties.put(
                    entry.getKey(),
                    new DefaultProperty(
                            entry.getValue().getName(),
                            entry.getValue().getValue(),
                            entry.getValue().getSignature()
                    )
            );
        }
        return properties;
    }

    /**
     * Serializes the Labyfy {@link com.mojang.authlib.properties.PropertyMap}
     * to the Mojang {@link com.mojang.authlib.properties.PropertyMap}
     *
     * @param value The properties being serialized
     * @return A serialized {@link com.mojang.authlib.properties.PropertyMap}
     */
    @Override
    public com.mojang.authlib.properties.PropertyMap serialize(PropertyMap value) {
        com.mojang.authlib.properties.PropertyMap properties = new com.mojang.authlib.properties.PropertyMap();

        for (Map.Entry<String, Set<Property>> entry : value.getProperties().entrySet()) {
            for (Property property : entry.getValue()) {
                properties.put(
                        entry.getKey(),
                        new com.mojang.authlib.properties.Property(
                                property.getName(),
                                property.getValue(),
                                property.getSignature()
                        )
                );
            }
        }

        return properties;
    }
}
