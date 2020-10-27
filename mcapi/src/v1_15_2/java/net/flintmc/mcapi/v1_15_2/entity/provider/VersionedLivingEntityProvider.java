package net.flintmc.mcapi.v1_15_2.entity.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;

@Singleton
@Implement(value = LivingEntity.Provider.class, version = "1.15.2")
public class VersionedLivingEntityProvider implements LivingEntity.Provider {

  private final LivingEntity.Factory livingEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedLivingEntityProvider(
      LivingEntity.Factory livingEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.livingEntityFactory = livingEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.LivingEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.LivingEntity.class.getName());
    }
    net.minecraft.entity.LivingEntity livingEntity = (net.minecraft.entity.LivingEntity) entity;

    return this.livingEntityFactory.create(
        livingEntity, this.entityTypeMapper.fromMinecraftEntityType(livingEntity.getType()));
  }
}
