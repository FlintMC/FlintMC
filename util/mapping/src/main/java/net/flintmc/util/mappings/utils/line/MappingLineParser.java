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

package net.flintmc.util.mappings.utils.line;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.utils.line.handler.LineMappingHandler;
import net.flintmc.util.mappings.utils.line.handler.LineMappingHandlerRegistry;

/**
 * Parser for code that translates the deobfuscated names of classes/fields/methods to their
 * obfuscated names from the {@link ClassMappingProvider}.
 */
@Singleton
public class MappingLineParser {

  private static final char PREFIX = '$';
  private static final char VALUE_OPEN = '{';
  private static final char VALUE_CLOSE = '}';

  private final LineMappingHandlerRegistry registry;

  @Inject
  private MappingLineParser(LineMappingHandlerRegistry registry) {
    this.registry = registry;
  }

  /**
   * Translates the classes, fields and methods from the given string to their obfuscated names. To
   * mark a class, field or method as one, variables like <b>$c{CLASS}</b> may be used. By default,
   * there are <b>c</b> (alias for <b>mapClass</b>), <b>f</b> (alias for <b>mapField</b>) and
   * <b>m</b> (alias for <b>mapMethod</b>), but you can also register your own variables via the
   * {@link LineMappingHandlerRegistry}.
   * <p>
   * If the client is not running in an obfuscated environment (e.g. the IDE), then it won't be
   * translated to the obfuscated names but to the normal ones.
   * <p>
   * <br>
   * For example the following line could be used to generate
   * <b>Minecraft.getInstance().runGameLoop(false)</b>, but obfuscated:
   * <p><b>$c{net.minecraft.client.Minecraft}.$m{getInstance}.$m{runGameLoop(Z)}(false)</b>
   *
   * @param src The non-null string that should be mapped
   * @return The new non-null string with the obfuscated (or in a deobfuscated environment the
   * deobfuscated) class/field/method names
   */
  public String translateMappings(String src) {
    StringBuilder builder = new StringBuilder();
    MappingLine line = new MappingLine(src);

    char[] chars = src.toCharArray();

    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];

      if (c == PREFIX) {
        String handlerName = this.parseHandlerName(src, i);
        if (handlerName == null || handlerName.contains(" ")) {
          builder.append(c);
          continue;
        }
        i += handlerName.length() + 2;

        LineMappingHandler handler = this.registry.getHandler(handlerName);

        String handlerValue = this.parseHandlerValue(src, i);
        i += handlerValue.length();

        try {
          String obfuscated = handler.mapToObfuscated(line, handlerValue);
          builder.append(obfuscated);
        } catch (Exception exception) {
          throw new MappingLineParseException(
              "Failed to pass '" + handlerValue + "' to handler " + handler.getClass().getName(),
              exception);
        }
        continue;
      }

      builder.append(c);
    }

    return builder.toString();
  }

  /**
   * Parses the name of a {@link LineMappingHandler} from the given string beginning at the given
   * index.
   *
   * @param src The non-null string to parse the handler name from
   * @param i   The index to begin at in the given string
   * @return The non-null parsed name of the {@link LineMappingHandler}, may not exist in the {@link
   * LineMappingHandlerRegistry}
   * @throws MappingLineParseException If no <b>{</b> exists in the given string after the given
   *                                   index
   */
  private String parseHandlerName(String src, int i) {
    int beginIndex = src.indexOf(VALUE_OPEN, i);
    if (beginIndex == -1) {
      return null;
    }

    return src.substring(i + 1, beginIndex);
  }

  /**
   * Parses the value of a {@link LineMappingHandler} from the given string beginning at the given
   * index.
   *
   * @param src The non-null string to parse the handler value from
   * @param i   The index to begin at in the given string
   * @return The non-null parsed value for the {@link LineMappingHandler}, may not exist in the
   * {@link LineMappingHandlerRegistry}
   * @throws MappingLineParseException If no <b>}</b> exists in the given string after the given
   *                                   index
   */
  private String parseHandlerValue(String src, int i) {
    int endIndex = src.indexOf(VALUE_CLOSE, i);
    if (endIndex == -1) {
      throw new MappingLineParseException(
          "Got " + VALUE_OPEN + " without end ('" + VALUE_CLOSE + "') in '" + src + "'");
    }

    return src.substring(i, endIndex);
  }

}
