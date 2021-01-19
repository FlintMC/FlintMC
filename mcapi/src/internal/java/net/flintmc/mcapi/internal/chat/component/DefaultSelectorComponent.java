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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.EntitySelector;
import net.flintmc.mcapi.chat.component.SelectorComponent;

@Implement(SelectorComponent.class)
public class DefaultSelectorComponent extends DefaultChatComponent implements SelectorComponent {

  private final Multimap<String, String> options = HashMultimap.create();
  private EntitySelector selector = EntitySelector.SELF;

  @AssistedInject
  private DefaultSelectorComponent() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySelector selector() {
    return this.selector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void selector(EntitySelector selector) {
    this.selector = selector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Multimap<String, String> selectorOptions() {
    return this.options;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void selectorOptions(Multimap<String, String> options) {
    this.options.clear();
    this.options.putAll(options);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void selectorOption(String option, String value) {
    if (value == null) {
      this.options.removeAll(option);
      return;
    }
    this.options.put(option, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<String> selectorOption(String option) {
    return this.options.get(option);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUnformattedText() {
    EntitySelector selector = this.selector != null ? this.selector : EntitySelector.ALL_PLAYERS;
    StringBuilder builder = new StringBuilder();
    builder.append(selector.getShortcut());

    if (!this.options.isEmpty()) {
      builder.append('[');

      StringBuilder optionBuilder = new StringBuilder();
      this.options.forEach(
          (key, value) -> optionBuilder.append(key).append('=').append(value).append(','));
      builder.append(
          optionBuilder.length() == 0
              ? ""
              : optionBuilder.substring(0, optionBuilder.length() - 1));

      builder.append(']');
    }

    return builder.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected DefaultChatComponent createCopy() {
    DefaultSelectorComponent component = new DefaultSelectorComponent();
    component.selector = this.selector;
    this.options.forEach(component.options::put);
    return component;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isSpecificEmpty() {
    return this.selector == null && this.options.isEmpty();
  }
}
