package net.labyfy.internal.component.entity.v1_15_2.type;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.mojang.datafixers.DataFixUtils;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.entity.type.EntityTypeBuilder;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;

@Implement(value = EntityTypeBuilder.class, version = "1.15.2")
public class VersionedEntityTypeBuilder implements EntityTypeBuilder {

  private final Entity.Classification classification;
  private final EntitySize.Factory entitySizeFactory;
  private final EntityType.Factory entityTypeFactory;
  private final Entity.Factory entityFactory;
  private boolean serializable;
  private boolean summonable;
  private boolean immuneToFire;
  private boolean canSpawnFarFromPlayer;
  private EntitySize size;

  @AssistedInject
  private VersionedEntityTypeBuilder(
          @Assisted("entityFactory") Entity.Factory entityFactory,
          @Assisted("classification") Entity.Classification classification,
          @Assisted("entityTypeFactory") EntityType.Factory entityTypeFactory,
          @Assisted("entitySizeFactory") EntitySize.Factory entitySizeFactory
  ) {
    this.entityFactory = entityFactory;
    this.classification = classification;
    this.entitySizeFactory = entitySizeFactory;
    this.entityTypeFactory = entityTypeFactory;
    this.serializable = true;
    this.summonable = true;
    this.canSpawnFarFromPlayer = classification == Entity.Classification.CREATURE ||
            classification == Entity.Classification.MISC;
    this.size = entitySizeFactory.create(0.6F, 1.8F, false);
  }

  @Override
  public EntityTypeBuilder size(float width, float height) {
    this.size = this.entitySizeFactory.create(width, height, false);
    return this;
  }

  @Override
  public EntityTypeBuilder disableSummoning() {
    this.summonable = false;
    return this;
  }

  @Override
  public EntityTypeBuilder disableSerialization() {
    this.serializable = false;
    return this;
  }

  @Override
  public EntityTypeBuilder immuneToFire() {
    this.immuneToFire = true;
    return this;
  }

  @Override
  public EntityTypeBuilder canSpawnFarFromPlayer() {
    this.canSpawnFarFromPlayer = true;
    return this;
  }

  @Override
  public EntityType build(String id) {
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
            this.entityFactory,
            this.classification,
            this.serializable,
            this.summonable,
            this.immuneToFire,
            this.canSpawnFarFromPlayer,
            this.size
    );
  }
}
