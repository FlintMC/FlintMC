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

package net.flintmc.framework.packages.load;

import net.flintmc.framework.packages.Package;
import net.flintmc.util.commons.Pair;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Util to build the best fitting dependency graph for a given list of
 * packages.
 */
public interface DependencyGraphBuilder {

  /**
   * Builds the best fitting set of Packages that can be loaded without
   * unresolved dependencies or conflicts (dependencies or versions).
   *
   * @param packages a list of potentially loadable packages
   * @return a list of packages that can be loaded in the given order without
   * producing dependency errors and a list of files that need to be on the
   * classpath for the packages to load
   */
  Pair<List<Package>, List<File>> buildDependencyGraph(List<Package> packages);

  /**
   * Checks whether a given {@link Package} is loadable assuming a list of given
   * Packages is already loaded. If true is returned, it is guaranteed that (1)
   * all dependencies of the Package are met in the given list and (2) the
   * Package does not have a conflict with a Package in the given list.
   *
   * @param pack      the {@link Package} to check
   * @param available the list of Packages that are available to meet
   *                  dependencies
   * @return true, if the Package should be loadable, false if not
   */
  boolean isLoadable(Package pack, List<Package> available);

  /**
   * Checks whether a given {@link Package} is loadable assuming a list of given
   * Packages is already loaded. If true is returned, it is guaranteed that (1)
   * all dependencies of the Package are met in the given list and (2) the
   * Package does not have a conflict with a Package in the given list.
   *
   * @param pack       the {@link Package} to check
   * @param available  the list of Packages that are available to meet
   *                   dependencies
   * @param errorTrack the Map in which to write short debug messages in case
   *                   false is returned (will not be modified if true is
   *                   returned, otherwise the value with pack as a key will be
   *                   written)
   * @return true, if the Package should be loadable, false if not
   */
  boolean isLoadable(Package pack, List<Package> available,
      Map<Package, List<String>> errorTrack);

}
