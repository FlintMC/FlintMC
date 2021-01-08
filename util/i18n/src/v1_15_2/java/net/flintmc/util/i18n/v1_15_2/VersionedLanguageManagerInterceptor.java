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

package net.flintmc.util.i18n.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.transform.hook.Hook;
import net.flintmc.util.i18n.Localization;
import net.flintmc.util.i18n.LocalizationLoader;
import net.flintmc.util.i18n.v1_15_2.accessible.AccessibleLanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.resources.IResourceManager;

@Singleton
public class VersionedLanguageManagerInterceptor {

  private final LocalizationLoader localizationLoader;

  @Inject
  private VersionedLanguageManagerInterceptor(LocalizationLoader localizationLoader) {
    this.localizationLoader = localizationLoader;
  }

  @Hook(
      className = "net.minecraft.client.resources.LanguageManager",
      methodName = "onResourceManagerReload",
      parameters = {@Type(reference = IResourceManager.class)},
      version = "1.15.2")
  public void hookOnResourceManagerReload(@Named("instance") Object instance) {
    LanguageManager languageManager = (LanguageManager) instance;
    AccessibleLanguageManager accessibleLanguageManager =
        (AccessibleLanguageManager) languageManager;

    Localization localization = (Localization) accessibleLanguageManager.getLocale();
    this.localizationLoader.load(
        localization, this.validLanguageCode(languageManager.getCurrentLanguage()));
  }

  private String validLanguageCode(Language language) {
    return language == null ? Minecraft.getInstance().gameSettings.language : language.getCode();
  }
}
