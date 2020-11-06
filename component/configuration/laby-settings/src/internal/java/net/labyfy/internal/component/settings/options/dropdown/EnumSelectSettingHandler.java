package net.labyfy.internal.component.settings.options.dropdown;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.EnumFieldResolver;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.dropdown.EnumSelectSetting;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.serializer.JsonSettingsSerializer;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Singleton
@RegisterSettingHandler(EnumSelectSetting.class)
public class EnumSelectSettingHandler implements SettingHandler<EnumSelectSetting> {

  private final JsonSettingsSerializer serializer;
  private final EnumFieldResolver fieldResolver;

  @Inject
  public EnumSelectSettingHandler(JsonSettingsSerializer serializer, EnumFieldResolver fieldResolver) {
    this.serializer = serializer;
    this.fieldResolver = fieldResolver;
  }

  @Override
  public JsonObject serialize(EnumSelectSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) setting.getReference().getSerializedType();
    JsonArray possible = new JsonArray();
    object.add("possible", possible);
    for (Enum<?> constant : enumType.getEnumConstants()) {
      possible.add(this.serialize(setting, constant));
    }

    object.addProperty("value", currentValue == null ? "" : ((Enum<?>) currentValue).name());

    object.addProperty("selectType", annotation.value().name());

    return object;
  }

  private JsonElement serialize(RegisteredSetting setting, Enum<?> constant) {
    JsonObject object = new JsonObject();
    object.addProperty("name", constant.name());

    Field field = this.fieldResolver.getEnumField(constant);

    for (Annotation annotation : field.getAnnotations()) {
      for (SettingsSerializationHandler handler : this.serializer.getHandlers(annotation.annotationType())) {
        handler.append(object, setting, annotation);
      }
    }

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, EnumSelectSetting annotation) {
    return input == null || reference.getSerializedType().equals(input.getClass());
  }
}
