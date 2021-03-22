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

import java.util.Collection;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.EntitySelector;
import net.flintmc.mcapi.chat.builder.SelectorComponentBuilder;
import net.flintmc.mcapi.chat.component.SelectorComponent;
import net.flintmc.mcapi.chat.component.TextComponent;
import net.flintmc.mcapi.chat.exception.InvalidSelectorException;

@Implement(SelectorComponentBuilder.class)
public class DefaultSelectorComponentBuilder
    extends DefaultComponentBuilder<SelectorComponentBuilder, SelectorComponent>
    implements SelectorComponentBuilder {

  @AssistedInject
  private DefaultSelectorComponentBuilder(
      TextComponent.Factory textComponentFactory,
      SelectorComponent.Factory factory) {
    super(textComponentFactory, factory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SelectorComponentBuilder selector(EntitySelector selector) {
    super.currentComponent.selector(selector);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySelector selector() {
    return super.currentComponent.selector();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SelectorComponentBuilder option(String option, String value) {
    super.currentComponent.selectorOption(option, value);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> option(String option) {
    return super.currentComponent.selectorOption(option);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SelectorComponentBuilder parse(String rawSelector) {
    rawSelector = rawSelector.replace(" ", "");

    if (rawSelector.length() == 0) {
      throw new InvalidSelectorException("Selector has to contain at least one character");
    }
    EntitySelector selector = EntitySelector.getByShortcut(rawSelector.charAt(0));
    if (selector == null) {
      throw new InvalidSelectorException("Unknown selector '" + rawSelector.charAt(0) + "'");
    }

    if (rawSelector.length() > 1
        && rawSelector.charAt(1) == '['
        && rawSelector.charAt(rawSelector.length() - 1) == ']') {
      String rawOptions = rawSelector.substring(2, rawSelector.length() - 1);
      String[] splitOptions = rawOptions.split(",");

      for (String option : splitOptions) {
        String[] keyValuePair = option.split("=", 2);
        if (keyValuePair.length == 2) {
          this.option(keyValuePair[0], keyValuePair[1]);
        }
      }
    }

    this.selector(selector);

    return this;
  }
}
