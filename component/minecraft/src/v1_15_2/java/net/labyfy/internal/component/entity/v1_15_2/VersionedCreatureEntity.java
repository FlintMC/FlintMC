package net.labyfy.internal.component.entity.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.CreatureEntity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.ai.EntitySenses;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.world.World;
import net.labyfy.component.world.util.BlockPosition;
import net.minecraft.util.math.BlockPos;

/**
 * 1.15.2 implementation of the {@link CreatureEntity}.
 */
@Implement(value = CreatureEntity.class, version = "1.15.2")
public class VersionedCreatureEntity extends VersionedMobEntity implements CreatureEntity {

  private final net.minecraft.entity.CreatureEntity creatureEntity;

  @AssistedInject
  public VersionedCreatureEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityMapper entityMapper,
          NBTMapper nbtMapper,
          EntitySenses.Factory entitySensesFactory
  ) {
    super(entity, entityType, world, entityMapper, nbtMapper, entitySensesFactory);


    if (!(entity instanceof net.minecraft.entity.CreatureEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.CreatureEntity.class.getName());
    }

    this.creatureEntity = (net.minecraft.entity.CreatureEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getBlockPathWeight(BlockPosition position) {
    return this.creatureEntity.getBlockPathWeight(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPath() {
    return this.creatureEntity.hasPath();
  }
}
