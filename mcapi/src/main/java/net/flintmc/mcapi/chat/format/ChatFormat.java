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

package net.flintmc.mcapi.chat.format;

import java.util.HashMap;
import java.util.Map;

/**
 * Formats for chat components, after a color is displayed in the legacy chat message, all formats
 * will be reset.
 */
public enum ChatFormat {

  /**
   * Defines that a chat component will be displayed bold.
   */
  BOLD('l'),

  /**
   * Defines that a chat component will be displayed italic.
   */
  ITALIC('o'),

  /**
   * Defines that a chat component will be underlined.
   */
  UNDERLINED('n'),

  /**
   * Defines that a chat component will be crossed out.
   */
  STRIKETHROUGH('m'),

  /**
   * Defines that a chat component will be displayed obfuscated, that means that no matter what the
   * content of the message is, it will always display random characters. Only the length of the
   * value matters.
   */
  OBFUSCATED('k');

  private static final Map<Character, ChatFormat> BY_CHAR = new HashMap<>();

  static {
    for (ChatFormat format : values()) {
      BY_CHAR.put(format.formatChar, format);
    }
  }

  private final String lowerName;
  private final char formatChar;

  ChatFormat(char formatChar) {
    this.lowerName = super.name().toLowerCase();
    this.formatChar = formatChar;
  }

  /**
   * Retrieves a format by the given format char or {@code null}, if no format with that char
   * exists.
   *
   * @return The nullable format by the given char
   */
  public static ChatFormat getByChar(char formatChar) {
    return BY_CHAR.get(formatChar);
  }

  /**
   * Retrieves the name of this format in the lower case for the serialization in Minecraft.
   *
   * @return The non-null name of this format in the lower case
   */
  public String getLowerName() {
    return this.lowerName;
  }

  /**
   * Retrieves the char which is being used by Minecraft in legacy chat lines to define a format.
   *
   * @return The char for formatting in the legacy chat message
   */
  public char getFormatChar() {
    return this.formatChar;
  }
}
