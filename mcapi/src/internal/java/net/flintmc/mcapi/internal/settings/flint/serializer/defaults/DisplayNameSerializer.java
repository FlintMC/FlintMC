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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializer;

@Singleton
@SettingsSerializer(DisplayName.class)
public class DisplayNameSerializer implements SettingsSerializationHandler<DisplayName> {

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  public DisplayNameSerializer(
      ComponentSerializer.Factory serializerFactory,
      ComponentAnnotationSerializer annotationSerializer) {
    this.serializerFactory = serializerFactory;
    this.annotationSerializer = annotationSerializer;
  }

  @Override
  public void append(JsonObject result, RegisteredSetting setting, DisplayName annotation) {
    if (annotation == null || annotation.value().length == 0) {
      return;
    }

    ChatComponent component = this.annotationSerializer.deserialize(annotation.value());

    result.add("displayName", this.serializerFactory.gson().getGson().toJsonTree(component));
  }
}
