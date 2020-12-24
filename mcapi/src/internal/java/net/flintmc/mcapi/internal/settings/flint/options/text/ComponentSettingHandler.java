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

package net.flintmc.mcapi.internal.settings.flint.options.text;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.text.ComponentSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(ComponentSetting.class)
public class ComponentSettingHandler implements SettingHandler<ComponentSetting> {

  private final ComponentSerializer.Factory serializerFactory;
  private final ChatComponent emptyComponent;

  @Inject
  public ComponentSettingHandler(
      ComponentSerializer.Factory serializerFactory, ComponentBuilder.Factory builderFactory) {
    this.serializerFactory = serializerFactory;
    this.emptyComponent = builderFactory.text().text("").build();
  }

  @Override
  public JsonObject serialize(
      ComponentSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.add(
        "value",
        this.serializerFactory
            .gson()
            .getGson()
            .toJsonTree(currentValue != null ? currentValue : this.emptyComponent));

    return object;
  }

  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, ComponentSetting annotation) {
    return input instanceof TextComponent;
  }
}
