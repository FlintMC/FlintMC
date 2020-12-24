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
import com.google.inject.name.Named;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.internal.DefaultLoggingProvider;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.*;
import net.flintmc.launcher.LaunchController;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Implement(PackageLoader.class)
public class DefaultPackageLoader implements PackageLoader {

  private final Logger logger;
  private final File packageFolder;
  private final Set<JarTuple> jars;
  private DefaultPackageManifestLoader descriptionLoader;
  private final Package.Factory packageFactory;

  private Set<Package> allPackages;

  @Inject
  private DefaultPackageLoader(
      DefaultLoggingProvider loggingProvider,
      @Named("flintPackageFolder") File packageFolder,
      DefaultPackageManifestLoader descriptionLoader,
      Package.Factory packageFactory,
      @InjectLogger Logger logger) {
    this.descriptionLoader = descriptionLoader;

    this.packageFactory = packageFactory;
    this.logger = logger;

    // Tell the logging provider we now are able to resolve logging prefixes
    loggingProvider.setPrefixProvider(this::getLogPrefix);

    this.packageFolder = packageFolder;

    boolean exists = true;
    Path packageFolderPath = packageFolder.toPath();
    if (Files.isDirectory(packageFolderPath)) {
      try {
        Files.createDirectories(packageFolderPath);
      } catch (IOException exception) {
        exists = false;
        logger.error("Failed to create package folder {}", packageFolderPath.toString(), exception);
      }
    }

    if (exists) {
      if (!packageFolder.exists()) {
        packageFolder.mkdirs();
      }
      this.jars =
          Stream.of(Objects.requireNonNull(packageFolder.listFiles()))
              // Filter for JAR files
              .filter(file -> file.getName().toLowerCase().endsWith(".jar"))
              // Map to JarFile references
              .map(
                  file -> {
                    try {
                      return new JarTuple(file, new JarFile(file));
                    } catch (IOException exception) {
                      logger.error("Failed to open {} as a jar", file.getAbsolutePath(), exception);
                      return null;
                    }
                  })
              // filter nulls produced by previous step
              .filter(Objects::nonNull)
              .filter(jarTuple -> jarTuple.getJar() != null)
              // Filter JARs that contain a package manifest
              .filter(
                  jarTuple -> {
                    if (descriptionLoader.isManifestPresent(jarTuple.getJar())) return true;
                    else {
                      this.logger.warn(
                          "{} is in the package directory, but doesn't not contain a package manifest. Ignoring it.",
                          jarTuple.getFile().getName());
                      return false;
                    }
                  })
              // Collect to Set
              .collect(Collectors.toSet());
    } else {
      jars = new HashSet<>();
      this.logger.error(
          "Cant load packages as package directory does not exist and is not creatable.");
    }
  }

  public void load() {
    if (this.jars.isEmpty()) {
      // We have no files to load, skip it
      this.logger.info("No loadable Packages found in {}...", packageFolder.getAbsolutePath());
      this.allPackages = Collections.emptySet();
      return;
    }
    this.logger.info("Loading packages from {}...", packageFolder.getAbsolutePath());

    // Construct a collection of all packages
    this.allPackages =
        jars.stream()
            .map(jarTuple -> this.packageFactory.create(jarTuple.getFile(), jarTuple.getJar()))
            .collect(Collectors.toSet());

    List<Package> libraryPackages = new ArrayList<>();
    int progress;
    do {
      outer:
      for (Package pack : this.allPackages) {

        for (String path : pack.getPackageManifest().getRuntimeClassPath()) {
          path =
              path.replace("${FLINT_PACKAGE_DIR}", packageFolder.getAbsolutePath())
                  .replace("${FLINT_LIBRARY_DIR}", "libraries");

          try {
            if (path.endsWith(".jar")) {
              File f = new File(path);
              if (!f.exists()) throw new IOException("Library not found");
              JarFile additionalJar = new JarFile(f);

              if (this.descriptionLoader.isManifestPresent(additionalJar)) {
                PackageManifest manifest = this.descriptionLoader.loadManifest(additionalJar);

                if (this.allPackages.stream()
                        .noneMatch(
                            availablePack ->
                                availablePack.getName().equals(manifest.getName())
                                    && availablePack.getVersion().equals(manifest.getVersion()))
                    && libraryPackages.stream()
                        .noneMatch(
                            availablePack ->
                                availablePack.getName().equals(manifest.getName())
                                    && availablePack.getVersion().equals(manifest.getVersion()))) {
                  libraryPackages.add(this.packageFactory.create(f, additionalJar));
                }
              } else {
                LaunchController.getInstance()
                    .getRootLoader()
                    .addURLs(Collections.singletonList(f.toURI().toURL()));
              }
            } else {
              LaunchController.getInstance()
                  .getRootLoader()
                  .addURLs(Collections.singletonList(new URL(path)));
            }

          } catch (IOException e) {
            this.logger.warn(
                "Couldn't resolve runtime classpath requirement of package {}. It may not work properly.",
                pack.getName());
            continue outer;
          }
        }
      }
      progress = libraryPackages.size();
      this.allPackages.addAll(libraryPackages);
      libraryPackages.clear();
    } while (progress > 0);

    // Filter out all packages which can be loaded
    Set<Package> loadablePackages =
        this.allPackages.stream()
            .filter(
                pack -> {
                  if (PackageState.NOT_LOADED.matches(pack)) return true;
                  else {
                    this.logger.warn(
                        "The package {} has the state {} and thus will not be loaded.",
                        pack.getName(),
                        pack.getState().name());
                    return false;
                  }
                })
            // TODO: check for Flint and Minecraft compatibility
            .collect(Collectors.toSet());

    Set<Package> loadedPackages = new HashSet<>();
    List<PackageManifest> classpathManifests = new ArrayList<>();
    try {
      Collections.list(
              LaunchController.getInstance()
                  .getRootLoader()
                  .getResources(DefaultPackageManifestLoader.MANIFEST_NAME))
          .forEach(
              manifestUrl -> {
                try {
                  classpathManifests.add(this.descriptionLoader.loadManifest(manifestUrl));
                } catch (IOException e) {
                  this.logger.warn(
                      "Couldn't read package manifest found on classpath at {}",
                      manifestUrl.toString(),
                      e);
                }
              });
    } catch (IOException e) {
      this.logger.warn("Couldn't scan classpath for additional already loaded packages.", e);
    }

    for (Package toLoad : resolveDependencies(loadablePackages, classpathManifests)) {
      // After all dependencies have been resolved, check if their dependencies have been loaded
      if (toLoad.getPackageManifest().getDependencies().stream()
          .allMatch(
              dependency ->
                  loadedPackages.stream()
                          .anyMatch(loaded -> dependency.matches(loaded.getPackageManifest()))
                      || classpathManifests.stream()
                          .anyMatch(
                              manifest ->
                                  dependency.getName().equals(manifest.getName())
                                      && dependency
                                          .getVersions()
                                          .contains(manifest.getVersion())))) {
        this.logger.info("Loading package {}...", toLoad.getName());

        // Check if the package has been loaded successfully, if not, log the error and continue
        if (toLoad.load().equals(PackageState.ERRORED)) {
          this.logger.error(
              "Failed to load package {}.", toLoad.getName(), toLoad.getLoadException());
        } else {
          loadedPackages.add(toLoad);
        }
      } else {
        // Dependencies failed to load
        this.logger.warn(
            "Can't load {} due to dependencies that failed to load.", toLoad.getName());
      }
    }

    this.allPackages.stream()
        .filter(PackageState.NOT_LOADED::matches)
        .forEach(
            pack -> {
              // Every package which by now is in the NOT_LOADED state has failed dependencies
              this.logger.warn(
                  "Can't load {} because at least one dependency is not present or failed to load.",
                  pack.getName());
              pack.setState(PackageState.UNSATISFIABLE_DEPENDENCIES);
            });

    // Enable all loaded packages
    this.allPackages.stream()
        .filter(PackageState.LOADED::matches)
        .forEach(
            pack -> {
              this.logger.info("Enabling package {}...", pack.getName());
              pack.enable();
            });
  }

  /**
   * Resolves the required load order regarding dependencies of a set of packages.
   *
   * @param packages The set of packages to resolve the load order of
   * @return A linked (ordered) list with a valid order of loading the given packages
   */
  private LinkedList<Package> resolveDependencies(
      final Set<Package> packages, List<PackageManifest> classpathManifests) {
    LinkedList<Package> resolvedPackages = new LinkedList<>();

    int progress;
    do {
      int previousSize = resolvedPackages.size();

      // Resolve dependencies by checking if the dependencies are already contained in the
      // list and if so adding the package. If not simply skip to the next package and try
      // again later.
      packages.stream()
          .filter(pack -> !resolvedPackages.contains(pack))
          .filter(
              pack ->
                  pack.getPackageManifest().getDependencies().stream()
                      .allMatch(
                          dependency ->
                              resolvedPackages.stream()
                                      .anyMatch(
                                          resolvedPackage ->
                                              dependency.matches(
                                                  resolvedPackage.getPackageManifest()))
                                  || classpathManifests.stream()
                                      .anyMatch(
                                          manifest ->
                                              dependency.getName().equals(manifest.getName())
                                                  && dependency.getVersions().stream()
                                                      .anyMatch(
                                                          v -> v.equals(manifest.getVersion())))))
          .forEach(resolvedPackages::add);

      progress = resolvedPackages.size() - previousSize;
      // Keep resolving while we make progress
    } while (progress > 0);

    return resolvedPackages;
  }

  /** {@inheritDoc} */
  @Override
  public Set<Package> getAllPackages() {
    return Collections.unmodifiableSet(this.allPackages);
  }

  /** {@inheritDoc} */
  @Override
  public Set<Package> getLoadedPackages() {
    return Collections.unmodifiableSet(
        this.allPackages.stream()
            .filter(pack -> PackageState.LOADED.matches(pack) || PackageState.ENABLED.matches(pack))
            .collect(Collectors.toSet()));
  }

  /**
   * Retrieves a log prefix for the given class. This will always be the name of the package if the
   * class has been loaded by a package class loader.
   *
   * @param clazz The clazz to determine the log prefix for
   * @return The log prefix to use for the class or null, if the class has not been loaded from a
   *     package
   * @implNote The log prefix is simply the name of the package the class has been loaded from
   */
  private String getLogPrefix(Class<?> clazz) {
    ClassLoader loader = clazz.getClassLoader();
    if (loader instanceof PackageClassLoader) {
      // The class has been loaded from a package
      return ((PackageClassLoader) loader).getOwner().getName();
    } else {
      return null;
    }
  }

  /** Helper class for storing a {@link JarFile} associated with a {@link File}. */
  private static class JarTuple {
    private final File file;
    private final JarFile jar;

    /**
     * Constructs a new {@link JarTuple}.
     *
     * @param file The jar file representation of file
     * @param jar The IO file representation of the jar file, assumed to point to the same file as
     *     the jar representation
     */
    public JarTuple(File file, JarFile jar) {
      this.file = file;
      this.jar = jar;
    }

    /**
     * Retrieves the file of this tuple.
     *
     * @return The file of this tuple
     */
    public File getFile() {
      return this.file;
    }

    /**
     * Retrieves the jar file of this tuple.
     *
     * @return The jar file of this tuple
     */
    public JarFile getJar() {
      return this.jar;
    }
  }
}
