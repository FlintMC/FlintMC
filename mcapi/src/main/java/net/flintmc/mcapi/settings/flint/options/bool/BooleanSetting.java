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

package net.flintmc.mcapi.settings.flint.options.bool;

import net.flintmc.framework.config.defval.annotation.DefaultBoolean;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a boolean input, the stored type has to be a boolean.
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'value' with the value from the setting as a boolean
 * </ul>
 *
 * <p>
 * {@link RegisteredSetting#getData()} will be an instance of {@link BooleanData}.
 *
 * @see ApplicableSetting
 * @see DefaultBoolean
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = boolean.class, name = "boolean", data = BooleanData.class)
public @interface BooleanSetting {

  /**
   * Retrieves the text that should be displayed if the setting is set to {@code true}.
   *
   * @return The text to be displayed if the setting is set to {@code true} or an empty array if the
   * default text should be displayed
   */
  Component[] enabled() default {};

  /**
   * Retrieves the text that should be displayed if the setting is set to {@code false}.
   *
   * @return The text to be displayed if the setting is set to {@code false} or an empty array if
   * the default text should be displayed
   */
  Component[] disabled() default {};

}
