/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.internal.player.gameprofile.property;

import com.google.gson.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.gameprofile.property.Property;
import net.flintmc.mcapi.player.gameprofile.property.PropertyMap;

import java.lang.reflect.Type;
import java.util.Map;

/** An implementation of {@link PropertyMap.Serializer} */
@Singleton
@Implement(PropertyMap.Serializer.class)
public class DefaultPropertyMapSerializer implements PropertyMap.Serializer {

  private final Property.Factory propertyFactory;
  private final PropertyMap.Factory propertyMapFactory;

  @Inject
  private DefaultPropertyMapSerializer(
      Property.Factory propertyFactory, PropertyMap.Factory propertyMapFactory) {
    this.propertyFactory = propertyFactory;
    this.propertyMapFactory = propertyMapFactory;
  }

  /**
   * Gson invokes this call-back method during deserialization when it encounters a field of the
   * specified type.
   *
   * <p>In the implementation of this call-back method, you should consider invoking {@link
   * JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects for any
   * non-trivial field of the returned object. However, you should never invoke it on the the same
   * type passing {@code json} since that will cause an infinite loop (Gson will call your call-back
   * method again).
   *
   * @param json The Json data being deserialized
   * @param typeOfT The type of the Object to deserialize to
   * @param context The json context to use for creating the property map
   * @return A deserialized object of the specified type typeOfT which is a subclass of {@code T}
   * @throws JsonParseException if json is not in the expected format of {@code typeofT}
   */
  @Override
  public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    PropertyMap result = this.propertyMapFactory.create();

    if (json instanceof JsonObject) {
      JsonObject object = (JsonObject) json;

      for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
        if (entry.getValue() instanceof JsonArray) {
          for (JsonElement element : ((JsonArray) entry.getValue())) {
            result.put(
                entry.getKey(), this.propertyFactory.create(entry.getKey(), element.getAsString()));
          }
        }
      }
    } else if (json instanceof JsonArray) {
      for (JsonElement element : (JsonArray) json) {
        if (element instanceof JsonObject) {
          JsonObject object = (JsonObject) element;
          String name = object.getAsJsonPrimitive("name").getAsString();
          String value = object.getAsJsonPrimitive("value").getAsString();

          if (object.has("signature")) {
            result.put(
                name,
                this.propertyFactory.create(
                    name, value, object.getAsJsonPrimitive("signature").getAsString()));
          } else {
            result.put(name, this.propertyFactory.create(name, value));
          }
        }
      }
    }

    return result;
  }

  /**
   * Gson invokes this call-back method during serialization when it encounters a field of the
   * specified type.
   *
   * <p>In the implementation of this call-back method, you should consider invoking {@link
   * JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
   * non-trivial field of the {@code src} object. However, you should never invoke it on the {@code
   * src} object itself since that will cause an infinite loop (Gson will call your call-back method
   * again).
   *
   * @param src The object that needs to be converted to Json.
   * @param typeOfSrc The actual type (fully generic version) of the source object.
   * @param context The json context to use for creating the json element
   * @return A JsonElement corresponding to the specified object.
   */
  @Override
  public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
    JsonArray result = new JsonArray();

    for (Property property : src.values()) {
      JsonObject object = new JsonObject();

      object.addProperty("name", property.getName());
      object.addProperty("value", property.getValue());

      if (property.hasSignature()) {
        object.addProperty("signature", property.getSignature());
      }

      result.add(object);
    }

    return result;
  }
}
