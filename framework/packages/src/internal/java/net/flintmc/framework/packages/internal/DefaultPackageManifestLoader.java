package net.flintmc.framework.packages.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.packages.DependencyDescription;
import net.flintmc.framework.packages.PackageManifest;
import net.flintmc.framework.packages.PackageManifestLoader;
import net.flintmc.installer.impl.InstallerModule;
import net.flintmc.installer.impl.repository.models.DependencyDescriptionModel;
import net.flintmc.installer.impl.repository.models.ModelSerializer;
import net.flintmc.installer.impl.repository.models.PackageModel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

@Singleton
@Implement(PackageManifestLoader.class)
public class DefaultPackageManifestLoader implements PackageManifestLoader {
  public static final String MANIFEST_NAME = "manifest.json";

  private final ModelSerializer manifestLoader;

  @Inject
  private DefaultPackageManifestLoader() {
    InjectionHolder.getInstance().addModules(new InstallerModule());
    this.manifestLoader = InjectionHolder.getInjectedInstance(ModelSerializer.class);
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
    return new DefaultPackageManifest(
        this.manifestLoader.fromString(readZipEntry(file, manifest), PackageModel.class));
  }

  private String readZipEntry(JarFile file, ZipEntry entry) throws IOException {
    InputStreamReader reader = new InputStreamReader(file.getInputStream(entry));
    StringBuilder manifest = new StringBuilder();
    char[] data = new char[1024];
    int read = 0;
    while ((read = reader.read(data)) > 0) {
      for (int i = 0; i < read; i++)
        manifest.append(data[i]);
    }
    return manifest.toString();
  }

  /** Default implementation of a {@link PackageManifest}. */
  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class DefaultPackageManifest implements PackageManifest, Serializable {

    private PackageModel model;
    private Set<DefaultDependencyDescription> dependencies = new HashSet<>();

    DefaultPackageManifest(PackageModel model) {
      this.model = model;
      model.getDependencies().forEach(dep -> dependencies.add(new DefaultDependencyDescription(dep)));
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
      return this.model.getName();
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
      return this.getName();
    }

    /** {@inheritDoc} */
    @Override
    public String getVersion() {
      return this.model.getVersion();
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> getAuthors() {
      return this.model.getAuthors();
    }

    @Override
    public Set<String> getRuntimeClassPath() {
      return this.model.getRuntimeClasspath();
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
      return this.model.getDescription() != null
          ? this.model.getDescription()
          : "flint." + this.getName() + ".packages.generic.description";
    }

    /** {@inheritDoc} */
    @Override
    public Set<? extends DependencyDescription> getDependencies() {
      return this.dependencies;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValid() {
      return this.getName() != null
          && this.getVersion() != null
          && this.getAuthors() != null
          && dependencies.stream().allMatch(dependency -> dependency.getName() != null);
    }
  }

  /** Default implementation of a {@link DependencyDescription}. */
  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class DefaultDependencyDescription implements DependencyDescription, Serializable {

    private DependencyDescriptionModel model;

    DefaultDependencyDescription(DependencyDescriptionModel model) {
      this.model = model;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
      return this.model.getName();
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getVersions() {
      return Arrays.asList(this.model.getVersions().split(","));
    }

    @Override
    public String getChannel() {
      return this.model.getChannel();
    }

    /** {@inheritDoc} */
    @Override
    public boolean matches(PackageManifest manifest) {
      return this.getName().equals(manifest.getName())
          && this.getVersions().stream().anyMatch(manifest.getVersion()::equals);
    }
  }
}
