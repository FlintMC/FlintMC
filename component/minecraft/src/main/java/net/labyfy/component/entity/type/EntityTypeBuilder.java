package net.labyfy.component.entity.type;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.inject.assisted.AssistedFactory;

/**
 * Represents a builder to built entity types.
 */
public interface EntityTypeBuilder {

  EntityTypeBuilder size(float width, float height);

  EntityTypeBuilder disableSummoning();

  EntityTypeBuilder disableSerialization();

  EntityTypeBuilder immuneToFire();

  EntityTypeBuilder canSpawnFarFromPlayer();

  EntityType build(String id);

  /**
   * A factory class for {@link EntityTypeBuilder}.
   */
  @AssistedFactory(EntityTypeBuilder.class)
  interface Factory {

    EntityTypeBuilder create(
            @Assisted("entityFactory") Entity.Factory entityFactory,
            @Assisted("classification") Entity.Classification classification,
            @Assisted("entityTypeFactory") EntityType.Factory entityTypeFactory,
            @Assisted("entitySizeFactory") EntitySize.Factory entitySizeFactory
    );

  }

  /**
   * A provider for the {@link EntityTypeBuilder}.
   */
  interface Provider {

    EntityTypeBuilder create(Entity.Classification classification);

  }

}
