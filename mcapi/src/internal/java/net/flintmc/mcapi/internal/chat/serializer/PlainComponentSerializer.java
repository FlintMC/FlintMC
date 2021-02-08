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

package net.flintmc.mcapi.internal.chat.serializer;

import net.flintmc.mcapi.chat.builder.ComponentBuilder;
import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.exception.InvalidChatColorException;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import org.apache.logging.log4j.Logger;

public class PlainComponentSerializer implements ComponentSerializer {

  private final Logger logger;
  private final ComponentBuilder.Factory componentFactory;
  private final boolean applyFormat;

  public PlainComponentSerializer(
      Logger logger,
      ComponentBuilder.Factory componentFactory,
      boolean applyFormat) {
    this.logger = logger;
    this.componentFactory = componentFactory;
    this.applyFormat = applyFormat;
  }

  @Override
  public String serialize(ChatComponent component) {
    StringBuilder builder = new StringBuilder();
    this.serialize(component, builder);
    return builder.toString();
  }

  private void serialize(ChatComponent component, StringBuilder builder) {
    if (this.applyFormat) {
      builder.append(ChatColor.PREFIX_CHAR).append(component.color().getColorCode());
      for (ChatColor chatColor : component.chatFormats()) {
        builder.append(ChatColor.PREFIX_CHAR).append(chatColor.getColorCode());
      }
    }

    builder.append(component.getUnformattedText());

    for (ChatComponent extra : component.extras()) {
      this.serialize(extra, builder);
    }
  }

  @Override
  public ChatComponent deserialize(String serialized) {
    if (!this.applyFormat) {
      // no need for parsing the colors
      return this.componentFactory.text().text(serialized).build();
    }

    char[] chars = serialized.toCharArray();
    TextComponentBuilder builder = this.componentFactory.text().text("");

    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];

      if (c == ChatColor.PREFIX_CHAR) {
        // begin with a color/format

        if (++i >= chars.length) {
          break;
        }

        c = chars[i];

        if (c == 'r') {
          builder.nextComponent();
          // reset every color/format
          continue;
        }

        if (c == '#') {
          if (this.parseHex(chars, i, builder)) {
            i += 6;
            continue;
          }

          break;
        }

        ChatColor color = ChatColor.getByChar(c);
        if (color != null) {

          if (color.isFormat()) {
            builder.chatFormat(color);
            continue;
          }

          builder.nextComponent();
          // for the text after a color, no formats before the color will be applied
          // it has the same effect as §r
          builder.color(color);
          continue;
        }
      }

      builder.appendText(String.valueOf(c));
    }

    return builder.build();
  }

  private boolean parseHex(char[] chars, int index, TextComponentBuilder builder) {
    if (index + 6 >= chars.length) {
      return false;
    }

    StringBuilder hexBuilder = new StringBuilder(6);
    for (int j = 0; j < 6; j++) {
      hexBuilder.append(chars[index + j]);
    }

    try {
      ChatColor color = ChatColor.forRGB(hexBuilder.toString());
      builder.nextComponent();
      builder.color(color);
    } catch (InvalidChatColorException exception) {
      this.logger.trace(
          "Invalid RGB color received while deserializing plain component", exception);
    }

    return true;
  }
}
