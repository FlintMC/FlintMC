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
import net.flintmc.framework.inject.logging.InjectLogger;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageManifestLoader;
import net.flintmc.framework.packages.load.PackageFinder;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.JarFile;

public class DefaultPackageFinder implements PackageFinder {

  private final Logger logger;
  private final Package.Factory packageFactory;
  private final PackageManifestLoader manifestLoader;

  @Inject
  private DefaultPackageFinder(@InjectLogger Logger logger,
      Package.Factory packageFactory, PackageManifestLoader manifestLoader) {
    this.logger = logger;
    this.packageFactory = packageFactory;
    this.manifestLoader = manifestLoader;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Package> findPackages(String path) throws IOException {
    File packageDir = this.ensureDirectory(path);
    return this.findPackages(packageDir);
  }

  private List<Package> findPackages(File packageDir) {
    List<Package> packages = new ArrayList<>();

    // iterate over file entries recursively and construct packages if possible
    for (File file : Objects.requireNonNull(packageDir.listFiles())) {
      if (file.isDirectory()) {
        packages.addAll(this.findPackages(file));
      } else if (file.getName().toLowerCase().endsWith("'.jar")) {
        this.constructPackage(file).ifPresent(packages::add);
      }
    }

    return packages;
  }

  // tries to construct a Package instance from the given file
  private Optional<Package> constructPackage(File file) {
    try {
      JarFile jar = new JarFile(file);
      if (manifestLoader.isManifestPresent(jar)) {
        return Optional.of(this.packageFactory.create(file, jar));
      } else {
        this.logger
            .warn(String.format(
                "The file '%s' is in the package directory, but it doesn't "
                    + "contain a package manifest.", file.getPath()));
      }
    } catch (Exception e) {
      this.logger.warn(String.format(
          "The file '%s' qualifies as a potential package but a package "
              + "could not be constructed from it.",
          file.getPath()));
    }

    return Optional.empty();
  }

  // Ensures the given path exists and is a directory.
  private File ensureDirectory(String path) throws IOException {
    File packageDir = new File(path);
    if ((!packageDir.exists() || !packageDir.isDirectory()) && packageDir
        .mkdirs()) {
      throw new IOException(
          String.format("Failed to create package directory at %s.", path));
    }
    return packageDir;
  }

}
