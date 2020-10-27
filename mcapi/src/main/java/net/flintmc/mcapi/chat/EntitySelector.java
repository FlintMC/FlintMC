package net.flintmc.mcapi.chat;

import net.flintmc.mcapi.chat.component.SelectorComponent;

import java.util.HashMap;
import java.util.Map;

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
   * @return The selector by the given shortcut or {@code null} if no selector with the given shortcut exists
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
