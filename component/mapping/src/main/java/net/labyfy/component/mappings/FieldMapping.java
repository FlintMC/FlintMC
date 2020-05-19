package net.labyfy.component.mappings;

import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.inject.InjectionHolder;

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

  public <T> T getStaticValue() {
    return this.getValue(null);
  }

  public String getName() {
    if (InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")))) {
      return this.obfuscatedFieldName;
    } else {
      return this.unObfuscatedFieldName;
    }
  }

  public Field getField() {
    if (this.cached == null) {
      try {
        Field declaredField = this.labyClassMapping.get().getDeclaredField(this.getName());
        declaredField.setAccessible(true);
        this.cached = declaredField;
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return this.cached;
  }

  public <T> T getValue(Object instance) {
    try {
      return (T) this.getField().get(instance);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void setStaticValue(Object value) {
    this.setValue(null, value);
  }

  public void setValue(Object instance, Object value) {
    try {
      this.getField().set(instance, value);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public boolean isDefault() {
    return this.unObfuscatedFieldName.equals(obfuscatedFieldName);
  }
}
