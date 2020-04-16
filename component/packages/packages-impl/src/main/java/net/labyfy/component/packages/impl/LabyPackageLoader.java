package net.labyfy.component.packages.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.inject.logging.InjectLogger;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageDescriptionLoader;
import net.labyfy.component.packages.PackageLoader;
import net.labyfy.component.packages.PackageState;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
@Implement(PackageLoader.class)
public class LabyPackageLoader implements PackageLoader {

  @InjectLogger private Logger logger;
  private final File packageFolder;
  private final Package.Factory packageFactory;
  private final Set<JarTuple> jars;

  private Set<Package> allPackages;

  @Inject
  private LabyPackageLoader(
      @Named("labyfyPackageFolder") File packageFolder,
      PackageDescriptionLoader descriptionLoader,
      Package.Factory packageFactory) {

    this.packageFolder = packageFolder;
    this.packageFactory = packageFactory;

    boolean exists = true;
    if (!this.packageFolder.exists() || !this.packageFolder.isDirectory()) {
      if (!this.packageFolder.mkdirs()) exists = false;
    }

    if (exists) {
      this.jars =
          Stream.of(Objects.requireNonNull(packageFolder.listFiles()))
              // Filter for JAR files
              .filter(file -> file.getName().toLowerCase().endsWith(".jar"))
              // Filter for readable files
              .filter(file -> file.isFile() && file.canRead())
              // Map to JarFile references
              .map(
                  file -> {
                    try {
                      return new JarTuple(file, new JarFile(file));
                    } catch (IOException e) {
                      return null;
                    }
                  })
              // filter nulls produced by previous step
              .filter(Objects::nonNull)
              .filter(jarTuple -> jarTuple.getJar() != null)
              // Filter JARs that contain a package manifest
              .filter(
                  jarTuple -> {
                    if (descriptionLoader.isDescriptionPresent(jarTuple.getJar())) return true;
                    else {
                      this.logger.warn(
                          "{} is in the package directory, but doesn not contain a package manifest. Ignoring it.",
                          jarTuple.getJarFile().getName());
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

  @TaskBody
  private void load() {
    if (this.jars.isEmpty()) return;
    this.logger.info("Loading packages from {}...", packageFolder.getAbsolutePath());

    this.allPackages =
        jars.stream()
            .map(jarTuple -> packageFactory.create(jarTuple.getJarFile(), jarTuple.getJar()))
            .collect(Collectors.toSet());

    Set<Package> loadablePackages =
        this.allPackages.stream()
            .filter(
                pack -> {
                  if (PackageState.NOT_LOADED.matches(pack)) return true;
                  else {
                    this.logger.warn(
                        "The package {} does not contain a valid package manifest and will not be loaded.",
                        pack.getName());
                    return false;
                  }
                })
            // TODO: check for Labyfy and Minecraft compatibility
            .collect(Collectors.toSet());

    Set<Package> loadedPackages = new HashSet<>();
    for (Package toLoad : resolveDependencies(loadablePackages)) {
      if (toLoad.getPackageDescription().getDependencies().stream()
          .allMatch(
              dependency ->
                  loadedPackages.stream()
                      .anyMatch(loaded -> dependency.matches(loaded.getPackageDescription())))) {
        this.logger.info("Loading package {}...", toLoad.getName());
        if (toLoad.load().equals(PackageState.ERRORED)) {
          this.logger.error("Failed to load package {}.", toLoad.getName());
          this.logger.throwing(Level.ERROR, toLoad.getLoadException());
        } else loadedPackages.add(toLoad);

      } else {
        this.logger.warn(
            "Can't load {} due to dependencies that failed to load.", toLoad.getName());
      }
    }

    this.allPackages.stream()
        .filter(PackageState.NOT_LOADED::matches)
        .forEach(
            pack -> {
              this.logger.warn(
                  "Can't load {} because at least one dependency is not present.", pack.getName());
              pack.setState(PackageState.UNSATISFIABLE_DEPENDENCIES);
            });

    this.allPackages.stream()
        .filter(PackageState.LOADED::matches)
        .forEach(
            pack -> {
              this.logger.info("Enabling package {}...", pack.getName());
              pack.enable();
            });
  }

  private LinkedList<Package> resolveDependencies(final Set<Package> loadablePackages) {

    LinkedList<Package> resolvedPackages = new LinkedList<>();

    int progress;
    do {
      int previousSize = resolvedPackages.size();

      loadablePackages.stream()
          .filter(pack -> !resolvedPackages.contains(pack))
          .filter(
              pack ->
                  pack.getPackageDescription().getDependencies().stream()
                      .allMatch(
                          dependency ->
                              resolvedPackages.stream()
                                  .anyMatch(
                                      resolvedPackage ->
                                          dependency.matches(
                                              resolvedPackage.getPackageDescription()))))
          .forEach(resolvedPackages::add);

      progress = resolvedPackages.size() - previousSize;

    } while (progress > 0);

    return resolvedPackages;
  }

  @Override
  public Set<Package> getAllPackages() {
    return Collections.unmodifiableSet(this.allPackages);
  }

  @Override
  public Set<Package> getLoadedPackages() {
    return Collections.unmodifiableSet(
        this.allPackages.stream()
            .filter(pack -> PackageState.LOADED.matches(pack) || PackageState.ENABLED.matches(pack))
            .collect(Collectors.toSet()));
  }

  private static class JarTuple {

    private final File jarFile;
    private final JarFile jar;

    public JarTuple(File jarFile, JarFile jar) {
      this.jarFile = jarFile;
      this.jar = jar;
    }

    public File getJarFile() {
      return this.jarFile;
    }

    public JarFile getJar() {
      return this.jar;
    }
  }
}
