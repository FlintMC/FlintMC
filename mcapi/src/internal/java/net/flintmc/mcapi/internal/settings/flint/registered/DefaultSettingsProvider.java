/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.internal.settings.flint.registered;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageResolver;
import net.flintmc.mcapi.settings.flint.annotation.ui.InternalCategory;
import net.flintmc.mcapi.settings.flint.annotation.ui.SubSettingsFor;
import net.flintmc.mcapi.settings.flint.event.SettingRegisterEvent;
import net.flintmc.mcapi.settings.flint.event.SettingRegisterEvent.Factory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredCategory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;
import org.apache.logging.log4j.Logger;

@Singleton
@Implement(SettingsProvider.class)
public class DefaultSettingsProvider implements SettingsProvider {

  private final Logger logger;
  private final PackageResolver packageResolver;
  private final EventBus eventBus;
  private final SettingRegisterEvent.Factory registerEventFactory;

  private final Collection<RegisteredSetting> settings;
  private final Map<String, RegisteredCategory> categories;

  @Inject
  private DefaultSettingsProvider(
      @InjectLogger Logger logger,
      PackageResolver packageResolver,
      EventBus eventBus,
      Factory registerEventFactory) {
    this.logger = logger;
    this.packageResolver = packageResolver;
    this.eventBus = eventBus;
    this.registerEventFactory = registerEventFactory;

    this.settings = new CopyOnWriteArrayList<>();
    this.categories = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerCategory(RegisteredCategory category) throws IllegalArgumentException {
    if (this.categories.containsKey(category.getRegistryName())) {
      throw new IllegalArgumentException(
          "A category with the name '" + category.getRegistryName() + "' is already registered");
    }
    this.categories.put(category.getRegistryName(), category);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<RegisteredCategory> getCategories() {
    return this.categories.values();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RegisteredCategory getCategory(String name) {
    return this.categories.get(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerSetting(RegisteredSetting setting) {
    SettingRegisterEvent event = this.registerEventFactory.create(setting);
    if (this.eventBus.fireEvent(event, Phase.PRE).isCancelled()) {
      this.logger.debug(
          "Not registering setting {}.{} because it has been cancelled by the EventBus",
          setting.getReference().getConfig().getConfigName(),
          setting.getReference().getKey());
      return;
    }

    if (!this.registerSubSettings(setting, false)) {
      this.settings.add(setting);
    }

    for (RegisteredSetting registeredSetting : this.settings) {
      this.registerSubSettings(registeredSetting, true);
    }

    this.eventBus.fireEvent(event, Phase.POST);
  }

  private boolean registerSubSettings(RegisteredSetting newSetting, boolean remove) {
    SubSettingsFor subSettingsFor =
        newSetting.getReference().findLastAnnotation(SubSettingsFor.class);
    if (subSettingsFor == null) {
      return false;
    }

    Class<?> declaring = newSetting.getReference().getDeclaringClass();
    if (declaring.getDeclaringClass() != null) {
      declaring = declaring.getDeclaringClass();
    }

    if (!subSettingsFor.declaring().equals(SubSettingsFor.Dummy.class)) {
      declaring = subSettingsFor.declaring();
    }

    boolean found = false;
    for (RegisteredSetting setting : this.settings) {
      if (newSetting.getReference().getConfig().getClass()
          != setting.getReference().getConfig().getClass()) {
        // there might be multiple instances of the declaring class of the setting
        // when for example an interface extends from another interface which contains
        // the sub settings
        continue;
      }

      if (declaring.isAssignableFrom(setting.getReference().getDeclaringClass())
          && setting.getReference().getLastName().equals(subSettingsFor.value())) {
        setting.getSubSettings().add(newSetting);
        if (remove) {
          this.settings.remove(newSetting);
        }
        found = true;
      }
    }

    return found;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<RegisteredSetting> getAllSettings() {
    return this.settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<RegisteredSetting> getCategorizedSettings(String internalCategory) {
    Collection<RegisteredSetting> settings = new ArrayList<>();
    for (RegisteredSetting setting : this.getAllSettings()) {
      InternalCategory category = setting.getReference().findLastAnnotation(InternalCategory.class);
      if (category != null && internalCategory.equals(category.value())) {
        settings.add(setting);
      }
    }
    return settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<RegisteredSetting> getSettings(String packageName) {
    Collection<RegisteredSetting> settings = new ArrayList<>();
    for (RegisteredSetting setting : this.getAllSettings()) {
      Package settingPackage =
          this.packageResolver.resolvePackage(setting.getReference().getConfigBaseClass());
      if (settingPackage != null && settingPackage.getName().equals(packageName)) {
        settings.add(setting);
      }
    }

    return settings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <A extends Annotation> Collection<RegisteredSetting> getSettings(Class<A> annotationType) {
    Collection<RegisteredSetting> filtered = new ArrayList<>();
    for (RegisteredSetting setting : this.settings) {
      if (setting.getAnnotation().annotationType().equals(annotationType)) {
        filtered.add(setting);
      }
    }
    return filtered;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<RegisteredSetting> getSettings(ParsedConfig config) {
    Collection<RegisteredSetting> filtered = new ArrayList<>();
    for (RegisteredSetting setting : this.settings) {
      if (setting.getReference().getConfig() == config) {
        filtered.add(setting);
      }
    }
    return filtered;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RegisteredSetting getSetting(ConfigObjectReference reference) {
    return this.getSetting(reference.getKey());
  }

  /**
   * {@inheritDoc}
   */
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
