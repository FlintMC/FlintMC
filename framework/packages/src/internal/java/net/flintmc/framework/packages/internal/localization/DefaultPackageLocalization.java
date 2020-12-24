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

package net.flintmc.framework.packages.internal.localization;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.localization.PackageLocalization;

/** Default implementation of the {@link PackageLocalization}. */
@Implement(PackageLocalization.class)
public class DefaultPackageLocalization implements PackageLocalization {

  private final String localizationCode;
  private final byte[] localizationContent;
  private final String localizationContentAsString;

  @AssistedInject
  private DefaultPackageLocalization(
      @Assisted("localizationCode") String localizationCode,
      @Assisted("localizationContent") byte[] localizationContent) {
    this.localizationCode = localizationCode;
    this.localizationContent = localizationContent;
    this.localizationContentAsString = new String(localizationContent);
  }

  /** {@inheritDoc} */
  @Override
  public String getLocalizationCode() {
    return this.localizationCode;
  }

  /** {@inheritDoc} */
  @Override
  public String getLocalizationContentAsString() {
    return this.localizationContentAsString;
  }

  /** {@inheritDoc} */
  @Override
  public byte[] getLocalizationContent() {
    return this.localizationContent;
  }
}
