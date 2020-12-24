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
 * A component for displaying the text of a translation in the language selected by the client.
 */
public interface TranslationComponent extends ChatComponent {

  /**
   * Retrieves the key for translations of this component or {@code null} if no key has been set.
   *
   * @return The key for translations or {@code null} if no translation key has been provided
   * @see #translationKey(String)
   */
  String translationKey();

  /**
   * Sets the key for translations of this component.
   *
   * @param translationKey The non-null key for translations
   */
  void translationKey(String translationKey);

  /**
   * Retrieves the translation value for the {@link #translationKey()} of this component or the key
   * if there is no translation available.
   *
   * @return The non-null result of the translation
   */
  String translate();

  /**
   * Sets the arguments for the translation of this component.
   *
   * <p>Minecraft will replace %s in the translation with the arguments of the {@link
   * TranslationComponent}.
   *
   * @param arguments The non-null arguments to pass to the client
   */
  void arguments(ChatComponent... arguments);

  /**
   * Appends a new argument to the arguments of this component for translation of the current
   * component.
   *
   * <p>Minecraft will replace %s in the translation with the arguments of the {@link
   * TranslationComponent}.
   *
   * @param argument The non-null argument to pass to the client
   */
  void appendArgument(ChatComponent argument);

  /**
   * Retrieves all arguments of this component that are used by Minecraft for the translation of the
   * {@link TranslationComponent}.
   *
   * @return A non-null array of all arguments. Modifications to this array will have no effect on
   * this component, however modifications to the components in this array will have an effect on
   * this component
   */
  ChatComponent[] arguments();
}
