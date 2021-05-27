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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mojang.blaze3d.systems.RenderSystem;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.event.PotionAddEvent.Factory;
import net.flintmc.mcapi.potion.event.PotionRemoveEvent;
import net.flintmc.mcapi.potion.mapper.PotionMapper;
import net.flintmc.mcapi.server.event.PacketEvent;
import net.flintmc.mcapi.server.event.PacketEvent.ProtocolPhase;
import net.flintmc.mcapi.world.ClientWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SRemoveEntityEffectPacket;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

@Singleton
public class VersionedPotionListener {

  private final ClientWorld world;
  private final EventBus eventBus;
  private final PotionMapper potionMapper;
  private final Factory potionAddEventFactory;
  private final PotionRemoveEvent.Factory potionRemoveEventFactory;

  @Inject
  private VersionedPotionListener(
      final ClientWorld world,
      final EventBus eventBus,
      final PotionMapper potionMapper,
      final Factory potionAddEventFactory,
      final PotionRemoveEvent.Factory potionRemoveEventFactory) {
    this.world = world;
    this.eventBus = eventBus;
    this.potionMapper = potionMapper;
    this.potionAddEventFactory = potionAddEventFactory;
    this.potionRemoveEventFactory = potionRemoveEventFactory;
  }

  @PostSubscribe
  public void addOrUpdateEntityEffect(PacketEvent event) {
    ProtocolPhase phase = event.getPhase();

    if (phase != ProtocolPhase.PLAY || !(event.getPacket() instanceof SPlayEntityEffectPacket)) {
      return;
    }

    SPlayEntityEffectPacket packet = (SPlayEntityEffectPacket) event.getPacket();
    LivingEntity livingEntity = this.getLivingEntity(packet.getEntityId());

    if (livingEntity == null) {
      return;
    }

    Effect effect = Effect.get(packet.getEffectId());

    if (effect == null) {
      return;
    }

    EffectInstance effectInstance = new EffectInstance(
        effect,
        packet.getDuration(),
        packet.getAmplifier(),
        packet.getIsAmbient(),
        packet.doesShowParticles(),
        packet.doesShowParticles()
    );
    effectInstance.setPotionDurationMax(packet.isMaxDuration());

    if (RenderSystem.isOnRenderThread()) {
      this.eventBus.fireEvent(this.potionAddEventFactory
              .create(livingEntity, this.potionMapper.fromMinecraftEffectInstance(effectInstance)),
          Phase.PRE);
    }
  }

  @PostSubscribe
  public void removeEntityEffect(PacketEvent event) {
    ProtocolPhase phase = event.getPhase();

    if (phase != ProtocolPhase.PLAY || !(event.getPacket() instanceof SRemoveEntityEffectPacket)) {
      return;
    }

    SRemoveEntityEffectPacket packet = (SRemoveEntityEffectPacket) event.getPacket();

    net.minecraft.entity.Entity entity = packet.getEntity(Minecraft.getInstance().world);
    LivingEntity livingEntity = this.getLivingEntity(entity.getEntityId());

    if (livingEntity == null) {
      return;
    }

    if (RenderSystem.isOnRenderThread()) {
      this.eventBus.fireEvent(this.potionRemoveEventFactory
              .create(livingEntity, this.potionMapper.fromMinecraftEffect(packet.getPotion())),
          Phase.PRE);
    }
  }

  private LivingEntity getLivingEntity(final int entityId) {
    Entity entity = this.world.getEntityByIdentifier(entityId);
    return !(entity instanceof LivingEntity) ? null : (LivingEntity) entity;
  }

}
