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

package net.flintmc.mcapi.settings.flint.options.text.string;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import java.util.Collection;

/**
 * {@link SettingData} implementation for the {@link StringSetting}.
 */
public interface StringData extends SettingData {

  /**
   * Retrieves the maximum number of characters of the string that can be used as the value of this
   * setting.
   *
   * @return The maximum length of the string value of this setting
   */
  int getMaxLength();

  /**
   * Changes the maximum number of characters of the string that can be used as the value of this
   * setting.
   *
   * @param maxLength The maximum length of the string value of this setting
   */
  void setMaxLength(int maxLength);

  /**
   * Retrieves the prefix that should be displayed in the text input (before the text) and which
   * cannot be modified by the user. This may be useful for something like "https://youtube.com/" to
   * let the user type in their channel name.
   *
   * @return The non-null prefix or an empty string to show no prefix
   */
  String getPrefix();

  /**
   * Changes the prefix that should be displayed in the text input (before the text) and which
   * cannot be modified by the user.
   *
   * @param prefix The non-null prefix or an empty string to show no prefix
   * @see #getPrefix()
   */
  void setPrefix(String prefix);

  /**
   * Retrieves the suffix that should be displayed in the text input (after the text) and which
   * cannot be modified by the user.
   *
   * @return The non-null suffix or an empty string to show no suffix
   */
  String getSuffix();

  /**
   * Changes the suffix that should be displayed in the text input (after the text) and which cannot
   * be modified by the user.
   *
   * @param suffix The non-null suffix or an empty string to show no suffix
   * @see #getSuffix()
   */
  void setSuffix(String suffix);

  /**
   * Retrieves the placeholder that should be displayed in the text input if no value is set by the
   * user.
   *
   * @return The non-null placeholder or an empty string to show no placeholder
   */
  String getPlaceholder();

  /**
   * Changes the placeholder that should be displayed in the text input if no value is set by the
   * user.
   *
   * @param placeholder The non-null placeholder or an empty string to show no placeholder
   */
  void setPlaceholder(String placeholder);

  /**
   * Retrieves all restrictions for the text input, if empty, there will be no restriction.
   *
   * @return The non-null collection of all restrictions for the input
   */
  Collection<StringRestriction> getRestrictions();

  /**
   * Adds a new restriction to this string setting.
   *
   * @param restriction The non-null restriction to be added
   */
  void addRestriction(StringRestriction restriction);

  /**
   * Removes a new restriction to this string setting.
   *
   * @param restriction The non-null restriction to be removed
   */
  void removeRestriction(StringRestriction restriction);

  /**
   * Factory for the {@link StringData}.
   */
  @AssistedFactory(StringData.class)
  interface Factory {

    /**
     * Creates a new {@link StringData} for the given setting.
     *
     * @param setting      The non-null setting to create the data for
     * @param prefix       The non-null prefix or an empty string to show no prefix
     * @param suffix       The non-null suffix or an empty string to show no suffix
     * @param placeholder  The non-null placeholder or an empty string to show no placeholder
     * @param maxLength    The maximum length of the string value of this setting
     * @param restrictions The non-null collection of all restrictions for the input
     * @return The new non-null {@link StringData}
     */
    StringData create(
        @Assisted("setting") RegisteredSetting setting,
        @Assisted("prefix") String prefix,
        @Assisted("suffix") String suffix,
        @Assisted("placeholder") String placeholder,
        @Assisted("maxLength") int maxLength,
        @Assisted("restrictions") Collection<StringRestriction> restrictions);

    /**
     * Creates a new {@link StringData} for the given setting and with every value being empty.
     *
     * @param setting The non-null setting to create the data for
     * @return The new non-null {@link StringData}
     */
    StringData create(@Assisted("setting") RegisteredSetting setting);

  }

}
