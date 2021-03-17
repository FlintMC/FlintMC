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

package net.flintmc.mcapi.settings.flint.options.selection.enumeration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.options.selection.SelectMenuType;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectSetting;
import net.flintmc.mcapi.settings.flint.options.selection.custom.Selection;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

/**
 * The same as {@link CustomSelectSetting}, but things like the {@link DisplayName} aren't got from
 * the {@link Selection}, but from the enum constant.
 * <p>
 * {@link RegisteredSetting#getData()} will be an instance of {@link EnumSelectData}.
 *
 * @see ApplicableSetting
 * @see CustomSelectSetting
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = Enum.class, name = "dropdown", data = EnumSelectData.class)
public @interface EnumSelectSetting {

  /**
   * Retrieves the type of this menu.
   *
   * @return The type of this menu
   */
  SelectMenuType value() default SelectMenuType.DROPDOWN;
}
