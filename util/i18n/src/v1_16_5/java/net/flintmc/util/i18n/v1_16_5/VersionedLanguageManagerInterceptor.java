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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.Collections;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.transform.hook.Hook;
import net.flintmc.util.i18n.LocalizationLoader;
import net.flintmc.util.i18n.v1_16_5.shadow.AccessibleClientLanguageMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ClientLanguageMap;
import net.minecraft.client.resources.Language;
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
      version = "1.16.5")
  public void hookOnResourceManagerReload(@Named("args") Object[] args) {
    IResourceManager resourceManager = (IResourceManager) args[0];

    Language currentLanguage = Minecraft.getInstance().getLanguageManager().getCurrentLanguage();

    AccessibleClientLanguageMap clientLanguageMap =
        (AccessibleClientLanguageMap)
            ClientLanguageMap.func_239497_a_(
                resourceManager, Collections.singletonList(currentLanguage));

    this.localizationLoader.load(
        new VersionedLocalization(clientLanguageMap), currentLanguage.getCode());
  }
}
