package net.flintmc.mcapi.internal.world.storage;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
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
      return this.lastTimePlayed > overview.getLastTimePlayed() ? -1 : this.fileName.compareTo(overview.getFileName());
    }
  }
}
