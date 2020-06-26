package net.labyfy.component.mappings;

import com.google.inject.Key;
import com.google.inject.name.Names;
import net.labyfy.component.inject.primitive.InjectionHolder;

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

  /**
   * @return the unobfuscated field name
   */
  public String getObfuscatedFieldName() {
    return obfuscatedFieldName;
  }

  /**
   * @return the obfuscated field name
   */
  public String getUnObfuscatedFieldName() {
    return unObfuscatedFieldName;
  }

  public static FieldMapping create(
      ClassMapping labyClassMapping, String obfuscatedFieldName, String unObfuscatedFieldName) {
    return new FieldMapping(labyClassMapping, obfuscatedFieldName, unObfuscatedFieldName);
  }

  /**
   * @return the parent {@link ClassMapping}
   */
  public ClassMapping getClassMapping() {
    return this.labyClassMapping;
  }

  /**
   * Asserts that the represented field is static and gets it static value.
   *
   * @param <T> Implicit type to cast to
   * @return The static fields value
   */
  public <T> T getStaticValue() {
    return this.getValue(null);
  }

  /**
   * Asserts that the represented field is static and sets it static value.
   *
   * @param value New value to set
   */
  public void setStaticValue(Object value) {
    this.setValue(null, value);
  }

  /**
   * @return Either {@link FieldMapping#getUnObfuscatedFieldName()} or {@link FieldMapping#getObfuscatedFieldName()} depending on if the current minecraft environment is obfuscated or not.
   */
  public String getName() {
    if (InjectionHolder.getInjectedInstance(Key.get(boolean.class, Names.named("obfuscated")))) {
      return this.obfuscatedFieldName;
    } else {
      return this.unObfuscatedFieldName;
    }
  }

  /**
   * @return The {@link Field} that this {@link FieldMapping} represents.
   */
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

  /**
   * Asserts that the represented field is non static and gets it value.
   *
   * @param instance Object instance to get value from
   * @param <T>      Implicit type to cast result to
   * @return Resolved field value
   */
  public <T> T getValue(Object instance) {
    try {
      return (T) this.getField().get(instance);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Asserts that the represented field is non static and gets it value.
   *
   * @param instance Object instance to set value to
   * @param value    New value to set
   */
  public void setValue(Object instance, Object value) {
    try {
      this.getField().set(instance, value);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }


  /**
   * Returns true if obfuscatedName and unObfuscatedName are equals.
   *
   * @return if this class mapping is "default"
   */
  public boolean isDefault() {
    return this.unObfuscatedFieldName.equals(obfuscatedFieldName);
  }
}
