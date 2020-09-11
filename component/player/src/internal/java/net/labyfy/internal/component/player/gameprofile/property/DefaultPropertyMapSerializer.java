package net.labyfy.internal.component.player.gameprofile.property;

import com.google.gson.*;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.gameprofile.property.Property;
import net.labyfy.component.player.gameprofile.property.PropertyMap;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of {@link PropertyMap.Serializer}
 */
@Singleton
@Implement(PropertyMap.Serializer.class)
public class DefaultPropertyMapSerializer implements PropertyMap.Serializer {

    /**
     * Gson invokes this call-back method during deserialization when it encounters a field of the
     * specified type.
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
     * for any non-trivial field of the returned object. However, you should never invoke it on the
     * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
     * call-back method again).
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context The json context to use for creating the property map
     * @return A deserialized object of the specified type typeOfT which is a subclass of {@code T}
     * @throws JsonParseException if json is not in the expected format of {@code typeofT}
     */
    @Override
    public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PropertyMap result = new DefaultPropertyMap();

        if (json instanceof JsonObject) {
            JsonObject object = (JsonObject) json;

            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                if (entry.getValue() instanceof JsonArray) {
                    for (JsonElement element : ((JsonArray) entry.getValue())) {
                        result.put(entry.getKey(), new DefaultProperty(entry.getKey(), element.getAsString()));
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
                                new DefaultProperty(
                                        name,
                                        value,
                                        object.getAsJsonPrimitive("signature").getAsString()
                                )
                        );
                    } else {
                        result.put(name, new DefaultProperty(name, value));
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
     * <p>In the implementation of this call-back method, you should consider invoking
     * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
     * non-trivial field of the {@code src} object. However, you should never invoke it on the
     * {@code src} object itself since that will cause an infinite loop (Gson will call your
     * call-back method again).</p>
     *
     * @param src       The object that needs to be converted to Json.
     * @param typeOfSrc The actual type (fully generic version) of the source object.
     * @param context The json context to use for creating the json element
     * @return A JsonElement corresponding to the specified object.
     */
    @Override
    public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray result = new JsonArray();

        for (Map.Entry<String, Set<Property>> entry : src.getProperties().entrySet()) {
            for (Property property : entry.getValue()) {
                JsonObject object = new JsonObject();

                object.addProperty("name", property.getName());
                object.addProperty("value", property.getValue());


                if (property.hasSignature()) object.addProperty("signature", property.getSignature());

                result.add(object);
            }
        }

        return result;
    }
}
