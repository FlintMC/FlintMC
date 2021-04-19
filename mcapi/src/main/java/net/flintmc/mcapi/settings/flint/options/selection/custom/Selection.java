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

package net.flintmc.mcapi.settings.flint.options.selection.custom;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectSetting;

/**
 * Represents a selection entry in a {@link CustomSelectSetting}.
 *
 * @see CustomSelectSetting
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Selection {

  /**
   * Retrieves the unique name per {@link CustomSelectSetting} that identifies this selection entry,
   * it is also used for the stored value in the setting.
   *
   * @return The unique name of this entry
   */
  String value();

  /**
   * Retrieves the optional display name of this selection entry, if it is not provided, the {@link
   * #value()} will be displayed.
   *
   * @return The display name of this entry
   */
  DisplayName display() default @DisplayName({});

  /**
   * Retrieves the optional description of this selection entry.
   *
   * @return The description of this entry
   */
  Description description() default @Description({});

  /**
   * Retrieves the optional icon of this selection entry.
   *
   * @return The icon of this entry
   */
  Icon icon() default @Icon();
}
