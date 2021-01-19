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
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;

/**
 * Builder for {@link TranslationComponent}s.
 */
public interface TranslationComponentBuilder extends ComponentBuilder<TranslationComponentBuilder> {

  /**
   * Sets the key for translations of the current component. Every component can only have one key,
   * so this overrides any calls that have been done before to this method.
   *
   * @param translationKey The key for the translation
   * @return this
   */
  TranslationComponentBuilder translationKey(String translationKey);

  /**
   * Retrieves the key for translations of the current component or {@code null} if no key has been
   * set.
   *
   * @return The key for translations of the current component or {@code null} if no translation key
   * has been provided
   * @see #translationKey(String)
   */
  String translationKey();

  /**
   * Sets the arguments for the translation of the current component.
   *
   * <p>Minecraft will replace %s the translation with the arguments of the {@link
   * TranslationComponent}.
   *
   * @param arguments The non-null arguments to pass to the client
   * @return this
   */
  TranslationComponentBuilder arguments(ChatComponent... arguments);

  /**
   * Appends a new argument to the arguments of the current component for the translation of the
   * current component.
   *
   * <p>Minecraft will replace %s in the translation with the arguments of the {@link
   * TranslationComponent}.
   *
   * @param argument The non-null argument to pass to the client
   * @return this
   */
  TranslationComponentBuilder appendArgument(ChatComponent argument);

  /**
   * Appends a new {@link TextComponent} to the current component for the translation of the current
   * component.
   *
   * <p>Minecraft will replace %s in the translation with the arguments of the {@link
   * TranslationComponent}.
   *
   * @param text The non-null text to be appended
   * @return this
   * @see #appendArgument(ChatComponent)
   */
  TranslationComponentBuilder appendArgument(String text);

  /**
   * Retrieves all arguments of the current component which Minecraft uses for the translation of
   * the {@link TranslationComponent}.
   *
   * @return A non-null array of all arguments. Modifications to this array will have no effect on
   * the current component
   */
  ChatComponent[] arguments();

  /**
   * Factory for the {@link TranslationComponentBuilder}.
   */
  @AssistedFactory(TranslationComponentBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link TranslationComponentBuilder} without any arguments.
     *
     * @return The new non-null {@link TranslationComponentBuilder}
     */
    TranslationComponentBuilder newBuilder();

  }
}
