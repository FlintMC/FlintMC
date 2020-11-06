package net.labyfy.internal.component.settings.options.dropdown;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.annotation.ui.Description;
import net.labyfy.component.settings.annotation.ui.DisplayName;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.dropdown.CustomSelectSetting;
import net.labyfy.component.settings.options.dropdown.Selection;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.serializer.JsonSettingsSerializer;
import net.labyfy.component.settings.serializer.SettingsSerializationHandler;

@Singleton
@RegisterSettingHandler(CustomSelectSetting.class)
public class CustomSelectSettingHandler implements SettingHandler<CustomSelectSetting> {

  private final JsonSettingsSerializer serializer;

  @Inject
  public CustomSelectSettingHandler(JsonSettingsSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public JsonObject serialize(CustomSelectSetting annotation, RegisteredSetting setting, Object currentValue) {
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

      for (SettingsSerializationHandler<DisplayName> handler : this.serializer.getHandlers(DisplayName.class)) {
        handler.append(object, setting, selection.display());
      }
      for (SettingsSerializationHandler<Description> handler : this.serializer.getHandlers(Description.class)) {
        handler.append(object, setting, selection.description());
      }
    }

    return array;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, CustomSelectSetting annotation) {
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
