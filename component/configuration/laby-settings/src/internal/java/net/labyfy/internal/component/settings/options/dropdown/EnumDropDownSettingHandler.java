package net.labyfy.internal.component.settings.options.dropdown;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.settings.InvalidSettingsException;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.dropdown.EnumDropDownSetting;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.serializer.JsonSettingsSerializer;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

@Singleton
@RegisterSettingHandler(EnumDropDownSetting.class)
public class EnumDropDownSettingHandler implements SettingHandler<EnumDropDownSetting> {

  private final Logger logger;

  private final JsonSettingsSerializer serializer;

  @Inject
  public EnumDropDownSettingHandler(@InjectLogger Logger logger, JsonSettingsSerializer serializer) {
    this.logger = logger;
    this.serializer = serializer;
  }

  @Override
  public Object getDefaultValue(EnumDropDownSetting annotation, ConfigObjectReference reference) {
    Type type = reference.getSerializedType();
    if (!(type instanceof Class) || !Enum.class.isAssignableFrom((Class<?>) type)) {
      // should never occur because this is already checked in the SettingsDiscoverer
      throw new InvalidSettingsException("Cannot use EnumDropDownSetting without Enum as the return type, " +
          "return type was: " + type.getTypeName());
    }
    return ((Class<?>) type).getEnumConstants()[annotation.defaultValue()];
  }

  @Override
  public JsonObject serialize(EnumDropDownSetting annotation, RegisteredSetting setting) {
    JsonObject object = new JsonObject();

    Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) setting.getReference().getSerializedType();
    JsonArray possible = new JsonArray();
    object.add("possible", possible);
    for (Enum<?> constant : enumType.getEnumConstants()) {
      possible.add(this.serialize(setting, constant));
    }

    object.addProperty("value", ((Enum<?>) setting.getCurrentValue()).name());

    return object;
  }

  private JsonElement serialize(RegisteredSetting setting, Enum<?> constant) {
    JsonObject object = new JsonObject();
    object.addProperty("name", constant.name());

    try {
      Field field = constant.getDeclaringClass().getDeclaredField(constant.name());

      for (Annotation annotation : field.getAnnotations()) {
        for (SettingsSerializationHandler handler : this.serializer.getHandlers(annotation.annotationType())) {
          handler.append(object, setting, annotation);
        }
      }

    } catch (NoSuchFieldException e) {
      // this should not happen because those are enum constants
      this.logger.error("Cannot find enum constant field '" + constant.name() + "'", e);
    }

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, EnumDropDownSetting annotation) {
    return input == null || reference.getSerializedType().equals(input.getClass());
  }
}
