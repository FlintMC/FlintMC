package net.labyfy.internal.component.entity.v1_15_2.type;

import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.mojang.datafixers.DataFixUtils;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;

@Implement(value = EntityType.Builder.class, version = "1.15.2")
public class VersionedEntityTypeBuilder<T extends Entity> implements EntityType.Builder<T> {

  private final Entity.Classification classification;
  private final EntitySize.Factory entitySizeFactory;
  private final EntityType.Factory entityTypeFactory;
  private boolean serializable;
  private boolean summonable;
  private boolean immuneToFire;
  private boolean canSpawnFarFromPlayer;
  private EntitySize size;

  @AssistedInject
  private VersionedEntityTypeBuilder(
          @Assisted("classification") Entity.Classification classification,
          @Assisted("entityTypeFactory") EntityType.Factory entityTypeFactory,
          @Assisted("entitySizeFactory") EntitySize.Factory entitySizeFactory
  ) {
    this.classification = classification;
    this.entitySizeFactory = entitySizeFactory;
    this.entityTypeFactory = entityTypeFactory;
    this.serializable = true;
    this.summonable = true;
    this.size = entitySizeFactory.create(0.6F, 1.8F, false);
  }

  @Override
  public EntityType.Builder<T> size(float width, float height) {
    this.size = this.entitySizeFactory.create(width, height, false);
    return this;
  }

  @Override
  public EntityType.Builder<T> disableSummoning() {
    this.summonable = false;
    return this;
  }

  @Override
  public EntityType.Builder<T> disableSerialization() {
    this.serializable = false;
    return this;
  }

  @Override
  public EntityType.Builder<T> immuneToFire() {
    this.immuneToFire = true;
    return this;
  }

  @Override
  public EntityType.Builder<T> canSpawnFarFromPlayer() {
    this.canSpawnFarFromPlayer = true;
    return this;
  }

  @Override
  public EntityType<T> build(String id) {
    if (this.serializable) {
      try {
        DataFixesManager.getDataFixer().getSchema(
                DataFixUtils.makeKey(
                        SharedConstants.getVersion().getWorldVersion()
                )
        ).getChoiceType(TypeReferences.ENTITY_TYPE, id);
      } catch (IllegalStateException exception) {
        if (SharedConstants.developmentMode) {
          throw exception;
        }
      }
    }

    return this.entityTypeFactory.create(
            this.classification,
            this.serializable,
            this.summonable,
            this.immuneToFire,
            this.canSpawnFarFromPlayer,
            this.size
    );
  }
}
