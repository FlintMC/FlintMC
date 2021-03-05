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

package net.flintmc.mcapi.internal.settings.flint.discover;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import net.flintmc.framework.config.event.ConfigDiscoveredEvent;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.settings.flint.InvalidSettingsException;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.Category;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredCategory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;

@Singleton
public class SettingsDiscoverer {

  private final SettingsProvider settingsProvider;
  private final RegisteredCategory.Factory categoryFactory;
  private final RegisteredSetting.Factory settingFactory;
  private final ComponentAnnotationSerializer annotationSerializer;
  private final Map<String, String> launchArguments;

  @Inject
  public SettingsDiscoverer(
      SettingsProvider settingsProvider,
      RegisteredCategory.Factory categoryFactory,
      RegisteredSetting.Factory settingFactory,
      ComponentAnnotationSerializer annotationSerializer,
      @Named("launchArguments") Map launchArguments) {
    this.settingsProvider = settingsProvider;
    this.categoryFactory = categoryFactory;
    this.settingFactory = settingFactory;
    this.annotationSerializer = annotationSerializer;
    this.launchArguments = launchArguments;
  }

  @Subscribe(Subscribe.Phase.POST)
  public void handleConfigDiscovered(ConfigDiscoveredEvent event) {
    ParsedConfig config = event.getConfig();

    for (ConfigObjectReference reference : config.getConfigReferences()) {
      this.handleSetting(config, reference);
    }
  }

  private void handleSetting(ParsedConfig config, ConfigObjectReference reference) {
    Annotation annotation = this.findSettingAnnotation(reference);
    if (annotation == null) {
      return;
    }
    ApplicableSetting applicableSetting =
        annotation.annotationType().getAnnotation(ApplicableSetting.class);

    // multiple settings on one method (e.g. get(Key key, Value value))
    Type type = reference.getSerializedType();
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterized = (ParameterizedType) type;
      if (parameterized.getRawType().equals(Map.class)) {
        type = parameterized.getActualTypeArguments()[1]; // value
      }
    }

    if (!(type instanceof Class)) {
      this.throwInvalidSetting(reference, annotation, config, type);
    }

    Class<?> clazz = (Class<?>) type;
    if (clazz.isPrimitive()) {
      clazz = PrimitiveTypeLoader.getWrappedClass(clazz);
    }

    Class<?> finalClazz = clazz;
    boolean assignable =
        Arrays.stream(applicableSetting.types())
            .anyMatch(
                required -> {
                  if (required.isPrimitive()) {
                    required = PrimitiveTypeLoader.getWrappedClass(required);
                  }
                  return required.isAssignableFrom(finalClazz);
                });

    if (!assignable) {
      // we need assignableFrom because for example the EnumDropDown uses Enum.class as the required
      // parameter
      // and can't specify more specific values
      this.throwInvalidSetting(reference, annotation, config, type);
    }

    String category = this.findCategoryName(reference);

    RegisteredSetting registeredSetting =
        this.settingFactory.create(annotation.annotationType(), category, reference);
    this.settingsProvider.registerSetting(registeredSetting);
  }

  private void throwInvalidSetting(
      ConfigObjectReference reference, Annotation annotation, ParsedConfig config, Type type) {
    throw new InvalidSettingsException(
        "Cannot register setting on '"
            + reference.getKey()
            + "' in config '"
            + config.getConfigName()
            + "' because none of the allowed types for "
            + annotation.annotationType().getName()
            + " match "
            + type.getTypeName());
  }

  private String findCategoryName(ConfigObjectReference reference) {
    Category category = reference.findLastAnnotation(Category.class);
    DefineCategory define = reference.findLastAnnotation(DefineCategory.class);

    String name = null;

    if (define != null) {
      name = define.name();

      // define the category if it doesn't exist yet (it may already exist if the annotation is
      // placed on an interface,
      // all methods inside of it would define a new category)
      if (this.settingsProvider.getCategory(name) == null) {
        this.settingsProvider.registerCategory(
            this.categoryFactory.create(
                name,
                this.annotationSerializer.deserialize(define.displayName(), define.name()),
                this.annotationSerializer.deserialize(define.description()),
                define.icon()));
      }
    }

    if (category != null) {
      name = category.value();
    }

    return name;
  }

  private Annotation findSettingAnnotation(ConfigObjectReference reference) {
    for (Annotation annotation : reference.findAllAnnotations()) {
      if (annotation.annotationType().isAnnotationPresent(ApplicableSetting.class)) {
        return annotation;
      }
    }

    return null;
  }
}
