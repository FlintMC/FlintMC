package net.labyfy.internal.component.gamesettings;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.gamesettings.LanguageSetting;
import net.labyfy.component.i18n.I18n;
import net.labyfy.component.settings.mapper.RegisterSettingHandler;
import net.labyfy.component.settings.mapper.SettingHandler;
import net.labyfy.component.settings.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(LanguageSetting.class)
public class LanguageSettingHandler implements SettingHandler<LanguageSetting> {

  private final I18n i18n;
  private final Gson gson;

  @Inject
  public LanguageSettingHandler(I18n i18n) {
    this.i18n = i18n;
    this.gson = new Gson();
  }

  @Override
  public JsonObject serialize(LanguageSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.add("languages", this.gson.toJsonTree(this.i18n.getAvailableLanguages()));
    object.addProperty("selected", (String) currentValue);

    return object;
  }

  @Override
  public boolean isValidInput(Object input, ConfigObjectReference reference, LanguageSetting annotation) {
    return input instanceof String && this.i18n.getAvailableLanguages().contains(input);
  }
}
