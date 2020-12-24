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

package net.flintmc.mcapi.chat.component;

/**
 * A component for displaying normal text in the chat.
 */
public interface TextComponent extends ChatComponent {

  /**
   * Retrieves the raw text of this component which will be displayed by the client or {@code null}
   * if no text has been specified.
   *
   * @return The text or {@code null} if no text has been provided
   * @see #text(String)
   */
  String text();

  /**
   * Sets the text of this component which will be displayed by the client.
   *
   * @param text The new non-null text for this component
   */
  void text(String text);
}
