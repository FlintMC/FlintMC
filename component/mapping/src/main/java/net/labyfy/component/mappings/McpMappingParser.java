package net.labyfy.component.mappings;

import com.google.common.collect.Maps;

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
  private ClassMappingProvider classMappingProvider;

  public Collection<ClassMapping> parse(
      ClassMappingProvider classMappingProvider, Map<String, InputStream> input) {
    assert input.containsKey("methods.csv") : "Methods not provided";
    assert input.containsKey("fields.csv") : "Fields not provided";
    assert input.containsKey("joined.tsrg") : "Joined not provided";

    this.classMappingProvider = classMappingProvider;

    Map<String, String> methodIdentifiers = this.parseIdentifiers(input.get("methods.csv"));
    Map<String, String> fieldIdentifiers = this.parseIdentifiers(input.get("fields.csv"));

    Scanner scanner = new Scanner(input.get("joined.tsrg"));
    ClassMapping lastClassMapping = null;
    while (scanner.hasNext()) {

      String line = scanner.nextLine();
      if (!line.startsWith("\t")) {
        line = line.replace('/', '.');
        String[] s = line.split(" ");
        lastClassMapping = this.add(s[0], s[1]);
      } else if (line.matches("(\\t)(.*)( )(.*)( )(.*)")) {
        String[] properties = line.replaceFirst("\t", "").split(" ");
        String unique = properties[2];
        String obfName = properties[0];
        String obfDesc = properties[1];

        String obfIdentifier = obfName + obfDesc.substring(0, obfDesc.lastIndexOf(")") + 1);

        String unobfName = methodIdentifiers.getOrDefault(unique, unique);
        lastClassMapping.addMethod(obfName, obfIdentifier, obfDesc, unobfName, null, null);

      } else {
        String[] properties = line.replaceFirst("\t", "").split(" ");
        lastClassMapping.addField(
            properties[0], fieldIdentifiers.getOrDefault(properties[1], properties[1]));
      }
    }

    for (ClassMapping mapping : this.obfuscatedClassMappings.values()) {
      for (MethodMapping methodMapping : mapping.getMethods()) {
        methodMapping.setUnObfuscatedMethodDescription(
            this.translateMethodDescription(methodMapping.getObfuscatedMethodDescription()));
        Pattern compile = Pattern.compile("\\(.*\\)(.+)");
        Matcher matcher = compile.matcher(methodMapping.getUnObfuscatedMethodDescription());
        if (matcher.matches()) {
          String unobfIdentifier =
              methodMapping.getUnObfuscatedMethodName()
                  + methodMapping
                      .getUnObfuscatedMethodDescription()
                      .substring(
                          0, methodMapping.getUnObfuscatedMethodDescription().lastIndexOf(")") + 1);
          methodMapping.setUnObfuscatedMethodIdentifier(unobfIdentifier);
        }
      }
    }

    return null;
  }

  public String translateMethodDescription(String obfDesc) {
    String tempObfDesc = obfDesc.substring(1, obfDesc.lastIndexOf(')'));
    StringBuilder unObfDesc = new StringBuilder("(");
    int currentIndex = 0;

    while (currentIndex != tempObfDesc.length()) {
      if (tempObfDesc.charAt(currentIndex) != 'L') {
        unObfDesc.append(tempObfDesc.charAt(currentIndex++));
      } else {
        int maxIndex = tempObfDesc.substring(currentIndex + 1).indexOf(';') + currentIndex + 1;
        String obfClassName = tempObfDesc.substring(currentIndex + 1, maxIndex);
        ClassMapping descClassMapping = classMappingProvider.get(obfClassName);
        unObfDesc
            .append("L")
            .append(
                descClassMapping != null
                    ? descClassMapping.getUnObfuscatedName().replace('.', '/')
                    : obfClassName)
            .append(";");
        currentIndex = maxIndex + 1;
      }
    }

    String methodType = obfDesc.substring(obfDesc.lastIndexOf(')') + 1);

    if (methodType.charAt(0) == 'L') {
      ClassMapping typeClassMapping =
          classMappingProvider.get(methodType.substring(1, methodType.length() - 1));

      if (typeClassMapping != null) {
        methodType = "L" + typeClassMapping.getUnObfuscatedName().replace('.', '/') + ";";
      }
    }

    unObfDesc.append(")").append(methodType);
    return unObfDesc.toString();
  }

  private ClassMapping add(String obfuscatedName, String unObfuscatedName) {
    ClassMapping classMapping =
        ClassMapping.create(classMappingProvider, obfuscatedName, unObfuscatedName);
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
