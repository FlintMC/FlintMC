package net.labyfy.component.entity;

import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.util.BlockPosition;

import java.util.Optional;
import java.util.Random;

public interface LivingEntity extends Entity {

  default boolean canAttack(EntityType entityType) {
    return true;
  }

  boolean canBreathUnderwater();

  float getSwimAnimation(float partialTicks);

  default boolean isChild() {
    return true;
  }

  float getRenderScale();

  Random getRandom();

  LivingEntity getRevengeTarget();

  void setRevengeTarget(LivingEntity entity);

  int getRevengeTimer();

  LivingEntity getLastAttackedEntity();

  void setLastAttackedEntity(Entity entity);

  int getLastAttackedEntityTime();

  int getIdleTime();

  void setIdleTime(int idleTime);

  double getVisibilityMultiplier(Entity entity);

  default boolean isPlayer() {
    return false;
  }

  boolean canAttack(LivingEntity entity);

  boolean clearActivePotions();

  boolean isEntityUndead();

  void heal(float health);

  float getHealth();

  void setHealth(float health);

  ResourceLocation getLootTableResourceLocation();

  void knockBack(Entity entity, float strength, double xRatio, double zRatio);

  Sound getEatSound(ItemStack itemStack);

  boolean isOnLadder();

  int getTotalArmorValue();

  float getMaxHealth();

  int getArrowCountInEntity();

  void setArrowCountInEntity(int count);

  int getBeeStingCount();

  void setBeeStingCount(int stingCount);

  void swingArm(Hand hand);

  void swing(Hand hand, boolean swing);

  ItemStack getHeldItem(Hand hand);

  void setHeldItem(Hand hand, ItemStack heldItem);

  boolean hasItemInSlot(EquipmentSlotType slotType);

  ItemStack getItemStackFromSlot(EquipmentSlotType slotType);

  float getArmorCoverPercentage();

  float getAIMoveSpeed();

  void setAIMoveSpeed(float speed);

  void attackEntityAsMob(Entity entity);

  void startSpinAttack(int duration);

  boolean isSpinAttacking();

  void setJumping(boolean jumping);

  boolean canEntityBeSeen(Entity entity);

  float getSwingProgress(float partialTicks);

  boolean isServerWorld();

  float getAbsorptionAmount();

  void setAbsorptionAmount(float absorptionAmount);

  void sendEnterCombat();

  void sendEndCombat();

  Hand.Side getPrimaryHand();

  Hand getActiveHand();

  void setActiveHand(Hand hand);

  ItemStack getActiveItemStack();

  int getItemInUseCount();

  int getItemInUseMaxCount();

  void stopActiveHand();

  void resetActiveHand();

  boolean isActiveItemStackBlocking();

  boolean isSuppressingSlidingDownLadder();

  boolean isElytraFlying();

  int getTicksElytraFlying();

  boolean attemptTeleport(double x, double y, double z, boolean teleportState);

  boolean canBeHitWithPotion();

  boolean attackable();

  void setPartying(BlockPosition position, boolean isPartying);

  boolean canPickUpItem(ItemStack stack);

  Optional<BlockPosition> getBedPosition();

  void setBedPosition(BlockPosition position);

  void clearBedPosition();

  boolean isSleeping();

  void startSleeping(BlockPosition position);

  void wakeUp();

  ItemStack findAmmo(ItemStack shootable);

  void sendBreakAnimation(EquipmentSlotType slotType);

  void sendBreakAnimation(Hand hand);


}
