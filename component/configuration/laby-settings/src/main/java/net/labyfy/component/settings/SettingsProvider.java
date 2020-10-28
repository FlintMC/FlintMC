package net.labyfy.component.settings;

import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;

import java.lang.annotation.Annotation;
import java.util.Collection;

// TODO add the possibility to block specific settings ("button grayed out")

public interface SettingsProvider {

  void registerCategory(RegisteredCategory category) throws IllegalArgumentException;

  Collection<RegisteredCategory> getCategories();

  RegisteredCategory getCategory(String name);

  void registerSetting(RegisteredSetting setting);

  Collection<RegisteredSetting> getAllSettings();

  <A extends Annotation> Collection<RegisteredSetting> getSettings(Class<A> annotationType);

  Collection<RegisteredSetting> getSettings(ParsedConfig config);

}
