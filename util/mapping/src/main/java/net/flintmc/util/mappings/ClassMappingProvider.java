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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.flintmc.util.mappings.exceptions.MappingParseException;

/**
 * <code>ClassMappingProvider</code> manages class mappings produced by a mapping parser.
 */
@Singleton
public final class ClassMappingProvider {

  private final Map<String, ClassMapping> obfuscatedClassMappings;
  private final Map<String, ClassMapping> deobfuscatedClassMappings = new HashMap<>();

  @Inject
  private ClassMappingProvider(
      final MappingFileProvider mappingFileProvider,
      @Named("launchArguments") final Map launchArguments)
      throws IOException, MappingParseException {
    if (launchArguments.containsKey("--disable-reobfuscation")) {
      this.obfuscatedClassMappings = Collections.emptyMap();
      return;
    }

    Map<MappingType, InputStream> mappings = mappingFileProvider
        .getMappings(launchArguments.get("--game-version").toString());

    if (mappings.isEmpty()) {
      this.obfuscatedClassMappings = Collections.emptyMap();
      return;
    }

    McpMappingParser mcpMappingParser = new McpMappingParser();
    this.obfuscatedClassMappings = mcpMappingParser.parse(mappings);

    for (ClassMapping classMapping : this.obfuscatedClassMappings.values()) {
      this.deobfuscatedClassMappings.put(classMapping.deobfuscatedName, classMapping);
    }
  }

  /**
   * Get a class mapping by obfuscated name.
   *
   * @param name An obfuscated name.
   * @return A class mapping.
   */
  public ClassMapping getByObfuscatedName(final String name) {
    return this.obfuscatedClassMappings.get(name);
  }

  /**
   * Get a class mapping by deobfuscated name.
   *
   * @param name An deobfuscated name.
   * @return A class mapping.
   */
  public ClassMapping getByDeobfuscatedName(final String name) {
    return this.deobfuscatedClassMappings.get(name);
  }

  /**
   * Get a class mapping by obfuscated or deobfuscated name. Obfuscated name being preferred.
   *
   * @param name An obfuscated name.
   * @return A class mapping.
   */
  public ClassMapping get(final String name) {
    if (this.obfuscatedClassMappings.containsKey(name)) {
      return this.obfuscatedClassMappings.get(name);
    }
    if (this.deobfuscatedClassMappings.containsKey(name)) {
      return this.deobfuscatedClassMappings.get(name);
    }
    return new ClassMapping(false, name, name);
  }

  /**
   * Get obfuscated class mappings.
   *
   * @return Obfuscated class mappings.
   */
  public Map<String, ClassMapping> getObfuscatedClassMappings() {
    return Collections.unmodifiableMap(this.obfuscatedClassMappings);
  }

  /**
   * Get deobfuscated class mappings.
   *
   * @return Deobfuscated class mappings.
   */
  public Map<String, ClassMapping> getDeobfuscatedClassMappings() {
    return Collections.unmodifiableMap(this.deobfuscatedClassMappings);
  }
}
