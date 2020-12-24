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

package net.flintmc.mcapi.settings.game.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import net.flintmc.mcapi.resources.pack.ResourcePackProvider;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;
import net.flintmc.mcapi.settings.game.configuration.ResourcePackConfiguration;

/**
 * The {@link ApplicableSetting} to define a setting to show all enabled and disabled resource
 * packs, the stored type has to be a list.
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'enabledPacks' with an array of the names and descriptions of all enabled resource packs
 *       (which is the stored type of the setting)
 *   <li>'disabledPacks' with an array of the names and descriptions of all disabled resource packs
 * </ul>
 * <p>
 * The arrays named above will both contain json objects with 'name' and 'description' being a
 * string with the name and description of the resource pack.
 *
 * <p>Every pack in the value of the setting has to exist in the {@link ResourcePackProvider}.
 *
 * @see ApplicableSetting
 * @see ResourcePackConfiguration#getResourcePacks()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ApplicableSetting(types = Collection.class, name = "resourcepack")
public @interface ResourcePackSetting {

}
