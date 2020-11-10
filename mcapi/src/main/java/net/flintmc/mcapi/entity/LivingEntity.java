package net.flintmc.mcapi.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.math.BlockPosition;

import java.util.Optional;
import java.util.Random;

/** Represents the living entity. */
public interface LivingEntity extends Entity {

  /**
   * Whether the living entity can attack the given entity type.
   *
   * @param entityType The entity type to attack.
   * @return {@code true} if the living entity can attack the given type, otherwise {@code false}.
   */
  default boolean canAttack(EntityType entityType) {
    return true;
  }

  /**
   * Whether the living entity can breath underwater.
   *
   * @return {@code true} if the living entity can breath underwater, otherwise {@code false}.
   */
  boolean canBreathUnderwater();

  /**
   * Retrieves the swim animation of this living entity.
   *
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *     full tick.
   * @return The swim animation of this living entity.
   */
  float getSwimAnimation(float partialTicks);

  /**
   * Whether the living entity is a child.
   *
   * @return {@code true} if the living entity is a child, otherwise {@code false}.
   */
  default boolean isChild() {
    return true;
  }

  /**
   * Retrieves the render scale of this living entity.
   *
   * @return The living entity render scale.
   */
  float getRenderScale();

  /**
   * Retrieves a random of this living entity.
   *
   * @return A random of this living entity.
   */
  Random getRandom();

  /**
   * Retrieves the revenge target of this living entity.
   *
   * @return The living entity revenge target.
   */
  LivingEntity getRevengeTarget();

  /**
   * Changes the revenge target of this living entity.
   *
   * @param entity The new revenge target.
   */
  void setRevengeTarget(LivingEntity entity);

  /**
   * Retrieves the revenge timer of this living entity.
   *
   * @return The living entity revenge timer.
   */
  int getRevengeTimer();

  /**
   * Retrieves the last attacked entity of this living entity.
   *
   * @return The living entity last attacked entity.
   */
  LivingEntity getLastAttackedEntity();

  /**
   * Changes the last attacked entity of this living entity.
   *
   * @param entity The new last attacked entity.
   */
  void setLastAttackedEntity(Entity entity);

  /**
   * Retrieves the last attacked entity time of this living entity.
   *
   * @return The living entity last attacked entity time.
   */
  int getLastAttackedEntityTime();

  /**
   * Retrieves the idle time of this living entity.
   *
   * @return The idle time of this living entity.
   */
  int getIdleTime();

  /**
   * Changes the idle time of this living entity.
   *
   * @param idleTime The new idle time.
   */
  void setIdleTime(int idleTime);

  /**
   * Retrieves the visibility multiplier of the given entity.
   *
   * @param entity The entity to be get the visibility multiplier.
   * @return The visibility multiplier of the given entity.
   */
  double getVisibilityMultiplier(Entity entity);

  /**
   * Whether the living entity is a player.
   *
   * @return {@code true} if the living entity is a player, otherwise {@code false}.
   */
  default boolean isPlayer() {
    return false;
  }

  /**
   * Whether the living entity can attack the given entity.
   *
   * @param entity The entity to be attack.
   * @return {@code true} if the living entity can attack the given entity, otherwise {@code false}.
   */
  boolean canAttack(LivingEntity entity);

  /**
   * Whether the active potions was cleared.
   *
   * @return {@code true} if the active was cleared, otherwise {@code false}.
   */
  boolean clearActivePotions();

  /**
   * Whether the living entity is undead.
   *
   * @return {@code true} if the living entity is undead, otherwise {@code false}.
   */
  boolean isEntityUndead();

  /**
   * Adds the given value to the {@link #getHealth()} of this living entity..
   *
   * @param health The health to be add.
   */
  void heal(float health);

  /**
   * Retrieves the health of this living entity.
   *
   * @return The living entity health.
   */
  float getHealth();

  /**
   * Changes the health of htis living entity.
   *
   * @param health The new health.
   */
  void setHealth(float health);

  /**
   * Retrieves the loot table resource location.
   *
   * @return The loot table resource location.
   */
  ResourceLocation getLootTableResourceLocation();

  /**
   * Knock back an entity.
   *
   * @param entity The entity to knock back.
   * @param strength The strength of the knock back.
   * @param xRatio The x ratio of the knock back.
   * @param zRatio The z ratio of the knock back.
   */
  void knockBack(Entity entity, float strength, double xRatio, double zRatio);

  /**
   * Retrieves the eat sound of the given item stack.
   *
   * @param itemStack The item stack to get the sound.
   * @return The eat sound of the given item stack.
   */
  Sound getEatSound(ItemStack itemStack);

  /**
   * Whether the living entity is on a ladder.
   *
   * @return {@code true} if the living entity is on a ladder, otherwise {@code false}.
   */
  boolean isOnLadder();

  /**
   * Retrieves the total armor value of this living entity.
   *
   * @return The living entity total armor value.
   */
  int getTotalArmorValue();

  /**
   * Retrieves the maximal health of this living entity.
   *
   * @return the maximal living entity health.
   */
  float getMaxHealth();

  /**
   * Retrieves the arrow count in the living entity.
   *
   * @return The living entity arrow count.
   */
  int getArrowCountInEntity();

  /**
   * Changes the arrow count in the living entity.
   *
   * @param count The new arrow count.
   */
  void setArrowCountInEntity(int count);

  /**
   * Retrieves the bee sting count of this living entity.
   *
   * @return The bee sting count.
   */
  int getBeeStingCount();

  /**
   * Changes the bee sting count of this living entity.
   *
   * @param stingCount The new sting count.
   */
  void setBeeStingCount(int stingCount);

  /**
   * Swings the arm of the living entity.
   *
   * @param hand The hand to swing.
   */
  void swingArm(Hand hand);

  /**
   * Swings the hand.
   *
   * @param hand The living entity hand.
   * @param sendToAll {@code true}, if the animated hand packet should be sent to itself, otherwise
   *     {@code false}
   */
  void swing(Hand hand, boolean sendToAll);

  /**
   * Retrieves the held item at the given hand.
   *
   * @param hand The hand to get the item stack.
   * @return An item stack or an empty item stack.
   */
  ItemStack getHeldItem(Hand hand);

  /**
   * Changes the held item.
   *
   * @param hand The living entity hand.
   * @param heldItem The new held item stack.
   */
  void setHeldItem(Hand hand, ItemStack heldItem);

  /**
   * Whether the living entity has an item at the given slot.
   *
   * @param slotType The slot type to be checked.
   * @return {@code true} if the living entity has an item at the given slot, otherwise {@code
   *     false}.
   */
  boolean hasItemInSlot(EquipmentSlotType slotType);

  /**
   * Retrieves the item stack from the given slot.
   *
   * @param slotType The slot type to get the item stack.
   * @return An item stack or an empty item stack.
   */
  ItemStack getItemStackFromSlot(EquipmentSlotType slotType);

  /**
   * Retrieves the armor cover percentage of this living entity.
   *
   * @return The armor cover percentage.
   */
  float getArmorCoverPercentage();

  /**
   * Retrieves the AI move speed of this living entity.
   *
   * @return The AI move speed.
   */
  float getAIMoveSpeed();

  /**
   * Changes the AI move speed of this living entity.
   *
   * @param speed The new AI move speed.
   */
  void setAIMoveSpeed(float speed);

  /**
   * Attacks an entity as a mob.
   *
   * @param entity The entity to be attacked.
   */
  void attackEntityAsMob(Entity entity);

  /**
   * Starts the spin attack.
   *
   * @param duration The duration how long take the spin attack.
   */
  void startSpinAttack(int duration);

  /**
   * Whether the living entity is spin attacking.
   *
   * @return {@code true} if the living entity is spin attacking.a
   */
  boolean isSpinAttacking();

  /**
   * Changes the jumping state of this living entity.
   *
   * @param jumping {@code true} if the living entity is jumping, otherwise {@code false}.
   */
  void setJumping(boolean jumping);

  /**
   * Whether the living entity can be seen the given entity.
   *
   * @param entity The entity to be checked.
   * @return {@code true} if the living entity can be seen the given entity, otherwise {@code
   *     false}.
   */
  boolean canEntityBeSeen(Entity entity);

  /**
   * Retrieves the swing progress of this living entity.
   *
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *     full tick.
   * @return The swing progress of this living entity.
   */
  float getSwingProgress(float partialTicks);

  /**
   * Whether the world is a server world.
   *
   * @return {@code true} if the world a server world, otherwise {@code false}.
   */
  boolean isServerWorld();

  /**
   * Retrieves the absorption amount of this living entity.
   *
   * @return The absorption amount.
   */
  float getAbsorptionAmount();

  /**
   * Changes the absorption amount of this living entity.
   *
   * @param absorptionAmount The new absorption amount.
   */
  void setAbsorptionAmount(float absorptionAmount);

  /** Sends the combat packet, when the living entity start to fight. */
  void sendEnterCombat();

  /** Sends the combat packet, when the living entity stop to fight. */
  void sendEndCombat();

  /**
   * Retrieves the primary hand of this living entity.
   *
   * @return The primary hand.
   */
  Hand.Side getPrimaryHand();

  /**
   * Retrieves the active hand of this living entity.
   *
   * @return The active hand.
   */
  Hand getActiveHand();

  /**
   * Changes the active hand of this living entity.
   *
   * @param hand The new active hand.
   */
  void setActiveHand(Hand hand);

  /**
   * Retrieves the active item stack of this living entity.
   *
   * @return The active item stack of this living entity.
   */
  ItemStack getActiveItemStack();

  /**
   * Retrieves the item in use count of this living entity.
   *
   * @return The item in use count.
   */
  int getItemInUseCount();

  /**
   * Retrieves the item in maximal use count of this living entity.
   *
   * @return The ite in maximal use count.
   */
  int getItemInUseMaxCount();

  /** Stops the active hand of this living entity. */
  void stopActiveHand();

  /** Resets the active hand of this living entity. */
  void resetActiveHand();

  /**
   * Whether the living entity is active blocking with an item stack.
   *
   * @return {@code true} if the living entity is active block with an item stack.
   */
  boolean isActiveItemStackBlocking();

  /**
   * Whether the living entity is suppressing sliding down the ladder.
   *
   * @return {@code true} if the living entity is suppressing sliding down the ladder, otherwise
   *     {@code false}.
   */
  boolean isSuppressingSlidingDownLadder();

  /**
   * Whether the living entity is flying with an elytra.
   *
   * @return {@code true} if the living entity is flying with an elytra, otherwise {@code false}.
   */
  boolean isElytraFlying();

  /**
   * Retrieves the ticks how long the living entity is flying with an elytra.
   *
   * @return The ticks how long the living entity is flying with an elytra.
   */
  int getTicksElytraFlying();

  /**
   * Attempts to teleport the living entity.
   *
   * @param x The x position where the living entity should be teleported to.
   * @param y The y position where the living entity should be teleported to.
   * @param z The z position where the living entity should be teleported to.
   * @param particleEffects {@code true} if particle effects should be displayed, otherwise {@code
   *     false}.
   * @return {@code true} if the teleport attempt was successful, otherwise {@code false}.
   */
  boolean attemptTeleport(double x, double y, double z, boolean particleEffects);

  /**
   * Whether the living entity can be hit with a potion.
   *
   * @return {@code true} if the living entity can be hit with a potion, otherwise {@code false}.
   */
  boolean canBeHitWithPotion();

  /**
   * Whether the living entity is attackable.
   *
   * @return {@code true} if the living entity is attackable, otherwise {@code false}.
   */
  boolean attackable();

  /**
   * Retrieves the move strafing speed of this living entity.
   *
   * @return The move strafing speed.
   */
  float getMoveStrafing();

  /**
   * Changes the move strafing speed of this living entity.
   *
   * @param moveStrafing The new move strafing speed.
   */
  void setMoveStrafing(float moveStrafing);

  /**
   * Retrieves the move vertical speed of this living entity.
   *
   * @return The move vertical speed.
   */
  float getMoveVertical();

  /**
   * Changes the move vertical speed of this living entity.
   *
   * @param moveVertical The new move vertical speed.
   */
  void setMoveVertical(float moveVertical);

  /**
   * Retrieves the move forward speed of this living entity.
   *
   * @return The move forward speed.
   */
  float getMoveForward();

  /**
   * Changes the move forward speed of this living entity.
   *
   * @param moveForward The new move forward speed.
   */
  void setMoveForward(float moveForward);

  /**
   * Changes the state of this entity if it is in party mood.
   *
   * @param position The position of this jukebox.
   * @param isPartying {@code true} if the party should start, otherwise {@code false}.
   */
  void setPartying(BlockPosition position, boolean isPartying);

  /**
   * Whether the living entity can pick up the given item stack.
   *
   * @param stack The item stack to pick up.
   * @return {@code true} if the living entity can pick up the given item stack, otherwise {@code
   *     false}.
   */
  boolean canPickUpItem(ItemStack stack);

  /**
   * Retrieves an optional bed position.
   *
   * @return An optional bed position.
   */
  Optional<BlockPosition> getBedPosition();

  /**
   * Changes the bed position of this living entity.
   *
   * @param position The new block position of the bed.
   */
  void setBedPosition(BlockPosition position);

  /** Clears the bed position of this living entity. */
  void clearBedPosition();

  /**
   * Whether the living entity is sleeping.
   *
   * @return {@code true} if the living entity is sleeping, otherwise {@code false}.
   */
  boolean isSleeping();

  /**
   * Starts sleeping at the given block position.
   *
   * @param position The block position to sleep.
   */
  void startSleeping(BlockPosition position);

  /** Wakes the living entity up. */
  void wakeUp();

  /**
   * Finds all ammo of the given item stack.
   *
   * @param shootable The shootable item stack.
   * @return An item stack or {@code null}.
   */
  ItemStack findAmmo(ItemStack shootable);

  /**
   * Sends the break animation.
   *
   * @param slotType The equipment slot for the break animation.
   */
  void sendBreakAnimation(EquipmentSlotType slotType);

  /**
   * Sends the break animation.
   *
   * @param hand The hand for the break animation.
   */
  void sendBreakAnimation(Hand hand);

  /** A factory class for the {@link LivingEntity}. */
  @AssistedFactory(LivingEntity.class)
  interface Factory {

    /**
     * Creates a new {@link LivingEntity} with the given parameters.
     *
     * @param entity The non-null Minecraft entity.
     * @param entityType The entity type.
     * @return A created {@link LivingEntity}.
     */
    LivingEntity create(
        @Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }

  /** Service interface for creating {@link LivingEntity}'s. */
  interface Provider {

    /**
     * Creates a new {@link LivingEntity} with the given parameter.
     *
     * @param entity The non-null Minecraft entity.
     * @return A created {@link LivingEntity}.
     */
    LivingEntity get(Object entity);
  }
}
