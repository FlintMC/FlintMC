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

package net.flintmc.mcapi.internal.chat.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;
import net.flintmc.util.i18n.I18n;

@Implement(TranslationComponent.class)
public class DefaultTranslationComponent extends DefaultChatComponent
    implements TranslationComponent {

  private final I18n i18n;
  private final TextComponent.Factory textFactory;

  private final List<ChatComponent> arguments = new ArrayList<>();
  private String translationKey;

  @AssistedInject
  private DefaultTranslationComponent(I18n i18n, TextComponent.Factory textFactory) {
    this.i18n = i18n;
    this.textFactory = textFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String translationKey() {
    return this.translationKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void translationKey(String translationKey) {
    this.translationKey = translationKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String translate() {
    if (this.translationKey == null) {
      return "null";
    }

    String[] arguments = new String[this.arguments.size()];
    for (int i = 0; i < arguments.length; i++) {
      arguments[i] = this.arguments.get(i).getUnformattedText();
    }

    return this.i18n.translate(this.translationKey, (Object[]) arguments);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void arguments(ChatComponent... arguments) {
    this.arguments.clear();
    this.arguments.addAll(Arrays.asList(arguments));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void appendArgument(ChatComponent argument) {
    this.arguments.add(argument);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void appendArgument(String text) {
    TextComponent component = this.textFactory.create();
    component.text(text);
    this.appendArgument(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent[] arguments() {
    return this.arguments.toArray(new ChatComponent[0]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUnformattedText() {
    return this.translate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected DefaultChatComponent createCopy() {
    DefaultTranslationComponent component = new DefaultTranslationComponent(i18n, textFactory);
    component.translationKey = this.translationKey;
    this.arguments.forEach(argument -> component.arguments.add(argument.copy()));
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isSpecificEmpty() {
    return this.translationKey == null && this.arguments.isEmpty();
  }
}
