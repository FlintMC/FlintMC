package net.flintmc.mcapi.internal.settings.flint.options;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.BooleanSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(BooleanSetting.class)
public class BooleanSettingHandler implements SettingHandler<BooleanSetting> {

  @Override
  public JsonObject serialize(
      BooleanSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.addProperty("value", currentValue != null && (boolean) currentValue);

    return object;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, BooleanSetting annotation) {
    return input instanceof Boolean;
  }
}
