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

import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.KeybindComponent;
import net.flintmc.mcapi.chat.component.ScoreComponent;
import net.flintmc.mcapi.chat.component.SelectorComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.HoverEvent;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;

/**
 * General builder for {@link ChatComponent}s and all other implementations of the components.
 *
 * @param <B> The instance of this builder
 */
public interface ComponentBuilder<B extends ComponentBuilder<B>> {

  /**
   * If used, combines the last component that has been created with {@link #nextComponent()} and
   * all other components in this builder.
   *
   * <p>If {@link #nextComponent()} has been used, the base component will always be a {@link
   * TextComponent} with an empty text and all other components as an extra of this {@link
   * TextComponent}. Otherwise it will just return the component of the type that has been specified
   * when creating this builder containing everything that has been specified in this builder.
   *
   * @return The new non-null component that has been built in this builder
   */
  ChatComponent build();

  /**
   * Enables the given format for the current component. If the format is already enabled, nothing
   * will happen.
   *
   * @param format The format to be enabled
   * @return this
   */
  B format(ChatFormat format);

  /**
   * Retrieves all formats that are enabled for the current component. By default no format is
   * enabled.
   *
   * @return An unmodifiable non-null array with all enabled chat formats
   * @see #format(ChatFormat)
   */
  ChatFormat[] enabledFormats();

  /**
   * Changes the color of the current component to the given color. Every component can only have
   * one color, so this overrides any calls that have been done before to this method. If the given
   * color is null, {@link ChatColor#WHITE} will be used.
   *
   * @param color The new color
   * @return this
   */
  B color(ChatColor color);

  /**
   * Retrieves the selected color of the current component. Defaults to {@link ChatColor#WHITE}.
   *
   * @return The non-null current color
   * @see #color(ChatColor)
   */
  ChatColor color();

  /**
   * Sets the click event of the current component. Every component can only have one click event,
   * so this overrides any calls that have been done before to this method.
   *
   * @param event The non-null event that will be executed when the player clicks on the component
   * @return this
   */
  B clickEvent(ClickEvent event);

  /**
   * Retrieves the current click event of the current component or {@code null}, if no click event
   * has been set by using {@link #clickEvent(ClickEvent)}.
   *
   * @return The click event or {@code null}, if no click event has been set
   * @see #clickEvent(ClickEvent)
   */
  ClickEvent clickEvent();

  /**
   * Sets the hover event of the current component. Every component can only have one hover event,
   * so this overrides any calls that have been done before to this method.
   *
   * @param event The non-null event that will be displayed when the player hovers over the
   *              component
   * @return this
   */
  B hoverEvent(HoverEvent event);

  /**
   * Retrieves the hover click event of the current component or {@code null}, if no hover event has
   * been set by using {@link #hoverEvent(HoverEvent)}.
   *
   * @return The hover event or {@code null}, if no hover event has been set
   * @see #hoverEvent(HoverEvent)
   */
  HoverEvent hoverEvent();

  /**
   * Adds a new component as an extra to the current component. In the chat, this will be displayed
   * after the current component and after all previously added extras.
   *
   * @param component The new non-null component
   * @return this
   */
  B append(ChatComponent component);

  /**
   * Creates a new component with all previously specified options in this builder. If no option has
   * been specified (and therefore {@link ChatComponent#isEmpty()} is {@code true}), this method
   * does nothing.
   *
   * @return this
   */
  B nextComponent();

  interface Factory {

    /**
     * Creates a new builder for the {@link TextComponent}. Equivalent to {@link
     * TextComponentBuilder.Factory#newBuilder()}.
     *
     * @return The newly created builder
     */
    TextComponentBuilder text();

    /**
     * Creates a new builder for the {@link KeybindComponent}. Equivalent to {@link
     * KeybindComponentBuilder.Factory#newBuilder()}.
     *
     * @return The newly created builder
     */
    KeybindComponentBuilder keybind();

    /**
     * Creates a new builder for the {@link ScoreComponent}. Equivalent to {@link
     * ScoreComponentBuilder.Factory#newBuilder()}.
     *
     * @return The newly created builder
     */
    ScoreComponentBuilder score();

    /**
     * Creates a new builder for the {@link SelectorComponent}. Equivalent to {@link
     * SelectorComponentBuilder.Factory#newBuilder()}.
     *
     * @return The newly created builder
     */
    SelectorComponentBuilder selector();

    /**
     * Creates a new builder for the {@link TranslationComponent}. Equivalent to {@link
     * TranslationComponentBuilder.Factory#newBuilder()}.
     *
     * @return The newly created builder
     */
    TranslationComponentBuilder translation();
  }
}
