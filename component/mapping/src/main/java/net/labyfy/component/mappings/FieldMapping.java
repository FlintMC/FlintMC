package net.labyfy.component.mappings;

import java.lang.reflect.Field;

public class FieldMapping {

  private final ClassMapping labyClassMapping;
  private final String obfuscatedFieldName;
  private final String unObfuscatedFieldName;
  private Field cached;

  private FieldMapping(
      ClassMapping labyClassMapping, String obfuscatedFieldName, String unObfuscatedFieldName) {
    this.labyClassMapping = labyClassMapping;
    this.obfuscatedFieldName = obfuscatedFieldName;
    this.unObfuscatedFieldName = unObfuscatedFieldName;
  }

  public String getObfuscatedFieldName() {
    return obfuscatedFieldName;
  }

  public String getUnObfuscatedFieldName() {
    return unObfuscatedFieldName;
  }

  public static FieldMapping create(
      ClassMapping labyClassMapping, String obfuscatedFieldName, String unObfuscatedFieldName) {
    return new FieldMapping(labyClassMapping, obfuscatedFieldName, unObfuscatedFieldName);
  }

  public ClassMapping getClassMapping() {
    return this.labyClassMapping;
  }
}
