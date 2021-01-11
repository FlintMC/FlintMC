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

import net.flintmc.framework.packages.DependencyDescription;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.load.DependencyGraphBuilder;
import java.util.ArrayList;
import java.util.List;

public class DefaultDependencyGraphBuilder implements DependencyGraphBuilder {

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Package> buildDependencyGraph(List<Package> packages) {

    List<Package> loadable = new ArrayList<>();

    int progress;
    do {
      progress = -loadable.size();
      for (Package pack : packages) {
        if (areDependenciesSatisfied(pack, loadable)) {
          loadable.add(pack);
        }
      }
      progress += loadable.size();
    } while (progress > 0);

    return loadable;
  }

  private boolean areDependenciesSatisfied(Package pack,
      List<Package> available) {

    for (Package p : available) {
      if (p.getName().equals(pack.getName())) {
        return false;
      }
    }

    for (DependencyDescription dependency : pack.getPackageManifest()
        .getDependencies()) {

      if (available.stream()
          .noneMatch(p -> dependency.matches(p.getPackageManifest()))) {
        return false;
      }

    }

    return true;
  }

}
