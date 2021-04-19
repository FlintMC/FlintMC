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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectData;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectSetting;
import net.flintmc.mcapi.settings.flint.options.selection.custom.Selection;
import net.flintmc.mcapi.settings.flint.options.selection.enumeration.EnumSelectData;
import net.flintmc.mcapi.settings.flint.options.selection.enumeration.EnumSelectSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import javax.annotation.Nullable;

/**
 * Represents an entry in a {@link CustomSelectSetting} or {@link EnumSelectSetting}.
 */
public interface SelectionEntry {

  /**
   * Retrieves the setting which this entry belongs to. {@link RegisteredSetting#getData()} will be
   * an instance of {@link SelectData}.
   *
   * @return The non-null setting which this entry belongs
   */
  RegisteredSetting getSetting();

  /**
   * Retrieves the unique identifier of this entry in a {@link SelectData}. For a {@link
   * CustomSelectData} this will be {@link Selection#value()} from the selection annotation, for an
   * {@link EnumSelectData} it will be the enum constant.
   *
   * @return The non-null identifier of this entry
   */
  Object getIdentifier();

  /**
   * Retrieves the display name of this entry. If no display name is set, {@link #getIdentifier()}
   * may be used as the display name.
   *
   * @return The display name of this entry or {@code null} if this entry doesn't have a display
   * name
   */
  ChatComponent getDisplayName();

  /**
   * Changes the display name of this entry.
   *
   * @param displayName The new display name for this entry or {@code null} if this entry shouldn't
   *                    have a display name
   */
  void setDisplayName(ChatComponent displayName);

  /**
   * Retrieves the description of this entry.
   *
   * @return The description of this entry or {@code null} if this entry doesn't have a description
   */
  ChatComponent getDescription();

  /**
   * Changes the description of this entry.
   *
   * @param description The new description for this entry or {@code null} if this entry shouldn't
   *                    have a description
   */
  void setDescription(ChatComponent description);

  /**
   * Retrieves whether this entry is currently the selected value of this setting. This is the case
   * if {@link RegisteredSetting#getCurrentValue()} equals the {@link #getIdentifier() identifier}
   * of this entry.
   *
   * @return {@code true} if this entry is currently selected, {@code false} otherwise
   */
  boolean isSelected();

  /**
   * Retrieves the icon of this entry.
   *
   * @return The icon of this entry or {@code null} if this entry doesn't have an icon
   */
  Icon getIcon();

  /**
   * Changes the icon of this entry.
   *
   * @param icon The new icon for this entry or {@code null} if this entry shouldn't have an icon
   */
  void setIcon(Icon icon);

  /**
   * Factory for the {@link SelectionEntry}.
   */
  @AssistedFactory(SelectionEntry.class)
  interface Factory {

    /**
     * Creates a new {@link SelectionEntry} for the given setting.
     *
     * @param setting     The non-null setting to create the data for
     * @param identifier  The non-null identifier of the new entry
     * @param displayName The display name for the new entry or {@code null} if this entry shouldn't
     *                    have a display name
     * @param description The description for the new entry or {@code null} if this entry shouldn't
     *                    have a description
     * @param icon        The icon of this entry or {@code null} if this entry shouldn't have an
     *                    icon
     * @return The new non-null {@link SelectionEntry}
     */
    SelectionEntry create(
        @Assisted("setting") RegisteredSetting setting,
        @Assisted("identifier") Object identifier,
        @Assisted("displayName") @Nullable ChatComponent displayName,
        @Assisted("description") @Nullable ChatComponent description,
        @Assisted("icon") @Nullable Icon icon);

  }

}
