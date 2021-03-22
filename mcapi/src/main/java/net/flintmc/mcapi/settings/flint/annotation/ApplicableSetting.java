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

package net.flintmc.mcapi.settings.flint.annotation;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.options.bool.BooleanSetting;
import net.flintmc.mcapi.settings.flint.options.data.SettingData;
import net.flintmc.mcapi.settings.flint.options.numeric.SliderSetting;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Marks an annotation to be used to define a setting like {@link BooleanSetting}, {@link
 * SliderSetting}, ...
 *
 * <p>A config with settings could look like the following:
 *
 * <pre>
 *     &#064;Config
 *     public interface MySettings {
 *
 *        &#064;DisplayName( &#064;Component("My string value")) // set an optional displayName
 *        &#064;Description( &#064;Component("Description of my string value")) // set an optional description
 *        &#064;StringSetting // define the value of this method to be a string setting
 *        &#064;DefineCategory( // define a new category and apply it to this setting
 *                         // - if the category already exists, nothing will happen
 *                         // - if you know that the category already exists, just use  &#064;Category
 *          name = "my unique category",
 *          displayName = &#064;Component("My category"),
 *          description = &#064;Component("This is a category")
 *        )
 *        &#064;SubCategory(&#064;Component("This is a splitter")) // set an optional sub category
 *        &#064;DefaultString("default value of this setting") // set the default, if not set this will be null
 *                                                              // for primitives it will be 0 for numbers,
 *                                                              // false for booleans and '\0' for characters
 *        String getValue();
 *
 *        &#064;SubSettingsFor("Value") // marks every setting in the SubSettings interface
 *                                 // to be sub settings of the setting on 'getValue'
 *        interface SubSettings {
 *
 *           &#064;BooleanSetting
 *           boolean isSomeSettingEnabled()
 *
 *        }
 *
 *     }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ApplicableSetting {

  /**
   * Retrieves the unique name of this setting to identify it in the serialized version.
   *
   * @return The unique name of this setting
   */
  String name();

  /**
   * Retrieves all applicable types for this setting that can be used as a return type in a {@link
   * Config}.
   *
   * <p>{@link ConfigObjectReference#getSerializedType()} (or if it is a {@link Map}, the value
   * type of it) has to be assignable to at least one of these types.
   *
   * @return The types for this setting
   */
  Class<?>[] types();

  /**
   * Retrieves the class that contains more information about a setting from the annotation of the
   * setting.
   *
   * @return The type of the setting data of this setting or {@link DummySettingData} if there is no
   * information to be stored in the {@link SettingData}
   * @see SettingData
   */
  Class<? extends SettingData> data() default DummySettingData.class;

  /**
   * Dummy data for {@link #data()}.
   */
  interface DummySettingData extends SettingData {

  }
}
