package net.flintmc.mcapi.settings.flint.registered;

import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;

import java.lang.annotation.Annotation;
import java.util.Collection;

public interface SettingsProvider {

  void registerCategory(RegisteredCategory category) throws IllegalArgumentException;

  Collection<RegisteredCategory> getCategories();

  RegisteredCategory getCategory(String name);

  void registerSetting(RegisteredSetting setting);

  Collection<RegisteredSetting> getAllSettings();

  <A extends Annotation> Collection<RegisteredSetting> getSettings(Class<A> annotationType);

  Collection<RegisteredSetting> getSettings(ParsedConfig config);

  RegisteredSetting getSetting(ConfigObjectReference reference);

  // the key from the config reference (+ example)
  // case-sensitive
  RegisteredSetting getSetting(String key);

}
