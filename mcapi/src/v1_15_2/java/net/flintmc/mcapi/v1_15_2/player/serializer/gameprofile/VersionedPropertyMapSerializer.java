package net.flintmc.mcapi.v1_15_2.player.serializer.gameprofile;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;
import net.flintmc.mcapi.player.serializer.gameprofile.PropertyMapSerializer;

import java.util.Map;

/** 1.15.2. implementation of {@link PropertyMapSerializer} */
@Singleton
@Implement(value = PropertyMapSerializer.class, version = "1.15.2")
public class VersionedPropertyMapSerializer
    implements PropertyMapSerializer<com.mojang.authlib.properties.PropertyMap> {

  private final Property.Factory propertyFactory;
  private final PropertyMap.Factory propertyMapFactory;

  @Inject
  private VersionedPropertyMapSerializer(
      Property.Factory propertyFactory, PropertyMap.Factory propertyMapFactory) {
    this.propertyFactory = propertyFactory;
    this.propertyMapFactory = propertyMapFactory;
  }

  /**
   * Deserializes the Mojang {@link com.mojang.authlib.properties.PropertyMap} to the Labyfy {@link
   * PropertyMap}
   *
   * @param value The properties being deserialized
   * @return A deserialized {@link PropertyMap}
   */
  @Override
  public PropertyMap deserialize(com.mojang.authlib.properties.PropertyMap value) {
    PropertyMap properties = this.propertyMapFactory.create();

    for (Map.Entry<String, com.mojang.authlib.properties.Property> entry : value.entries()) {
      properties.put(
          entry.getKey(),
          this.propertyFactory.create(
              entry.getValue().getName(),
              entry.getValue().getValue(),
              entry.getValue().getSignature()));
    }
    return properties;
  }

  /**
   * Serializes the Labyfy {@link com.mojang.authlib.properties.PropertyMap} to the Mojang {@link
   * com.mojang.authlib.properties.PropertyMap}
   *
   * @param value The properties being serialized
   * @return A serialized {@link com.mojang.authlib.properties.PropertyMap}
   */
  @Override
  public com.mojang.authlib.properties.PropertyMap serialize(PropertyMap value) {
    com.mojang.authlib.properties.PropertyMap properties =
        new com.mojang.authlib.properties.PropertyMap();

    for (Map.Entry<String, Property> entry : value.entries()) {
      properties.put(
          entry.getKey(),
          new com.mojang.authlib.properties.Property(
              entry.getValue().getName(),
              entry.getValue().getValue(),
              entry.getValue().getSignature()));
    }

    return properties;
  }
}
