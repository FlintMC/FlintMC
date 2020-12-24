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

package net.flintmc.util.mappings;

import com.google.inject.Key;
import com.google.inject.name.Names;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.util.csv.lexical.Tokenizer;
import net.flintmc.util.csv.parsing.NamedCSVParser;
import net.flintmc.util.mappings.exceptions.MappingParseException;
import net.flintmc.util.mappings.utils.IOUtils;
import net.flintmc.util.mappings.utils.MappingUtils;

public final class McpMappingParser implements MappingParser {

  /**
   * Update method descriptors.
   *
   * @param classMappings Class mappings.
   */
  private static void updateMethodDescriptors(final Map<String, ClassMapping> classMappings) {
    for (ClassMapping mapping : classMappings.values()) {
      for (MethodMapping methodMapping : mapping.obfuscatedMethods.values()) {
        String descriptor =
            MappingUtils.deobfuscateMethodDescriptor(
                classMappings, methodMapping.obfuscatedDescriptor);
        String identifier =
            methodMapping.deobfuscatedName
                + '('
                + descriptor.substring(1, descriptor.lastIndexOf(')'))
                + ")";

        methodMapping.deobfuscatedDescriptor = descriptor;
        methodMapping.deobfuscatedIdentifier = identifier;
        mapping.deobfuscatedMethods.put(identifier, methodMapping);
      }
    }
  }

  @Override
  public Map<String, ClassMapping> parse(final Map<String, InputStream> input)
      throws MappingParseException {
    boolean obfuscated =
        InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")));
    Map<String, String> fieldLookupTable, methodLookupTable;
    String source;

    try {
      Tokenizer tokenizer = new Tokenizer(',');
      NamedCSVParser csvParser = new NamedCSVParser();
      fieldLookupTable =
          csvParser
              .parse(tokenizer.tokenize(IOUtils.readToString(input.get("fields.csv"))))
              .relation("searge", "name");
      methodLookupTable =
          csvParser
              .parse(tokenizer.tokenize(IOUtils.readToString(input.get("methods.csv"))))
              .relation("searge", "name");
      source = IOUtils.readToString(input.get("joined.tsrg"));
    } catch (IOException exception) {
      throw new MappingParseException("Cannot read input", exception);
    }

    Map<String, ClassMapping> classMappings = new HashMap<>();

    String[] lines = source.split("\n");
    ClassMapping classMapping = null;

    for (String line : lines) {
      if (line.charAt(0) == '\t') {
        String[] split = line.substring(1).split(" ");

        if (classMapping == null) {
          throw new MappingParseException("Invalid mapping: field/method start before class");
        }

        if (split.length == 3) {
          String to = methodLookupTable.get(split[2]);

          if (to == null) {
            to = split[2];
          }

          String identifier =
              split[0] + "(" + split[1].substring(1, split[1].lastIndexOf(')')) + ")";
          MethodMapping methodMapping =
              new MethodMapping(obfuscated, classMapping, split[1], identifier, split[0], to);
          classMapping.obfuscatedMethods.put(identifier, methodMapping);
        } else if (split.length == 2) {
          String to = fieldLookupTable.get(split[1]);

          if (to == null) {
            to = split[1];
          }

          FieldMapping fieldMapping = new FieldMapping(obfuscated, classMapping, split[0], to);
          classMapping.obfuscatedFields.put(split[0], fieldMapping);
          classMapping.deobfuscatedFields.put(to, fieldMapping);
        } else {
          throw new MappingParseException("Illegal parameter count: " + split.length + ": " + line);
        }
      } else {
        String[] split = line.split(" ");

        if (classMapping != null) {
          classMappings.put(classMapping.obfuscatedName.replace("/", "."), classMapping);
        }

        classMapping =
            new ClassMapping(obfuscated, split[0].replace("/", "."), split[1].replace("/", "."));
      }
    }

    if (classMapping != null) {
      classMappings.put(classMapping.obfuscatedName.replace("/", "."), classMapping);
    }

    updateMethodDescriptors(classMappings);
    return classMappings;
  }
}
