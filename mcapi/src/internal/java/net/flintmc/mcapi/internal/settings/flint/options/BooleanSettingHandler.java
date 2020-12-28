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

package net.flintmc.mcapi.internal.settings.flint.options;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.BooleanSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(BooleanSetting.class)
public class BooleanSettingHandler implements SettingHandler<BooleanSetting> {

  @Override
  public JsonObject serialize(
      BooleanSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.addProperty("value", currentValue != null && (boolean) currentValue);

    return object;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, BooleanSetting annotation) {
    return input instanceof Boolean;
  }
}
