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
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.mapper.RegisterSettingHandler;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.text.string.StringData;
import net.flintmc.mcapi.settings.flint.options.text.string.StringData.Factory;
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
  public JsonObject serialize(
      StringSetting annotation, RegisteredSetting setting, Object currentValue) {
    JsonObject object = new JsonObject();

    object.addProperty("value", currentValue == null ? "" : (String) currentValue);
    if (annotation.value().length != 0) {
      object.add("restrictions", this.gson.toJsonTree(annotation.value()));
    }
    if (annotation.maxLength() != Integer.MAX_VALUE) {
      object.addProperty("maxLength", annotation.maxLength());
    }
    if (!annotation.prefix().isEmpty()) {
      object.addProperty("prefix", annotation.prefix());
    }
    if (!annotation.suffix().isEmpty()) {
      object.addProperty("suffix", annotation.suffix());
    }
    if (!annotation.placeholder().isEmpty()) {
      object.addProperty("placeholder", annotation.placeholder());
    }

    return object;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidInput(
      Object input, ConfigObjectReference reference, StringSetting annotation) {
    if (!(input instanceof String)) {
      return false;
    }
    String value = (String) input;
    if (value.length() > annotation.maxLength()) {
      return false;
    }

    for (StringRestriction restriction : annotation.value()) {
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
