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

import net.flintmc.framework.config.defval.annotation.DefaultString;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.options.selection.SelectMenuType;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a dropdown or the minecraft selection menu (like the
 * Smooth Lighting: Maximum/Minimum/Off), to choose between those types see {@link #type()}.
 *
 * <p>To define a default value, see {@link DefaultString} and define {@link Selection#value()} as
 * the string.
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'value' with the current value from the setting as a string
 *   <li>'selectType' with the {@link #type() select type} as a string
 *   <li>'possible' with a json array of objects that contain the following:
 *       <ul>
 *         <li>'name' with the {@link Selection#value() name of the entry}
 *         <li>If present, the {@link DisplayName} from {@link Selection#display()}
 *         <li>If present, the {@link Description} from {@link Selection#description()}
 *         <li>If present, the {@link Icon} from {@link Selection#icon()}
 *       </ul>
 * </ul>
 *
 * <p>
 * {@link RegisteredSetting#getData()} will be an instance of {@link CustomSelectData}.
 *
 * @see ApplicableSetting
 * @see DefaultString
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = String.class, name = "dropdown", data = CustomSelectData.class)
public @interface CustomSelectSetting {

  /**
   * Retrieves all selections that are possible in this selection menu. The array has to contain at
   * least two entries.
   *
   * @return The array of all selections in this menu
   */
  Selection[] value();

  /**
   * Retrieves the type of this menu.
   *
   * @return The type of this menu
   */
  SelectMenuType type() default SelectMenuType.DROPDOWN;
}
