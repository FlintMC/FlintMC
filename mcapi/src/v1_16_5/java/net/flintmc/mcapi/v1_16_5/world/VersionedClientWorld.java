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

package net.flintmc.mcapi.v1_16_5.world;

import com.beust.jcommander.internal.Sets;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityDespawnEvent;
import net.flintmc.mcapi.entity.event.EntitySpawnEvent;
import net.flintmc.mcapi.player.ClientPlayer;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.world.ClientWorld;
import net.flintmc.mcapi.world.block.Block;
import net.flintmc.mcapi.world.block.BlockState;
import net.flintmc.mcapi.world.border.WorldBorder;
import net.flintmc.mcapi.world.math.BlockPosition.Factory;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.flintmc.mcapi.world.type.difficulty.DifficultyLocal;
import net.minecraft.client.Minecraft;

/**
 * 1.16.5 implementation of {@link ClientWorld}
 */
@Singleton
@Implement(value = ClientWorld.class, version = "1.16.5")
public class VersionedClientWorld extends VersionedWorld implements ClientWorld {

  private final ClientPlayer player;
  private final Set<PlayerEntity> players;
  private final Map<Integer, Entity> entities;
  private final Scoreboard scoreboard;

  @Inject
  public VersionedClientWorld(
      Factory blockPositionFactory,
      BlockState.Factory stateFactory,
      Block.Factory blockFactory,
      DifficultyLocal.Factory difficultyLocalFactory,
      WorldBorder worldBorder,
      ClientPlayer player,
      Scoreboard scoreboard) {
    super(stateFactory, blockFactory, blockPositionFactory, difficultyLocalFactory, worldBorder, scoreboard);
    this.player = player;
    this.scoreboard = scoreboard;
    this.entities = Maps.newHashMap();
    this.players = Sets.newHashSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity getEntityByIdentifier(int identifier) {
    return this.entities.get(identifier);
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
  public boolean addPlayer(PlayerEntity player) {
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
  public Set<PlayerEntity> getPlayers() {
    return this.players;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPlayerCount() {
    return this.players.size();
  }

  @PreSubscribe
  public void entitySpawn(EntitySpawnEvent event) {
    Entity entity = event.getEntity();

    if (entity instanceof PlayerEntity) {
      PlayerEntity playerEntity = (PlayerEntity) entity;
      if (playerEntity.getUniqueId().equals(this.player.getUniqueId())) {
        this.addPlayer(this.player);
      } else {
        this.addPlayer(playerEntity);
      }
    }

    this.entities.put(event.getIdentifier(), entity);
  }

  @PreSubscribe
  public void entityDespawn(EntityDespawnEvent event) {
    Entity entity = event.getEntity();

    if (entity instanceof PlayerEntity) {
      PlayerEntity playerEntity = (PlayerEntity) entity;
      this.removePlayer(playerEntity.getUniqueId());
    }

    this.entities.remove(entity.getIdentifier());
  }
}
