package net.labyfy.internal.component.entity.v1_15_2;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.AgeableEntity;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.ai.EntitySenses;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.nbt.NBTCompound;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.world.World;
import net.minecraft.nbt.CompoundNBT;

/**
 * 1.15.2 implementation of the {@link AgeableEntity}.
 */
@Implement(value = AgeableEntity.class, version = "1.15.2")
public class VersionedAgeableEntity extends VersionedCreatureEntity implements AgeableEntity {

  private final net.minecraft.entity.AgeableEntity ageableEntity;
  private final NBTMapper nbtMapper;

  @AssistedInject
  public VersionedAgeableEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityBaseMapper entityBaseMapper,
          NBTMapper nbtMapper,
          EntitySenses.Factory entitySensesFactory
  ) {
    super(entity, entityType, world, entityBaseMapper, nbtMapper, entitySensesFactory);

    if (!(entity instanceof net.minecraft.entity.AgeableEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.AgeableEntity.class.getName());
    }
    this.nbtMapper = nbtMapper;
    this.ageableEntity = (net.minecraft.entity.AgeableEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean processInteract(PlayerEntity entity, Hand hand) {
    return this.ageableEntity.processInteract(
            (net.minecraft.entity.player.PlayerEntity) this.getEntityBaseMapper().getEntityMapper().toMinecraftPlayerEntity(entity),
            (net.minecraft.util.Hand) this.getEntityBaseMapper().getHandMapper().toMinecraftHand(hand)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getGrowingAge() {
    return this.ageableEntity.getGrowingAge();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGrowingAge(int age) {
    this.ageableEntity.setGrowingAge(age);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void ageUp(int growth, boolean updateForcedAge) {
    this.ageableEntity.ageUp(growth, updateForcedAge);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGrowth(int growth) {
    this.ageableEntity.addGrowth(growth);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.ageableEntity.readAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.ageableEntity.writeAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild() {
    return this.ageableEntity.isChild();
  }


}
