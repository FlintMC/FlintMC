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

import java.util.Collection;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.selection.SelectData;
import net.flintmc.mcapi.settings.flint.options.selection.SelectMenuType;
import net.flintmc.mcapi.settings.flint.options.selection.SelectionEntry;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

/**
 * {@link SettingData} implementation for the {@link CustomSelectSetting}.
 */
public interface CustomSelectData extends SelectData {

  /**
   * Adds a new entry to be selectable and displayed to this setting.
   *
   * @param entry The non-null entry to be added to this selection
   */
  void addEntry(SelectionEntry entry);

  /**
   * Removes the given entry from this selection so that it won't be selectable anymore. If the
   * given entry is currently the {@link RegisteredSetting#getCurrentValue() value of this setting},
   * this won't be changed automatically and should be changed to an entry that is still available
   * in this selection.
   *
   * @param entry The non-null to be removed from this selection
   */
  void removeEntry(SelectionEntry entry);

  /**
   * Factory for the {@link CustomSelectData}.
   */
  @AssistedFactory(CustomSelectData.class)
  interface Factory {

    /**
     * Creates a new {@link CustomSelectData} for the given setting.
     *
     * @param setting The non-null setting to create the data for
     * @param type    The non-null type how the selection should be displayed
     * @param entries The non-null collection of entries to be selectable and displayed
     * @return The new non-null {@link CustomSelectData}
     */
    CustomSelectData create(
        @Assisted("setting") RegisteredSetting setting,
        @Assisted("type") SelectMenuType type,
        @Assisted("entries") Collection<SelectionEntry> entries);

  }

}
