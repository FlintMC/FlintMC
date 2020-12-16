package net.flintmc.mcapi.internal.settings.flint.options.dropdown;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.EnumFieldResolver;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.dropdown.EnumSelectSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Singleton
@RegisterSettingHandler(EnumSelectSetting.class)
public class EnumSelectSettingHandler implements SettingHandler<EnumSelectSetting> {

  private final JsonSettingsSerializer serializer;
  private final EnumFieldResolver fieldResolver;

  @Inject
  public EnumSelectSettingHandler(
      JsonSettingsSerializer serializer, EnumFieldResolver fieldResolver) {
    this.serializer = serializer;
    this.fieldResolver = fieldResolver;
  }

  @Override
  public JsonObject serialize(
      EnumSelectSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    Class<? extends Enum<?>> enumType =
        (Class<? extends Enum<?>>) setting.getReference().getSerializedType();
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
      for (SettingsSerializationHandler handler :
          this.serializer.getHandlers(annotation.annotationType())) {
        handler.append(object, setting, annotation);
      }
    }

    return object;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, EnumSelectSetting annotation) {
    return input == null || reference.getSerializedType().equals(input.getClass());
  }
}
