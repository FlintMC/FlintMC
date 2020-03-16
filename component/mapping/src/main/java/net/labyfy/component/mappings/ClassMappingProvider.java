package net.labyfy.component.mappings;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Singleton
public class ClassMappingProvider {

  private final Map<String, ClassMapping> obfuscatedClassMappings = Maps.newConcurrentMap();
  private final Map<String, ClassMapping> unObfuscatedClassMappings = Maps.newConcurrentMap();

  @Inject
  private ClassMappingProvider() {
    McpMappingParser mcpMappingParser = new McpMappingParser();
    try {
      Collection<ClassMapping> parse =
          mcpMappingParser.parse(
              this,
              ImmutableMap.of(
                  "methods.csv",
                  new FileInputStream("./Labyfy/assets/methods.csv"),
                  "fields.csv",
                  new FileInputStream("./Labyfy/assets/fields.csv"),
                  "joined.tsrg",
                  new FileInputStream("./Labyfy/assets/joined.tsrg")));

      for (ClassMapping classMapping : parse) {
        this.obfuscatedClassMappings.put(classMapping.getObfuscatedName(), classMapping);
        this.unObfuscatedClassMappings.put(classMapping.getUnObfuscatedName(), classMapping);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private ClassMapping add(String obfuscatedName, String unObfuscatedName) {
    ClassMapping classMapping = ClassMapping.create(this, obfuscatedName, unObfuscatedName);
    this.unObfuscatedClassMappings.put(unObfuscatedName, classMapping);
    this.obfuscatedClassMappings.put(obfuscatedName, classMapping);
    return classMapping;
  }

  public Map<String, ClassMapping> getObfuscatedClassMappings() {
    return Collections.unmodifiableMap(this.obfuscatedClassMappings);
  }

  public Map<String, ClassMapping> getUnObfuscatedClassMappings() {
    return Collections.unmodifiableMap(this.unObfuscatedClassMappings);
  }

  public ClassMapping getByObfuscatedName(String key) {
    return this.obfuscatedClassMappings.get(key);
  }

  public ClassMapping getByUnObfuscatedName(String key) {
    return this.unObfuscatedClassMappings.get(key);
  }

  public ClassMapping get(String key) {
    ClassMapping mapping = this.getByObfuscatedName(key);
    if (mapping == null) mapping = this.getByUnObfuscatedName(key);
    if (mapping != null) return mapping;
    return ClassMapping.create(this, key, key);
  }
}
