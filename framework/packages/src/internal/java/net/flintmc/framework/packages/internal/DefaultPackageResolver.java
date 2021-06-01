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

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.PackageClassLoader;
import net.flintmc.framework.packages.PackageLoader;
import net.flintmc.framework.packages.PackageResolver;

import javax.inject.Inject;
import javax.inject.Singleton;

@Implement(PackageResolver.class)
@Singleton
public class DefaultPackageResolver implements PackageResolver {

  private final PackageLoader loader;

  @Inject
  private DefaultPackageResolver(PackageLoader loader) {
    this.loader = loader;
  }

  @Override
  public Package resolvePackageByName(String name) {
    return loader.getAllPackages(true).stream().filter((p) -> p.getName().equals(name)).findFirst()
        .orElse(null);
  }

  @Override
  public Package resolvePackage(Class<?> clazz) {
    ClassLoader classLoader = clazz.getClassLoader();
    if (classLoader instanceof PackageClassLoader) {
      return ((PackageClassLoader) classLoader).getOwner();
    }
    return null;
  }
}
