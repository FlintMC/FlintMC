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

package net.flintmc.mcapi.settings.flint.options.numeric;

import net.flintmc.framework.config.defval.annotation.DefaultNumber;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplay;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplays;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a number input in a specified range, the stored type has
 * to be a any number like int, short, double, ...
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'value' with the value from the setting
 *   <li>'range' as a json object with:
 *       <ul>
 *         <li>'min' (only if not {@link Double#MIN_VALUE} with the {@link Range#min() minimum
 *             value} from the {@link #value() range}
 *         <li>'max' (only if not {@link Double#MAX_VALUE} with the {@link Range#max() maximum
 *             value} from the {@link #value() range}
 *         <li>'decimals' with the {@link Range#decimals() number of decimal places } from the
 *             {@link #value() range}
 *       </ul>
 *   <li>'displays' (only if at least one {@link NumericDisplay} or {@link
 *       NumericDisplays}) as an object with {@link NumericDisplay#value()} being the key
 *       and {@link NumericDisplay#display()} serialized with the {@link
 *       ComponentAnnotationSerializer}
 * </ul>
 *
 * <p>
 * {@link RegisteredSetting#getData()} will be an instance of {@link NumericData}.
 *
 * @see ApplicableSetting
 * @see NumericDisplay
 * @see DefaultNumber
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(
    types = {byte.class, short.class, int.class, long.class, double.class, float.class},
    name = "number",
    data = NumericData.class)
public @interface NumericSetting {

  /**
   * Retrieves the range in which numbers may be specified.
   *
   * @return The range for the value
   */
  Range value() default @Range(min = Double.MIN_VALUE, max = Double.MAX_VALUE);
}
