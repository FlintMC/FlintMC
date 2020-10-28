package net.labyfy.internal.component.settings;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.settings.SettingsProvider;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

@Singleton
@Implement(SettingsProvider.class)
public class DefaultSettingsProvider implements SettingsProvider {

  private final Collection<RegisteredSetting> settings = new ArrayList<>();

  @Override
  public void registerCategory(RegisteredCategory category) throws IllegalArgumentException {

  }

  @Override
  public Collection<RegisteredCategory> getCategories() {
    return null;
  }

  @Override
  public RegisteredCategory getCategory(String name) {
    return null;
  }

  @Override
  public void registerSetting(RegisteredSetting setting) {
    System.out.println(setting);
    this.settings.add(setting);
  }

  @Override
  public Collection<RegisteredSetting> getAllSettings() {
    return null;
  }

  @Override
  public <A extends Annotation> Collection<RegisteredSetting> getSettings(Class<A> annotationType) {
    return null;
  }

  @Override
  public Collection<RegisteredSetting> getSettings(ParsedConfig config) {
    return null;
  }
}
