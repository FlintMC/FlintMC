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
import net.flintmc.framework.inject.internal.DefaultLoggingProvider;
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageClassLoader;
import net.flintmc.framework.packages.PackageLoader;
import net.flintmc.framework.packages.PackageState;
import net.flintmc.framework.packages.load.PackageFinder;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@Implement(PackageLoader.class)
public class DefaultPackageLoader implements PackageLoader {

  private final Logger logger;
  private DefaultPackageManifestLoader descriptionLoader;

  private Set<Package> allPackages;

  private List<Package> discoveredPackages;

  @Inject
  private DefaultPackageLoader(
      DefaultLoggingProvider loggingProvider,
      DefaultPackageManifestLoader descriptionLoader,
      PackageFinder packageFinder,
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

    for (Package pack : this.discoveredPackages) {
      if (pack.getState() == PackageState.INVALID_MANIFEST) {
        this.logger.error(
            "The file %s appears to be a package, but it does not contain a "
                + "valid manifest.");
      } else {
        packagesToLoad.add(pack);
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
