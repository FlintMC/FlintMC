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
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.component.TranslationComponent;

public class DefaultTranslationComponent extends DefaultChatComponent
    implements TranslationComponent {

  private final List<ChatComponent> arguments = new ArrayList<>();
  private String translationKey;

  @Override
  public String translationKey() {
    return this.translationKey;
  }

  @Override
  public void translationKey(String translationKey) {
    this.translationKey = translationKey;
  }

  @Override
  public String translate() {
    if (this.translationKey == null) {
      return "null";
    }

    return this
        .translationKey; // TODO translate with the given translationKey (return the translationKey
    // if no translation exists)
  }

  @Override
  public void arguments(ChatComponent... arguments) {
    this.arguments.clear();
    this.arguments.addAll(Arrays.asList(arguments));
  }

  @Override
  public void appendArgument(ChatComponent argument) {
    this.arguments.add(argument);
  }

  @Override
  public ChatComponent[] arguments() {
    return this.arguments.toArray(new ChatComponent[0]);
  }

  @Override
  public String getUnformattedText() {
    return this.translate();
  }

  @Override
  protected DefaultChatComponent createCopy() {
    DefaultTranslationComponent component = new DefaultTranslationComponent();
    component.translationKey = this.translationKey;
    this.arguments.forEach(argument -> component.arguments.add(argument.copy()));
    return component;
  }

  @Override
  protected boolean isSpecificEmpty() {
    return this.translationKey == null && this.arguments.isEmpty();
  }
}
