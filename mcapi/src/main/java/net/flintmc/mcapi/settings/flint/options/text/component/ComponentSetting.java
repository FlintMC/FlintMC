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

package net.flintmc.mcapi.settings.flint.options.text.component;

import net.flintmc.mcapi.chat.annotation.DefaultComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;
import net.flintmc.mcapi.chat.serializer.GsonComponentSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a string input with all pre defined {@link ChatColor
 * colors} and {@link ChatFormat formats}, the stored type has to be a {@link TextComponent}.
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'value' with the value from the setting, serialized with the {@link
 *       GsonComponentSerializer}
 * </ul>
 * <p>
 * {@link RegisteredSetting#getData()} will be {@code null}.
 *
 * @see ApplicableSetting
 * @see DefaultComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = TextComponent.class, name = "component")
public @interface ComponentSetting {

}
