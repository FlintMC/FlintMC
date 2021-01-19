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

package net.flintmc.mcapi.settings.flint.serializer;

import com.google.gson.JsonObject;
import java.lang.annotation.Annotation;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.options.text.StringSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

/**
 * Handler for the serialization of annotations that can be applied on a method that is annotated
 * with any {@link ApplicableSetting} (e.g. {@link StringSetting}).
 *
 * <p>See the documentation of the annotation for the specific contents of the resulting json.
 *
 * @param <A> The type of annotation that can be serialized by this handler
 */
public interface SettingsSerializationHandler<A extends Annotation> {

  /**
   * Serializes the given {@code annotation} into the given json object.
   *
   * <p>This method should not add the following values to the json object because they are already
   * added by the {@link JsonSettingsSerializer} by default: type, name, enabled, category, native,
   * subSettings, subCategory
   *
   * <p>The new contents of the json object depend on the implementation.
   *
   * @param result     The non-null json object to serialize the given annotation into
   * @param setting    The non-null setting where the given annotation has been found
   * @param annotation The annotation that has been found matching the type that is supported by
   *                   this handler, may be {@code null}, if no annotation with the given type was
   *                   found
   */
  void append(JsonObject result, RegisteredSetting setting, A annotation);
}
