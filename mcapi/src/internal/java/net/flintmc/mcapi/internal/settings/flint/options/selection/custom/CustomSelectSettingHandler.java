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

package net.flintmc.mcapi.internal.settings.flint.options.selection.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.selection.SelectionEntry;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectData;
import net.flintmc.mcapi.settings.flint.options.selection.custom.CustomSelectSetting;
import net.flintmc.mcapi.settings.flint.options.selection.custom.Selection;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

@Singleton
@RegisterSettingHandler(CustomSelectSetting.class)
public class CustomSelectSettingHandler implements SettingHandler<CustomSelectSetting> {

  private final JsonSettingsSerializer serializer;
  private final ComponentAnnotationSerializer annotationSerializer;
  private final CustomSelectData.Factory dataFactory;
  private final SelectionEntry.Factory entryFactory;

  @Inject
  private CustomSelectSettingHandler(
      JsonSettingsSerializer serializer,
      ComponentAnnotationSerializer annotationSerializer,
      CustomSelectData.Factory dataFactory,
      SelectionEntry.Factory entryFactory) {
    this.serializer = serializer;
    this.annotationSerializer = annotationSerializer;
    this.dataFactory = dataFactory;
    this.entryFactory = entryFactory;
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

  @Override
  public SettingData createData(CustomSelectSetting annotation, RegisteredSetting setting) {
    Collection<SelectionEntry> entries = new ArrayList<>();

    for (Selection selection : annotation.value()) {
      SelectionEntry entry = this.entryFactory.create(
          setting,
          selection.value(),
          selection.display().value().length == 0 ? null
              : this.annotationSerializer.deserialize(selection.display().value()),
          selection.description().value().length == 0 ? null
              : this.annotationSerializer.deserialize(selection.description().value()),
          selection.icon());

      entries.add(entry);
    }

    return this.dataFactory.create(setting, annotation.type(), entries);
  }
}
