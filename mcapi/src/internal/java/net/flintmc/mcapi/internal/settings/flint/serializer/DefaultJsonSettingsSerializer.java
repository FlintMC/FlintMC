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

package net.flintmc.mcapi.internal.settings.flint.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.EnumFieldResolver;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.TranslateKey;
import net.flintmc.mcapi.settings.flint.annotation.ui.NativeSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.SubCategory;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredCategory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

@Singleton
@Implement(JsonSettingsSerializer.class)
public class DefaultJsonSettingsSerializer implements JsonSettingsSerializer {

  private final EnumFieldResolver fieldResolver;
  private final SettingsProvider provider;
  private final SettingHandler<Annotation> handler;

  private final ComponentAnnotationSerializer annotationSerializer;

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentBuilder.Factory builderFactory;

  private final Collection<RegisteredSettingsSerializer<?>> serializers;

  @Inject
  public DefaultJsonSettingsSerializer(
      EnumFieldResolver fieldResolver,
      SettingsProvider provider,
      SettingHandler handler,
      ComponentAnnotationSerializer annotationSerializer,
      ComponentSerializer.Factory serializerFactory,
      ComponentBuilder.Factory builderFactory) {
    this.fieldResolver = fieldResolver;
    this.provider = provider;
    this.handler = handler;
    this.annotationSerializer = annotationSerializer;
    this.serializerFactory = serializerFactory;
    this.builderFactory = builderFactory;

    this.serializers = new HashSet<>();
  }

  @Override
  public JsonArray serializeSettings() {
    return this.serializeSettings(this.provider.getAllSettings());
  }

  private JsonArray serializeSettings(Collection<RegisteredSetting> settings) {
    JsonArray array = new JsonArray();

    for (RegisteredSetting setting : settings) {
      JsonElement element = this.serializeSetting(setting);
      if (element.isJsonArray()) {
        array.addAll(element.getAsJsonArray());
        continue;
      }

      array.add(element);
    }

    return array;
  }

  @Override
  public JsonElement serializeSetting(RegisteredSetting setting) {
    Object fullValue = setting.getCurrentValue();
    if (fullValue == null) {
      fullValue = setting.getReference().getDefaultValue();
    }

    if (fullValue instanceof Map) {
      JsonArray array = new JsonArray();

      for (Map.Entry<?, ?> entry : ((Map<?, ?>) fullValue).entrySet()) {
        Object rawKey = entry.getKey();
        String key = this.formatKey(setting, rawKey);
        Object value = entry.getValue();

        Field enumConstant =
            rawKey instanceof Enum<?> ? this.fieldResolver.getEnumField((Enum<?>) rawKey) : null;

        JsonObject object =
            this.serializeSettingValue(
                setting,
                setting.getReference().getKey() + "#" + key,
                annotationType ->
                    enumConstant != null ? enumConstant.getAnnotation(annotationType) : null,
                value);

        if (setting.getReference().findLastAnnotation(TranslateKey.class) != null) {
          object.add(
              "displayName",
              this.serializerFactory
                  .gson()
                  .getGson()
                  .toJsonTree(this.builderFactory.translation().translationKey(key).build()));
        }

        array.add(object);
      }

      return array;
    } else {
      return this.serializeSettingValue(setting, setting.getReference().getKey(), null, fullValue);
    }
  }

  private String formatKey(RegisteredSetting setting, Object raw) {
    if (raw instanceof Enum<?>) {
      return ((Enum<?>) raw).name();
    }

    if (raw instanceof String
        || raw instanceof Number
        || raw instanceof Boolean
        || raw instanceof Character) {
      return String.valueOf(raw);
    }

    throw new UnsupportedOperationException(
        "Unsupported key type "
            + raw.getClass().getName()
            + " in setting "
            + setting.getReference().getKey());
  }

  private JsonObject serializeSettingValue(
      RegisteredSetting setting,
      String key,
      Function<Class<? extends Annotation>, Annotation> annotationResolver,
      Object value) {
    JsonObject object = this.handler.serialize(setting.getAnnotation(), setting, value);

    ApplicableSetting applicableSetting =
        setting.getAnnotation().annotationType().getAnnotation(ApplicableSetting.class);
    object.addProperty("type", applicableSetting.name());

    object.addProperty("name", key);
    object.addProperty("enabled", setting.isEnabled());

    for (RegisteredSettingsSerializer serializer : this.serializers) {
      Annotation annotation =
          setting.getReference().findLastAnnotation(serializer.getAnnotationType());
      if (annotationResolver != null) {
        Annotation resolved = annotationResolver.apply(serializer.getAnnotationType());
        if (resolved != null) {
          annotation = resolved;
        }
      }
      serializer.getHandler().append(object, setting, annotation);
    }

    object.addProperty("category", setting.getCategoryName());

    if (setting.getReference().findLastAnnotation(NativeSetting.class) != null) {
      object.addProperty("native", true);
    }

    if (!setting.getSubSettings().isEmpty()) {
      object.add("subSettings", this.serializeSettings(setting.getSubSettings()));
    }

    SubCategory subCategory = setting.getReference().findLastAnnotation(SubCategory.class);
    if (subCategory != null) {
      object.add(
          "subCategory",
          this.serializerFactory
              .gson()
              .getGson()
              .toJsonTree(this.annotationSerializer.deserialize(subCategory.value())));
    }

    return object;
  }

  @Override
  public JsonObject serializeCategories() {
    JsonObject result = new JsonObject();

    for (RegisteredCategory category : this.provider.getCategories()) {
      result.add(category.getRegistryName(), this.serializeCategory(category));
    }

    return result;
  }

  @Override
  public JsonObject serializeCategory(RegisteredCategory category) {
    JsonObject object = new JsonObject();

    Gson gson = this.serializerFactory.gson().getGson();
    object.add("displayName", gson.toJsonTree(category.getDisplayName()));
    object.add("description", gson.toJsonTree(category.getDescription()));

    return object;
  }

  @Override
  public <A extends Annotation> void registerHandler(
      Class<A> annotationType, SettingsSerializationHandler<A> handler) {
    this.serializers.add(new RegisteredSettingsSerializer<>(annotationType, handler));
  }

  @Override
  public Collection<SettingsSerializationHandler<Annotation>> getHandlers() {
    Collection<SettingsSerializationHandler<Annotation>> handlers =
        new HashSet<>(this.serializers.size());

    for (RegisteredSettingsSerializer<?> serializer : this.serializers) {
      handlers.add((SettingsSerializationHandler<Annotation>) serializer.getHandler());
    }

    return handlers;
  }

  @Override
  public <A extends Annotation> Collection<SettingsSerializationHandler<A>> getHandlers(
      Class<A> annotationType) {
    Collection<SettingsSerializationHandler<A>> handlers = new HashSet<>(this.serializers.size());

    for (RegisteredSettingsSerializer<?> serializer : this.serializers) {
      if (serializer.getAnnotationType().equals(annotationType)) {
        handlers.add((SettingsSerializationHandler<A>) serializer.getHandler());
      }
    }

    return handlers;
  }
}
