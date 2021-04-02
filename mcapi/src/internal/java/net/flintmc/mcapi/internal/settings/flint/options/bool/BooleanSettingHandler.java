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

package net.flintmc.mcapi.internal.settings.flint.options.bool;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.bool.BooleanData;
import net.flintmc.mcapi.settings.flint.options.bool.BooleanSetting;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(BooleanSetting.class)
public class BooleanSettingHandler implements SettingHandler<BooleanSetting> {

  private final BooleanData.Factory dataFactory;
  private final ComponentAnnotationSerializer annotationSerializer;
  private final GsonComponentSerializer componentSerializer;

  @Inject
  private BooleanSettingHandler(
      BooleanData.Factory dataFactory,
      ComponentAnnotationSerializer annotationSerializer,
      ComponentSerializer.Factory serializerFactory) {
    this.dataFactory = dataFactory;
    this.annotationSerializer = annotationSerializer;
    this.componentSerializer = serializerFactory.gson();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonObject serialize(RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    BooleanData data = setting.getData();

    object.addProperty("value", currentValue != null && (boolean) currentValue);
    object.add("enabledText", this.componentSerializer.getGson().toJsonTree(data.getEnabledText()));
    object.add(
        "disabledText", this.componentSerializer.getGson().toJsonTree(data.getDisabledText()));

    return object;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidInput(Object input, RegisteredSetting setting) {
    return input instanceof Boolean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SettingData createData(BooleanSetting annotation, RegisteredSetting setting) {
    return this.dataFactory.create(
        setting,
        annotation.enabled().length == 0 ? null
            : this.annotationSerializer.deserialize(annotation.enabled()),
        annotation.disabled().length == 0 ? null
            : this.annotationSerializer.deserialize(annotation.enabled()));
  }
}
