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

package net.flintmc.framework.packages.localization;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/** Represents the localization of a package. */
public interface PackageLocalization {

  /**
   * Retrieves the localization code.
   *
   * @return The code of the localization.
   */
  String getLocalizationCode();

  /**
   * Retrieves the {@link #getLocalizationContent()} as a {@link String}.
   *
   * @return The localization content as a string.
   */
  String getLocalizationContentAsString();

  /**
   * Retrieves the localization content as a byte array.
   *
   * @return The byte array of the localization content.
   */
  byte[] getLocalizationContent();

  /** A factory class for creating {@link PackageLocalization}'s. */
  @AssistedFactory(PackageLocalization.class)
  interface Factory {

    /**
     * Creates a new {@link PackageLocalization} with the given {@code localizationCode} and {@code
     * localizationContent}.
     *
     * @param localizationCode The code of the localization.
     * @param localizationContent The content of the localization.
     * @return A created package localization.
     */
    PackageLocalization create(
        @Assisted("localizationCode") String localizationCode,
        @Assisted("localizationContent") byte[] localizationContent);
  }
}
