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

package net.flintmc.metaprogramming;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Contains the most basic metadata about a package - its group, name and version.
 */
public class PackageMeta {

  private static final Map<String, PackageMeta> CACHE = new HashMap<>();

  /**
   * Retrieves the package meta for the given combination of group, name and version.
   *
   * <p>This is statically cached to prevent object duplication (the generated code would produce
   * a lot identical objects).
   *
   * @param group   The group of the package
   * @param name    The name of the package
   * @param version The version of the package
   * @return The meta instance for the given parameters
   */
  public static PackageMeta of(String group, String name, String version) {
    String key = cacheKey(group, name, version);

    return CACHE.computeIfAbsent(key, (k) -> new PackageMeta(group, name, version));
  }

  /**
   * Generates a cache key for the combination of group, name and version. This currently is a maven
   * identifier, <b>but considered internal, so there is no guarantee given that this wont change or
   * be removed entirely!</b>
   *
   * @param group   The group of the package
   * @param name    The name of the package
   * @param version The version of the package
   * @return The cache key for the given parameters
   */
  private static String cacheKey(String group, String name, String version) {
    return group + ":" + name + ":" + version;
  }

  private final String group;
  private final String name;
  private final String version;

  private PackageMeta(String group, String name, String version) {
    this.group = group;
    this.name = name;
    this.version = version;
  }

  /**
   * Retrieves the group of the package.
   *
   * @return The group of the package
   */
  public String getGroup() {
    return group;
  }

  /**
   * Retrieves the name of the package
   *
   * @return The name of the package
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the version of the package
   *
   * @return The version of the package
   */
  public String getVersion() {
    return version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PackageMeta)) {
      return false;
    }
    PackageMeta that = (PackageMeta) o;
    return group.equals(that.group) && name.equals(that.name) && version.equals(that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group, name, version);
  }

  @Override
  public String toString() {
    return group + ":" + name + ":" + version;
  }
}
