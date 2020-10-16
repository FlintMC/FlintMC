package net.labyfy.internal.component.entity.v1_15_2.passive;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.ai.EntitySenses;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.passive.AmbientEntity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.world.World;
import net.labyfy.internal.component.entity.v1_15_2.VersionedMobEntity;

@Implement(value = AmbientEntity.class, version = "1.15.2")
public class VersionedAmbientEntity extends VersionedMobEntity implements AmbientEntity {

  @AssistedInject
  public VersionedAmbientEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityBaseMapper entityBaseMapper,
          NBTMapper nbtMapper,
          EntitySenses.Factory entitySensesFactory
  ) {
    super(entity, entityType, world, entityBaseMapper, nbtMapper, entitySensesFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeLeashedTo(PlayerEntity playerEntity) {
    return false;
  }
}
