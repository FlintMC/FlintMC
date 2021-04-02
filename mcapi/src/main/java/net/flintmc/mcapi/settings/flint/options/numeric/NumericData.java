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

package net.flintmc.mcapi.settings.flint.options.numeric;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import java.util.Map;

/**
 * {@link SettingData} implementation for the {@link NumericSetting} and {@link SliderSetting}.
 */
public interface NumericData extends SettingData {

  /**
   * Retrieves the minimum that can be the value of this setting.
   *
   * @return The minimum value of this setting
   */
  double getMinValue();

  /**
   * Changes the minimum that can be the value of this setting.
   *
   * @param minValue The minimum value of this setting
   */
  void setMinValue(double minValue);

  /**
   * Retrieves the maximum that can be the value of this setting.
   *
   * @return The maximum value of this setting
   */
  double getMaxValue();

  /**
   * Changes the maximum that can be the value of this setting.
   *
   * @param maxValue The maximum value of this setting
   */
  void setMaxValue(double maxValue);

  /**
   * Retrieves the maximum number of decimal places that the value of this setting can have.
   *
   * @return The minimum number of decimal places of this setting, 0 for integers only
   */
  int getDecimalPlaces();

  /**
   * Changes the maximum number of decimal places that the value of this setting can have.
   *
   * @param decimalPlaces The minimum number of decimal places of this setting, 0 for integers only
   */
  void setDecimalPlaces(int decimalPlaces);

  /**
   * Retrieves the text that should be displayed for the given value (if the given value is the
   * value of this setting) instead of the value itself.
   *
   * @param value The value to get the display for
   * @return The text to be displayed for the given value or {@code null} if there is no specific
   * text to be displayed for the given value
   */
  ChatComponent getOverriddenDisplay(double value);

  /**
   * Sets the text that should be displayed if the given value is set as the value of this setting.
   *
   * @param value   The value that needs to be set as the value of this setting for the given text
   *                to be displayed
   * @param display The text to be displayed or {@code null} for no specific text and thus the value
   *                itself to be displayed
   */
  void setOverriddenDisplay(double value, ChatComponent display);

  /**
   * Retrieves a map of all texts that should be displayed for their numeric values (being the key
   * in the map) instead of the numeric value itself.
   *
   * @return The non-null map with all texts to be displayed (being the value in the map) for the
   * given numeric value (being the key in the map)
   */
  Map<Double, ChatComponent> getOverriddenDisplays();

  /**
   * Factory for the {@link NumericData}.
   */
  @AssistedFactory(NumericData.class)
  interface Factory {

    /**
     * Creates a new {@link NumericData} for the given setting.
     *
     * @param setting            The non-null setting to create the data for
     * @param minValue           The minimum that can be the value of this setting
     * @param maxValue           The maximum that can be the value of this setting
     * @param decimalPlaces      The minimum number of decimal places of this setting, 0 for
     *                           integers only
     * @param overriddenDisplays The non-null map with all texts to be displayed (being the value in
     *                           the map) for the given numeric value (being the key in the map)
     * @return The new non-null {@link NumericData}
     */
    NumericData create(
        @Assisted("setting") RegisteredSetting setting,
        @Assisted("minValue") double minValue,
        @Assisted("maxValue") double maxValue,
        @Assisted("decimalPlaces") int decimalPlaces,
        @Assisted("overriddenDisplays") Map<Double, ChatComponent> overriddenDisplays);

  }

}
