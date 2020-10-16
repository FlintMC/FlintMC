package net.labyfy.internal.component.world.v1_15_2;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.AbstractClientPlayer;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.border.WorldBorder;
import net.labyfy.component.world.difficult.DifficultyLocal;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.math.BlockPosition;
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

  private final Set<AbstractClientPlayer> players;
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
  public boolean addPlayer(AbstractClientPlayer player) {
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
  public Set<AbstractClientPlayer> getPlayers() {
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
