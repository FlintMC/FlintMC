package net.labyfy.internal.component.world.v1_16_3;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.AbstractClientPlayerEntity;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.border.WorldBorder;
import net.labyfy.component.world.difficult.DifficultyLocal;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.util.BlockPosition;
import net.minecraft.client.Minecraft;

import java.util.*;

/**
 * 1.16.3 implementation of {@link ClientWorld}
 */
@Singleton
@Implement(value = ClientWorld.class, version = "1.16.3")
public class VersionedClientWorld extends VersionedWorld implements ClientWorld {

  private final Set<AbstractClientPlayerEntity> players;
  private final Scoreboard scoreboard;

  @Inject
  public VersionedClientWorld(
          BlockPosition.Factory blockPositionFactory,
          DifficultyLocal.Factory difficultyLocalFactory,
          Scoreboard scoreboard,
          WorldBorder worldBorder
  ) {
    super(blockPositionFactory, difficultyLocalFactory, worldBorder);
    this.scoreboard = scoreboard;
    this.players = new HashSet<>();
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
  public boolean addPlayer(AbstractClientPlayerEntity player) {
    return this.players.add(player);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removePlayer(UUID uniqueId) {
    return this.players.removeIf(predicatePlayer ->
            predicatePlayer
                    .getGameProfile()
                    .getUniqueId()
                    .equals(uniqueId)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<AbstractClientPlayerEntity> getPlayers() {
    return this.players;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPlayerCount() {
    return this.players.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }

  @Override
  public Map<Integer, Entity> getEntities() {
    return null;
  }
}
