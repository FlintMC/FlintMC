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

package net.flintmc.mcapi.v1_16_5.entity.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityDespawnEvent;
import net.flintmc.mcapi.entity.event.EntitySpawnEvent;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;

@Singleton
public class VersionedEntitySpawnEventInjector {

  private final EntityMapper entityMapper;
  private final EventBus eventBus;

  private final EntitySpawnEvent.Factory spawnEventFactory;
  private final EntityDespawnEvent.Factory despawnEventFactory;

  @Inject
  private VersionedEntitySpawnEventInjector(
      EntityMapper entityMapper,
      EventBus eventBus,
      EntitySpawnEvent.Factory spawnEventFactory,
      EntityDespawnEvent.Factory despawnEventFactory) {
    this.entityMapper = entityMapper;
    this.eventBus = eventBus;
    this.spawnEventFactory = spawnEventFactory;
    this.despawnEventFactory = despawnEventFactory;
  }

  @Hook(
      className = "net.minecraft.client.world.ClientWorld",
      methodName = "addEntityImpl",
      parameters = {@Type(reference = int.class), @Type(typeName = "net.minecraft.entity.Entity")},
      executionTime = ExecutionTime.BEFORE)
  public void addEntity(@Named("args") Object[] args) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(args[1]);
    if (entity == null) {
      return;
    }

    int entityIdentifier = (int) args[0];

    EntitySpawnEvent event = this.spawnEventFactory.create(entityIdentifier, entity);
    this.eventBus.fireEvent(event, Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.entity.Entity",
      methodName = "remove",
      executionTime = ExecutionTime.BEFORE)
  public void remove(@Named("instance") Object instance) {
    Entity entity = this.entityMapper.fromAnyMinecraftEntity(instance);
    if (entity == null) {
      return;
    }

    EntityDespawnEvent event = this.despawnEventFactory.create(entity);
    this.eventBus.fireEvent(event, Phase.PRE);
  }
}
