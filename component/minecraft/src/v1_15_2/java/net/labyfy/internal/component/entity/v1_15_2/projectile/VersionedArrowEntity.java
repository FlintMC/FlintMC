package net.labyfy.internal.component.entity.v1_15_2.projectile;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.projectile.ArrowEntity;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.nbt.NBTCompound;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.world.World;
import net.minecraft.nbt.CompoundNBT;

@Implement(ArrowEntity.class)
public class VersionedArrowEntity extends VersionedArrowBaseEntity implements ArrowEntity {

  private final net.minecraft.entity.projectile.ArrowEntity arrowEntity;

  @AssistedInject
  public VersionedArrowEntity(
          @Assisted("entity") Object entity,
          World world,
          EntityBaseMapper entityBaseMapper,
          EntityTypeRegister entityTypeRegister,
          NBTMapper nbtMapper
  ) {
    super(entity, world, entityBaseMapper, entityTypeRegister, nbtMapper);

    if (!(entity instanceof net.minecraft.entity.projectile.ArrowEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.projectile.ArrowEntity.class.getName());
    }

    this.arrowEntity = (net.minecraft.entity.projectile.ArrowEntity) entity;
  }

  @AssistedInject
  public VersionedArrowEntity(
          @Assisted("entity") Object entity,
          @Assisted("x") double x,
          @Assisted("y") double y,
          @Assisted("z") double z,
          World world,
          EntityBaseMapper entityBaseMapper,
          EntityTypeRegister entityTypeRegister,
          NBTMapper nbtMapper
  ) {
    super(entity, x, y, z, world, entityBaseMapper, entityTypeRegister, nbtMapper);

    if (!(entity instanceof net.minecraft.entity.projectile.ArrowEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.projectile.ArrowEntity.class.getName());
    }

    this.arrowEntity = (net.minecraft.entity.projectile.ArrowEntity) entity;
  }

  @AssistedInject
  public VersionedArrowEntity(
          @Assisted("entity") Object entity,
          @Assisted("shooter") LivingEntity shooter,
          World world,
          EntityBaseMapper entityBaseMapper,
          EntityTypeRegister entityTypeRegister,
          NBTMapper nbtMapper
  ) {
    super(entity, shooter, world, entityBaseMapper, entityTypeRegister, nbtMapper);

    if (!(entity instanceof net.minecraft.entity.projectile.ArrowEntity)) {
      throw new IllegalArgumentException(entity.getClass().getName() + " is not an instance of " + net.minecraft.entity.projectile.ArrowEntity.class.getName());
    }

    this.arrowEntity = (net.minecraft.entity.projectile.ArrowEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPotionEffect(ItemStack itemStack) {
    this.arrowEntity.setPotionEffect(
            (net.minecraft.item.ItemStack) this.getEntityBaseMapper().getItemMapper().toMinecraft(itemStack)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getColor() {
    return this.arrowEntity.getColor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.arrowEntity.readAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.arrowEntity.writeAdditional((CompoundNBT) this.nbtMapper.fromMinecraftNBT(compound));
  }

}
