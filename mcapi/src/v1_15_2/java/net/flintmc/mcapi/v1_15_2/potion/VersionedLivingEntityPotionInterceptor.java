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

package net.flintmc.mcapi.v1_15_2.potion;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Map;
import java.util.Optional;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent.State;
import net.flintmc.mcapi.potion.event.PotionUpdateEvent;
import net.flintmc.mcapi.potion.event.PotionUpdateEvent.Factory;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.flintmc.mcapi.world.ClientWorld;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;

@Singleton
public class VersionedLivingEntityPotionInterceptor {

  private static final String LIVING_ENTITY = "net.minecraft.entity.LivingEntity";

  private final ClientWorld clientWorld;
  private final PotionMapper potionMapper;
  private final EventBus eventBus;
  private final PotionUpdateEvent.Factory potionUpdateEventFactory;
  private final PotionStateUpdateEvent.Factory potionStateUpdateEventFactory;

  @Inject
  public VersionedLivingEntityPotionInterceptor(
      ClientWorld clientWorld,
      PotionMapper potionMapper,
      EventBus eventBus,
      Factory potionUpdateEventFactory,
      PotionStateUpdateEvent.Factory potionStateUpdateEventFactory) {
    this.clientWorld = clientWorld;
    this.potionMapper = potionMapper;
    this.eventBus = eventBus;
    this.potionUpdateEventFactory = potionUpdateEventFactory;
    this.potionStateUpdateEventFactory = potionStateUpdateEventFactory;
  }

  @Hook(
      executionTime = {ExecutionTime.AFTER, ExecutionTime.BEFORE},
      className = LIVING_ENTITY,
      methodName = "updatePotionEffects")
  public void hookUpdatePotionEffects(
      @Named("instance") Object owner, ExecutionTime executionTime) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity -> {
              // TODO: 5/19/2021 Server side potion update event
              if (RenderSystem.isOnRenderThread()) {
                PotionUpdateEvent potionUpdateEvent = this.potionUpdateEventFactory
                    .create(livingEntity);
                this.eventBus.fireEvent(potionUpdateEvent, executionTime);
              }
            });
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "onNewPotionEffect",
      parameters = {@Type(reference = EffectInstance.class)})
  public void hookOnNewPotionEffect(@Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity ->
                this.eventBus.fireEvent(
                    this.potionStateUpdateEventFactory.create(
                        livingEntity,
                        this.potionMapper.fromMinecraftEffectInstance(args[0]),
                        State.NEW),
                    Phase.PRE));
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "onChangedPotionEffect",
      parameters = {@Type(reference = EffectInstance.class), @Type(reference = boolean.class)})
  public void hookOnChangedPotionEffect(
      @Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity ->
                this.eventBus.fireEvent(
                    this.potionStateUpdateEventFactory.create(
                        livingEntity,
                        this.potionMapper.fromMinecraftEffectInstance(args[0]),
                        State.CHANGED),
                    Phase.PRE));
  }

  @Hook(
      executionTime = ExecutionTime.BEFORE,
      className = LIVING_ENTITY,
      methodName = "onFinishedPotionEffect",
      parameters = {@Type(reference = EffectInstance.class)})
  public void hookOnFinishedPotionEffect(
      @Named("instance") Object owner, @Named("args") Object[] args) {
    this.getLivingEntity((LivingEntity) owner)
        .ifPresent(
            livingEntity -> {
              this.eventBus.fireEvent(
                  this.potionStateUpdateEventFactory.create(
                      livingEntity,
                      this.potionMapper.fromMinecraftEffectInstance(args[0]),
                      State.FINISHED),
                  Phase.PRE);
            });
  }

  private Optional<net.flintmc.mcapi.entity.LivingEntity> getLivingEntity(
      LivingEntity livingEntity) {
    Optional<net.flintmc.mcapi.entity.LivingEntity> optionalLivingEntity = Optional.empty();

    Entity entity = this.clientWorld.getEntityByIdentifier(livingEntity.getEntityId());
    if (entity == null) {
      return optionalLivingEntity;
    }

    if (entity instanceof net.flintmc.mcapi.entity.LivingEntity) {
      net.flintmc.mcapi.entity.LivingEntity flintLivingEntity =
          (net.flintmc.mcapi.entity.LivingEntity) entity;
      optionalLivingEntity = Optional.of(flintLivingEntity);
    }

    return optionalLivingEntity;
  }
}
