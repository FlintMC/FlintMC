package net.labyfy.component.mappings;

public class Version {
  private Mapping modCoderPack;

  public Mapping getModCoderPack() {
    return modCoderPack;
  }

  public static class Mapping {
    private String configVersion;
    private String configDownload;
    private String mappingsVersion;
    private String mappingsDownload;

    public String getConfigVersion() {
      return configVersion;
    }

    public String getConfigDownload() {
      return configDownload;
    }

    public String getMappingsVersion() {
      return mappingsVersion;
    }

    public String getMappingsDownload() {
      return mappingsDownload;
    }
  }
}
