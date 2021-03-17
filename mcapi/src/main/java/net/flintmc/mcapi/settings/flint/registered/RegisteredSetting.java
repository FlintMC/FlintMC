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

package net.flintmc.mcapi.settings.flint.registered;

import java.lang.annotation.Annotation;
import java.util.Collection;
import javax.annotation.Nullable;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.ForceFullWidth;
import net.flintmc.mcapi.settings.flint.annotation.ui.SubSettingsFor;
import net.flintmc.mcapi.settings.flint.annotation.version.VersionExclude;
import net.flintmc.mcapi.settings.flint.annotation.version.VersionOnly;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.text.string.StringData;
import net.flintmc.mcapi.settings.flint.options.text.string.StringSetting;

/**
 * Represents a setting in the {@link SettingsProvider}.
 *
 * @see ApplicableSetting
 */
public interface RegisteredSetting {

  /**
   * Retrieves the reference to the config entry where this setting is stored.
   *
   * @return The non-null config reference of this setting
   */
  ConfigObjectReference getReference();

  /**
   * Retrieves the type from the {@link ApplicableSetting} annotation which marks this setting.
   *
   * @return The non-null type
   */
  String getType();

  /**
   * Retrieves the {@link ApplicableSetting} annotation which marks this setting. This can be for
   * example {@link StringSetting}.
   *
   * @return The non-null annotation of this setting
   */
  Annotation getAnnotation();

  /**
   * Retrieves the data about this setting created from the {@link #getAnnotation() annotation}. See
   * the documentation of this annotation for more information about the return value of this
   * method.
   *
   * @param <T> The type of setting data (e.g. {@link StringData} for {@link StringSetting})
   * @return The data or null if the {@link ApplicableSetting} of this setting doesn't have any
   * additional data
   */
  <T extends SettingData> T getData();

  /**
   * Retrieves the current value that is stored in the config.
   *
   * @return The current value, may be {@code null}
   * @see ConfigObjectReference#getValue()
   */
  Object getCurrentValue();

  /**
   * Checks if the given value can be set on this setting and if it can, it sets the given value to
   * this setting.
   *
   * @param value The value to be set, may be {@code null}
   * @return {@code true} if it has been successfully set, {@code false} otherwise
   * @see SettingHandler#isValidInput(Object, ConfigObjectReference, Annotation)
   */
  boolean setCurrentValue(Object value);

  /**
   * Retrieves the name of the category which this setting belongs to.
   *
   * @return The name of the category or {@code null}, if it doesn't belong to any category
   * @see SettingsProvider#getCategory(String)
   */
  String getCategoryName();

  /**
   * Retrieves whether this setting is currently enabled or not. If is not enabled, the user cannot
   * change it.
   *
   * @return {@code true} if it is enabled, {@code false} otherwise
   * @see #setEnabled(boolean)
   */
  boolean isEnabled();

  /**
   * Enables/disables this setting. If it is not enabled, the user cannot change anything on it. If
   * the {@link #isEnabled() enabled state} is already the same as the given {@code enabled} value,
   * nothing will happen.
   *
   * @param enabled {@code true} if it should be enabled, {@code false} otherwise
   */
  void setEnabled(boolean enabled);

  /**
   * Retrieves the display name of this setting for the given {@code key}.
   *
   * @param key see {@link ConfigObjectReference#findLastAnnotation(Class, Object)}
   * @return The display name of this setting or {@code null} if there is no {@link DisplayName}
   * annotation on config reference of this setting
   * @see DisplayName
   */
  ChatComponent getDisplayName(Object key);

  /**
   * Retrieves the description of this setting for the given {@code key}.
   *
   * @param key see {@link ConfigObjectReference#findLastAnnotation(Class, Object)}
   * @return The description of this setting or {@code null} if there is no {@link Description}
   * annotation on config reference of this setting
   * @see Description
   */
  ChatComponent getDescription(Object key);

  /**
   * Retrieves whether the {@link ForceFullWidth} annotation is present on this setting.
   *
   * @param key see {@link ConfigObjectReference#findLastAnnotation(Class, Object)}
   * @return {@code true} if the annotation is present on this setting, {@code false} otherwise
   * @see ForceFullWidth
   */
  boolean isFullWidthForced(Object key);

  /**
   * Retrieves whether the this setting is hidden and shouldn't be rendered. It may be hidden
   * because {@link VersionOnly} or {@link VersionExclude} specify that the setting shouldn't be
   * available in the running version.
   *
   * @param key see {@link ConfigObjectReference#findLastAnnotation(Class, Object)}
   * @return {@code true} if this setting should be hidden and therefore not rendered, {@code false}
   * otherwise
   */
  boolean isHidden(Object key);

  /**
   * Retrieves a collection of all sub settings of this setting. The collection can be modified
   *
   * @return The non-null collection of sub settings in this setting
   * @see SubSettingsFor
   */
  Collection<RegisteredSetting> getSubSettings();

  /**
   * Factory for the {@link RegisteredSetting}.
   */
  @AssistedFactory(RegisteredSetting.class)
  interface Factory {

    /**
     * Creates a new {@link RegisteredSetting} with the given values.
     *
     * @param annotationType The non-null type of annotation which marks this setting ({@link
     *                       #getAnnotation()})
     * @param categoryName   The name of the category the new setting belongs to, or {@code null},
     *                       if it doesn't belong to any category
     * @param reference      The non-null reference in a {@link Config} where this setting has been
     *                       discovered
     * @return The new non-null {@link RegisteredSetting}
     */
    RegisteredSetting create(
        @Assisted Class<? extends Annotation> annotationType,
        @Assisted @Nullable String categoryName,
        @Assisted ConfigObjectReference reference);
  }
}
