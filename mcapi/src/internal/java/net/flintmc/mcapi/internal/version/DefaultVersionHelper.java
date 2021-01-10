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

package net.flintmc.mcapi.internal.version;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.version.VersionHelper;

/**
 * Default implementation of the {@link VersionHelper}.
 */
@Singleton
@Implement(value = VersionHelper.class)
public class DefaultVersionHelper implements VersionHelper {

  private final Map<String, String> launchArguments;
  private int majorVersion;
  private int minorVersion;
  private int patchVersion;

  @Inject
  private DefaultVersionHelper(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
    this.splitVersion();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMajor() {
    return this.majorVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMinor() {
    return this.minorVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPatch() {
    return this.patchVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUnder(int minor) {
    return this.getMinor() < minor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUnder(int minor, int patch) {
    return this.getMinor() < minor && this.getPatch() < patch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUnder(int major, int minor, int patch) {
    return this.getMajor() < major && this.getMinor() < major && this.getPatch() < patch;
  }

  /**
   * Splits up the versioning of the client.
   */
  private void splitVersion() {
    String version = this.getVersion();

    if (!version.contains(".")) {
      throw new IllegalStateException("Not valid version. (" + version + ")");
    }

    String[] split = this.getVersion().split("\\.");

    for (int i = 0; i < split.length; i++) {
      int versionNumber = Integer.parseInt(split[i]);
      if (i == 0) {
        this.majorVersion = versionNumber;
      } else if (i == 1) {
        this.minorVersion = versionNumber;
      } else if (i == 2) {
        this.patchVersion = versionNumber;
      }
    }

    // Is only for version without any patches
    if (split.length < 3) {
      this.patchVersion = 0;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion() {
    return this.launchArguments.get("--game-version");
  }
}
