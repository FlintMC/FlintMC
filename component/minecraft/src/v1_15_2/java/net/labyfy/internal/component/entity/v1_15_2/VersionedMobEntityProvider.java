package net.labyfy.internal.component.entity.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.entity.MobEntity;
import net.labyfy.component.entity.type.EntityTypeMapper;
import net.labyfy.component.inject.implement.Implement;

/**
 * 1.15.2 implementation of the {@link MobEntity.Provider}.
 */
@Singleton
@Implement(value = MobEntity.Provider.class, version = "1.15.2")
public class VersionedMobEntityProvider implements MobEntity.Provider {

  private final MobEntity.Factory mobEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedMobEntityProvider(MobEntity.Factory mobEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.mobEntityFactory = mobEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MobEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.MobEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.MobEntity.class.getName());
    }
    net.minecraft.entity.MobEntity mobEntity = (net.minecraft.entity.MobEntity) entity;


    return this.mobEntityFactory.create(
            mobEntity,
            this.entityTypeMapper.fromMinecraftEntityType(mobEntity.getType())
    );
  }
}
