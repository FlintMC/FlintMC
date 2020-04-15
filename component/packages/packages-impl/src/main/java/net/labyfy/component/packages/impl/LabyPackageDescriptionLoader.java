package net.labyfy.component.packages.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.DependencyDescription;
import net.labyfy.component.packages.PackageDescription;
import net.labyfy.component.packages.PackageDescriptionLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

@Singleton
@Implement(PackageDescriptionLoader.class)
public class LabyPackageDescriptionLoader implements PackageDescriptionLoader {

  public static final String MANIFEST_NAME = "package.json";

  private final Gson gson;

  @Inject
  private LabyPackageDescriptionLoader() {
    this.gson = new GsonBuilder().create();
  }

  @Override
  public boolean isDescriptionPresent(JarFile file) {
    Preconditions.checkArgument(
        file.getName().toLowerCase().endsWith(".jar"), "File doesn't seem to be a JAR.");

    return Collections.list(file.entries()).stream()
        .anyMatch(entry -> entry.getName().equals(MANIFEST_NAME));
  }

  @Override
  public Optional<PackageDescription> loadDescription(JarFile file) {
    Preconditions.checkArgument(isDescriptionPresent(file));
    ZipEntry manifest = file.getEntry(MANIFEST_NAME);
    try {
      PackageDescription description =
          this.gson.fromJson(
              new InputStreamReader(file.getInputStream(manifest)), LabyPackageDescription.class);
      return Optional.of(description);
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class LabyPackageDescription implements PackageDescription, Serializable {

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

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public String getDisplayName() {
      return this.displayName != null ? this.displayName : this.name;
    }

    @Override
    public String getVersion() {
      return this.version;
    }

    @Override
    public Set<String> getAuthors() {
      return this.authors;
    }

    @Override
    public String getDescription() {
      // TODO: Internationalization.
      return this.description != null
          ? this.description
          : "i18n:labyfy:packages.generic.description";
    }

    @Override
    public Set<? extends DependencyDescription> getDependencies() {
      return this.dependencies;
    }

    @Override
    public Set<String> getAutoloadClasses() {
      return this.autoloadClasses;
    }

    @Override
    public Set<String> getAutoloadExcludedClasses() {
      return this.autoloadExcludedClasses;
    }

    @Override
    public boolean isValid() {
      return this.name != null
          && this.version != null
          && this.authors != null
          && dependencies.stream().allMatch(dependency -> dependency.getName() != null);
    }
  }

  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class LabyDependencyDescription implements DependencyDescription, Serializable {

    private String name;
    private List<String> versions = new ArrayList<>();

    @Override
    public String getName() {
      return this.name;
    }

    @Override
    public List<String> getVersions() {
      return this.versions;
    }

    @Override
    public boolean matches(PackageDescription description) {
      // TODO: implement version check
      return false;
    }
  }
}
