package net.labyfy.component.mappings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.mappings.exceptions.MappingParseException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <code>ClassMappingProvider</code> manages class mappings produced by a mapping parser.
 */
@Singleton
public final class ClassMappingProvider {
  private final Map<String, ClassMapping> obfuscatedClassMappings;
  private final Map<String, ClassMapping> deobfuscatedClassMappings = new HashMap<>();

  @Inject
  private ClassMappingProvider(final MappingFileProvider mappingFileProvider,
                               @Named("launchArguments") final Map launchArguments) throws IOException, MappingParseException {
    McpMappingParser mcpMappingParser = new McpMappingParser();
    obfuscatedClassMappings = mcpMappingParser.parse(mappingFileProvider.getMappings(launchArguments.get("--game-version").toString()));

    for (ClassMapping classMapping : obfuscatedClassMappings.values()) {
      deobfuscatedClassMappings.put(classMapping.deobfuscatedName, classMapping);
    }
  }

  /**
   * Get a class mapping by obfuscated name.
   *
   * @param name An obfuscated name.
   * @return A class mapping.
   */
  public ClassMapping getByObfuscatedName(final String name) {
    return obfuscatedClassMappings.get(name);
  }

  /**
   * Get a class mapping by deobfuscated name.
   *
   * @param name An deobfuscated name.
   * @return A class mapping.
   */
  public ClassMapping getByDeobfuscatedName(final String name) {
    return deobfuscatedClassMappings.get(name);
  }

  /**
   * Get a class mapping by obfuscated or deobfuscated name. Obfuscated name being preferred.
   *
   * @param name An obfuscated name.
   * @return A class mapping.
   */
  public ClassMapping get(final String name) {
    if (obfuscatedClassMappings.containsKey(name)) return obfuscatedClassMappings.get(name);
    return deobfuscatedClassMappings.get(name);
  }

  /**
   * Get obfuscated class mappings.
   *
   * @return Obfuscated class mappings.
   */
  public Map<String, ClassMapping> getObfuscatedClassMappings() {
    return Collections.unmodifiableMap(obfuscatedClassMappings);
  }

  /**
   * Get deobfuscated class mappings.
   *
   * @return Deobfuscated class mappings.
   */
  public Map<String, ClassMapping> getDeobfuscatedClassMappings() {
    return Collections.unmodifiableMap(deobfuscatedClassMappings);
  }
}
