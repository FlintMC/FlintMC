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

package net.flintmc.mcapi.v1_15_2.entity.type;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntitySize;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.entity.type.EntityTypeBuilder;

/**
 * 1.15.2 implementation of the {@link EntityTypeBuilder}.
 */
@Implement(value = EntityTypeBuilder.class, version = "1.15.2")
public class VersionedEntityTypeBuilder implements EntityTypeBuilder {

  private final Entity.Classification classification;
  private final EntitySize.Factory entitySizeFactory;
  private final EntityType.Factory entityTypeFactory;
  private ChatComponent displayName;
  private boolean serializable;
  private boolean summonable;
  private boolean immuneToFire;
  private boolean canSpawnFarFromPlayer;
  private EntitySize size;

  @AssistedInject
  private VersionedEntityTypeBuilder(
      @Assisted("classification") Entity.Classification classification,
      EntityType.Factory entityTypeFactory,
      EntitySize.Factory entitySizeFactory) {
    this.classification = classification;
    this.entitySizeFactory = entitySizeFactory;
    this.entityTypeFactory = entityTypeFactory;
    this.serializable = true;
    this.summonable = true;
    this.canSpawnFarFromPlayer =
        classification == Entity.Classification.CREATURE
            || classification == Entity.Classification.MISC;
    this.size = entitySizeFactory.create(0.6F, 1.8F, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityTypeBuilder displayName(ChatComponent displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityTypeBuilder size(float width, float height) {
    this.size = this.entitySizeFactory.create(width, height, false);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityTypeBuilder disableSummoning() {
    this.summonable = false;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityTypeBuilder disableSerialization() {
    this.serializable = false;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityTypeBuilder immuneToFire() {
    this.immuneToFire = true;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityTypeBuilder canSpawnFarFromPlayer() {
    this.canSpawnFarFromPlayer = true;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityType build(String id) {
    return this.entityTypeFactory.create(
        this.displayName,
        this.classification,
        this.serializable,
        this.summonable,
        this.immuneToFire,
        this.canSpawnFarFromPlayer,
        this.size);
  }
}
