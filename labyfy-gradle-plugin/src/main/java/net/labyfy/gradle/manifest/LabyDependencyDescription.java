package net.labyfy.gradle.manifest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class LabyDependencyDescription implements Serializable {

  private String name;
  private List<String> versions = new ArrayList<>();

  public String getName() {
    return this.name;
  }

  public List<String> getVersions() {
    return this.versions;
  }

  public boolean matches(LabyPackageDescription description) {
    return this.name.equals(description.getName())
        && this.versions.stream().anyMatch(description.getVersion()::equals);
  }
}
