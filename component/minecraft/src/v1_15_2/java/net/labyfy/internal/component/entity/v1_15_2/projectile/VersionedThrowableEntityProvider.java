package net.labyfy.internal.component.entity.v1_15_2.projectile;

import com.google.inject.Inject;
import net.labyfy.component.entity.projectile.ThrowableEntity;
import net.labyfy.component.entity.type.EntityTypeMapper;
import net.labyfy.component.inject.implement.Implement;

@Implement(value = ThrowableEntity.Provider.class, version = "1.15.2")
public class VersionedThrowableEntityProvider implements ThrowableEntity.Provider {

  private final ThrowableEntity.Factory throwableEntityFactory;
  private final EntityTypeMapper entityTypeMapper;

  @Inject
  private VersionedThrowableEntityProvider(ThrowableEntity.Factory throwableEntityFactory, EntityTypeMapper entityTypeMapper) {
    this.throwableEntityFactory = throwableEntityFactory;
    this.entityTypeMapper = entityTypeMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ThrowableEntity get(Object entity) {
    if (!(entity instanceof net.minecraft.entity.projectile.ThrowableEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.projectile.ThrowableEntity.class.getName());
    }
    net.minecraft.entity.projectile.ThrowableEntity throwableEntity = (net.minecraft.entity.projectile.ThrowableEntity) entity;

    return this.throwableEntityFactory.create(
            throwableEntity,
            this.entityTypeMapper.fromMinecraftEntityType(throwableEntity.getType())
    );
  }
}
