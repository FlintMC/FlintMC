package net.labyfy.internal.component.settings.options.text;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.options.text.StringRestriction;
import net.labyfy.component.settings.options.text.StringSetting;

import java.util.regex.Pattern;

@Singleton
@RegisterSettingHandler(StringSetting.class)
public class StringSettingHandler implements SettingHandler<StringSetting> {

  private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

  @Override
  public Object getDefaultValue(StringSetting annotation, ConfigObjectReference reference) {
    return annotation.defaultValue();
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
