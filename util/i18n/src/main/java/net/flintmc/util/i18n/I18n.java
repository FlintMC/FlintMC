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

package net.flintmc.util.i18n;

import java.util.Collection;

/**
 * Represents Minecraft's I18n.
 */
public interface I18n {

  /**
   * Translates the {@code translationKey} to a `human readable` message.
   *
   * @param translationKey The translation key.
   * @param parameters     The parameters for the translations.
   * @return A translated human readable message.
   */
  String translate(String translationKey, Object... parameters);

  /**
   * Whether the translation is exists in the selected language.
   *
   * @param key The key to be checked.
   * @return {@code true} if the given is exists the selected language, otherwise {@code false}.
   */
  boolean hasTranslation(String key);

  /**
   * Retrieves a collection of all available languages in the format "Name (Region)"..
   *
   * @return A non-null collection of all available languages, modification to it won't have any
   * effect
   */
  Collection<String> getAvailableLanguages();

}
