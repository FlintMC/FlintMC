package net.labyfy.gradle.manifest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LabyPackageDescription implements Serializable {

  private String name;
  private String displayName;
  private String version;
  private Set<String> authors;
  private String description;
  private Set<LabyDependencyDescription> dependencies = new HashSet<>();

  @SerializedName("autoload")
  private Set<String> autoloadClasses = new HashSet<>();

  @SerializedName("exclude")
  private Set<String> autoloadExcludedClasses = new HashSet<>();

  public String getName() {
    return this.name;
  }

  public String getDisplayName() {
    return this.displayName != null ? this.displayName : this.name;
  }

  public String getVersion() {
    return this.version;
  }

  public Set<String> getAuthors() {
    return this.authors;
  }

  public String getDescription() {
    // TODO: Internationalization.
    return this.description != null ? this.description : "i18n:labyfy:packages.generic.description";
  }

  public Set<? extends LabyDependencyDescription> getDependencies() {
    return this.dependencies;
  }

  public Set<String> getAutoloadClasses() {
    return this.autoloadClasses;
  }

  public Set<String> getAutoloadExcludedClasses() {
    return this.autoloadExcludedClasses;
  }

  public boolean isValid() {
    return this.name != null
        && this.version != null
        && this.authors != null
        && dependencies.stream().allMatch(dependency -> dependency.getName() != null);
  }
}
