package net.flintmc.mcapi.internal.settings.flint.options.dropdown;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.dropdown.CustomSelectSetting;
import net.flintmc.mcapi.settings.flint.options.dropdown.Selection;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

@Singleton
@RegisterSettingHandler(CustomSelectSetting.class)
public class CustomSelectSettingHandler implements SettingHandler<CustomSelectSetting> {

  private final JsonSettingsSerializer serializer;

  @Inject
  public CustomSelectSettingHandler(JsonSettingsSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public JsonObject serialize(
      CustomSelectSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();
    object.add("possible", this.serialize(setting, annotation.value()));

    object.addProperty("value", currentValue == null ? "" : (String) currentValue);

    object.addProperty("selectType", annotation.type().name());
    return object;
  }

  private JsonArray serialize(RegisteredSetting setting, Selection[] selections) {
    JsonArray array = new JsonArray();

    for (Selection selection : selections) {
      JsonObject object = new JsonObject();
      array.add(object);

      object.addProperty("name", selection.value());

      for (SettingsSerializationHandler<DisplayName> handler :
          this.serializer.getHandlers(DisplayName.class)) {
        handler.append(object, setting, selection.display());
      }
      for (SettingsSerializationHandler<Description> handler :
          this.serializer.getHandlers(Description.class)) {
        handler.append(object, setting, selection.description());
      }
      for (SettingsSerializationHandler<Icon> handler :
              this.serializer.getHandlers(Icon.class)) {
        handler.append(object, setting, selection.icon());
      }
    }

    return array;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, CustomSelectSetting annotation) {
    if (input == null) {
      return true;
    }
    if (!(input instanceof String)) {
      return false;
    }

    for (Selection selection : annotation.value()) {
      if (selection.value().equals(input)) {
        return true;
      }
    }

    return false;
  }
}
