package net.flintmc.mcapi.settings.flint.registered;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.packages.Package;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.options.text.StringSetting;

import java.lang.annotation.Annotation;
import java.util.Collection;

/** Provider which contains every {@link RegisteredSetting} and {@link RegisteredCategory}. */
public interface SettingsProvider {

  /**
   * Registers a new category in this provider.
   *
   * @param category The non-null category to be registered
   * @throws IllegalArgumentException If a category with the {@link
   *     RegisteredCategory#getRegistryName() name} already exists
   */
  void registerCategory(RegisteredCategory category) throws IllegalArgumentException;

  /**
   * Retrieves a collection of all categories in this provider, it shouldn't be modified.
   *
   * @return The non-null collection of all categories in this provider
   */
  Collection<RegisteredCategory> getCategories();

  /**
   * Retrieves the category with the given name that is registered in this provider.
   *
   * @param name The non-null case-sensitive name of the category to get
   * @return The category with the given name or {@code null} if there is no category with the given
   *     name
   */
  RegisteredCategory getCategory(String name);

  /**
   * Registers a new setting in this provider. This shouldn't be called manually, consider using one
   * of the {@link ApplicableSetting} annotations in a {@link Config} to register a setting.
   *
   * @param setting The non-null setting to be registered
   */
  void registerSetting(RegisteredSetting setting);

  /**
   * Retrieves a collection of all settings that are registered in this provider, it shouldn't be
   * modified. {@link RegisteredSetting#getSubSettings() Sub settings} of settings aren't contained
   * in this collection.
   *
   * @return The non-null collection of all settings in this provider
   */
  Collection<RegisteredSetting> getAllSettings();

  /**
   * Retrieves a collection of all settings that are registered in this provider and that are loaded
   * from the given package. {@link RegisteredSetting#getSubSettings() Sub settings} of settings
   * aren't contained in this collection.
   *
   * @param packageName The non-null name of the package to search settings from
   * @return The new non-null collection containing all settings, modification to it won't have any
   *     effect
   * @see Package#getName()
   */
  Collection<RegisteredSetting> getSettings(String packageName);

  /**
   * Retrieves a collection of all settings that are registered in this provider and that match the
   * given annotation type.
   *
   * @param annotationType The type of annotations of the settings to be retrieved (e.g. {@link
   *     StringSetting})
   * @param <A> The type of annotations of the settings
   * @return The new non-null collection of all settings in this provider with the given annotation
   *     type, modification to this collection won't have any effect.
   * @see RegisteredSetting#getAnnotation()
   */
  <A extends Annotation> Collection<RegisteredSetting> getSettings(Class<A> annotationType);

  /**
   * Retrieves all settings in the given config.
   *
   * @param config The non-null config to get all settings from
   * @return The new non-null collection of all settings in this provider that match the given
   *     config
   */
  Collection<RegisteredSetting> getSettings(ParsedConfig config);

  /**
   * Retrieves the setting that matches the given reference.
   *
   * @param reference The non-null reference to match the setting
   * @return The setting to match the given reference or {@code null} if the given reference doesn't
   *     match any setting
   */
  RegisteredSetting getSetting(ConfigObjectReference reference);

  /**
   * Retrieves the setting that matches the given {@link ConfigObjectReference#getKey() key}.
   *
   * @param key The non-null case-sensitive key to match the setting (see {@link
   *     ConfigObjectReference#getKey()})
   * @return The setting to match the given key or {@code null} if the given key doesn't match any
   *     setting
   */
  RegisteredSetting getSetting(String key);
}
