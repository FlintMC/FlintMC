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
import net.flintmc.framework.inject.logging.LoggingProvider;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.*;
import net.flintmc.framework.packages.load.DependencyGraphBuilder;
import net.flintmc.framework.packages.load.PackageFinder;
import net.flintmc.util.commons.Pair;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@Implement(PackageLoader.class)
public class DefaultPackageLoader implements PackageLoader {

  private final Logger logger;
  private PackageManifestLoader descriptionLoader;
  private DependencyGraphBuilder dependencyGraphBuilder;

  private Set<Package> allPackages;

  private List<Package> discoveredPackages;

  @Inject
  private DefaultPackageLoader(
      LoggingProvider loggingProvider,
      PackageManifestLoader descriptionLoader,
      PackageFinder packageFinder,
      DependencyGraphBuilder dependencyGraphBuilder,
      @InjectLogger Logger logger) {
    this.descriptionLoader = descriptionLoader;
    this.logger = logger;

    // Tell the logging provider we now are able to resolve logging prefixes
    loggingProvider.setPrefixProvider(this::getLogPrefix);

    try {
      this.discoveredPackages = packageFinder
          .findPackages(PackageLoader.PACKAGE_DIR);
    } catch (IOException e) {
      this.logger.error("Failed to discover packages.", e);
    }

  }

  public void load() {
    if (this.discoveredPackages == null) {
      return;
    }

    List<Package> packagesToLoad = new ArrayList<>();

    // make sure every discovered package contains a valid manifest
    for (Package pack : this.discoveredPackages) {
      if (pack.getState() == PackageState.INVALID_MANIFEST) {
        this.logger.error(
            String.format(
                "The file '%s' appears to be a package, but it does not "
                    + "contain a valid manifest.",
                pack.getFile().getName()));
      } else {
        packagesToLoad.add(pack);
      }
    }

    if (packagesToLoad.isEmpty()) {
      this.logger.info("No valid packages have been found...");
      return;
    }

    // build a loadable dependency graph
    Pair<List<Package>, List<File>> loadable = this.dependencyGraphBuilder
        .buildDependencyGraph(packagesToLoad);
    List<Package> loadablePackages = loadable.first();

    List<Package> loadedSuccessfully = new ArrayList<>();
    Map<Package, List<String>> errorTrack = new HashMap<>();

    // check which packages have been loaded successfully
    // and print error messages if needed
    for (Package pack : loadablePackages) {
      if (dependencyGraphBuilder
          .isLoadable(pack, loadedSuccessfully, errorTrack)) {
        if (pack.load() == PackageState.LOADED) {
          loadedSuccessfully.add(pack);
        } else {
          this.logger.error(
              String.format("Failed to load package %s (version %s).",
                  pack.getName(), pack.getVersion()), pack.getLoadException());
        }
      } else {
        this.logger.error(
            String.format(
                "Failed to load package %s (version %s) due to unmet "
                    + "dependencies. This may cause additional errors while "
                    + "trying to load subsequent packages.", pack.getName(),
                pack.getVersion()));
      }


    }

  }


  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Package> getAllPackages() {
    return Collections.unmodifiableSet(this.allPackages);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Package> getLoadedPackages() {
    return Collections.unmodifiableSet(
        this.allPackages.stream()
            .filter(pack -> PackageState.LOADED.matches(pack)
                || PackageState.ENABLED.matches(pack))
            .collect(Collectors.toSet()));
  }

  /**
   * Retrieves a log prefix for the given class. This will always be the name of
   * the package if the class has been loaded by a package class loader.
   *
   * @param clazz The clazz to determine the log prefix for
   * @return The log prefix to use for the class or null, if the class has not
   * been loaded from a package
   * @implNote The log prefix is simply the name of the package the class has
   * been loaded from
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
}
