package net.labyfy.internal.component.settings.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.settings.annotation.ApplicableSetting;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.registered.SettingsProvider;
import net.labyfy.component.settings.serializer.JsonSettingsSerializer;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;

@Singleton
@Implement(JsonSettingsSerializer.class)
public class DefaultJsonSettingsSerializer implements JsonSettingsSerializer {

  private final SettingsProvider provider;
  private final SettingHandler<Annotation> handler;
  private final ComponentSerializer.Factory serializerFactory;

  private final Collection<RegisteredSettingsSerializer<?>> serializers;

  @Inject
  public DefaultJsonSettingsSerializer(SettingsProvider provider, SettingHandler handler, ComponentSerializer.Factory serializerFactory) {
    this.provider = provider;
    this.handler = handler;
    this.serializerFactory = serializerFactory;

    this.serializers = new HashSet<>();
  }

  @Override
  public JsonArray serializeSettings() {
    JsonArray array = new JsonArray();

    for (RegisteredSetting setting : this.provider.getAllSettings()) {
      JsonObject object = this.handler.serialize(setting.getAnnotation(), setting);
      array.add(object);

      ApplicableSetting applicableSetting = setting.getAnnotation().annotationType().getAnnotation(ApplicableSetting.class);
      object.addProperty("type", applicableSetting.type());

      object.addProperty("name", setting.getReference().getKey());
      object.addProperty("enabled", setting.isEnabled());

      for (RegisteredSettingsSerializer serializer : this.serializers) {
        Annotation annotation = setting.getReference().findLastAnnotation(serializer.getAnnotationType());
        serializer.getHandler().append(object, setting, annotation);
      }

      object.addProperty("category", setting.getCategoryName());

      if (setting.isNative()) {
        object.addProperty("native", true);
      }
    }

    return array;
  }

  @Override
  public JsonObject serializeCategories() {
    JsonObject result = new JsonObject();

    for (RegisteredCategory category : this.provider.getCategories()) {
      JsonObject object = new JsonObject();

      Gson gson = this.serializerFactory.gson().getGson();
      object.add("displayName", gson.toJsonTree(category.getDisplayName()));
      object.add("description", gson.toJsonTree(category.getDescription()));

      result.add(category.getRegistryName(), object);
    }

    return result;
  }

  @Override
  public <A extends Annotation> void registerHandler(Class<A> annotationType, SettingsSerializationHandler<A> handler) {
    this.serializers.add(new RegisteredSettingsSerializer<>(annotationType, handler));
  }

  @Override
  public Collection<SettingsSerializationHandler<Annotation>> getHandlers() {
    Collection<SettingsSerializationHandler<Annotation>> handlers = new HashSet<>(this.serializers.size());

    for (RegisteredSettingsSerializer<?> serializer : this.serializers) {
      handlers.add((SettingsSerializationHandler<Annotation>) serializer.getHandler());
    }

    return handlers;
  }

  @Override
  public <A extends Annotation> Collection<SettingsSerializationHandler<A>> getHandlers(Class<A> annotationType) {
    Collection<SettingsSerializationHandler<A>> handlers = new HashSet<>(this.serializers.size());

    for (RegisteredSettingsSerializer<?> serializer : this.serializers) {
      if (serializer.getAnnotationType().equals(annotationType)) {
        handlers.add((SettingsSerializationHandler<A>) serializer.getHandler());
      }
    }

    return handlers;
  }

}
