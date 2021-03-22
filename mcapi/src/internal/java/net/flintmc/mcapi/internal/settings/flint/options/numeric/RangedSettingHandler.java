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

package net.flintmc.mcapi.internal.settings.flint.options.numeric;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.numeric.NumericData;
import net.flintmc.mcapi.settings.flint.options.numeric.Range;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplay;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplays;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class RangedSettingHandler {

  private final NumericData.Factory dataFactory;
  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  @Inject
  private RangedSettingHandler(
      NumericData.Factory dataFactory,
      ComponentSerializer.Factory serializerFactory,
      ComponentAnnotationSerializer annotationSerializer) {
    this.dataFactory = dataFactory;
    this.serializerFactory = serializerFactory;
    this.annotationSerializer = annotationSerializer;
  }

  public boolean inRange(NumericData data, Object value) {
    if (!(value instanceof Number)) {
      return false;
    }

    Number number = (Number) value;
    double d = number.doubleValue();
    int decimals = data.getDecimalPlaces();

    if (decimals == 0 && d != (long) d) {
      return false;
    }

    int decimalLength = String.valueOf(d - (long) d).length() - 2; // 2 = '0.'
    if (decimalLength > decimals) {
      return false;
    }

    return d >= data.getMinValue() && d <= data.getMaxValue();
  }

  public JsonObject serialize(Number value, NumericData data, RegisteredSetting setting) {
    JsonObject object = new JsonObject();

    object.addProperty("value", value);

    JsonObject rangeObject = new JsonObject();
    object.add("range", rangeObject);

    if (data.getMinValue() != Double.MIN_VALUE) {
      rangeObject.addProperty("min", data.getMinValue());
    }
    if (data.getMaxValue() != Double.MAX_VALUE) {
      rangeObject.addProperty("max", data.getMaxValue());
    }
    rangeObject.addProperty("decimals", data.getDecimalPlaces());

    NumericDisplays repeatable =
        setting.getReference().findLastAnnotation(NumericDisplays.class);
    if (repeatable != null) {
      JsonObject displays = new JsonObject();
      object.add("displays", displays);

      for (NumericDisplay display : repeatable.value()) {
        displays.add(
            String.valueOf(display.value()),
            this.serializerFactory
                .gson()
                .getGson()
                .toJsonTree(this.annotationSerializer.deserialize(display.display())));
      }
    }

    return object;
  }

  public SettingData createData(Range range, RegisteredSetting setting) {
    Map<Double, ChatComponent> overriddenDisplays = new HashMap<>();

    NumericDisplays displays = setting.getReference().findLastAnnotation(NumericDisplays.class);
    if (displays != null && displays.value().length != 0) {
      for (NumericDisplay display : displays.value()) {
        overriddenDisplays.put(
            display.value(), this.annotationSerializer.deserialize(display.display()));
      }
    }

    return this.dataFactory.create(
        setting, range.min(), range.max(), range.decimals(), overriddenDisplays);
  }

}
