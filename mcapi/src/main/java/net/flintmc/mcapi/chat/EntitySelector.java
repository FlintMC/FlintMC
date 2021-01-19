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

package net.flintmc.mcapi.chat;

import java.util.HashMap;
import java.util.Map;
import net.flintmc.mcapi.chat.component.SelectorComponent;

/**
 * All available selectors for the {@link SelectorComponent}.
 */
public enum EntitySelector {

  /**
   * Selector to select all players.
   */
  ALL_PLAYERS('a'),

  /**
   * Selector to select only the nearest player.
   */
  NEAREST_PLAYER('p'),

  /**
   * Selector to select one random player.
   */
  RANDOM_PLAYER('r'),

  /**
   * Selector to select the executor himself.
   */
  SELF('s'),

  /**
   * Selector to select the nearest entity.
   */
  NEAREST_ENTITY('e');

  private static final Map<Character, EntitySelector> BY_SHORTCUT = new HashMap<>();

  static {
    for (EntitySelector selector : values()) {
      BY_SHORTCUT.put(selector.shortcut, selector);
    }
  }

  private final char shortcut;

  EntitySelector(char shortcut) {
    this.shortcut = shortcut;
  }

  /**
   * Retrieves the selector by the given shortcut char.
   *
   * @param shortcut The shortcut for the specific selector
   * @return The selector by the given shortcut or {@code null}, if no selector with the given
   * shortcut exists
   */
  public static EntitySelector getByShortcut(char shortcut) {
    return BY_SHORTCUT.get(shortcut);
  }

  /**
   * Retrieves the shortcut char of this selector which is unique for each selector.
   *
   * @return The shortcut of this selector
   */
  public char getShortcut() {
    return this.shortcut;
  }
}
