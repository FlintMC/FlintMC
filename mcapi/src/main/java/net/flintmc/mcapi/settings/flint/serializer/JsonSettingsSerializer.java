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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.CategoryGroup;
import net.flintmc.mcapi.settings.flint.annotation.ui.SubCategory;
import net.flintmc.mcapi.settings.flint.mapper.SettingHandler;
import net.flintmc.mcapi.settings.flint.options.text.string.StringSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredCategory;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.registered.SettingsProvider;

/**
 * Json serializer for {@link RegisteredSetting RegisteredSettings} and {@link RegisteredCategory
 * RegisteredCategories}.
 */
public interface JsonSettingsSerializer {

  /**
   * Serializes all {@link RegisteredSetting RegisteredSettings} in the {@link SettingsProvider}
   * into an array of json objects.
   *
   * @return The new non-null json array containing all settings from the {@link SettingsProvider}
   * @see #serializeSetting(RegisteredSetting)
   */
  JsonArray serializeSettings();

  /**
   * Serializes the given setting into a json element, the result can either be a {@link JsonObject}
   * or {@link JsonArray}. It will be an array of multiple objects if the {@link
   * ConfigObjectReference#getSerializedType() stored type of the setting} is a {@link Map}, then
   * the array will contain multiple objects just like they are described below. If it is not an
   * array but an object, it will simply be the object described below.
   *
   * <p>
   *
   * <p>Every object will contain:
   *
   * <ul>
   *   <li>'type' being the type of the setting, which is {@link ApplicableSetting#name()}
   *   <li>'name' being the name of the setting, which is {@link ConfigObjectReference#getKey()} (or
   *       if the setting is a {@link Map} as described above '{@link ConfigObjectReference#getKey()
   *       key}#The key of the entry in the map'
   *   <li>'enabled' being a boolean which says whether the setting is {@link
   *       RegisteredSetting#isEnabled()}
   *   <li>'category' (optional) being the {@link RegisteredSetting#getCategoryName() name of the
   *       category} of the setting
   *   <li>'native' (only if it is enabled) if the setting is a native minecraft setting ({@link
   *       CategoryGroup}) and not one by some FlintMC package
   *   <li>'subSettings' (only if there are any) being the {@link RegisteredSetting#getSubSettings()
   *       sub settings} of the setting
   *   <li>'subCategory' (only if it is provided) being the {@link SubCategory} of the setting
   * </ul>
   *
   * <p>
   *
   * <p>But first, the data from {@link SettingHandler#serialize(Annotation, RegisteredSetting,
   * Object)} will be used, every entry above overrides the entries from the {@link SettingHandler}.
   * The contents from this handler depend on its implementation, see the specific {@link
   * ApplicableSetting} annotation (e.g. {@link StringSetting}) for more information about its
   * contents.
   *
   * <p>Additionally, all data from the {@link SettingsSerializationHandler
   * SettingsSerializationHandlers} that are registered and have an annotation on the given setting
   * will be added to the object. See the specific annotation (e.g. {@link DisplayName}) for more
   * information about the contents of this.
   *
   * @param setting The non-null setting to be serialized into json
   * @return The new non-null json object/array with the data from the setting(s)
   */
  JsonElement serializeSetting(RegisteredSetting setting);

  /**
   * Serializes all categories from {@link SettingsProvider#getCategories()} into a json object. The
   * keys of the values are always the {@link RegisteredCategory#getRegistryName() name of the
   * category}.
   *
   * @return THe new non-null json object with all categories
   * @see #serializeCategory(RegisteredCategory)
   */
  JsonObject serializeCategories();

  /**
   * Serializes the given category into a json object.
   *
   * <p>The object will contain:
   *
   * <ul>
   *   <li>'displayName' being the {@link RegisteredCategory#getDisplayName() displayName} of the
   *       category
   *   <li>'description' being the {@link RegisteredCategory#getDescription() description} of the
   *       category
   * </ul>
   *
   * @param category The non-null category to be serialized
   * @return The new non-null json object with the data from the category
   */
  JsonObject serializeCategory(RegisteredCategory category);

  /**
   * Registers a new handler for the serialization of annotations that can be used on settings (e.g.
   * {@link DisplayName}).
   *
   * @param annotationType The non-null type of annotations that can be handled by the given
   *                       handler
   * @param handler        The non-null handler for the serialization of annotations of the given
   *                       type
   * @param <A>            The type of annotations that can be serialized by the given handler
   */
  <A extends Annotation> void registerHandler(
      Class<A> annotationType, SettingsSerializationHandler<A> handler);

  /**
   * Retrieves a collection of all handlers that are registered in this serializer for the
   * serialization of annotations like {@link DisplayName}. Modification to this collection won't
   * have any effect.
   *
   * @return The new non-null collection of all handlers
   */
  Collection<SettingsSerializationHandler<Annotation>> getHandlers();

  /**
   * Retrieves a collection of all handlers that are registered in this serializer for the
   * serialization of annotations from the given {@code annotationType}.
   *
   * @param annotationType The non-null type of annotations that should be handled by the retrieved
   *                       handlers
   * @param <A>            The type of annotations that should be handled by the retrieved handlers
   * @return The new non-null collection of handlers that is able to serialize the given type of
   * annotations
   */
  <A extends Annotation> Collection<SettingsSerializationHandler<A>> getHandlers(
      Class<A> annotationType);
}
