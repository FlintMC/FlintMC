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

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.settings.flint.annotation.ui.Category;
import net.flintmc.mcapi.settings.flint.annotation.ui.DefineCategory;

/**
 * Represents a category of settings in the {@link SettingsProvider}.
 *
 * @see DefineCategory
 * @see Category
 */
public interface RegisteredCategory {

  /**
   * Retrieves the name of this category, unique per {@link SettingsProvider}.
   *
   * @return The non-null registry name of this category
   */
  String getRegistryName();

  /**
   * Retrieves the display name with all formats/colors as it will be displayed.
   *
   * @return The non-null display name of this category
   */
  ChatComponent getDisplayName();

  /**
   * Retrieves the description with all formats/colors as it will be displayed. If no description is
   * set, this will be a {@link TextComponent} with an empty text.
   *
   * @return The non-null description of this category
   */
  ChatComponent getDescription();

  /**
   * Retrieves the URL to the icon of this category. If no icon is set, this will be an empty
   * string.
   *
   * @return The non-null URL to the icon of this category
   */
  String getIconUrl();

  /**
   * Factory for the {@link RegisteredCategory}.
   */
  @AssistedFactory(RegisteredCategory.class)
  interface Factory {

    /**
     * Creates a new {@link RegisteredCategory} which can be registered with {@link
     * SettingsProvider#registerCategory(RegisteredCategory)}.
     *
     * @param registryName The non-null name of the new category, unique per {@link
     *                     SettingsProvider}
     * @param displayName  The non-null display name of the new category as it will be displayed
     * @param description  The non-null description of the new category as it will be displayed, if
     *                     no description is available, this should be a {@link TextComponent} with
     *                     an empty text
     * @param iconUrl      The non-null URL to the icon of the new category, if no icon is set, this
     *                     should be an empty string
     * @return The new non-null {@link RegisteredCategory}
     */
    RegisteredCategory create(
        @Assisted("registryName") String registryName,
        @Assisted("displayName") ChatComponent displayName,
        @Assisted("description") ChatComponent description,
        @Assisted("iconUrl") String iconUrl);
  }
}
