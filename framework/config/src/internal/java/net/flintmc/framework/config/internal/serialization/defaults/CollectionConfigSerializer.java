package net.flintmc.framework.config.internal.serialization.defaults;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.serialization.ConfigSerializationHandler;
import net.flintmc.framework.config.serialization.ConfigSerializer;

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
  public Collection<?> deserialize(JsonElement element) {
    return this.gson.fromJson(element, Collection.class);
  }
}
