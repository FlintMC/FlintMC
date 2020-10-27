package net.labyfy.internal.component.entity.v1_15_2.passive.farmanimal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.ai.EntitySenses;
import net.labyfy.component.entity.mapper.EntityFoundationMapper;
import net.labyfy.component.entity.passive.PassiveEntityMapper;
import net.labyfy.component.entity.passive.farmanimal.PigEntity;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.nbt.NBTCompound;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.world.World;
import net.labyfy.internal.component.entity.v1_15_2.passive.VersionedAnimalEntity;
import net.minecraft.nbt.CompoundNBT;

@Implement(value = PigEntity.class, version = "1.15.2")
public class VersionedPigEntity extends VersionedAnimalEntity implements PigEntity {

  private final net.minecraft.entity.passive.PigEntity pigEntity;

  @AssistedInject
  public VersionedPigEntity(
          @Assisted("entity") Object entity,
          EntityTypeRegister entityTypeRegister,
          World world,
          EntityFoundationMapper entityFoundationMapper,
          EntitySenses.Factory entitySensesFactory,
          PassiveEntityMapper passiveEntityMapper
  ) {
    super(entity, entityTypeRegister.getEntityType("pig"), world, entityFoundationMapper, entitySensesFactory, passiveEntityMapper);

    if (!(entity instanceof net.minecraft.entity.passive.PigEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.passive.PigEntity.class.getName());
    }
    this.pigEntity = (net.minecraft.entity.passive.PigEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSaddled() {
    return this.pigEntity.getSaddled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSaddled(boolean saddled) {
    this.pigEntity.setSaddled(saddled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean boost() {
    return this.pigEntity.boost();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean processInteract(PlayerEntity entity, Hand hand) {
    return this.pigEntity.processInteract(
            (net.minecraft.entity.player.PlayerEntity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftPlayerEntity(entity),
            (net.minecraft.util.Hand) this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBreedingItem(ItemStack breedingItem) {
    return this.pigEntity.isBreedingItem(
            (net.minecraft.item.ItemStack) this.getEntityFoundationMapper().getItemMapper().toMinecraft(breedingItem)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.pigEntity.readAdditional((CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.pigEntity.writeAdditional((CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeSteered() {
    return this.pigEntity.canBeSteered();
  }
}
