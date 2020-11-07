package net.flintmc.mcapi.v1_15_2.entity;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.Random;

@Implement(value = LivingEntity.class, version = "1.15.2")
public class VersionedLivingEntity extends VersionedEntity implements LivingEntity {

  private final net.minecraft.entity.LivingEntity livingEntity;

  @AssistedInject
  public VersionedLivingEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    super(entity, entityType, world, entityFoundationMapper);

    if (!(entity instanceof net.minecraft.entity.LivingEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.LivingEntity.class.getName());
    }

    this.livingEntity = (net.minecraft.entity.LivingEntity) entity;
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBreathUnderwater() {
    return this.livingEntity.canBreatheUnderwater();
  }

  /** {@inheritDoc} */
  @Override
  public float getSwimAnimation(float partialTicks) {
    return this.livingEntity.getSwimAnimation(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getRenderScale() {
    return this.livingEntity.getRenderScale();
  }

  /** {@inheritDoc} */
  @Override
  public Random getRandom() {
    return this.livingEntity.getRNG();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getRevengeTarget() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.livingEntity.getRevengeTarget());
  }

  /** {@inheritDoc} */
  @Override
  public void setRevengeTarget(LivingEntity entity) {
    this.livingEntity.setRevengeTarget(
        (net.minecraft.entity.LivingEntity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getRevengeTimer() {
    return this.livingEntity.getRevengeTimer();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getLastAttackedEntity() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.livingEntity.getRevengeTarget());
  }

  /** {@inheritDoc} */
  @Override
  public void setLastAttackedEntity(Entity entity) {
    this.livingEntity.setLastAttackedEntity(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getLastAttackedEntityTime() {
    return this.livingEntity.getLastAttackedEntityTime();
  }

  /** {@inheritDoc} */
  @Override
  public int getIdleTime() {
    return this.livingEntity.getIdleTime();
  }

  /** {@inheritDoc} */
  @Override
  public void setIdleTime(int idleTime) {
    this.livingEntity.setIdleTime(idleTime);
  }

  /** {@inheritDoc} */
  @Override
  public double getVisibilityMultiplier(Entity entity) {
    return this.livingEntity.getVisibilityMultiplier(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canAttack(LivingEntity entity) {
    return this.livingEntity.canAttack(
        (net.minecraft.entity.EntityType<?>)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean clearActivePotions() {
    return this.livingEntity.clearActivePotions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEntityUndead() {
    return this.livingEntity.isEntityUndead();
  }

  /** {@inheritDoc} */
  @Override
  public void heal(float health) {
    this.livingEntity.heal(health);
  }

  /** {@inheritDoc} */
  @Override
  public float getHealth() {
    return this.livingEntity.getHealth();
  }

  /** {@inheritDoc} */
  @Override
  public void setHealth(float health) {
    this.livingEntity.setHealth(health);
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityFoundationMapper()
        .getResourceLocationProvider()
        .get(
            this.livingEntity.getLootTableResourceLocation().getPath(),
            this.livingEntity.getLootTableResourceLocation().getNamespace());
  }

  /** {@inheritDoc} */
  @Override
  public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
    this.livingEntity.knockBack(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity),
        strength,
        xRatio,
        zRatio);
  }

  @Override
  public Sound getEatSound(ItemStack itemStack) {
    return this.getEntityFoundationMapper()
        .getSoundMapper()
        .fromMinecraftSoundEvent(
            this.livingEntity.getEatSound(
                (net.minecraft.item.ItemStack)
                    this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack)));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnLadder() {
    return this.livingEntity.isOnLadder();
  }

  /** {@inheritDoc} */
  @Override
  public int getTotalArmorValue() {
    return this.livingEntity.getTotalArmorValue();
  }

  /** {@inheritDoc} */
  @Override
  public float getMaxHealth() {
    return this.livingEntity.getMaxHealth();
  }

  /** {@inheritDoc} */
  @Override
  public int getArrowCountInEntity() {
    return this.livingEntity.getArrowCountInEntity();
  }

  /** {@inheritDoc} */
  @Override
  public void setArrowCountInEntity(int count) {
    this.livingEntity.setArrowCountInEntity(count);
  }

  /** {@inheritDoc} */
  @Override
  public int getBeeStingCount() {
    return this.livingEntity.getBeeStingCount();
  }

  @Override
  public void setBeeStingCount(int stingCount) {
    this.livingEntity.setBeeStingCount(stingCount);
  }

  /** {@inheritDoc} */
  @Override
  public void swingArm(Hand hand) {
    this.livingEntity.swingArm(
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void swing(Hand hand, boolean sendToAll) {
    this.livingEntity.swing(
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand),
        sendToAll);
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getHeldItem(Hand hand) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.livingEntity.getHeldItem(
                (net.minecraft.util.Hand)
                    this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand)));
  }

  /** {@inheritDoc} */
  @Override
  public void setHeldItem(Hand hand, ItemStack heldItem) {
    this.livingEntity.setHeldItem(
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand),
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(heldItem));
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasItemInSlot(EquipmentSlotType slotType) {
    return this.livingEntity.hasItemInSlot(
        (net.minecraft.inventory.EquipmentSlotType)
            this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.livingEntity.getItemStackFromSlot(
                (net.minecraft.inventory.EquipmentSlotType)
                    this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType)));
  }

  /** {@inheritDoc} */
  @Override
  public float getArmorCoverPercentage() {
    return this.livingEntity.getArmorCoverPercentage();
  }

  /** {@inheritDoc} */
  @Override
  public float getAIMoveSpeed() {
    return this.livingEntity.getAIMoveSpeed();
  }

  /** {@inheritDoc} */
  @Override
  public void setAIMoveSpeed(float speed) {
    this.livingEntity.setAIMoveSpeed(speed);
  }

  /** {@inheritDoc} */
  @Override
  public void applyEntityCollision(Entity entity) {
    this.livingEntity.applyEntityCollision(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw(float partialTicks) {
    return this.livingEntity.getYaw(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public void attackEntityAsMob(Entity entity) {
    this.livingEntity.attackEntityAsMob(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void setSprinting(boolean sprinting) {
    this.livingEntity.setSprinting(sprinting);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActuallySwimming() {
    return this.livingEntity.isActualySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void startSpinAttack(int duration) {
    this.livingEntity.startSpinAttack(duration);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSpinAttacking() {
    return this.livingEntity.isSpinAttacking();
  }

  /** {@inheritDoc} */
  @Override
  public void setJumping(boolean jumping) {
    this.livingEntity.setJumping(jumping);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canEntityBeSeen(Entity entity) {
    return this.livingEntity.canEntityBeSeen(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getSwingProgress(float partialTicks) {
    return this.livingEntity.getSwingProgress(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isServerWorld() {
    return this.livingEntity.isServerWorld();
  }

  /** {@inheritDoc} */
  @Override
  public float getAbsorptionAmount() {
    return this.livingEntity.getAbsorptionAmount();
  }

  /** {@inheritDoc} */
  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    this.livingEntity.setAbsorptionAmount(absorptionAmount);
  }

  /** {@inheritDoc} */
  @Override
  public void sendEnterCombat() {
    this.livingEntity.sendEnterCombat();
  }

  /** {@inheritDoc} */
  @Override
  public void sendEndCombat() {
    this.livingEntity.sendEndCombat();
  }

  /** {@inheritDoc} */
  @Override
  public Hand.Side getPrimaryHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHandSide(this.livingEntity.getPrimaryHand());
  }

  /** {@inheritDoc} */
  @Override
  public Hand getActiveHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHand(this.livingEntity.getActiveHand());
  }

  /** {@inheritDoc} */
  @Override
  public void setActiveHand(Hand hand) {
    this.livingEntity.setActiveHand(
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getActiveItemStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.livingEntity.getActiveItemStack());
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseCount() {
    return this.livingEntity.getItemInUseCount();
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseMaxCount() {
    return this.livingEntity.getItemInUseMaxCount();
  }

  /** {@inheritDoc} */
  @Override
  public void stopActiveHand() {
    this.livingEntity.stopActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public void resetActiveHand() {
    this.livingEntity.resetActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActiveItemStackBlocking() {
    return this.livingEntity.isActiveItemStackBlocking();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSuppressingSlidingDownLadder() {
    return this.livingEntity.isSuppressingSlidingDownLadder();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isElytraFlying() {
    return this.livingEntity.isElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public int getTicksElytraFlying() {
    return this.livingEntity.getTicksElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public boolean attemptTeleport(double x, double y, double z, boolean particleEffects) {
    return this.livingEntity.attemptTeleport(x, y, z, particleEffects);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeHitWithPotion() {
    return this.livingEntity.canBeHitWithPotion();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeRiddenInWater() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public boolean attackable() {
    return this.livingEntity.attackable();
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveStrafing() {
    return this.livingEntity.moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveStrafing(float moveStrafing) {
    this.livingEntity.moveStrafing = moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveVertical() {
    return this.livingEntity.moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveVertical(float moveVertical) {
    this.livingEntity.moveVertical = moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveForward() {
    return this.livingEntity.moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveForward(float moveForward) {
    this.livingEntity.moveForward = moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setPartying(BlockPosition position, boolean isPartying) {
    this.livingEntity.setPartying(
        (BlockPos) this.getWorld().toMinecraftBlockPos(position), isPartying);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return this.livingEntity.canPickUpItem(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack));
  }

  /** {@inheritDoc} */
  @Override
  public Optional<BlockPosition> getBedPosition() {
    Optional<BlockPosition> optional = Optional.empty();

    if (this.livingEntity.getBedPosition().isPresent()) {
      optional =
          Optional.of(
              this.getWorld().fromMinecraftBlockPos(this.livingEntity.getBedPosition().get()));
    }

    return optional;
  }

  /** {@inheritDoc} */
  @Override
  public void setBedPosition(BlockPosition position) {
    this.livingEntity.setBedPosition((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void clearBedPosition() {
    this.livingEntity.clearBedPosition();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSleeping() {
    return this.livingEntity.isSleeping();
  }

  /** {@inheritDoc} */
  @Override
  public void startSleeping(BlockPosition position) {
    this.livingEntity.startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void wakeUp() {
    this.livingEntity.wakeUp();
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.livingEntity.findAmmo(
                (net.minecraft.item.ItemStack)
                    this.getEntityFoundationMapper().getItemMapper().toMinecraft(shootable)));
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(EquipmentSlotType slotType) {
    this.livingEntity.sendBreakAnimation(
        (net.minecraft.inventory.EquipmentSlotType)
            this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(Hand hand) {
    this.livingEntity.sendBreakAnimation(
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.livingEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.livingEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlive() {
    return this.livingEntity.isAlive();
  }
}
