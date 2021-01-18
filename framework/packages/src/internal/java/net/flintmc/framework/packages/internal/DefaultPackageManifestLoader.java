/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.packages.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.packages.DependencyDescription;
import net.flintmc.framework.packages.PackageManifest;
import net.flintmc.framework.packages.PackageManifestLoader;
import net.flintmc.installer.impl.InstallerModule;
import net.flintmc.installer.impl.repository.models.DependencyDescriptionModel;
import net.flintmc.installer.impl.repository.models.ModelSerializer;
import net.flintmc.installer.impl.repository.models.PackageModel;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

@Singleton
@Implement(PackageManifestLoader.class)
public class DefaultPackageManifestLoader implements PackageManifestLoader {

  public static final String MANIFEST_NAME = "manifest.json";

  private final ModelSerializer manifestLoader;
  private Logger logger;

  @Inject
  private DefaultPackageManifestLoader(@InjectLogger Logger logger) {
    this.logger = logger;
    InjectionHolder.getInstance().addModules(new InstallerModule());
    this.manifestLoader = InjectionHolder
        .getInjectedInstance(ModelSerializer.class);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isManifestPresent(JarFile file) {
    // Search the jar file for entry with the name of the manifest file
    return Collections.list(file.entries()).stream()
        .anyMatch(entry -> entry.getName().equals(MANIFEST_NAME));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PackageManifest loadManifest(JarFile file) throws IOException {
    ZipEntry manifest = file.getEntry(MANIFEST_NAME);
    return new DefaultPackageManifest(
        this.manifestLoader
            .fromString(readZipEntry(file, manifest), PackageModel.class));
  }

  @Override
  public PackageManifest loadManifest(URL url) throws IOException {
    return new DefaultPackageManifest(this.manifestLoader
        .fromString(IOUtils.toString(url, StandardCharsets.UTF_8),
            PackageModel.class));
  }

  private String readZipEntry(JarFile file, ZipEntry entry) throws IOException {
    InputStreamReader reader = new InputStreamReader(
        file.getInputStream(entry));
    StringBuilder manifest = new StringBuilder();
    char[] data = new char[1024];
    int read = 0;
    while ((read = reader.read(data)) > 0) {
      for (int i = 0; i < read; i++) {
        manifest.append(data[i]);
      }
    }
    return manifest.toString();
  }

  /**
   * Default implementation of a {@link PackageManifest}.
   */
  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class DefaultPackageManifest implements PackageManifest,
      Serializable {

    private PackageModel model;
    private Set<DefaultDependencyDescription> dependencies = new HashSet<>();

    DefaultPackageManifest(PackageModel model) {
      this.model = model;
      model.getDependencies().forEach(
          dep -> dependencies.add(new DefaultDependencyDescription(dep)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return this.model.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
      return this.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
      return this.model.getVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAuthors() {
      return this.model.getAuthors();
    }

    @Override
    public Set<String> getRuntimeClassPath() {
      return this.model.getRuntimeClasspath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
      return this.model.getDescription() != null
          ? this.model.getDescription()
          : "flint." + this.getName() + ".packages.generic.description";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<? extends DependencyDescription> getDependencies() {
      return this.dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
      return this.getName() != null
          && this.getVersion() != null
          && this.getAuthors() != null
          && dependencies.stream()
          .allMatch(dependency -> dependency.getName() != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDependency(String packageName, String versions,
        String channel) {
      this.dependencies.add(new DefaultDependencyDescription(
          new DependencyDescriptionModel(packageName, versions, channel)));
    }
  }

  /**
   * Default implementation of a {@link DependencyDescription}.
   */
  @SuppressWarnings({"unused", "FieldMayBeFinal"})
  private static class DefaultDependencyDescription implements
      DependencyDescription, Serializable {

    private DependencyDescriptionModel model;

    DefaultDependencyDescription(DependencyDescriptionModel model) {
      this.model = model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return this.model.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getVersions() {
      return Arrays.asList(this.model.getVersions().split(","));
    }

    @Override
    public String getChannel() {
      return this.model.getChannel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(PackageManifest manifest) {
      return this.getName().equals(manifest.getName())
          && this.getVersions().stream()
          .anyMatch(manifest.getVersion()::equals);
    }
  }
}
