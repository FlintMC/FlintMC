package net.labyfy.internal.component.entity.v1_15_2;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.world.ClientWorld;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Implement(value = Entity.Provider.class, version = "1.15.2")
public class VersionedEntityProvider implements Entity.Provider {

  private final Entity.Factory entityFactory;
  private final ClientWorld world;
  private final EntityMapper entityMapper;


  @Inject
  private VersionedEntityProvider(Entity.Factory entityFactory, ClientWorld world, EntityMapper entityMapper) {
    this.entityFactory = entityFactory;
    this.world = world;
    this.entityMapper = entityMapper;
  }

  @Override
  public Entity get(Object entity, EntityType entityType) {
    return this.entityFactory.create(
            entity,
            entityType,
            this.world,
            this.entityMapper
    );
  }
}
