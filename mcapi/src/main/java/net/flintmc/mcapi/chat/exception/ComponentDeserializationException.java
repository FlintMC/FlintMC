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

package net.flintmc.mcapi.chat.exception;

import net.flintmc.mcapi.chat.serializer.ComponentSerializer;

/**
 * This exception will be thrown by the {@link ComponentSerializer} when an error occurred while
 * deserializing a string into a chat component.
 */
public class ComponentDeserializationException extends RuntimeException {

  public ComponentDeserializationException(String message) {
    super(message);
  }

  public ComponentDeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ComponentDeserializationException(Throwable cause) {
    super(cause);
  }
}
