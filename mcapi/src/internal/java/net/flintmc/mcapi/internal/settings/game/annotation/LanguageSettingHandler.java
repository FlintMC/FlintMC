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

package net.flintmc.mcapi.internal.settings.game.annotation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.game.annotation.LanguageSetting;
import net.flintmc.util.i18n.I18n;

@Singleton
@RegisterSettingHandler(LanguageSetting.class)
public class LanguageSettingHandler implements SettingHandler<LanguageSetting> {

  private final I18n i18n;
  private final Gson gson;

  @Inject
  public LanguageSettingHandler(I18n i18n) {
    this.i18n = i18n;
    this.gson = new Gson();
  }

  @Override
  public JsonObject serialize(
      LanguageSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.add("languages", this.gson.toJsonTree(this.i18n.getAvailableLanguages()));
    object.addProperty("selected", (String) currentValue);

    return object;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, LanguageSetting annotation) {
    return input instanceof String && this.i18n.getAvailableLanguages().contains(input);
  }
}
