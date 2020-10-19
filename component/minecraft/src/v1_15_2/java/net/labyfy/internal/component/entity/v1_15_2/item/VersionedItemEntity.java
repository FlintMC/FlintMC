package net.labyfy.internal.component.entity.v1_15_2.item;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.item.ItemEntity;
import net.labyfy.component.entity.mapper.EntityBaseMapper;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.nbt.NBTCompound;
import net.labyfy.component.nbt.mapper.NBTMapper;
import net.labyfy.component.world.World;
import net.labyfy.internal.component.entity.v1_15_2.VersionedEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

@Implement(value = ItemEntity.class, version = "1.15.2")
public class VersionedItemEntity extends VersionedEntity implements ItemEntity {

  private final net.minecraft.entity.item.ItemEntity itemEntity;
  private final NBTMapper nbtMapper;

  @AssistedInject
  private VersionedItemEntity(
          @Assisted("entity") Object entity,
          EntityTypeRegister entityTypeRegister,
          World world,
          EntityBaseMapper entityBaseMapper,
          NBTMapper nbtMapper) {
    super(entity, entityTypeRegister.getEntityType("item"), world, entityBaseMapper);
    this.nbtMapper = nbtMapper;


    if (!(entity instanceof net.minecraft.entity.item.ItemEntity)) {
      throw new IllegalArgumentException("");
    }

    this.itemEntity = (net.minecraft.entity.item.ItemEntity) entity;
  }

  @AssistedInject
  private VersionedItemEntity(
          @Assisted("entity") Object entity,
          EntityTypeRegister entityTypeRegister,
          World world,
          EntityBaseMapper entityBaseMapper,
          @Assisted("x") double x,
          @Assisted("y") double y,
          @Assisted("z") double z,
          NBTMapper nbtMapper) {
    this(entity, entityTypeRegister, world, entityBaseMapper, nbtMapper);
    this.setPosition(x, y, z);
    this.setYaw(this.getRandom().nextFloat() * 360.0F);
    this.setMotion(this.getRandom().nextDouble() * 0.2D - 0.1D, 0.2D, this.getRandom().nextDouble() * 0.2D - 0.1D);
  }

  @AssistedInject
  private VersionedItemEntity(
          @Assisted("entity") Object entity,
          EntityTypeRegister entityTypeRegister,
          World world,
          EntityBaseMapper entityBaseMapper,
          @Assisted("x") double x,
          @Assisted("y") double y,
          @Assisted("z") double z,
          @Assisted("itemStack") ItemStack itemStack,
          NBTMapper nbtMapper) {
    this(entity, entityTypeRegister, world, entityBaseMapper, x, y, z, nbtMapper);
    this.setItemStack(itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStack() {
    return this.getEntityBaseMapper().getItemMapper().fromMinecraft(this.itemEntity.getItem());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setItemStack(ItemStack itemStack) {
    this.itemEntity.setItem((net.minecraft.item.ItemStack) this.getEntityBaseMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getOwnerIdentifier() {
    return this.itemEntity.getOwnerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOwnerIdentifier(UUID ownerIdentifier) {
    this.itemEntity.setOwnerId(ownerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getThrowerIdentifier() {
    return this.itemEntity.getThrowerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setThrowerIdentifier(UUID throwerIdentifier) {
    this.itemEntity.setThrowerId(throwerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAge() {
    return this.itemEntity.getAge();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDefaultPickupDelay() {
    this.itemEntity.setDefaultPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoPickupDelay() {
    this.itemEntity.setNoPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInfinitePickupDelay() {
    this.itemEntity.setInfinitePickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPickupDelay(int ticks) {
    this.itemEntity.setPickupDelay(ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean cannotPickup() {
    return this.itemEntity.cannotPickup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoDespawn() {
    this.itemEntity.setNoDespawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void makeFakeItem() {
    this.itemEntity.makeFakeItem();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.itemEntity.readAdditional((CompoundNBT) this.nbtMapper.toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.itemEntity.writeAdditional((CompoundNBT) this.nbtMapper.toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getName() {
    return this.getEntityBaseMapper().getComponentMapper().fromMinecraft(this.itemEntity.getName());
  }

}
