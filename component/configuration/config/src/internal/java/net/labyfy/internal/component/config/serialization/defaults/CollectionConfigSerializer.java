package net.labyfy.internal.component.config.serialization.defaults;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.serialization.ConfigSerializationHandler;
import net.labyfy.component.config.serialization.ConfigSerializer;

import java.util.Collection;

@Singleton
@ConfigSerializer(Collection.class)
public class CollectionConfigSerializer implements ConfigSerializationHandler<Collection<?>> {

  private final Gson gson;

  @Inject
  public CollectionConfigSerializer() {
    this.gson = new Gson();
  }

  @Override
  public JsonElement serialize(Collection<?> collection) {
    return this.gson.toJsonTree(collection);
  }

  @Override
  public Collection<?> deserialize(JsonElement object) {
    return this.gson.fromJson(object, Collection.class);
  }
}
