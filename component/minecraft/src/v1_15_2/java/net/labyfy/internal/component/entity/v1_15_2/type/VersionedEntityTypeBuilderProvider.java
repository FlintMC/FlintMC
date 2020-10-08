package net.labyfy.internal.component.entity.v1_15_2.type;

import com.google.inject.Inject;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.entity.type.EntityTypeBuilder;
import net.labyfy.component.inject.implement.Implement;

@Implement(value = EntityTypeBuilder.Provider.class, version = "1.15.2")
public class VersionedEntityTypeBuilderProvider implements EntityTypeBuilder.Provider {

  private final Entity.Factory entityFactory;
  private final EntityType.Factory entityTypeFactory;
  private final EntityTypeBuilder.Factory factory;
  private final EntitySize.Factory entitySizeFactory;

  @Inject
  private VersionedEntityTypeBuilderProvider(
          Entity.Factory entityFactory,
          EntityType.Factory entityTypeFactory,
          EntityTypeBuilder.Factory entityTypeBuilderFactory,
          EntitySize.Factory entitySizeFactory
  ) {
    this.entityFactory = entityFactory;
    this.entityTypeFactory = entityTypeFactory;
    this.factory = entityTypeBuilderFactory;
    this.entitySizeFactory = entitySizeFactory;
  }

  @Override
  public EntityTypeBuilder create(Entity.Classification classification) {
    return this.factory.create(
            this.entityFactory,
            classification,
            this.entityTypeFactory,
            this.entitySizeFactory
    );
  }
}
