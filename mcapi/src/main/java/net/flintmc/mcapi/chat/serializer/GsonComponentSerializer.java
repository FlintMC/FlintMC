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

package net.flintmc.mcapi.chat.serializer;

import com.google.gson.Gson;
import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.component.event.content.HoverContent;
import net.flintmc.mcapi.chat.component.event.content.HoverContentSerializer;

/**
 * Serializer for components which uses {@link Gson} for serialization and deserialization.
 */
public interface GsonComponentSerializer extends ComponentSerializer {

  /**
   * Retrieves the {@link Gson} instance which is used to serialize/deserialize components in this
   * serializer.
   *
   * @return The non-null gson instance for serialization
   */
  Gson getGson();

  /**
   * Deserializes the given component into a {@link HoverContent} by using the {@link
   * #getHoverContentSerializer(HoverEvent.Action)} with the given action.
   *
   * @param component The non-null component to be deserialized
   * @param action    The non-null action of the content in the given component
   * @return A new {@link HoverContent} or {@code null}, if an invalid component has been provided
   * @throws UnsupportedOperationException If no serializer for the given action was registered
   * @see HoverContentSerializer#deserialize(ChatComponent, ComponentBuilder.Factory, Gson)
   * @see #getHoverContentSerializer(HoverEvent.Action)
   */
  HoverContent deserializeHoverContent(ChatComponent component, HoverEvent.Action action);

  /**
   * Serializes the given content into a {@link ChatComponent} by using the {@link
   * #getHoverContentSerializer(HoverEvent.Action)} with the action out of the given content.
   *
   * @param content The non-null content to be serialized
   * @return A new {@link ChatComponent} or {@code null}, if an invalid content has been provided
   * @throws UnsupportedOperationException If no serializer for the given action was registered
   * @see HoverContentSerializer#serialize(HoverContent, ComponentBuilder.Factory, Gson)
   * @see #getHoverContentSerializer(HoverEvent.Action)
   */
  ChatComponent serializeHoverContent(HoverContent content);

  /**
   * Registers a new {@link HoverContentSerializer} for the given action which will be used to
   * serialize {@link HoverContent}s into {@link ChatComponent}s.
   *
   * @param action     The non-null action to use this serializer for
   * @param serializer The non-null serializer to be registered
   * @throws IllegalArgumentException If a serializer for the given action is already registered
   */
  void registerHoverContentSerializer(HoverEvent.Action action, HoverContentSerializer serializer);

  /**
   * Retrieves the serializer for {@link HoverContent}s with the given action.
   *
   * @param action The non-null action of the serializer
   * @return The non-null serializer for contents of the given action
   * @throws UnsupportedOperationException If no serializer for the given action was registered
   */
  HoverContentSerializer getHoverContentSerializer(HoverEvent.Action action)
      throws UnsupportedOperationException;
}
