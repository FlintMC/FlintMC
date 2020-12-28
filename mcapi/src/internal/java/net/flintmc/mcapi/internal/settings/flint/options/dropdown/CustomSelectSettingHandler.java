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

package net.flintmc.mcapi.internal.settings.flint.options.dropdown;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.dropdown.CustomSelectSetting;
import net.flintmc.mcapi.settings.flint.options.dropdown.Selection;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

@Singleton
@RegisterSettingHandler(CustomSelectSetting.class)
public class CustomSelectSettingHandler implements SettingHandler<CustomSelectSetting> {

  private final JsonSettingsSerializer serializer;

  @Inject
  public CustomSelectSettingHandler(JsonSettingsSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public JsonObject serialize(
      CustomSelectSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();
    object.add("possible", this.serialize(setting, annotation.value()));

    object.addProperty("value", currentValue == null ? "" : (String) currentValue);

    object.addProperty("selectType", annotation.type().name());
    return object;
  }

  private JsonArray serialize(RegisteredSetting setting, Selection[] selections) {
    JsonArray array = new JsonArray();

    for (Selection selection : selections) {
      JsonObject object = new JsonObject();
      array.add(object);

      object.addProperty("name", selection.value());

      for (SettingsSerializationHandler<DisplayName> handler :
          this.serializer.getHandlers(DisplayName.class)) {
        handler.append(object, setting, selection.display());
      }
      for (SettingsSerializationHandler<Description> handler :
          this.serializer.getHandlers(Description.class)) {
        handler.append(object, setting, selection.description());
      }
      for (SettingsSerializationHandler<Icon> handler :
              this.serializer.getHandlers(Icon.class)) {
        handler.append(object, setting, selection.icon());
      }
    }

    return array;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, CustomSelectSetting annotation) {
    if (input == null) {
      return true;
    }
    if (!(input instanceof String)) {
      return false;
    }

    for (Selection selection : annotation.value()) {
      if (selection.value().equals(input)) {
        return true;
      }
    }

    return false;
  }
}
