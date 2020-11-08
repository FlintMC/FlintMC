package net.flintmc.mcapi.internal.settings.game.annotation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.game.annotation.LanguageSetting;
import net.flintmc.util.i18n.I18n;

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
  public JsonObject serialize(
      LanguageSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.add("languages", this.gson.toJsonTree(this.i18n.getAvailableLanguages()));
    object.addProperty("selected", (String) currentValue);

    return object;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, LanguageSetting annotation) {
    return input instanceof String && this.i18n.getAvailableLanguages().contains(input);
  }
}
