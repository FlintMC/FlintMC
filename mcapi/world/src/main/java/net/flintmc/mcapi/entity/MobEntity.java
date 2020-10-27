package net.flintmc.mcapi.entity;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.world.math.BlockPosition;

/**
 * Represents the Minecraft mob.
 */
public interface MobEntity extends LivingEntity {

  /**
   * Retrieves the senses of the mob entity.
   *
   * @return The senses of the mob entity.
   */
  EntitySenses getEntitySenses();

  /**
   * Retrieves the attack target of this mob entity.
   *
   * @return The attack target.
   */
  LivingEntity getAttackTarget();

  /**
   * Changes the attack target of this mob entity.
   *
   * @param entity The new attack target.
   */
  void setAttackTarget(LivingEntity entity);

  /**
   * Eat grass for a bonus.
   */
  void eatGrassBonus();

  /**
   * Retrieves the talk interval of this mob entity.
   *
   * @return The mob entity talk interval.
   */
  int getTalkInterval();

  /**
   * Plays ambient sounds at the position of the mob entity.
   */
  void playAmbientSound();

  /**
   * Spawns explosion particle at the position of the mob entity.
   */
  void spawnExplosionParticle();

  /**
   * Changes the move forward value by the given value.
   *
   * @param amount The move forward value.
   */
  void setMoveForward(float amount);

  /**
   * Changes the move vertical value by the given value.
   *
   * @param amount The move vertical value.
   */
  void setMoveVertical(float amount);

  /**
   * Changes the move strafing value by the given value.
   *
   * @param amount The move strafing value.
   */
  void setMoveStrafing(float amount);

  /**
   * Whether the mob entity can despawn.
   *
   * @param distanceToClosestPlayer The distance to the closet player.
   * @return {@code true} if the mob entity can despawn, otherwise {@code false}.
   */
  boolean canDespawn(double distanceToClosestPlayer);

  /**
   * Whether the mob entity prevent from despawn.
   *
   * @return {@code true} if the mob entity prevent from despawn, otherwise {@code false}.
   */
  boolean preventDespawn();

  /**
   * Retrieves the vertical face speed of this mob entity.
   *
   * @return The vertical face speed.
   */
  int getVerticalFaceSpeed();

  /**
   * Retrieves the horizontal face speed of this mob entity.
   *
   * @return The horizontal face speed.
   */
  int getHorizontalFaceSpeed();

  /**
   * Retrieves the face rotation speed of this mob entity.
   *
   * @return The face rotation speed.
   */
  int getFaceRotationSpeed();

  /**
   * Lets the face of the mob entity look at the given entity.
   *
   * @param entity           The entity to look at.
   * @param maxYawIncrease   The maximum yaw increase.
   * @param maxPitchIncrease The maximum pitch increase.
   */
  void faceEntity(Entity entity, float maxYawIncrease, float maxPitchIncrease);

  /**
   * Retrieves the maximal spawned mob entity in chunk.
   *
   * @return The maximal spawned in chunk count.
   */
  int getMaxSpawnedInChunk();

  /**
   * Whether the mob entity maximal group size.
   *
   * @param size The size for the group.
   * @return {@code true} if the mob entity maximal group size, otherwise {@code false}.
   */
  boolean isMaxGroupSize(int size);

  /**
   * Whether the mob entity can be steered.
   *
   * @return {@code true} if the mob entity can be steered, otherwise {@code false}.
   */
  boolean canBeSteered();

  /**
   * Enables the persistence of this mob entity.
   */
  void enablePersistence();

  /**
   * Changes the drop chance of an equipment slot.
   *
   * @param slotType The equipment slot.
   * @param chance   The new drop chance.
   */
  void setDropChance(EquipmentSlotType slotType, float chance);

  /**
   * Whether the mob entity can pickup loot.
   *
   * @return {@code true} if the mob entity can pickup loot, otherwise {@code false}.
   */
  boolean canPickUpLoot();

  /**
   * Changes the mob entity can pickup loot.
   *
   * @param canPickUpLoot {@code true} if the mob entity can pickup loot, otherwise {@code false}.
   */
  void setCanPickUpLoot(boolean canPickUpLoot);

  /**
   * Whether the mob entity is no despawn required.
   *
   * @return {@code true} if the mob entity is no despawn required, otherwise {@code false}.
   */
  boolean isNoDespawnRequired();

  /**
   * Whether the mob entity is within home distance to the current position.
   *
   * @return {@code true} if the mob entity is within home distance to the current position, otherwise {@code false}.
   */
  boolean isWithinHomeDistanceCurrentPosition();

  /**
   * Whether the mob entity is within home distance from the given position.
   *
   * @param position The block position to be checked.
   * @return {@code true} if the mob entity is within home distance from the given position, otherwise {@code false}.
   */
  boolean isWithinHomeDistanceFromPosition(BlockPosition position);

  /**
   * Changes the home position and the distance of this mob entity.
   *
   * @param position The new home position.
   * @param distance The new distance.
   */
  void setHomePositionAndDistance(BlockPosition position, int distance);

  /**
   * Retrieves the home position of this mob entity.
   *
   * @return The home position of this mob entity.
   */
  BlockPosition getHomePosition();

  /**
   * Retrieves the maximum home distance of this mob entity.
   *
   * @return The maximum home distance.
   */
  float getMaximumHomeDistance();

  /**
   * Whether the mob entity detach home.
   *
   * @return {@code true} if the mob entity detach home, otherwise {@code false}.
   */
  boolean detachHome();

  /**
   * Clears all leads from the mob entity.
   *
   * @param sendPacket {@code true} if the packet should be sent, otherwise {@code false}.
   * @param dropLead   {@code true} if the lead is dropped, otherwise {@code false}.
   */
  void clearLeashed(boolean sendPacket, boolean dropLead);

  /**
   * Whether the mob entity can be leashed to given {@link PlayerEntity}.
   *
   * @param playerEntity The player entity to be checked.
   * @return {@code true} if the mob entity can be leashed to given {@link PlayerEntity}, otherwise {@code false}.
   */
  boolean canBeLeashedTo(PlayerEntity playerEntity);

  /**
   * Whether the mob entity is leashed.
   *
   * @return {@link true} if the mob entity is leashed, otherwise {@code false}.
   */
  boolean isLeashed();

  /**
   * Retrieves the leash holder of this mob entity.
   *
   * @return The leash holder.
   */
  Entity getLeashHolder();

  /**
   * Changes the leash holder of this mob entity.
   *
   * @param entity      The new leash holder.
   * @param leashHolder {@code true} if the new leash holder, otherwise {@code false}.
   */
  void setLeashHolder(Entity entity, boolean leashHolder);

  /**
   * Changes the vehicle entity identifier of this mob entity.
   *
   * @param vehicleEntityId The new vehicle entity identifier.
   */
  void setVehicleEntityId(int vehicleEntityId);

  /**
   * Whether the given {@link ItemStack} is in the slot type.
   *
   * @param slotType  The slot type of the item stack.
   * @param itemStack The item stack to be checked.
   * @return {@code true} if the given item stack is in the slot type, otherwise {@code false}.
   */
  boolean isItemStackInSlot(EquipmentSlotType slotType, ItemStack itemStack);

  /**
   * Changes the mob entity no AI.
   *
   * @param noAI {@code true} if the entity should have no AI, otherwise {@code false}.
   */
  void setNoAI(boolean noAI);

  /**
   * Whether the AI is disabled.
   *
   * @return {@code true} if the entity AI is disabled, otherwise {@code false}.
   */
  boolean isAIDisabled();

  /**
   * Whether the entity is left handed.
   *
   * @return {@code true} if the entity is left handed, otherwise {@code false}.
   */
  boolean isLeftHanded();

  /**
   * Changes whether the entity is left-handed.
   *
   * @param leftHanded {@code true} if the entity is left-handed, otherwise {@code false}.
   */
  void setLeftHanded(boolean leftHanded);

  /**
   * Whether the entity is aggressive.
   *
   * @return {@code true} if the entity is aggressive, otherwise {@code false}.
   */
  boolean isAggressive();

  /**
   * Changes the aggressive of this mob entity.
   *
   * @param aggressive {@code true} if the entity should be aggressive, otherwise {@code false}.
   */
  void setAggressive(boolean aggressive);

  /**
   * A factory class for the {@link MobEntity}.
   */
  @AssistedFactory(MobEntity.class)
  interface Factory {

    /**
     * Creates a new {@link MobEntity} with the given entity.
     *
     * @param entity     The given entity.
     * @param entityType The type of the entity.
     * @return A created mob entity.
     */
    MobEntity create(@Assisted("mobEntity") Object entity, @Assisted("entityType") EntityType entityType);

  }

  /**
   * Service interface for creating {@link MobEntity}'s.
   */
  interface Provider {

    /**
     * Creates a new {@link MobEntity} with the given entity.
     *
     * @param entity The given entity.
     * @return A created mob entity.
     * @see Factory#create(Object, EntityType)
     */
    MobEntity get(Object entity);

  }

}
