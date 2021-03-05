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

package net.flintmc.mcapi.internal.chat.builder;

import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.builder.TranslationComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;

@Implement(TranslationComponentBuilder.class)
public class DefaultTranslationComponentBuilder
    extends DefaultComponentBuilder<TranslationComponentBuilder, TranslationComponent>
    implements TranslationComponentBuilder {

  @AssistedInject
  private DefaultTranslationComponentBuilder(
      TextComponent.Factory textComponentFactory,
      TranslationComponent.Factory factory) {
    super(textComponentFactory, factory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TranslationComponentBuilder translationKey(String translationKey) {
    super.currentComponent.translationKey(translationKey);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String translationKey() {
    return super.currentComponent.translationKey();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TranslationComponentBuilder arguments(ChatComponent... arguments) {
    super.currentComponent.arguments(arguments);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TranslationComponentBuilder appendArgument(ChatComponent argument) {
    super.currentComponent.appendArgument(argument);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TranslationComponentBuilder appendArgument(String text) {
    super.currentComponent.appendArgument(text);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent[] arguments() {
    return super.currentComponent.arguments();
  }
}
