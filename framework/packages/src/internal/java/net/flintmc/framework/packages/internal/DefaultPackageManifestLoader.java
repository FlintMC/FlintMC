package net.flintmc.framework.packages.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.DependencyDescription;
import net.flintmc.framework.packages.PackageManifest;
import net.flintmc.framework.packages.PackageManifestLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

@Singleton
@Implement(PackageManifestLoader.class)
public class DefaultPackageManifestLoader implements PackageManifestLoader {
  public static final String MANIFEST_NAME = "package.json";

  private final Gson gson;

  @Inject
  private DefaultPackageManifestLoader() {
    this.gson = new GsonBuilder().create();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isManifestPresent(JarFile file) {
    // Search the jar file for entry with the name of the manifest file
    return Collections.list(file.entries()).stream()
        .anyMatch(entry -> entry.getName().equals(MANIFEST_NAME));
  }

  /** {@inheritDoc} */
  @Override
  public PackageManifest loadManifest(JarFile file) throws IOException {
    ZipEntry manifest = file.getEntry(MANIFEST_NAME);
    return this.gson.fromJson(
        new InputStreamReader(file.getInputStream(manifest)), DefaultPackageManifest.class);
  }

  /** Default implementation of a {@link PackageManifest}. */
  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class DefaultPackageManifest implements PackageManifest, Serializable {
    private String name;
    private String displayName;
    private String version;
    private Set<String> authors;
    private String description;
    private Set<DefaultDependencyDescription> dependencies = new HashSet<>();

    /** {@inheritDoc} */
    @Override
    public String getName() {
      return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
      return this.displayName != null ? this.displayName : this.name;
    }

    /** {@inheritDoc} */
    @Override
    public String getVersion() {
      return this.version;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> getAuthors() {
      return this.authors;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
      return this.description != null
          ? this.description
          : "labyfy." + this.name + ".packages.generic.description";
    }

    /** {@inheritDoc} */
    @Override
    public Set<? extends DependencyDescription> getDependencies() {
      return this.dependencies;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValid() {
      return this.name != null
          && this.version != null
          && this.authors != null
          && dependencies.stream().allMatch(dependency -> dependency.getName() != null);
    }
  }

  /** Default implementation of a {@link DependencyDescription}. */
  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class DefaultDependencyDescription implements DependencyDescription, Serializable {
    private String name;
    private List<String> versions = new ArrayList<>();

    /** {@inheritDoc} */
    @Override
    public String getName() {
      return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getVersions() {
      return this.versions;
    }

    /** {@inheritDoc} */
    @Override
    public boolean matches(PackageManifest manifest) {
      return this.name.equals(manifest.getName())
          && this.versions.stream().anyMatch(manifest.getVersion()::equals);
    }
  }
}
