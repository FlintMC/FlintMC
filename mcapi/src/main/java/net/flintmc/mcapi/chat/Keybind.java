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
import net.flintmc.mcapi.chat.component.KeybindComponent;

/**
 * All available keybindings for the {@link KeybindComponent}.
 */
public enum Keybind {
  JUMP("key.jump"),
  SNEAK("key.sneak"),
  SPRINT("key.sprint"),

  LEFT("key.left"),
  RIGHT("key.right"),
  BACK("key.back"),
  FORWARD("key.forward"),

  ATTACK("key.attack"),
  PICK_ITEM("key.pickItem"),
  USE("key.use"),

  DROP_ITEM("key.drop"),
  HOTBAR_1("key.hotbar.1"),
  HOTBAR_2("key.hotbar.2"),
  HOTBAR_3("key.hotbar.3"),
  HOTBAR_4("key.hotbar.4"),
  HOTBAR_5("key.hotbar.5"),
  HOTBAR_6("key.hotbar.6"),
  HOTBAR_7("key.hotbar.7"),
  HOTBAR_8("key.hotbar.8"),
  HOTBAR_9("key.hotbar.9"),
  OPEN_INVENTORY("key.inventory"),
  SWAP_HANDS("key.swapHands"),

  LOAD_TOOLBAR("key.loadToolbarActivator"),
  SAVE_TOOLBAR("key.saveToolbarActivator"),

  SHOW_PLAYER_LIST("key.playerlist"),
  OPEN_CHAT("key.chat"),
  OPEN_COMMAND("key.command"),

  ADVANCEMENTS("key.advancements"),
  SPECTATOR_OUTLINES("key.spectatorOutlines"),
  TAKE_SCREENSHOT("key.screenshot"),
  SMOOTH_CAMERA("key.smoothCamera"),
  TOGGLE_FULLSCREEN("key.fullscreen"),
  TOGGLE_PERSPECTIVE("key.togglePerspective");

  private static final Map<String, Keybind> BY_KEY = new HashMap<>();

  static {
    for (Keybind keybind : values()) {
      BY_KEY.put(keybind.key, keybind);
    }
  }

  private final String key;

  Keybind(String key) {
    this.key = key;
  }

  /**
   * Retrieves the keybind by the given minecraft key.
   *
   * @param key The non-null key for the keybind
   * @return The keybind by the given key or {@code null} if no keybind with the given key exists
   */
  public static Keybind getByKey(String key) {
    return BY_KEY.get(key);
  }

  /**
   * Retrieves the minecraft key for this keybind.
   *
   * @return The non-null name of this keybind
   */
  public String getKey() {
    return this.key;
  }
}
