package net.flintmc.mcapi.internal.chat.serializer;

import net.flintmc.mcapi.chat.builder.TextComponentBuilder;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.exception.InvalidChatColorException;
import net.flintmc.mcapi.chat.format.ChatColor;
import net.flintmc.mcapi.chat.format.ChatFormat;
import net.flintmc.mcapi.internal.chat.builder.DefaultTextComponentBuilder;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import org.apache.logging.log4j.Logger;

public class PlainComponentSerializer implements ComponentSerializer {

  private final Logger logger;
  private final boolean applyFormat;

  public PlainComponentSerializer(Logger logger, boolean applyFormat) {
    this.logger = logger;
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
      for (ChatFormat format : component.formats()) {
        builder.append(ChatColor.PREFIX_CHAR).append(format.getFormatChar());
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
      return new DefaultTextComponentBuilder().text(serialized).build();
    }

    char[] chars = serialized.toCharArray();
    TextComponentBuilder builder = new DefaultTextComponentBuilder();

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
          builder.nextComponent();
          // for the text after a color, no formats before the color will be applied
          // it has the same effect as §r
          builder.color(color);
          continue;
        }

        ChatFormat format = ChatFormat.getByChar(c);
        if (format != null) {
          builder.format(format);
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
      this.logger.trace("Invalid RGB color received while deserializing plain component", exception);
    }

    return true;
  }
}
