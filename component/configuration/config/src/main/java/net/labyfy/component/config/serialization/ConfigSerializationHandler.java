package net.labyfy.component.config.serialization;

import com.google.gson.JsonElement;

public interface ConfigSerializationHandler<T> {

  JsonElement serialize(T t);

  T deserialize(JsonElement object);

}
