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

package net.flintmc.util.i18n.v1_16_5;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageLoader;
import net.flintmc.framework.packages.localization.PackageLocalization;
import net.flintmc.util.i18n.Localization;
import net.flintmc.util.i18n.LocalizationLoader;

/**
 * 1.16.5 implementation of the {@link LocalizationLoader}.
 */
@Singleton
@Implement(LocalizationLoader.class)
public class VersionedLocalizationLoader implements LocalizationLoader {

  private final PackageLoader packageLoader;

  @Inject
  private VersionedLocalizationLoader(PackageLoader packageLoader) {
    this.packageLoader = packageLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void load(Localization localization, String languageCode) {
    if (!this.packageLoader.getLoadedPackages().isEmpty()) {
      for (Package loadedPackage : this.packageLoader.getLoadedPackages()) {
        if (loadedPackage.getFile() == null) {
          continue;
        }

        PackageLocalization packageLocalization =
            loadedPackage.getPackageLocalizationLoader().getLocalizations().get(languageCode);

        if (packageLocalization != null) {
          JsonObject object =
              JsonParser.parseString(packageLocalization.getLocalizationContentAsString())
                  .getAsJsonObject();

          for (Map.Entry<String, JsonElement> elementEntry : object.entrySet()) {
            localization.add(elementEntry.getKey(), elementEntry.getValue().getAsString());
          }
        }
      }
    }
  }
}
