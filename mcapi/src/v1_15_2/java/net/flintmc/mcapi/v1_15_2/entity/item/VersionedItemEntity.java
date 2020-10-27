package net.flintmc.mcapi.v1_15_2.entity.item;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.v1_15_2.entity.VersionedEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

@Implement(value = ItemEntity.class, version = "1.15.2")
public class VersionedItemEntity extends VersionedEntity implements ItemEntity {

  private final net.minecraft.entity.item.ItemEntity itemEntity;

  @AssistedInject
  private VersionedItemEntity(
          @Assisted("entity") Object entity,
          EntityTypeRegister entityTypeRegister,
          World world,
          EntityFoundationMapper entityFoundationMapper) {
    super(entity, entityTypeRegister.getEntityType("item"), world, entityFoundationMapper);

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
          EntityFoundationMapper entityFoundationMapper,
          @Assisted("x") double x,
          @Assisted("y") double y,
          @Assisted("z") double z) {
    this(entity, entityTypeRegister, world, entityFoundationMapper);
    this.setPosition(x, y, z);
    this.setYaw(this.getRandom().nextFloat() * 360.0F);
    this.setMotion(this.getRandom().nextDouble() * 0.2D - 0.1D, 0.2D, this.getRandom().nextDouble() * 0.2D - 0.1D);
  }

  @AssistedInject
  private VersionedItemEntity(
          @Assisted("entity") Object entity,
          EntityTypeRegister entityTypeRegister,
          World world,
          EntityFoundationMapper entityFoundationMapper,
          @Assisted("x") double x,
          @Assisted("y") double y,
          @Assisted("z") double z,
          @Assisted("itemStack") ItemStack itemStack) {
    this(entity, entityTypeRegister, world, entityFoundationMapper, x, y, z);
    this.setItemStack(itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStack() {
    return this.getEntityFoundationMapper().getItemMapper().fromMinecraft(this.itemEntity.getItem());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setItemStack(ItemStack itemStack) {
    this.itemEntity.setItem((net.minecraft.item.ItemStack) this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
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
    this.itemEntity.readAdditional((CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.itemEntity.writeAdditional((CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getName() {
    return this.getEntityFoundationMapper().getComponentMapper().fromMinecraft(this.itemEntity.getName());
  }

}
