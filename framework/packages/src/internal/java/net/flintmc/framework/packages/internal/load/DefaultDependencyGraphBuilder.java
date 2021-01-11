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
import net.flintmc.framework.packages.DependencyDescription;
import net.flintmc.framework.packages.Package;
import net.flintmc.framework.packages.load.DependencyGraphBuilder;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultDependencyGraphBuilder implements DependencyGraphBuilder {

  private final Logger logger;

  @Inject
  private DefaultDependencyGraphBuilder(@InjectLogger Logger logger) {
    this.logger = logger;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Package> buildDependencyGraph(List<Package> packages) {

    List<Package> loadable = new ArrayList<>();
    Map<Package, List<String>> errorTrack = new HashMap<>();

    int progress;
    do {
      progress = -loadable.size();
      for (Package pack : packages) {
        if (isLoadable(pack, loadable, errorTrack)) {
          loadable.add(pack);
        }
      }
      progress += loadable.size();
    } while (progress > 0);

    if (loadable.size() < packages.size()) {
      // some packages cannot be loaded due to unmet dependencies or conflicts

      this.logger.error(
          "Some packages were discovered but cannot be loaded due to unmet "
              + "dependencies or conflicts:");
      int i = 0;
      for (Package pack : packages) {
        if (!loadable.contains(pack)) {
          this.logger
              .error(String
                  .format("    [%d] %s (version %s):", ++i, pack.getName(),
                      pack.getVersion()));
          for (String error : errorTrack.get(pack)) {
            this.logger.error(String.format("        - %s", error));
          }
        }
      }

    }

    return loadable;
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
