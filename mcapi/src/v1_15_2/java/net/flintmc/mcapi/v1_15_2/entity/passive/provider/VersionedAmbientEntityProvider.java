package net.flintmc.mcapi.v1_15_2.entity.passive.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.mcapi.entity.passive.AmbientEntity;
import net.flintmc.mcapi.entity.type.EntityTypeMapper;
import net.flintmc.framework.inject.implement.Implement;

@Singleton
@Implement(value = AmbientEntity.Provider.class, version = "1.15.2")
public class VersionedAmbientEntityProvider implements AmbientEntity.Provider {

  private final AmbientEntity.Factory ambientEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedAmbientEntityProvider(AmbientEntity.Factory ambientEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.ambientEntityFactory = ambientEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AmbientEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.passive.AmbientEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.passive.AmbientEntity.class.getName());
    }
    net.minecraft.entity.passive.AmbientEntity ambientEntity = (net.minecraft.entity.passive.AmbientEntity) entity;

    return this.ambientEntityFactory.create(
            ambientEntity,
            this.entityTypeMapper.fromMinecraftEntityType(ambientEntity.getType())
    );
  }
}
