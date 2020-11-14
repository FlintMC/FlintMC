package net.flintmc.mcapi.v1_15_2.entity.item;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.v1_15_2.entity.VersionedEntity;
import net.flintmc.mcapi.world.World;
import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

@Implement(value = ItemEntity.class, version = "1.15.2")
public class VersionedItemEntity extends VersionedEntity<net.minecraft.entity.item.ItemEntity> implements ItemEntity {

  @AssistedInject
  private VersionedItemEntity(
      @Assisted("entity") Object entity,
      EntityTypeRegister entityTypeRegister,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityRenderContext.Factory entityRenderContextFactory
  ) {
    super(entity, entityTypeRegister.getEntityType("item"), world, entityFoundationMapper, entityRenderContextFactory);

    if (!(entity instanceof net.minecraft.entity.item.ItemEntity)) {
      throw new IllegalArgumentException("");
    }

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
      EntityRenderContext.Factory entityRenderContextFactory
  ) {
    this(entity, entityTypeRegister, world, entityFoundationMapper, entityRenderContextFactory);
    this.setPosition(x, y, z);
    this.setYaw(this.getRandom().nextFloat() * 360.0F);
    this.setMotion(
        this.getRandom().nextDouble() * 0.2D - 0.1D,
        0.2D,
        this.getRandom().nextDouble() * 0.2D - 0.1D);
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
      @Assisted("itemStack") ItemStack itemStack,
      EntityRenderContext.Factory entityRenderContextFactory
  ) {
    this(entity, entityTypeRegister, world, entityFoundationMapper, x, y, z, entityRenderContextFactory);
    this.setItemStack(itemStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.getHandle().getItem());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setItemStack(ItemStack itemStack) {
    this.getHandle().setItem(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getOwnerIdentifier() {
    return this.getHandle().getOwnerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setOwnerIdentifier(UUID ownerIdentifier) {
    this.getHandle().setOwnerId(ownerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getThrowerIdentifier() {
    return this.getHandle().getThrowerId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setThrowerIdentifier(UUID throwerIdentifier) {
    this.getHandle().setThrowerId(throwerIdentifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAge() {
    return this.getHandle().getAge();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDefaultPickupDelay() {
    this.getHandle().setDefaultPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoPickupDelay() {
    this.getHandle().setNoPickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInfinitePickupDelay() {
    this.getHandle().setInfinitePickupDelay();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPickupDelay(int ticks) {
    this.getHandle().setPickupDelay(ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean cannotPickup() {
    return this.getHandle().cannotPickup();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoDespawn() {
    this.getHandle().setNoDespawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void makeFakeItem() {
    this.getHandle().makeFakeItem();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.getHandle().readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.getHandle().writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getName() {
    return this.getEntityFoundationMapper()
        .getComponentMapper()
        .fromMinecraft(this.getHandle().getName());
  }
}
