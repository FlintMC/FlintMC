package net.flintmc.mcapi.v1_15_2.world;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.player.BaseClientPlayer;
import net.flintmc.mcapi.world.ClientWorld;
import net.flintmc.mcapi.world.border.WorldBorder;
import net.flintmc.mcapi.world.difficulty.DifficultyLocal;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 1.15.2 implementation of {@link ClientWorld}
 */
@Singleton
@Implement(value = ClientWorld.class, version = "1.15.2")
public class VersionedClientWorld extends VersionedWorld implements ClientWorld {

  private final Set<BaseClientPlayer> players;
  private final Map<Integer, Entity> entities;
  private final Scoreboard scoreboard;

  @Inject
  public VersionedClientWorld(
          BlockPosition.Factory blockPositionFactory,
          DifficultyLocal.Factory difficultyLocalFactory,
          WorldBorder worldBorder,
          Scoreboard scoreboard
  ) {
    super(blockPositionFactory, difficultyLocalFactory, worldBorder, scoreboard);
    this.scoreboard = scoreboard;
    this.entities = Maps.newHashMap();
    this.players = Sets.newHashSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getEntityCount() {
    return Minecraft.getInstance().world.getCountLoadedEntities();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addPlayer(BaseClientPlayer player) {
    return this.players.add(player);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removePlayer(UUID uniqueId) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<Integer, Entity> getEntities() {
    return this.entities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<BaseClientPlayer> getPlayers() {
    return this.players;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPlayerCount() {
    return this.players.size();
  }

}
