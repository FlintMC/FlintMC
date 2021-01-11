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

import java.util.List;
import net.flintmc.framework.packages.Package;

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
   * producing dependency errors
   */
  List<Package> buildDependencyGraph(List<Package> packages);

}
