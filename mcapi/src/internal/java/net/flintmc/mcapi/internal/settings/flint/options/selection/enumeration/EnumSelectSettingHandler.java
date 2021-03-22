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

package net.flintmc.mcapi.internal.settings.flint.options.selection.enumeration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import net.flintmc.framework.config.EnumFieldResolver;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.icon.Icon;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.selection.SelectData;
import net.flintmc.mcapi.settings.flint.options.selection.SelectionEntry;
import net.flintmc.mcapi.settings.flint.options.selection.enumeration.EnumSelectData;
import net.flintmc.mcapi.settings.flint.options.selection.enumeration.EnumSelectSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.flint.serializer.SettingsSerializationHandler;

@Singleton
@RegisterSettingHandler(EnumSelectSetting.class)
public class EnumSelectSettingHandler implements SettingHandler<EnumSelectSetting> {

  private final JsonSettingsSerializer serializer;
  private final EnumFieldResolver fieldResolver;
  private final ComponentAnnotationSerializer annotationSerializer;
  private final EnumSelectData.Factory dataFactory;
  private final SelectionEntry.Factory entryFactory;

  @Inject
  private EnumSelectSettingHandler(
      JsonSettingsSerializer serializer,
      EnumFieldResolver fieldResolver,
      ComponentAnnotationSerializer annotationSerializer,
      EnumSelectData.Factory dataFactory,
      SelectionEntry.Factory entryFactory) {
    this.serializer = serializer;
    this.fieldResolver = fieldResolver;
    this.annotationSerializer = annotationSerializer;
    this.dataFactory = dataFactory;
    this.entryFactory = entryFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonObject serialize(RegisteredSetting setting, Object currentValue) {
    SelectData data = setting.getData();

    JsonObject object = new JsonObject();

    Class<? extends Enum<?>> enumType =
        (Class<? extends Enum<?>>) setting.getReference().getSerializedType();
    JsonArray possible = new JsonArray();
    object.add("possible", possible);
    for (Enum<?> constant : enumType.getEnumConstants()) {
      possible.add(this.serialize(setting, constant));
    }

    object.addProperty("value", currentValue == null ? "" : ((Enum<?>) currentValue).name());

    object.addProperty("selectType", data.getType().name());

    return object;
  }

  private JsonElement serialize(RegisteredSetting setting, Enum<?> constant) {
    JsonObject object = new JsonObject();
    object.addProperty("name", constant.name());

    Field field = this.fieldResolver.getEnumField(constant);

    for (Annotation annotation : field.getAnnotations()) {
      for (SettingsSerializationHandler handler :
          this.serializer.getHandlers(annotation.annotationType())) {
        handler.append(object, setting, annotation);
      }
    }

    return object;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidInput(Object input, RegisteredSetting setting) {
    return input == null || setting.getReference().getSerializedType().equals(input.getClass());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SettingData createData(EnumSelectSetting annotation, RegisteredSetting setting) {
    ConfigObjectReference reference = setting.getReference();

    Class<? extends Enum<?>> enumType =
        (Class<? extends Enum<?>>) reference.getSerializedType();

    Collection<SelectionEntry> entries = new ArrayList<>();
    for (Enum<?> constant : enumType.getEnumConstants()) {
      DisplayName name = reference.findLastAnnotation(DisplayName.class, constant);
      Description description = reference.findLastAnnotation(Description.class, constant);
      Icon icon = reference.findLastAnnotation(Icon.class, constant);

      SelectionEntry entry = this.entryFactory.create(
          setting,
          constant,
          name == null ? null : this.annotationSerializer.deserialize(name.value()),
          description == null ? null : this.annotationSerializer.deserialize(description.value()),
          icon);

      entries.add(entry);
    }

    return this.dataFactory.create(setting, annotation.value(), entries);
  }
}
