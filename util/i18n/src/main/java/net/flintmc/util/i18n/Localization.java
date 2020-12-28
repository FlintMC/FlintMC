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

import java.util.Map;

/**
 * Represents the localization.
 */
public interface Localization {

  /**
   * Adds a new translation to the key-value system.
   *
   * @param key         The key for the translation.
   * @param translation The human readable translation.
   */
  void add(String key, String translation);

  /**
   * Retrieves a key-value system with all available translations.
   *
   * @return A key-value system with all available translations.
   */
  Map<String, String> getProperties();
}
