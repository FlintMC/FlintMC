package net.flintmc.mcapi.v1_15_2.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
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
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

@Implement(value = LivingEntity.class, version = "1.15.2")
public class VersionedLivingEntity extends VersionedEntity implements LivingEntity {

  private final Map<StatusEffect, StatusEffectInstance> activePotions;

  @AssistedInject
  public VersionedLivingEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    super(entity, entityType, world, entityFoundationMapper);
    this.activePotions = new HashMap<>();
  }

  protected VersionedLivingEntity(
      Supplier<Object> entitySupplier,
      EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    super(entitySupplier, entityType, world, entityFoundationMapper);
    this.activePotions = new HashMap<>();
  }

  @Override
  protected net.minecraft.entity.LivingEntity wrapped() {
    net.minecraft.entity.Entity entity = super.wrapped();

    if (!(entity instanceof net.minecraft.entity.LivingEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.LivingEntity.class.getName());
    }

    return (net.minecraft.entity.LivingEntity) entity;
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBreathUnderwater() {
    return this.wrapped().canBreatheUnderwater();
  }

  /** {@inheritDoc} */
  @Override
  public float getSwimAnimation(float partialTicks) {
    return this.wrapped().getSwimAnimation(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getRenderScale() {
    return this.wrapped().getRenderScale();
  }

  /** {@inheritDoc} */
  @Override
  public Random getRandom() {
    return this.wrapped().getRNG();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getRevengeTarget() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.wrapped().getRevengeTarget());
  }

  /** {@inheritDoc} */
  @Override
  public void setRevengeTarget(LivingEntity entity) {
    this.wrapped()
        .setRevengeTarget(
            (net.minecraft.entity.LivingEntity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getRevengeTimer() {
    return this.wrapped().getRevengeTimer();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getLastAttackedEntity() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.wrapped().getRevengeTarget());
  }

  /** {@inheritDoc} */
  @Override
  public void setLastAttackedEntity(Entity entity) {
    this.wrapped()
        .setLastAttackedEntity(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getLastAttackedEntityTime() {
    return this.wrapped().getLastAttackedEntityTime();
  }

  /** {@inheritDoc} */
  @Override
  public int getIdleTime() {
    return this.wrapped().getIdleTime();
  }

  /** {@inheritDoc} */
  @Override
  public void setIdleTime(int idleTime) {
    this.wrapped().setIdleTime(idleTime);
  }

  /** {@inheritDoc} */
  @Override
  public double getVisibilityMultiplier(Entity entity) {
    return this.wrapped()
        .getVisibilityMultiplier(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canAttack(LivingEntity entity) {
    return this.wrapped()
        .canAttack(
            (net.minecraft.entity.EntityType<?>)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean clearActivePotions() {
    return this.wrapped().clearActivePotions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEntityUndead() {
    return this.wrapped().isEntityUndead();
  }

  /** {@inheritDoc} */
  @Override
  public void heal(float health) {
    this.wrapped().heal(health);
  }

  /** {@inheritDoc} */
  @Override
  public float getHealth() {
    return this.wrapped().getHealth();
  }

  /** {@inheritDoc} */
  @Override
  public void setHealth(float health) {
    this.wrapped().setHealth(health);
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityFoundationMapper()
        .getResourceLocationProvider()
        .get(
            this.wrapped().getLootTableResourceLocation().getPath(),
            this.wrapped().getLootTableResourceLocation().getNamespace());
  }

  /** {@inheritDoc} */
  @Override
  public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
    this.wrapped()
        .knockBack(
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
            this.wrapped()
                .getEatSound(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack)));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnLadder() {
    return this.wrapped().isOnLadder();
  }

  /** {@inheritDoc} */
  @Override
  public int getTotalArmorValue() {
    return this.wrapped().getTotalArmorValue();
  }

  /** {@inheritDoc} */
  @Override
  public float getMaxHealth() {
    return this.wrapped().getMaxHealth();
  }

  /** {@inheritDoc} */
  @Override
  public int getArrowCountInEntity() {
    return this.wrapped().getArrowCountInEntity();
  }

  /** {@inheritDoc} */
  @Override
  public void setArrowCountInEntity(int count) {
    this.wrapped().setArrowCountInEntity(count);
  }

  /** {@inheritDoc} */
  @Override
  public int getBeeStingCount() {
    return this.wrapped().getBeeStingCount();
  }

  @Override
  public void setBeeStingCount(int stingCount) {
    this.wrapped().setBeeStingCount(stingCount);
  }

  /** {@inheritDoc} */
  @Override
  public void swingArm(Hand hand) {
    this.wrapped()
        .swingArm(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void swing(Hand hand, boolean sendToAll) {
    this.wrapped()
        .swing(
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
            this.wrapped()
                .getHeldItem(
                    (net.minecraft.util.Hand)
                        this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand)));
  }

  /** {@inheritDoc} */
  @Override
  public void setHeldItem(Hand hand, ItemStack heldItem) {
    this.wrapped()
        .setHeldItem(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand),
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(heldItem));
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasItemInSlot(EquipmentSlotType slotType) {
    return this.wrapped()
        .hasItemInSlot(
            (net.minecraft.inventory.EquipmentSlotType)
                this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.wrapped()
                .getItemStackFromSlot(
                    (net.minecraft.inventory.EquipmentSlotType)
                        this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType)));
  }

  /** {@inheritDoc} */
  @Override
  public float getArmorCoverPercentage() {
    return this.wrapped().getArmorCoverPercentage();
  }

  /** {@inheritDoc} */
  @Override
  public float getAIMoveSpeed() {
    return this.wrapped().getAIMoveSpeed();
  }

  /** {@inheritDoc} */
  @Override
  public void setAIMoveSpeed(float speed) {
    this.wrapped().setAIMoveSpeed(speed);
  }

  /** {@inheritDoc} */
  @Override
  public void applyEntityCollision(Entity entity) {
    this.wrapped()
        .applyEntityCollision(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw(float partialTicks) {
    return this.wrapped().getYaw(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public void attackEntityAsMob(Entity entity) {
    this.wrapped()
        .attackEntityAsMob(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void setSprinting(boolean sprinting) {
    this.wrapped().setSprinting(sprinting);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActuallySwimming() {
    return this.wrapped().isActualySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void startSpinAttack(int duration) {
    this.wrapped().startSpinAttack(duration);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSpinAttacking() {
    return this.wrapped().isSpinAttacking();
  }

  /** {@inheritDoc} */
  @Override
  public void setJumping(boolean jumping) {
    this.wrapped().setJumping(jumping);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canEntityBeSeen(Entity entity) {
    return this.wrapped()
        .canEntityBeSeen(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getSwingProgress(float partialTicks) {
    return this.wrapped().getSwingProgress(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isServerWorld() {
    return this.wrapped().isServerWorld();
  }

  /** {@inheritDoc} */
  @Override
  public float getAbsorptionAmount() {
    return this.wrapped().getAbsorptionAmount();
  }

  /** {@inheritDoc} */
  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    this.wrapped().setAbsorptionAmount(absorptionAmount);
  }

  /** {@inheritDoc} */
  @Override
  public void sendEnterCombat() {
    this.wrapped().sendEnterCombat();
  }

  /** {@inheritDoc} */
  @Override
  public void sendEndCombat() {
    this.wrapped().sendEndCombat();
  }

  /** {@inheritDoc} */
  @Override
  public Hand.Side getPrimaryHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHandSide(this.wrapped().getPrimaryHand());
  }

  /** {@inheritDoc} */
  @Override
  public Hand getActiveHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHand(this.wrapped().getActiveHand());
  }

  /** {@inheritDoc} */
  @Override
  public void setActiveHand(Hand hand) {
    this.wrapped()
        .setActiveHand(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getActiveItemStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.wrapped().getActiveItemStack());
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseCount() {
    return this.wrapped().getItemInUseCount();
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseMaxCount() {
    return this.wrapped().getItemInUseMaxCount();
  }

  /** {@inheritDoc} */
  @Override
  public void stopActiveHand() {
    this.wrapped().stopActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public void resetActiveHand() {
    this.wrapped().resetActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActiveItemStackBlocking() {
    return this.wrapped().isActiveItemStackBlocking();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSuppressingSlidingDownLadder() {
    return this.wrapped().isSuppressingSlidingDownLadder();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isElytraFlying() {
    return this.wrapped().isElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public int getTicksElytraFlying() {
    return this.wrapped().getTicksElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public boolean attemptTeleport(double x, double y, double z, boolean particleEffects) {
    return this.wrapped().attemptTeleport(x, y, z, particleEffects);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeHitWithPotion() {
    return this.wrapped().canBeHitWithPotion();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeRiddenInWater() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public boolean attackable() {
    return this.wrapped().attackable();
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveStrafing() {
    return this.wrapped().moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveStrafing(float moveStrafing) {
    this.wrapped().moveStrafing = moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveVertical() {
    return this.wrapped().moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveVertical(float moveVertical) {
    this.wrapped().moveVertical = moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveForward() {
    return this.wrapped().moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveForward(float moveForward) {
    this.wrapped().moveForward = moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setPartying(BlockPosition position, boolean isPartying) {
    this.wrapped()
        .setPartying((BlockPos) this.getWorld().toMinecraftBlockPos(position), isPartying);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return this.wrapped()
        .canPickUpItem(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack));
  }

  /** {@inheritDoc} */
  @Override
  public Optional<BlockPosition> getBedPosition() {
    Optional<BlockPosition> optional = Optional.empty();

    if (this.wrapped().getBedPosition().isPresent()) {
      optional =
          Optional.of(this.getWorld().fromMinecraftBlockPos(this.wrapped().getBedPosition().get()));
    }

    return optional;
  }

  /** {@inheritDoc} */
  @Override
  public void setBedPosition(BlockPosition position) {
    this.wrapped().setBedPosition((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void clearBedPosition() {
    this.wrapped().clearBedPosition();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSleeping() {
    return this.wrapped().isSleeping();
  }

  /** {@inheritDoc} */
  @Override
  public void startSleeping(BlockPosition position) {
    this.wrapped().startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void wakeUp() {
    this.wrapped().wakeUp();
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.wrapped()
                .findAmmo(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(shootable)));
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(EquipmentSlotType slotType) {
    this.wrapped()
        .sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType)
                this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(Hand hand) {
    this.wrapped()
        .sendBreakAnimation(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public Collection<StatusEffectInstance> getActivePotionEffects() {
    return this.activePotions.values();
  }

  /** {@inheritDoc} */
  @Override
  public Map<StatusEffect, StatusEffectInstance> getActivePotions() {
    return this.activePotions;
  }

  /** {@inheritDoc} */
  @Override
  public boolean addPotionEffect(StatusEffectInstance instance) {
    if (instance == null) {
      return false;
    }

    if (!this.activePotions.containsKey(instance.getPotion())) {
      this.activePotions.put(instance.getPotion(), instance);
      return true;
    }

    return false;
  }

  /** {@inheritDoc} */
  @Override
  public boolean removePotionEffect(StatusEffect effect) {
    if (effect == null) {
      return false;
    }

    return this.activePotions.remove(effect) != null;
  }

  /** {@inheritDoc} */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.wrapped()
        .readAdditional(
            (CompoundNBT)
                this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.wrapped()
        .writeAdditional(
            (CompoundNBT)
                this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlive() {
    return this.wrapped().isAlive();
  }
}
