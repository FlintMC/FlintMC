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

package net.flintmc.mcapi.chat.builder;

import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.TextComponent;

/**
 * Builder for {@link TextComponent}s.
 */
public interface TextComponentBuilder extends ComponentBuilder<TextComponentBuilder> {

  /**
   * Sets the text of the current component. Every component can only have one text, so this
   * overrides any calls that have been done before to this method.
   *
   * @param text The new text of the component
   * @return this
   */
  TextComponentBuilder text(String text);

  /**
   * Appends a new text to the text of the current component.
   *
   * @param text The text to be appended
   * @return this
   */
  TextComponentBuilder appendText(String text);

  /**
   * Retrieves the text of the current component or {@code null} if no text has been set.
   *
   * @return The text of the current component or {@code null} if no text has been provided
   * @see #text(String)
   * @see #appendText(String)
   */
  String text();

  /**
   * Factory for the {@link TextComponentBuilder}.
   */
  @AssistedFactory(TextComponentBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link TextComponentBuilder} without any arguments.
     *
     * @return The new non-null {@link TextComponentBuilder}
     */
    TextComponentBuilder newBuilder();

  }
}
