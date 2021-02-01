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

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.exception.ComponentDeserializationException;

public interface ComponentSerializer {

  /**
   * Serializes the given component into a string
   *
   * @param component The non-null component to be serialized
   * @return The serialized text or {@code null}, if an invalid component has been provided (e.g. no
   * text in the text component)
   */
  String serialize(ChatComponent component);

  /**
   * Deserializes the given text into a component
   *
   * @param serialized The non-null text to be deserialized
   * @return The deserialized component or {@code null}, if an invalid text has been provided
   * @throws ComponentDeserializationException If an invalid text has been provided
   */
  ChatComponent deserialize(String serialized);

  /**
   * Singleton factory for the {@link ComponentSerializer}.
   */
  interface Factory {

    /**
     * Legacy serializer which serializes the text of the components, all formats and colors.
     *
     * @return The non-null singleton instance of the serializer
     */
    ComponentSerializer legacy();

    /**
     * Legacy serializer which serializes the text of the components without any formats and
     * colors.
     *
     * @return The non-null singleton instance of the serializer
     */
    ComponentSerializer plain();

    /**
     * Json serializer which serializes the components in the JSON format by using Gson.
     *
     * @return The non-null singleton instance of the serializer
     */
    GsonComponentSerializer gson();
  }
}
