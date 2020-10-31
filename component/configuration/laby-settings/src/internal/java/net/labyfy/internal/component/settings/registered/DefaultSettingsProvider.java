package net.labyfy.internal.component.settings.registered;

import com.google.inject.Singleton;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.registered.SettingsProvider;

import java.lang.annotation.Annotation;
import java.util.*;

@Singleton
@Implement(SettingsProvider.class)
public class DefaultSettingsProvider implements SettingsProvider {

  private final Collection<RegisteredSetting> settings = new ArrayList<>();
  private final Map<String, RegisteredCategory> categories = new HashMap<>();

  @Override
  public void registerCategory(RegisteredCategory category) throws IllegalArgumentException {
    if (this.categories.containsKey(category.getRegistryName())) {
      throw new IllegalArgumentException("A category with the name '" + category.getRegistryName() + "' is already registered");
    }
    this.categories.put(category.getRegistryName(), category);
  }

  @Override
  public Collection<RegisteredCategory> getCategories() {
    return this.categories.values();
  }

  @Override
  public RegisteredCategory getCategory(String name) {
    return this.categories.get(name);
  }

  @Override
  public void registerSetting(RegisteredSetting setting) {
    this.settings.add(setting);
  }

  @Override
  public Collection<RegisteredSetting> getAllSettings() {
    return this.settings;
  }

  @Override
  public <A extends Annotation> Collection<RegisteredSetting> getSettings(Class<A> annotationType) {
    Collection<RegisteredSetting> filtered = new HashSet<>();
    for (RegisteredSetting setting : this.settings) {
      if (setting.getAnnotation().annotationType().equals(annotationType)) {
        filtered.add(setting);
      }
    }
    return filtered;
  }

  @Override
  public Collection<RegisteredSetting> getSettings(ParsedConfig config) {
    Collection<RegisteredSetting> filtered = new HashSet<>();
    for (RegisteredSetting setting : this.settings) {
      if (setting.getConfig() == config) {
        filtered.add(setting);
      }
    }
    return filtered;
  }

  @Override
  public RegisteredSetting getSetting(ConfigObjectReference reference) {
    return this.getSetting(reference.getKey());
  }

  @Override
  public RegisteredSetting getSetting(String key) {
    for (RegisteredSetting setting : this.settings) {
      if (setting.getReference().getKey().equals(key)) {
        return setting;
      }
    }

    return null;
  }
}
