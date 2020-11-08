package net.flintmc.mcapi.internal.settings.flint.options.text;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.options.text.StringRestriction;
import net.flintmc.mcapi.settings.flint.options.text.StringSetting;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import java.util.regex.Pattern;

@Singleton
@RegisterSettingHandler(StringSetting.class)
public class StringSettingHandler implements SettingHandler<StringSetting> {

  private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

  private final Gson gson = new Gson();

  @Override
  public JsonObject serialize(StringSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.addProperty("value", currentValue == null ? "" : (String) currentValue);
    if (annotation.value().length != 0) {
      object.add("restrictions", this.gson.toJsonTree(annotation.value()));
    }
    if (annotation.maxLength() != Integer.MAX_VALUE) {
      object.addProperty("maxLength", annotation.maxLength());
    }
    if (!annotation.prefix().isEmpty()) {
      object.addProperty("prefix", annotation.prefix());
    }
    if (!annotation.suffix().isEmpty()) {
      object.addProperty("suffix", annotation.suffix());
    }

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, StringSetting annotation) {
    if (!(input instanceof String)) {
      return false;
    }
    String value = (String) input;
    if (value.length() > annotation.maxLength()) {
      return false;
    }

    for (StringRestriction restriction : annotation.value()) {
      if (restriction == StringRestriction.URL_ONLY && !URL_PATTERN.matcher(value).matches()) {
        return false;
      }
    }

    return true;
  }
}
