package net.flintmc.mcapi.v1_15_2.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.AgeableEntity;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

/** 1.15.2 implementation of the {@link AgeableEntity}. */
@Implement(value = AgeableEntity.class, version = "1.15.2")
public class VersionedAgeableEntity extends VersionedCreatureEntity implements AgeableEntity {

  private final net.minecraft.entity.AgeableEntity ageableEntity;

  @AssistedInject
  public VersionedAgeableEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory) {
    super(entity, entityType, world, entityFoundationMapper, entitySensesFactory);

    if (!(entity instanceof net.minecraft.entity.AgeableEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.AgeableEntity.class.getName());
    }
    this.ageableEntity = (net.minecraft.entity.AgeableEntity) entity;
  }

  /** {@inheritDoc} */
  @Override
  public boolean processInteract(PlayerEntity entity, Hand hand) {
    return this.ageableEntity.processInteract(
        (net.minecraft.entity.player.PlayerEntity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftPlayerEntity(entity),
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public int getGrowingAge() {
    return this.ageableEntity.getGrowingAge();
  }

  /** {@inheritDoc} */
  @Override
  public void setGrowingAge(int age) {
    this.ageableEntity.setGrowingAge(age);
  }

  /** {@inheritDoc} */
  @Override
  public void ageUp(int growth, boolean updateForcedAge) {
    this.ageableEntity.ageUp(growth, updateForcedAge);
  }

  /** {@inheritDoc} */
  @Override
  public void addGrowth(int growth) {
    this.ageableEntity.addGrowth(growth);
  }

  /** {@inheritDoc} */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.ageableEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.ageableEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isChild() {
    return this.ageableEntity.isChild();
  }
}
