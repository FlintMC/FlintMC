package net.labyfy.internal.component.settings.options.dropdown;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.dropdown.CustomDropDownSetting;
import net.labyfy.component.settings.registered.RegisteredSetting;

import java.util.Arrays;

@Singleton
@RegisterSettingHandler(CustomDropDownSetting.class)
public class CustomDropDownSettingHandler implements SettingHandler<CustomDropDownSetting> {

  private final Gson gson = new Gson();

  @Override
  public Object getDefaultValue(CustomDropDownSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
  }

  @Override
  public JsonObject serialize(CustomDropDownSetting annotation, RegisteredSetting setting) {
    JsonObject object = new JsonObject();
    object.add("possible", this.gson.toJsonTree(annotation.value()));
    object.addProperty("value", (String) setting.getCurrentValue());
    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, CustomDropDownSetting annotation) {
    return input == null || (input instanceof String && Arrays.asList(annotation.value()).contains(input));
  }
}
