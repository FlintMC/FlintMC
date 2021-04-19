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

package net.flintmc.mcapi.chat.annotation;

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer.Factory;

/**
 * Serializer to map {@link Component} annotations to {@link ChatComponent}s.
 */
public interface ComponentAnnotationSerializer {

  /**
   * Maps the given component into a {@link ChatComponent}.
   *
   * @param component The non-null component annotation to be deserialized
   * @return The new non-null component
   * @see Component#value()
   */
  ChatComponent deserialize(Component component);

  /**
   * Maps the given components into a {@link ChatComponent}. The components will be deserialized
   * like in {@link #deserialize(Component)} and chained together.
   *
   * @param components The non-null component annotation to be deserialized
   * @return The new non-null component
   * @see Component#value()
   */
  ChatComponent deserialize(Component[] components);

  /**
   * Maps the given components into a {@link ChatComponent}. The components will be deserialized
   * like in {@link #deserialize(Component)} and chained together.
   *
   * <p>If {@code components.length} is 0, {@code def} will be returned.
   *
   * @param components The non-null component annotation to be deserialized
   * @param def        The non-null optional component to be used if {@code components.length} is 0
   * @return The new non-null component or {@code def}
   * @see Component#value()
   */
  ChatComponent deserialize(Component[] components, ChatComponent def);

  /**
   * Maps the given components into a {@link ChatComponent}. The components will be deserialized
   * like in {@link #deserialize(Component)} and chained together.
   *
   * <p>If {@code components.length} is 0, {@code def} will be parsed into a {@link ChatComponent}
   * with {@link Factory#legacy()} and returned.
   *
   * @param components The non-null component annotation to be deserialized
   * @param def        The non-null optional string to be used if {@code components.length} is 0
   * @return The new non-null component or {@code def}
   * @see Component#value()
   */
  ChatComponent deserialize(Component[] components, String def);
}
