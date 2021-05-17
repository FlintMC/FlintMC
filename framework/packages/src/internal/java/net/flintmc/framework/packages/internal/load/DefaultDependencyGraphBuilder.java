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

package net.flintmc.framework.packages.internal.load;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.*;
import net.flintmc.framework.packages.load.DependencyGraphBuilder;
import net.flintmc.util.commons.Pair;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Singleton
@Implement(DependencyGraphBuilder.class)
public class DefaultDependencyGraphBuilder implements DependencyGraphBuilder {

  private final Logger logger;
  private final Package.Factory packageFactory;
  private final PackageManifestLoader manifestLoader;

  @Inject
  private DefaultDependencyGraphBuilder(@InjectLogger Logger logger,
      Package.Factory packageFactory,
      PackageManifestLoader manifestLoader) {
    this.logger = logger;
    this.packageFactory = packageFactory;
    this.manifestLoader = manifestLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Pair<List<Package>, List<File>> buildDependencyGraph(
      List<Package> packages) {

    Pair<List<Package>, List<File>> dependencyGraph = Pair
        .of(new ArrayList<>(), new ArrayList<>());

    Pair<List<Package>, List<File>> resolved = Pair
        .of(new ArrayList<>(), new ArrayList<>());

    // resolve additional classpath requirements
    for (Package pack : packages) {
      if (PackageState.NOT_LOADED.matches(pack)) {
        this.resolveClasspathRecursively(pack, resolved, packages);
      }
    }

    List<Package> loadable = dependencyGraph.first();
    loadable.addAll(
        resolved.first().stream()
            .filter(PackageState.ENABLED::matches)
            .collect(Collectors.toList()));
    loadable.addAll(
        packages.stream()
            .filter(PackageState.ENABLED::matches)
            .collect(Collectors.toList()));

    Set<Package> originalAvailable = resolved.first().stream()
        .filter(PackageState.NOT_LOADED::matches).collect(
            Collectors.toSet());
    originalAvailable.addAll(
        packages.stream().filter(PackageState.NOT_LOADED::matches).collect(
            Collectors.toList()));

    List<Package> available = new ArrayList<>(originalAvailable);

    dependencyGraph.setSecond(resolved.second());

    Map<Package, List<String>> errorTrack = new HashMap<>();

    // build a dependency graph by searching for loadable
    // packages as long as progress is being made
    // Note: This is just a heuristic. The algorithm does
    //       not necessarily find the biggest loadable graph.
    //       However it should be good enough for now and
    //       gives extensive debugging information to resolve
    //       problems manually if needed.
    int progress;
    do {
      progress = -loadable.size();
      ListIterator<Package> it = available.listIterator();
      while (it.hasNext()) {
        Package pack = it.next();
        if (isLoadable(pack, loadable)) {
          loadable.add(pack);
          it.remove();
        }
      }
      progress += loadable.size();
    } while (progress > 0);

    // compute error messages
    for (Package pack : originalAvailable) {
      if (!loadable.contains(pack) && PackageState.NOT_LOADED.matches(pack)) {
        isLoadable(pack, loadable, errorTrack);
      }
    }

    loadable.removeIf(PackageState.ENABLED::matches);
    originalAvailable.removeIf(PackageState.ENABLED::matches);

    if (loadable.size() < originalAvailable.size()) {
      // some packages cannot be loaded due to unmet dependencies or conflicts

      this.logger.error(
          "Some packages were discovered but cannot be loaded due to unmet "
              + "dependencies or conflicts:");
      int i = 0;
      for (Package pack : originalAvailable) {
        if (!loadable.contains(pack)) {
          this.logger
              .error(String
                  .format("    [%d] %s (version %s):", ++i, pack.getName(),
                      pack.getVersion()));
          for (String error : errorTrack
              .computeIfAbsent(pack, p -> new ArrayList<>())) {
            this.logger.error(String.format("        - %s", error));
          }
        }
      }
    }

    return dependencyGraph;
  }

  private void resolveClasspathRecursively(
      Package pack,
      Pair<List<Package>, List<File>> alreadyPresent,
      List<Package> environment) {

    List<Package> packages = alreadyPresent.first();
    List<File> files = alreadyPresent.second();

    List<Package> libraryPackages = new ArrayList<>();

    for (String entry : pack.getPackageManifest().getRuntimeClassPath()) {
      File requiredFile = new File(replacePath(entry));

      // check whether the current entry is already being tracked
      if (this.containsPackage(packages, requiredFile) || this
          .containsFile(files, requiredFile) || this
          .containsPackage(libraryPackages, requiredFile)) {
        continue;
      }

      // check via required classpath element is jar file

      if (requiredFile.getName().toLowerCase().endsWith(".jar")) {
        try {
          JarFile jar = new JarFile(requiredFile);

          if (!this.manifestLoader.isManifestPresent(jar)) {
            // the file is a jar, but there is no package manifest,
            // so just add it to list of classpath files
            this.addFileIfNotPresent(files, requiredFile);
            continue;
          }

          PackageManifest manifest = this.manifestLoader.loadManifest(jar);

          if (packages.stream().anyMatch(
              present -> present.getName().equals(manifest.getName()))
              || environment.stream().anyMatch(
              present -> present.getName().equals(manifest.getName()))) {
            continue;
          }

          Package libraryPack = this.packageFactory.create(requiredFile, jar);

          if (libraryPack.getState() != PackageState.NOT_LOADED) {
            this.logger.error(
                String.format(
                    "Filed to read manifest of library package '%s'. "
                        + "Adding it to the classpath anyway.",
                    requiredFile.getPath()), libraryPack.getLoadException());
            this.addFileIfNotPresent(files, requiredFile);
            continue;
          }

          // add library package as a dependency to track errors later
          pack.getPackageManifest()
              .addDependency(libraryPack.getName(), libraryPack.getVersion(),
                  "");

          // add package to load target and resolve classpath
          // recursively later
          libraryPackages.add(libraryPack);

        } catch (Exception e) {
          this.logger.error(String.format(
              "Failed to read file '%s' required in "
                  + "classpath by package %s (version %s).",
              this.replacePath(requiredFile.getPath()),
              pack.getName(), pack.getVersion()), e);
          this.addFileIfNotPresent(files, requiredFile);
        }


      } else {
        // no need for recursion, file can be added to classpath
        // if not already present
        this.addFileIfNotPresent(files, requiredFile);
      }

      packages.addAll(libraryPackages);

      // recursively resolve classpath requirement
      // of library packages
      for (Package libraryPack : libraryPackages) {
        this.resolveClasspathRecursively(libraryPack, alreadyPresent,
            environment);
      }

    }

  }

  private boolean containsPackage(List<Package> packages, File file) {
    return packages.stream().anyMatch(pack -> pack.getFile().getAbsolutePath()
        .equals(file.getAbsolutePath()));
  }

  private boolean containsFile(List<File> files, File file) {
    return files.stream().anyMatch(f -> f.getAbsolutePath()
        .equals(file.getAbsolutePath()));
  }

  private void addFileIfNotPresent(List<File> files, File file) {
    if (!this.containsFile(files, file)) {
      files.add(file);
    }
  }

  private String replacePath(String path) {
    return path
        .replace(PackageLoader.PACKAGE_DIR_TEMPLATE, PackageLoader.PACKAGE_DIR)
        .replace(PackageLoader.LIBRARY_DIR_TEMPLATE, PackageLoader.LIBRARY_DIR);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLoadable(Package pack, List<Package> available) {
    return this.isLoadable(pack, available, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLoadable(Package pack,
      List<Package> available, Map<Package, List<String>> errorTrack) {

    boolean conflicts = false;

    for (Package p : available) {
      if (p.getName().equals(pack.getName())) {
        if (errorTrack != null) {
          this.addErrorMessage(pack, errorTrack,
              String.format(
                  "the package conflicts with %s (version %s).",
                  p.getName(), p.getVersion()));
        }
        if (errorTrack == null) {
          return false;
        } else {
          conflicts = true;
        }
      }
    }

    boolean dependenciesMet = true;
    for (DependencyDescription dependency : pack.getPackageManifest()
        .getDependencies()) {

      if (available.stream()
          .noneMatch(p -> dependency.matches(p.getPackageManifest()))) {
        if (errorTrack == null) {
          return false;
        } else {
          dependenciesMet = false;
          this.addErrorMessage(pack, errorTrack,
              String.format("dependency not met: %s (version %s)",
                  dependency.getName(),
                  String.join(" or ", dependency.getVersions())));
        }
      }

    }

    return !conflicts && dependenciesMet;
  }

  private void addErrorMessage(Package pack,
      Map<Package, List<String>> errorTrack, String message) {
    errorTrack.computeIfAbsent(pack, p -> new ArrayList<>()).add(message);
  }

}
