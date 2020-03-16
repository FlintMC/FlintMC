package net.labyfy.component.mappings;

import com.google.common.collect.Maps;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class McpMappingParser implements MappingParser {

  private final Map<String, ClassMapping> obfuscatedClassMappings = Maps.newConcurrentMap();
  private final Map<String, ClassMapping> unObfuscatedClassMappings = Maps.newConcurrentMap();

  public Collection<ClassMapping> parse(Map<String, InputStream> input) {
    assert input.containsKey("methods.csv") : "Methods not provided";
    assert input.containsKey("fields.csv") : "Fields not provided";
    assert input.containsKey("joined.tsrg") : "Joined not provided";

    Map<String, String> methodIdentifiers = this.parseIdentifiers(input.get("methods.csv"));
    Map<String, String> fieldIdentifiers = this.parseIdentifiers(input.get("fields.csv"));

    return null;
  }

  private ClassMapping add(String obfuscatedName, String unObfuscatedName) {
    ClassMapping classMapping = ClassMapping.create(this, obfuscatedName, unObfuscatedName);
    this.unObfuscatedClassMappings.put(unObfuscatedName, classMapping);
    this.obfuscatedClassMappings.put(obfuscatedName, classMapping);
    return classMapping;
  }

  private Map<String, String> parseIdentifiers(InputStream input) {
    Map<String, String> translations = new HashMap<>();
    Scanner scanMethods = new Scanner(input);
    scanMethods.nextLine();
    while (scanMethods.hasNext()) {
      String[] split = scanMethods.nextLine().split(",");
      translations.put(split[0], split[1]);
    }
    return translations;
  }
}
