package net.labyfy.component.mappings;

public final class Version {
  private Mapping modCoderPack;

  /**
   * Get the mod coder pack mapping.
   *
   * @return A mod coder pack mapping.
   */
  public Mapping getModCoderPack() {
    return modCoderPack;
  }

  public static class Mapping {
    private String configVersion;
    private String configDownload;
    private String mappingsVersion;
    private String mappingsDownload;

    /**
     * Get config version.
     *
     * @return A config version.
     */
    public String getConfigVersion() {
      return configVersion;
    }

    /**
     * Get config download URL.
     *
     * @return A config download URL.
     */
    public String getConfigDownload() {
      return configDownload;
    }

    /**
     * Get mapping version.
     *
     * @return A mapping version.
     */
    public String getMappingsVersion() {
      return mappingsVersion;
    }

    /**
     * Get mappings download URL.
     *
     * @return A mappings download URL.
     */
    public String getMappingsDownload() {
      return mappingsDownload;
    }
  }
}
