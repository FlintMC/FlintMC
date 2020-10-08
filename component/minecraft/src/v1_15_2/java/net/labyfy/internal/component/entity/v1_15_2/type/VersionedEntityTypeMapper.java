package net.labyfy.internal.component.entity.v1_15_2.type;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.entity.type.EntityTypeMapper;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.World;
import net.minecraft.entity.EntityClassification;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implement(value = EntityTypeMapper.class, version = "1.15.2")
public class VersionedEntityTypeMapper implements EntityTypeMapper {

  private final Entity.Factory entityFactory;
  private final EntityType.Factory entityTypeFactory;
  private final EntitySize.Factory entitySizeFactory;

  @Inject
  private VersionedEntityTypeMapper(Entity.Factory entityFactory, EntityType.Factory entityTypeFactory, EntitySize.Factory entitySizeFactory) {
    this.entityFactory = entityFactory;
    this.entityTypeFactory = entityTypeFactory;
    this.entitySizeFactory = entitySizeFactory;
  }

  @Override
  public Object toMinecraftEntityType(EntityType type) {
    return null;
  }

  @Override
  public EntityType fromMinecraftEntityType(Object object) {
    if (!(object instanceof net.minecraft.entity.EntityType)) {
      return null;
    }

    net.minecraft.entity.EntityType type = (net.minecraft.entity.EntityType) object;

    return this.entityTypeFactory.create(
            this.entityFactory,
            this.fromMinecraftEntityClassification(type.getClassification()),
            type.isSerializable(),
            type.isSummonable(),
            type.isImmuneToFire(),
            type.func_225437_d(),
            this.fromMinecraftEntitySize(type.getSize())
    );
  }

  @Override
  public Object toMinecraftEntityClassification(Entity.Classification classification) {
    return null;
  }

  @Override
  public Entity.Classification fromMinecraftEntityClassification(Object object) {
    if (!(object instanceof EntityClassification)) {
      return null;
    }

    EntityClassification entityClassification = (EntityClassification) object;

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

  @Override
  public Object toMinecraftEntitySize(EntitySize entitySize) {
    return null;
  }

  @Override
  public EntitySize fromMinecraftEntitySize(Object object) {
    if (!(object instanceof net.minecraft.entity.EntitySize)) {
      return null;
    }

    net.minecraft.entity.EntitySize entitySize = (net.minecraft.entity.EntitySize) object;

    return this.entitySizeFactory.create(entitySize.width, entitySize.height, entitySize.fixed);
  }
}
