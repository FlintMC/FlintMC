package net.labyfy.component.config.serialization;

import com.google.gson.JsonElement;
import javassist.CtClass;

public interface ConfigSerializationService {

  boolean hasSerializer(Class<?> interfaceType);

  boolean hasSerializer(CtClass interfaceType);

  <T> ConfigSerializationHandler<T> getSerializer(Class<T> interfaceType);

  <T> void registerSerializer(Class<T> interfaceType, ConfigSerializationHandler<T> handler);

  <T> JsonElement serialize(Class<T> interfaceType, T value);

  <T> T deserialize(Class<T> interfaceType, JsonElement value);

}
