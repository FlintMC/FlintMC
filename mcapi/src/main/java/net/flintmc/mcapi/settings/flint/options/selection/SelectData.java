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

package net.flintmc.mcapi.settings.flint.options.selection;

import java.util.Collection;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectSetting;
import net.flintmc.mcapi.settings.flint.options.selection.enumeration.EnumSelectSetting;

/**
 * {@link SettingData} implementation for the {@link CustomSelectSetting} and {@link
 * EnumSelectSetting}.
 */
public interface SelectData extends SettingData {

  /**
   * Retrieves the type how this selection should be displayed.
   *
   * @return The non-null type how this selection should be displayed
   */
  SelectMenuType getType();

  /**
   * Changes the type how this selection should be displayed.
   *
   * @param type The non-null type how this selection should be displayed
   */
  void setType(SelectMenuType type);

  /**
   * Retrieves a collection of all selectable and displayed entries of this selection.
   *
   * @return The non-null collection of entries to be selectable and displayed
   */
  Collection<SelectionEntry> getEntries();

}
