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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Arrays;
import java.util.regex.Pattern;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.text.string.StringData;
import net.flintmc.mcapi.settings.flint.options.text.string.StringRestriction;
import net.flintmc.mcapi.settings.flint.options.text.string.StringSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

@Singleton
@RegisterSettingHandler(StringSetting.class)
public class StringSettingHandler implements SettingHandler<StringSetting> {

  private static final Pattern URL_PATTERN =
      Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

  private final Gson gson = new Gson();

  private final StringData.Factory dataFactory;

  @Inject
  private StringSettingHandler(StringData.Factory dataFactory) {
    this.dataFactory = dataFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonObject serialize(RegisteredSetting setting, Object currentValue) {
    StringData data = setting.getData();

    JsonObject object = new JsonObject();

    object.addProperty("value", currentValue == null ? "" : (String) currentValue);
    if (!data.getRestrictions().isEmpty()) {
      object.add("restrictions", this.gson.toJsonTree(data.getRestrictions()));
    }
    if (data.getMaxLength() != Integer.MAX_VALUE) {
      object.addProperty("maxLength", data.getMaxLength());
    }
    if (!data.getPrefix().isEmpty()) {
      object.addProperty("prefix", data.getPrefix());
    }
    if (!data.getSuffix().isEmpty()) {
      object.addProperty("suffix", data.getSuffix());
    }
    if (!data.getPlaceholder().isEmpty()) {
      object.addProperty("placeholder", data.getPlaceholder());
    }

    return object;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidInput(Object input, RegisteredSetting setting) {
    if (!(input instanceof String)) {
      return false;
    }
    StringData data = setting.getData();

    String value = (String) input;
    if (value.length() > data.getMaxLength()) {
      return false;
    }

    for (StringRestriction restriction : data.getRestrictions()) {
      if (restriction == StringRestriction.URL_ONLY && !URL_PATTERN.matcher(value).matches()) {
        return false;
      }
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SettingData createData(StringSetting annotation, RegisteredSetting setting) {
    return this.dataFactory.create(
        setting,
        annotation.prefix(),
        annotation.suffix(),
        annotation.placeholder(),
        annotation.maxLength(),
        Arrays.asList(annotation.value()));
  }
}
