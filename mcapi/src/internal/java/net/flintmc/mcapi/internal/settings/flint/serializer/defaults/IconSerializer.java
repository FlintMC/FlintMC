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

package net.flintmc.mcapi.internal.settings.flint.serializer.defaults;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializer;

@Singleton
@SettingsSerializer(Icon.class)
public class IconSerializer implements SettingsSerializationHandler<Icon> {
  @Override
  public void append(JsonObject result, RegisteredSetting setting, Icon annotation) {
    if (annotation == null) {
      return;
    }

    JsonObject icon = new JsonObject();
    result.add("icon", icon);

    if (!annotation.item().value().isEmpty()) {
      icon.addProperty("type", annotation.item().value());
      icon.addProperty("amount", annotation.item().amount());
      icon.addProperty("enchanted", annotation.item().enchanted());
      return;
    }

    if (!annotation.resource().isEmpty()) {
      String url = "file:///$resource/" + annotation.resource();
      icon.addProperty("url", url);
      return;
    }

    if (!annotation.url().isEmpty()) {
      icon.addProperty("url", annotation.url());
      return;
    }

    if (!annotation.html().isEmpty()) {
      icon.addProperty("html", annotation.html());
    }
  }
}
