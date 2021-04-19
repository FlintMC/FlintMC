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

package net.flintmc.mcapi.settings.flint.options.text.string;

import net.flintmc.framework.config.defval.annotation.DefaultString;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a string input, the stored type has to be a string.
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'value' with the value from the setting, empty if it is {@code null}
 *   <li>'restrictions' (only if {@link #value()} has at least one restriction) with a list of all
 *       restrictions from {@link #value()}
 *   <li>'maxLength' (only if {@link #maxLength()} is not {@link Integer#MAX_VALUE}) with {@link
 *       #maxLength()}
 *   <li>'prefix' (only if {@link #prefix()} is not empty) with {@link #prefix()}
 *   <li>'suffix' (only if {@link #suffix()} is not empty) with {@link #suffix()}
 * </ul>
 * <p>
 * {@link RegisteredSetting#getData()} will be an instance of {@link StringData}.
 *
 * @see ApplicableSetting
 * @see DefaultString
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = String.class, name = "string", data = StringData.class)
public @interface StringSetting {

  /**
   * Retrieves all restrictions for the text input, if empty, there will be no restriction.
   *
   * @return An array of all restrictions for the input
   */
  StringRestriction[] value() default {};

  /**
   * Retrieves the max number of characters in the input.
   *
   * @return The max number of characters in the input, has to be &gt; 0
   */
  int maxLength() default Integer.MAX_VALUE;

  /**
   * Retrieves the prefix that should be displayed in the text input (before the text) and which
   * cannot be modified by the user. This may be useful for something like "https://youtube.com/" to
   * let the user type in their channel name.
   *
   * @return The prefix or an empty string to show no prefix
   */
  String prefix() default "";

  /**
   * Retrieves the suffix that should be displayed in the text input (after the text) and which
   * cannot be modified by the user.
   *
   * @return The suffix or an empty string to show no suffix
   */
  String suffix() default "";

  /**
   * Retrieves the placeholder that should be displayed in the text input if no value is set by the
   * user.
   *
   * @return The placeholder or an empty string to show no placeholder
   */
  String placeholder() default "";
}
