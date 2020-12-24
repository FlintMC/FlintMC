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

package net.flintmc.mcapi.chat;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.exception.ComponentMappingException;

/**
 * Mapper between the minecraft components and the flint components.
 */
public interface MinecraftComponentMapper {

  /**
   * Creates a new {@link ChatComponent} by using the given minecraft component as the base.
   *
   * @param handle The non-null minecraft component
   * @return The new Flint component or {@code null} if the given component was invalid
   * @throws ComponentMappingException If the given object is no minecraft component
   */
  ChatComponent fromMinecraft(Object handle) throws ComponentMappingException;

  /**
   * Creates a new minecraft component by using the given Flint component as the base.
   *
   * @param component The non-null Flint component
   * @return The new minecraft component or {@code null} if the given component was invalid
   */
  Object toMinecraft(ChatComponent component);
}
