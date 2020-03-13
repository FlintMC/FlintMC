package net.labyfy.component.transform.tweaker.mapping;

import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassMappingProvider {

  private final Map<String, ClassMapping> obfuscatedClassMappings = Maps.newConcurrentMap();
  private final Map<String, ClassMapping> unObfuscatedClassMappings = Maps.newConcurrentMap();

  public ClassMappingProvider(String labyModVersionedAssetsRootPath) {
    try {
      Map<String, String> methodTranslations = new HashMap<>();
      Map<String, String> fieldTranslations = new HashMap<>();

      Scanner scanMethods = new Scanner(new File(labyModVersionedAssetsRootPath + "/methods.csv"));
      scanMethods.nextLine();
      while (scanMethods.hasNext()) {
        String[] split = scanMethods.nextLine().split(",");
        methodTranslations.put(split[0], split[1]);
      }
      Scanner scanFields = new Scanner(new File(labyModVersionedAssetsRootPath + "/fields.csv"));
      scanFields.nextLine();
      while (scanFields.hasNext()) {
        String[] split = scanFields.nextLine().split(",");
        fieldTranslations.put(split[0], split[1]);
      }

      Scanner scanner = new Scanner(new File(labyModVersionedAssetsRootPath + "/joined.tsrg"));
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

          String unobfName = methodTranslations.getOrDefault(unique, unique);
          lastClassMapping.addMethod(obfName, obfIdentifier, obfDesc, unobfName, null, null);

        } else {
          String[] properties = line.replaceFirst("\t", "").split(" ");
          lastClassMapping.addField(
              properties[0], fieldTranslations.getOrDefault(properties[1], properties[1]));
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
                            0,
                            methodMapping.getUnObfuscatedMethodDescription().lastIndexOf(")") + 1);
            methodMapping.setUnObfuscatedMethodIdentifier(unobfIdentifier);
          }
        }
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
        ClassMapping descClassMapping = get(obfClassName);
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
      ClassMapping typeClassMapping = get(methodType.substring(1, methodType.length() - 1));

      if (typeClassMapping != null) {
        methodType = "L" + typeClassMapping.getUnObfuscatedName().replace('.', '/') + ";";
      }
    }

    unObfDesc.append(")").append(methodType);
    return unObfDesc.toString();
  }

  public ClassMapping getByObfuscatedName(String key) {
    return this.obfuscatedClassMappings.get(key);
  }

  public ClassMapping getByUnObfuscatedName(String key) {
    return this.unObfuscatedClassMappings.get(key);
  }

  public Map<String, ClassMapping> getObfuscatedClassMappings() {
    return this.obfuscatedClassMappings;
  }

  public Map<String, ClassMapping> getUnObfuscatedClassMappings() {
    return this.unObfuscatedClassMappings;
  }

  public ClassMapping get(String key) {
    ClassMapping mapping = this.getByObfuscatedName(key);
    if (mapping == null) mapping = this.getByUnObfuscatedName(key);
    if (mapping != null) return mapping;
    return ClassMapping.create(this, key, key);
  }
}
