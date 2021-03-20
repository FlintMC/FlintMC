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

package net.flintmc.framework.packages;

import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.primitive.InjectionHolder;

/**
 * Represents a semantic version (see <a>https://semver.org/</a> for more
 * information).
 */
public interface SemanticVersion {

  /**
   * Creates a new semantic version from a given version string.
   *
   * @param string the version string
   * @return a new {@link SemanticVersion} instance representing the given
   * version
   */
  static SemanticVersion from(String string) {
    return InjectionHolder.getInjectedInstance(Factory.class).create(string);
  }

  /**
   * @return the major version
   */
  int getMajor();

  /**
   * @return the minor version
   */
  int getMinor();

  /**
   * @return the patch version
   */
  int getPatch();

  /**
   * @return a list of prerelease identifiers
   */
  List<String> getPrereleaseIdentifiers();

  /**
   * @return a list of build identifiers
   */
  List<String> getBuildIdentifiers();

  /**
   * Builds a version string out of this semantic version.
   *
   * @return a version string representing this semantic version
   */
  String toString();

  /**
   * Checks whether this semantic version satisfies a dependency on a given
   * semantic version.
   *
   * @param dependency the version that the dependency is on
   * @return true, if this version satisfies the dependency
   */
  boolean satisfiesDependency(SemanticVersion dependency);

  /**
   * Checks whether a dependency on this version is satisfied by the given
   * "available" version.
   *
   * @param available the version to check whether it satisfies this dependency
   * @return true, if this dependency is satisfied by the given version
   */
  boolean isSatisfiedBy(SemanticVersion available);

  @AssistedFactory(SemanticVersion.class)
  interface Factory {

    /**
     * Parses a semantic version represented as a string.
     *
     * @param versionString the version string to parse
     * @return a new {@link SemanticVersion} instance representing the given
     * version
     */
    SemanticVersion create(@Assisted("versionString") String versionString);

    /**
     * Creates a new {@link SemanticVersion} without prerelease and build
     * identifiers.
     *
     * @param major the major version
     * @param minor the minor version
     * @param patch the patch version
     * @return a new {@link SemanticVersion} instance
     */
    SemanticVersion create(@Assisted("major") int major,
        @Assisted("minor") int minor, @Assisted("patch") int patch);

    /**
     * Creates a new {@link SemanticVersion} with prerelease and build
     * identifiers. The lists can also be empty.
     *
     * @param major                 the major version
     * @param minor                 the minor version
     * @param patch                 the patch version
     * @param prereleaseIdentifiers a non-null list of prerelease identifiers
     * @param buildIdentifiers      a non-null list of build identifiers
     * @return a new {@link SemanticVersion} instance
     */
    SemanticVersion create(@Assisted("major") int major,
        @Assisted("minor") int minor, @Assisted("patch") int patch,
        @Assisted("prereleaseIdentifiers") List<String> prereleaseIdentifiers,
        @Assisted("buildIdentifiers") List<String> buildIdentifiers);

  }

}
