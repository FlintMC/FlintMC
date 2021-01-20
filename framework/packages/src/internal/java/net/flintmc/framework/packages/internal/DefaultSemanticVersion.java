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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.packages.SemanticVersion;

@Implement(SemanticVersion.class)
public class DefaultSemanticVersion implements SemanticVersion {

  private final int major;
  private final int minor;
  private final int patch;
  private final List<String> prereleaseIdentifiers;
  private final List<String> buildIdentifiers;

  @AssistedInject
  private DefaultSemanticVersion(@Assisted("major") int major,
      @Assisted("minor") int minor, @Assisted("patch") int patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.prereleaseIdentifiers = new ArrayList<>();
    this.buildIdentifiers = new ArrayList<>();
  }

  @AssistedInject
  private DefaultSemanticVersion(@Assisted("major") int major,
      @Assisted("minor") int minor, @Assisted("patch") int patch,
      @Assisted("prereleaseIdentifiers") List<String> prereleaseIdentifiers,
      @Assisted("buildIdentifiers") List<String> buildIdentifiers) {
    this(major, minor, patch);
    this.prereleaseIdentifiers.addAll(prereleaseIdentifiers);
    this.buildIdentifiers.addAll(buildIdentifiers);
  }

  @AssistedInject
  private DefaultSemanticVersion(
      @Assisted("versionString") String versionString) {
    String version = versionString;
    String identifiers = "";
    if (versionString.contains("-")) {
      String[] split = versionString.split("-");
      version = split[0];
      identifiers = split[1];
    }

    String[] levels = version.split("\\.");

    if (levels.length != 3) {
      throw new IllegalArgumentException(
          "The given string is not a valid semantic version: " + versionString);
    }

    String prereleases = "";
    String builds = "";

    if (versionString.contains("+")) {
      String[] split = versionString.split("\\+");
      builds = split[1];
      if (identifiers.length() > 0) {
        prereleases = split[0].split("-")[1];
      }
    }

    try {
      this.major = Integer.parseInt(levels[0]);
      this.minor = Integer.parseInt(levels[1]);
      this.patch = Integer.parseInt(levels[2]);

      this.prereleaseIdentifiers = new ArrayList<>();
      if (!prereleases.isEmpty()) {
        Collections
            .addAll(this.prereleaseIdentifiers, prereleases.split("\\."));
      }

      this.buildIdentifiers = new ArrayList<>();
      if (!builds.isEmpty()) {
        Collections.addAll(this.buildIdentifiers, prereleases.split("\\."));
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "The given string is not a valid semantic version: " + versionString,
          e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMajor() {
    return this.major;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMinor() {
    return this.minor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPatch() {
    return this.patch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getPrereleaseIdentifiers() {
    return this.prereleaseIdentifiers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getBuildIdentifiers() {
    return this.buildIdentifiers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean satisfiesDependency(SemanticVersion dependency) {
    return dependency.isSatisfiedBy(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSatisfiedBy(SemanticVersion available) {
    // a dependency on version A.B.C is satisfied if
    // the available version matches A.>=B.*
    return available.getMajor() == this.getMajor()
        && available.getMinor() >= this.getMinor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {

    String version = String
        .format("%d.%d.%d", this.major, this.minor, this.patch);

    if (!this.prereleaseIdentifiers.isEmpty()) {
      version += String
          .format("-%s",
              String.join(".", this.prereleaseIdentifiers));
    }

    if (!this.buildIdentifiers.isEmpty()) {
      version += String
          .format("+%s",
              String.join(".", this.buildIdentifiers));
    }

    return version;
  }

}
