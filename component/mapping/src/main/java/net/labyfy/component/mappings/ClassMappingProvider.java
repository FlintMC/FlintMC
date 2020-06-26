package net.labyfy.component.mappings;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Provides all fields, methods and class name obfuscation provided by the chosen {@link MappingParser}.
 * At the moment only the mcp mappings are used. This will probably change in the future.
 */
@Singleton
public class ClassMappingProvider {

  private final Map<String, ClassMapping> obfuscatedClassMappings = Maps.newConcurrentMap();
  private final Map<String, ClassMapping> unObfuscatedClassMappings = Maps.newConcurrentMap();

  @Inject
  private ClassMappingProvider(MappingFileProvider mappingFileProvider, @Named("launchArguments") Map launchArguments) {
    McpMappingParser mcpMappingParser = new McpMappingParser();
    Collection<ClassMapping> parse = mcpMappingParser.parse(this, mappingFileProvider.getMappings(launchArguments.get("--version").toString()));

    for (ClassMapping classMapping : parse) {
      this.obfuscatedClassMappings.put(classMapping.getObfuscatedName(), classMapping);
      this.unObfuscatedClassMappings.put(classMapping.getUnObfuscatedName(), classMapping);
    }
  }

  private ClassMapping add(String obfuscatedName, String unObfuscatedName) {
    ClassMapping classMapping = ClassMapping.create(this, obfuscatedName, unObfuscatedName);
    this.unObfuscatedClassMappings.put(unObfuscatedName, classMapping);
    this.obfuscatedClassMappings.put(obfuscatedName, classMapping);
    return classMapping;
  }

  /**
   * @return all known class mappings, mapped to this format.
   * Key: {@link ClassMapping#getObfuscatedName()} ()}
   * Value: {@link ClassMapping}
   */
  public Map<String, ClassMapping> getObfuscatedClassMappings() {
    return Collections.unmodifiableMap(this.obfuscatedClassMappings);
  }

  /**
   * @return all known class mappings, mapped to this format.
   * Key: {@link ClassMapping#getUnObfuscatedName()}
   * Value: {@link ClassMapping}
   */
  public Map<String, ClassMapping> getUnObfuscatedClassMappings() {
    return Collections.unmodifiableMap(this.unObfuscatedClassMappings);
  }

  /**
   * Find {@link ClassMapping} by the obfuscated class name. ({@link ClassMapping#getObfuscatedName()})
   *
   * @param key obfuscated class name
   * @return classMapping found by obfuscated class name. If no mapping was found, a default {@link ClassMapping} with same obfuscated and unobfuscated name will be created. ({@link ClassMapping#isDefault()} will be true)
   */
  public ClassMapping getByObfuscatedName(String key) {
    return this.obfuscatedClassMappings.get(key);
  }

  /**
   * Find {@link ClassMapping} by the unobfuscated class name. ({@link ClassMapping#getUnObfuscatedName()} ()})
   *
   * @param key unobfuscated class name
   * @return classMapping found by unobfuscated class name. If no mapping was found, a default {@link ClassMapping} with same obfuscated and unobfuscated name will be created. ({@link ClassMapping#isDefault()} will be true)
   */
  public ClassMapping getByUnObfuscatedName(String key) {
    return this.unObfuscatedClassMappings.get(key);
  }

  /**
   * Find {@link ClassMapping} by the obfuscated or the unobfuscated class name. ({@link ClassMapping#getObfuscatedName()} || {@link ClassMapping#getUnObfuscatedName()})
   *
   * @param key obfuscated or unobfuscated class name
   * @return classMapping found by obfuscated or unobfuscated class name. If no mapping was found, a default {@link ClassMapping} with same obfuscated and unobfuscated name will be created. ({@link ClassMapping#isDefault()} will be true)
   */
  public ClassMapping get(String key) {
    ClassMapping mapping = this.getByObfuscatedName(key);
    if (mapping == null) mapping = this.getByUnObfuscatedName(key);
    if (mapping != null) return mapping;
    return ClassMapping.create(this, key, key);
  }
}
