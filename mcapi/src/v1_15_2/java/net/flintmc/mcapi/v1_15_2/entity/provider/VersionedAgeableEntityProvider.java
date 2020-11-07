package net.flintmc.mcapi.v1_15_2.entity.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.AgeableEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

/** 1.15.2 implementation of the {@link AgeableEntity.Provider}. */
@Singleton
@Implement(value = AgeableEntity.Provider.class, version = "1.15.2")
public class VersionedAgeableEntityProvider implements AgeableEntity.Provider {

  private final AgeableEntity.Factory ageableEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedAgeableEntityProvider(
      AgeableEntity.Factory ageableEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.ageableEntityFactory = ageableEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /** {@inheritDoc} */
  @Override
  public AgeableEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.AgeableEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.AgeableEntity.class.getName());
    }
    net.minecraft.entity.AgeableEntity ageableEntity = (net.minecraft.entity.AgeableEntity) entity;

    return this.ageableEntityFactory.create(
        ageableEntity, this.entityTypeMapper.fromMinecraftEntityType(ageableEntity.getType()));
  }
}
