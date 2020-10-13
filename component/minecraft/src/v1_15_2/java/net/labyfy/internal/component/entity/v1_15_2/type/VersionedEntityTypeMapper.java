package net.labyfy.internal.component.entity.v1_15_2.type;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.entity.type.EntityTypeMapper;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.entity.EntityClassification;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 1.15.2 implementation of the {@link EntityTypeMapper}.
 */
@Singleton
@Implement(value = EntityTypeMapper.class, version = "1.15.2")
public class VersionedEntityTypeMapper implements EntityTypeMapper {

  private final EntityType.Factory entityTypeFactory;
  private final EntitySize.Factory entitySizeFactory;

  @Inject
  private VersionedEntityTypeMapper(
          EntityTypeRegister entityTypeRegister,
          EntityType.Factory entityTypeFactory,
          EntitySize.Factory entitySizeFactory
  ) {
    this.entityTypeFactory = entityTypeFactory;
    this.entitySizeFactory = entitySizeFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraftEntityType(EntityType type) {
    // TODO: 13.10.2020 Implement
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityType fromMinecraftEntityType(Object handle) {
    if (!(handle instanceof net.minecraft.entity.EntityType)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.EntityType.class.getName());
    }

    net.minecraft.entity.EntityType type = (net.minecraft.entity.EntityType) handle;

    return this.entityTypeFactory.create(
            this.fromMinecraftEntityClassification(type.getClassification()),
            type.isSerializable(),
            type.isSummonable(),
            type.isImmuneToFire(),
            type.func_225437_d(),
            this.fromMinecraftEntitySize(type.getSize())
    );
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
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + EntityClassification.class.getName());
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
            entitySize.getWidth(),
            entitySize.getHeight(),
            entitySize.isFixed()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySize fromMinecraftEntitySize(Object handle) {
    if (!(handle instanceof net.minecraft.entity.EntitySize)) {
      throw new IllegalArgumentException(handle.getClass().getName() + " is not an instance of " + net.minecraft.entity.EntitySize.class.getName());
    }

    net.minecraft.entity.EntitySize entitySize = (net.minecraft.entity.EntitySize) handle;

    return this.entitySizeFactory.create(entitySize.width, entitySize.height, entitySize.fixed);
  }
}
