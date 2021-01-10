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

package net.flintmc.mcapi.internal.world.storage;

import javax.annotation.Nullable;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.world.storage.WorldOverview;

/**
 * Default implementation of the {@link WorldOverview}.
 */
@Implement(WorldOverview.class)
public class DefaultWorldOverview implements WorldOverview {

  private final String fileName;
  private final String displayName;
  private final String version;
  private final long lastTimePlayed;
  private final long sizeOnDisk;
  private final boolean requiresConversion;
  private final GameMode gameMode;
  private final boolean hardcoreMode;
  private final boolean cheats;
  private final boolean askToOpenWorld;
  private final boolean markWorldInList;
  private final boolean futureWorld;
  private final boolean outdatedWorld;

  @AssistedInject
  private DefaultWorldOverview(
      @Assisted("fileName") String fileName,
      @Assisted("displayName") String displayName,
      @Assisted("version") @Nullable String version,
      @Assisted("lastTimePlayed") long lastTimePlayed,
      @Assisted("sizeOnDisk") long sizeOnDisk,
      @Assisted("requiresConversion") boolean requiresConversion,
      @Assisted("gameMode") GameMode gameMode,
      @Assisted("hardcoreMode") boolean hardcoreMode,
      @Assisted("cheats") boolean cheats,
      @Assisted("askToOpenWorld") boolean askToOpenWorld,
      @Assisted("markWorldInList") boolean markWorldInList,
      @Assisted("futureWorld") boolean futureWorld,
      @Assisted("outdatedWorld") boolean outdatedWorld) {
    this.fileName = fileName;
    this.displayName = displayName;
    this.version = version;
    this.lastTimePlayed = lastTimePlayed;
    this.sizeOnDisk = sizeOnDisk;
    this.requiresConversion = requiresConversion;
    this.gameMode = gameMode;
    this.hardcoreMode = hardcoreMode;
    this.cheats = cheats;

    this.askToOpenWorld = askToOpenWorld;
    this.markWorldInList = markWorldInList;
    this.futureWorld = futureWorld;
    this.outdatedWorld = outdatedWorld;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFileName() {
    return this.fileName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDisplayName() {
    return this.displayName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion() {
    return this.version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getSizeOnDisk() {
    return this.sizeOnDisk;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean requiresConversion() {
    return this.requiresConversion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getLastTimePlayed() {
    return this.lastTimePlayed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameMode getGameMode() {
    return this.gameMode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHardcoreMode() {
    return this.hardcoreMode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCheats() {
    return this.cheats;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean markWorldInList() {
    return this.markWorldInList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean askToOpenWorld() {
    return this.askToOpenWorld;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isFutureWorld() {
    return this.futureWorld;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOutdatedWorld() {
    return this.outdatedWorld;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(WorldOverview overview) {
    if (this.lastTimePlayed < overview.getLastTimePlayed()) {
      return 1;
    } else {
      return this.lastTimePlayed > overview.getLastTimePlayed() ? -1
          : this.fileName.compareTo(overview.getFileName());
    }
  }
}
