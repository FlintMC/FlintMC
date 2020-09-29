package net.labyfy.component.mappings;

import javassist.CtClass;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.mappings.utils.MappingUtils;

import java.util.HashMap;
import java.util.Map;

public final class ClassMapping extends BaseMapping {
  final Map<String, FieldMapping> obfuscatedFields = new HashMap<>();
  final Map<String, FieldMapping> deobfuscatedFields = new HashMap<>();
  final Map<String, MethodMapping> obfuscatedMethods = new HashMap<>();
  final Map<String, MethodMapping> deobfuscatedMethods = new HashMap<>();

  /**
   * Construct a class mapping.
   *
   * @param obfuscated       Whether the current environment is encrypted.
   * @param obfuscatedName   An obfuscated name.
   * @param deobfuscatedName A deobfuscated name.
   */
  public ClassMapping(final boolean obfuscated, final String obfuscatedName, final String deobfuscatedName) {
    super(obfuscated, obfuscatedName, deobfuscatedName);
  }

  /**
   * Get obfuscated fields.
   *
   * @return Obfuscated fields.
   */
  public Map<String, FieldMapping> getObfuscatedFields() {
    return obfuscatedFields;
  }

  /**
   * Get deobfuscated fields.
   *
   * @return Deobfuscated fields.
   */
  public Map<String, FieldMapping> getDeobfuscatedFields() {
    return deobfuscatedFields;
  }

  /**
   * Get obfuscated methods.
   *
   * @return Obfuscated methods.
   */
  public Map<String, MethodMapping> getObfuscatedMethods() {
    return obfuscatedMethods;
  }

  /**
   * Get deobfuscated methods.
   *
   * @return Deobfuscated methods.
   */
  public Map<String, MethodMapping> getDeobfuscatedMethods() {
    return deobfuscatedMethods;
  }

  /**
   * Get a field mapping by name,
   *
   * @param name A field name.
   * @return A field mapping or null.
   */
  public FieldMapping getField(final String name) {
    if (obfuscatedFields.containsKey(name)) {
      return obfuscatedFields.get(name);
    } else if (deobfuscatedFields.containsKey(name)) {
      return deobfuscatedFields.get(name);
    }
    return null;
  }

  /**
   * Get a method by name without parameters.
   *
   * @param name A method name.
   * @return A method mapping or null.
   */
  public MethodMapping getMethod(final String name) {
    return getMethod(name, new Class[0]);
  }

  /**
   * Get a method by name and parameters.
   *
   * @param name       A method name.
   * @param parameters Method parameters.
   * @return A method mapping or null.
   */
  public MethodMapping getMethod(final String name, final Class<?>... parameters) {
    String identifier = name + '(' + MappingUtils.generateDescriptor(parameters) + ")";
    return this.getMethodByIdentifier(identifier);
  }

  /**
   * Get a method by name and parameters.
   *
   * @param name       A method name.
   * @param parameters Method parameters.
   * @return A method mapping or null.
   */
  public MethodMapping getMethod(final String name, final CtClass... parameters) {
    String identifier = name + '(' + MappingUtils.generateDescriptor(parameters) + ")";
    return this.getMethodByIdentifier(identifier);
  }

  /**
   * Get a metrhod by the explicit identifier
   *
   * @param identifier the identifier of the method. Must have the format {methodName}({parameter types})
   * @return the target method or null
   */
  public MethodMapping getMethodByIdentifier(String identifier) {
    if (obfuscatedMethods.containsKey(identifier)) {
      return obfuscatedMethods.get(identifier);
    } else if (deobfuscatedMethods.containsKey(identifier)) {
      return deobfuscatedMethods.get(identifier);
    }
    return null;
  }

  /**
   * Resolve this class.
   *
   * @return A class.
   * @throws ClassNotFoundException If the class could not be found.
   */
  public Class<?> get() throws ClassNotFoundException {
    return get(LaunchController.getInstance().getRootLoader());
  }

  /**
   * Resolve this class.
   *
   * @param classLoader A class loader.
   * @return A class loaded by the class loader.
   * @throws ClassNotFoundException If the class could not be loaded.
   */
  public Class<?> get(ClassLoader classLoader) throws ClassNotFoundException {
    return Class.forName(this.getName(), false, classLoader);
  }
}
