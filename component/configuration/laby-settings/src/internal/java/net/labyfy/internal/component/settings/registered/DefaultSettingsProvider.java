package net.labyfy.internal.component.settings.registered;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.config.generator.ParsedConfig;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.settings.annotation.ui.SubSettingsFor;
import net.labyfy.component.settings.registered.RegisteredCategory;
import net.labyfy.component.settings.registered.RegisteredSetting;
import net.labyfy.component.settings.registered.SettingsProvider;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
@Implement(SettingsProvider.class)
public class DefaultSettingsProvider implements SettingsProvider {

  private final Logger logger;

  private final Collection<RegisteredSetting> settings = new CopyOnWriteArrayList<>();
  private final Map<String, RegisteredCategory> categories = new HashMap<>();

  @Inject
  public DefaultSettingsProvider(@InjectLogger Logger logger) {
    this.logger = logger;
  }

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
    if (!this.registerSubSettings(setting, false)) {
      this.settings.add(setting);
    }

    for (RegisteredSetting registeredSetting : this.settings) {
      this.registerSubSettings(registeredSetting, true);
    }
  }

  private boolean registerSubSettings(RegisteredSetting newSetting, boolean remove) {
    SubSettingsFor subSettingsFor = newSetting.getReference().findLastAnnotation(SubSettingsFor.class);
    if (subSettingsFor == null) {
      return false;
    }

    Class<?> declaring = newSetting.getReference().getDeclaringClass().getDeclaringClass();

    if (!subSettingsFor.declaring().equals(SubSettingsFor.Dummy.class)) {
      declaring = subSettingsFor.declaring();
    }

    if (declaring == null) {
      this.logger.trace("No declaring class found to map the SubSettings of " + newSetting.getReference().getKey()
          + " to '" + subSettingsFor.value() + "'");
      return false;
    }

    boolean found = false;
    for (RegisteredSetting setting : this.settings) {
      if (setting.getReference().getDeclaringClass().equals(declaring) && setting.getReference().getLastName().equals(subSettingsFor.value())) {
        setting.getSubSettings().add(newSetting);
        if (remove) {
          this.settings.remove(newSetting);
        }
        found = true;
      }
    }

    return found;
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
