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

package net.flintmc.mcapi.v1_16_4.entity.type;

import javax.inject.Inject;
import javax.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.Entity.Classification;
import net.flintmc.mcapi.entity.EntitySize;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.minecraft.entity.EntityClassification;

/**
 * 1.16.4 implementation of the {@link EntityTypeMapper}.
 */
@Singleton
@Implement(value = EntityTypeMapper.class, version = "1.16.4")
public class VersionedEntityTypeMapper implements EntityTypeMapper {

  private final EntityType.Factory entityTypeFactory;
  private final EntitySize.Factory entitySizeFactory;

  @Inject
  private VersionedEntityTypeMapper(
      EntityType.Factory entityTypeFactory, EntitySize.Factory entitySizeFactory) {
    this.entityTypeFactory = entityTypeFactory;
    this.entitySizeFactory = entitySizeFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityType fromMinecraftEntityType(Object handle) {
    if (!(handle instanceof net.minecraft.entity.EntityType)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.EntityType.class.getName());
    }

    net.minecraft.entity.EntityType type = (net.minecraft.entity.EntityType) handle;

    return this.entityTypeFactory.create(
        this.fromMinecraftEntityClassification(type.getClassification()),
        type.isSerializable(),
        type.isSummonable(),
        type.isImmuneToFire(),
        type.func_225437_d(),
        this.fromMinecraftEntitySize(type.getSize()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEntityClassification(Entity.Classification classification) {
    switch (classification) {
      case MONSTER:
        return EntityClassification.MONSTER;
      case CREATURE:
        return EntityClassification.CREATURE;
      case AMBIENT:
        return EntityClassification.AMBIENT;
      case WATER_CREATURE:
        return EntityClassification.WATER_CREATURE;
      case WATER_AMBIENT:
        return EntityClassification.WATER_AMBIENT;
      default:
        return EntityClassification.MISC;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity.Classification fromMinecraftEntityClassification(Object handle) {
    if (!(handle instanceof EntityClassification)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + EntityClassification.class.getName());
    }

    EntityClassification entityClassification = (EntityClassification) handle;

    switch (entityClassification) {
      case MONSTER:
        return Entity.Classification.MONSTER;
      case CREATURE:
        return Entity.Classification.CREATURE;
      case AMBIENT:
        return Entity.Classification.AMBIENT;
      case WATER_CREATURE:
        return Entity.Classification.WATER_CREATURE;
      case WATER_AMBIENT:
        return Classification.WATER_AMBIENT;
      case MISC:
        return Entity.Classification.MISC;
      default:
        throw new IllegalStateException("Unexpected value: " + entityClassification);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEntitySize(EntitySize entitySize) {
    return new net.minecraft.entity.EntitySize(
        entitySize.getWidth(), entitySize.getHeight(), entitySize.isFixed());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySize fromMinecraftEntitySize(Object handle) {
    if (!(handle instanceof net.minecraft.entity.EntitySize)) {
      throw new IllegalArgumentException(
          handle.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.EntitySize.class.getName());
    }

    net.minecraft.entity.EntitySize entitySize = (net.minecraft.entity.EntitySize) handle;

    return this.entitySizeFactory.create(entitySize.width, entitySize.height, entitySize.fixed);
  }
}
