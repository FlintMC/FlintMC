package net.labyfy.component.packages.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.packages.Package;
import net.labyfy.component.packages.PackageDescriptionLoader;
import net.labyfy.component.packages.PackageLoader;
import net.labyfy.component.packages.PackageState;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;

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

  private final File packageFolder;
  private final Package.Factory packageFactory;
  private final Set<JarFile> jars;

  private Set<Package> allPackages;

  @Inject
  private LabyPackageLoader(
      @Named("labyfyPackageFolder") File packageFolder,
      PackageDescriptionLoader descriptionLoader,
      Package.Factory packageFactory) {

    this.packageFolder = packageFolder;
    this.packageFactory = packageFactory;

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
                    return new JarFile(file);
                  } catch (IOException e) {
                    return null;
                  }
                })
            // filter nulls produced by previous step
            .filter(Objects::nonNull)
            // Filter JARs that contain a package manifest
            .filter(
                jarFile -> {
                  if (descriptionLoader.isDescriptionPresent(jarFile)) return true;
                  else {
                    System.out.println(
                        String.format(
                            "%s is in the package directory, but doesn not contain a package manifest. Ignoring it.",
                            jarFile.getName()));
                    return false;
                  }
                })
            // Collect to Set
            .collect(Collectors.toSet());
  }

  @TaskBody
  private void load() {
    System.out.println("Loading packages from " + packageFolder.getAbsolutePath() + "...");

    this.allPackages = jars.stream().map(packageFactory::create).collect(Collectors.toSet());

    Set<Package> loadablePackages =
        this.allPackages.stream()
            .filter(
                pack -> {
                  if (PackageState.NOT_LOADED.matches(pack)) return true;
                  else {
                    System.out.println(
                        String.format(
                            "WARNING: The package %s does not contain a valid package manifest and will not be loaded.",
                            pack.getName()));
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
        System.out.println(String.format("Loading package %s...", toLoad.getName()));
        if (toLoad.load().equals(PackageState.ERRORED)) {
          System.out.println(String.format("ERROR: Failed to load package %s.", toLoad.getName()));
          toLoad.getLoadException().printStackTrace();
        } else loadedPackages.add(toLoad);

      } else {
        System.out.println(
            String.format(
                "WARNING: Can't load %s due to dependencies that failed to load.",
                toLoad.getName()));
      }
    }

    this.allPackages.stream()
        .filter(PackageState.NOT_LOADED::matches)
        .forEach(
            pack -> {
              System.out.println(
                  String.format(
                      "WARNING: Can't load %s because at least one dependency is not present.",
                      pack.getName()));
              pack.setState(PackageState.UNSATISFIABLE_DEPENDENCIES);
            });

    this.allPackages.stream()
        .filter(PackageState.LOADED::matches)
        .forEach(
            pack -> {
              System.out.println(String.format("Enabling package %s...", pack.getName()));
              pack.enable();
            });
  }

  private LinkedList<Package> resolveDependencies(final Set<Package> loadablePackages) {

    LinkedList<Package> resolvedPackages = new LinkedList<>();

    int progress;
    do {
      int previousSize = resolvedPackages.size();

      loadablePackages.stream()
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
}
