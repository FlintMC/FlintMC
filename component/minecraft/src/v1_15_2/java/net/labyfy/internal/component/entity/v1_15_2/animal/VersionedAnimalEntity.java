package net.labyfy.internal.component.entity.v1_15_2.animal;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.ai.EntitySenses;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.passive.AnimalEntity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.world.World;
import net.labyfy.internal.component.entity.v1_15_2.VersionedAgeableEntity;

@Implement(value = AnimalEntity.class, version = "1.15.2")
public class VersionedAnimalEntity extends VersionedAgeableEntity implements AnimalEntity {

  private final net.minecraft.entity.passive.AnimalEntity animalEntity;

  @AssistedInject
  public VersionedAnimalEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          World world,
          EntityBaseMapper entityBaseMapper,
          NBTMapper nbtMapper,
          EntitySenses.Factory entitySensesFactory
  ) {
    super(entity, entityType, world, entityBaseMapper, nbtMapper, entitySensesFactory);
    this.animalEntity = (net.minecraft.entity.passive.AnimalEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBreedingItem(ItemStack breedingItem) {
    return this.animalEntity.isBreedingItem(
            (net.minecraft.item.ItemStack) this.getEntityBaseMapper().getItemMapper().toMinecraft(breedingItem)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBreed() {
    return this.animalEntity.canBreed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInLove() {
    return this.animalEntity.isInLove();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInLove(int ticks) {
    this.animalEntity.setInLove(ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInLove(PlayerEntity player) {
    this.animalEntity.setInLove(
            (net.minecraft.entity.player.PlayerEntity) this.getEntityBaseMapper().getEntityMapper().toMinecraftPlayerEntity(player)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetInLove() {
    this.animalEntity.resetInLove();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canMateWith(AnimalEntity entity) {
    return this.animalEntity.canMateWith(
            (net.minecraft.entity.passive.AnimalEntity) this.getEntityBaseMapper().getEntityMapper().toMinecraftAnimalEntity(entity)
    );
  }
}
