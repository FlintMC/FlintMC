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

package net.flintmc.mcapi.settings.flint.options.bool;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import javax.annotation.Nullable;

/**
 * {@link SettingData} implementation for the {@link BooleanSetting}.
 */
public interface BooleanData extends SettingData {

  /**
   * Retrieves the text that should be displayed if the setting is set to {@code true}.
   *
   * @return The text to be displayed if the setting is set to {@code true} or {@code null} if the
   * default text should be displayed
   */
  ChatComponent getEnabledText();

  /**
   * Changes the text that should be displayed if the setting is set to {@code true}.
   *
   * @param enabledText The text to be displayed if the setting is set to {@code true} or {@code
   *                    null} if the default text should be displayed
   */
  void setEnabledText(ChatComponent enabledText);

  /**
   * Retrieves the text that should be displayed if the setting is set to {@code false}.
   *
   * @return The text to be displayed if the setting is set to {@code false} or {@code null} if the
   * default text should be displayed
   */
  ChatComponent getDisabledText();

  /**
   * Changes the text that should be displayed if the setting is set to {@code false}.
   *
   * @param disabledText The text to be displayed if the setting is set to {@code false} or {@code
   *                     null} if the default text should be displayed
   */
  void setDisabledText(ChatComponent disabledText);

  /**
   * Factory for the {@link BooleanData}.
   */
  @AssistedFactory(BooleanData.class)
  interface Factory {

    /**
     * Creates a new {@link BooleanData} for the given setting.
     *
     * @param setting      The non-null setting to create the data for
     * @param enabledText  The text to be displayed if the setting is set to {@code true} or {@code
     *                     null} if the default text should be displayed
     * @param disabledText The text to be displayed if the setting is set to {@code false} or {@code
     *                     null} if the default text should be displayed
     * @return The new non-null {@link BooleanData}
     */
    BooleanData create(
        @Assisted("setting") RegisteredSetting setting,
        @Assisted("enabledText") @Nullable ChatComponent enabledText,
        @Assisted("disabledText") @Nullable ChatComponent disabledText);
  }

}
