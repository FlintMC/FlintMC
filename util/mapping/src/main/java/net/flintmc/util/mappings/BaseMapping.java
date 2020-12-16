package net.flintmc.util.mappings;

/** A mapping base. */
class BaseMapping {
  private boolean obfuscated;
  final String obfuscatedName;
  final String deobfuscatedName;
  final String name;

  /**
   * Construct a base mapping.
   *
   * @param obfuscated Whether the current environment is encrypted.
   * @param obfuscatedName An obfuscated name.
   * @param deobfuscatedName A deobfuscated name.
   */
  public BaseMapping(
      final boolean obfuscated, final String obfuscatedName, final String deobfuscatedName) {
    this.obfuscated = obfuscated;
    this.obfuscatedName = obfuscatedName;
    this.deobfuscatedName = deobfuscatedName;
    this.name = obfuscated ? obfuscatedName : deobfuscatedName;
  }

  /**
   * Get the name based on current environment.
   *
   * @return The obfuscated name if the environment is obfuscated. Deobfuscated name otherwise.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the obfuscated name.
   *
   * @return The obfuscated name.
   */
  public String getObfuscatedName() {
    return obfuscatedName;
  }

  /**
   * Get the deobfuscated name.
   *
   * @return The deobfuscated name.
   */
  public String getDeobfuscatedName() {
    return deobfuscatedName;
  }

  public boolean isDefault() {
    return obfuscatedName.equals(deobfuscatedName);
  }

  public boolean isObfuscated() {
    return obfuscated;
  }
}
