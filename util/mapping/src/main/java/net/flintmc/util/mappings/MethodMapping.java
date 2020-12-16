package net.flintmc.util.mappings;

public final class MethodMapping extends BaseMapping {

  final ClassMapping classMapping;
  final String obfuscatedDescriptor;
  final String obfuscatedIdentifier;
  /* final */ String deobfuscatedDescriptor;
  /* final */ String deobfuscatedIdentifier;

  /**
   * Construct a method mapping.
   *
   * @param obfuscated           Whether the current environment is encrypted.
   * @param classMapping         The class mapping the method belongs to.
   * @param obfuscatedDescriptor An obfuscated method descriptor.
   * @param obfuscatedIdentifier An obfuscated method identifier.
   * @param obfuscatedName       An obfuscated name.
   * @param deobfuscatedName     A deobfuscated name.
   */
  public MethodMapping(
      final boolean obfuscated,
      final ClassMapping classMapping,
      final String obfuscatedDescriptor,
      final String obfuscatedIdentifier,
      final String obfuscatedName,
      final String deobfuscatedName) {
    super(obfuscated, obfuscatedName, deobfuscatedName);
    this.classMapping = classMapping;
    this.obfuscatedDescriptor = obfuscatedDescriptor;
    this.obfuscatedIdentifier = obfuscatedIdentifier;
  }

  /**
   * Get the class mapping the method belongs to.
   *
   * @return A class mapping.
   */
  public ClassMapping getClassMapping() {
    return classMapping;
  }

  /**
   * Get obfuscated method descriptor.
   *
   * @return An obfuscated method descriptor.
   */
  public String getObfuscatedDescriptor() {
    return obfuscatedDescriptor;
  }

  public String getDescriptor() {
    return this.isObfuscated() ? this.obfuscatedDescriptor : this.obfuscatedDescriptor;
  }

  /**
   * Get deobfuscated method descriptor.
   *
   * @return A deobfuscated method descriptor.
   */
  public String getDeobfuscatedDescriptor() {
    return deobfuscatedDescriptor;
  }

  /**
   * Get obfuscated method identifier.
   *
   * @return An obfuscated method identifier.
   */
  public String getObfuscatedIdentifier() {
    return obfuscatedIdentifier;
  }

  /**
   * Get deobfuscated method identifier.
   *
   * @return A deobfuscated method identifier.
   */
  public String getDeobfuscatedIdentifier() {
    return deobfuscatedIdentifier;
  }
}
