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

import com.google.common.collect.Multimap;
import java.util.Collection;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.EntitySelector;

/**
 * A component for the selection of entities.
 */
public interface SelectorComponent extends ChatComponent {

  /**
   * Retrieves the target selector for the type of target entities of this component or {@code null}
   * if no selector has been set.
   *
   * @return The selector or {@code null} if no selector has been provided
   * @see #selector(EntitySelector)
   */
  EntitySelector selector();

  /**
   * Sets the target selector for the type of target entities of this component.
   *
   * @param selector The new non-null selector for this component
   */
  void selector(EntitySelector selector);

  /**
   * Retrieves all options of this selector component.
   *
   * @return A non-null modifiable map containing all options and their values
   * @see #selectorOptions(Multimap)
   */
  Multimap<String, String> selectorOptions();

  /**
   * Sets the options of this component.
   *
   * @param options The new non-null options
   */
  void selectorOptions(Multimap<String, String> options);

  /**
   * Adds a specific option to this component. If the specified value is null, the option will be
   * removed out of this component.
   *
   * @param option The non-null key for the option
   * @param value  The nullable value for the option
   */
  void selectorOption(String option, String value);

  /**
   * Retrieves the options for the given key out of this component.
   *
   * @param option The non-null key for the option
   * @return The non-null values for the given option, empty if no option with the given key is set.
   */
  Collection<String> selectorOption(String option);

  /**
   * Factory for the {@link SelectorComponent}.
   */
  @AssistedFactory(SelectorComponent.class)
  interface Factory extends ChatComponent.Factory<SelectorComponent> {

  }
}
