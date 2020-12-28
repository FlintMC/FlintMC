/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.type.EntityType;
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
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  EntitySenses getEntitySenses();

  /**
   * Retrieves the attack target of this mob entity.
   *
   * @return The attack target.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  LivingEntity getAttackTarget();

  /**
   * Changes the attack target of this mob entity.
   *
   * @param entity The new attack target.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setAttackTarget(LivingEntity entity);

  /**
   * Eat grass for a bonus.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void eatGrassBonus();

  /**
   * Retrieves the talk interval of this mob entity.
   *
   * @return The mob entity talk interval.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getTalkInterval();

  /**
   * Plays ambient sounds at the position of the mob entity.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void playAmbientSound();

  /**
   * Spawns explosion particle at the position of the mob entity.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void spawnExplosionParticle();

  /**
   * Changes the move forward value by the given value.
   *
   * @param amount The move forward value.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setMoveForward(float amount);

  /**
   * Changes the move vertical value by the given value.
   *
   * @param amount The move vertical value.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setMoveVertical(float amount);

  /**
   * Changes the move strafing value by the given value.
   *
   * @param amount The move strafing value.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setMoveStrafing(float amount);

  /**
   * Whether the mob entity can despawn.
   *
   * @param distanceToClosestPlayer The distance to the closet player.
   * @return {@code true} if the mob entity can despawn, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean canDespawn(double distanceToClosestPlayer);

  /**
   * Whether the mob entity prevent from despawn.
   *
   * @return {@code true} if the mob entity prevent from despawn, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean preventDespawn();

  /**
   * Retrieves the vertical face speed of this mob entity.
   *
   * @return The vertical face speed.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getVerticalFaceSpeed();

  /**
   * Retrieves the horizontal face speed of this mob entity.
   *
   * @return The horizontal face speed.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getHorizontalFaceSpeed();

  /**
   * Retrieves the face rotation speed of this mob entity.
   *
   * @return The face rotation speed.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getFaceRotationSpeed();

  /**
   * Lets the face of the mob entity look at the given entity.
   *
   * @param entity           The entity to look at.
   * @param maxYawIncrease   The maximum yaw increase.
   * @param maxPitchIncrease The maximum pitch increase.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void faceEntity(Entity entity, float maxYawIncrease, float maxPitchIncrease);

  /**
   * Retrieves the maximal spawned mob entity in chunk.
   *
   * @return The maximal spawned in chunk count.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  int getMaxSpawnedInChunk();

  /**
   * Whether the mob entity maximal group size.
   *
   * @param size The size for the group.
   * @return {@code true} if the mob entity maximal group size, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isMaxGroupSize(int size);

  /**
   * Whether the mob entity can be steered.
   *
   * @return {@code true} if the mob entity can be steered, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean canBeSteered();

  /**
   * Enables the persistence of this mob entity.
   *
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void enablePersistence();

  /**
   * Changes the drop chance of an equipment slot.
   *
   * @param slotType The equipment slot.
   * @param chance   The new drop chance.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setDropChance(EquipmentSlotType slotType, float chance);

  /**
   * Whether the mob entity can pickup loot.
   *
   * @return {@code true} if the mob entity can pickup loot, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean canPickUpLoot();

  /**
   * Changes the mob entity can pickup loot.
   *
   * @param canPickUpLoot {@code true} if the mob entity can pickup loot, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setCanPickUpLoot(boolean canPickUpLoot);

  /**
   * Whether the mob entity is no despawn required.
   *
   * @return {@code true} if the mob entity is no despawn required, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isNoDespawnRequired();

  /**
   * Whether the mob entity is within home distance to the current position.
   *
   * @return {@code true} if the mob entity is within home distance to the current position,
   * otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isWithinHomeDistanceCurrentPosition();

  /**
   * Whether the mob entity is within home distance from the given position.
   *
   * @param position The block position to be checked.
   * @return {@code true} if the mob entity is within home distance from the given position,
   * otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isWithinHomeDistanceFromPosition(BlockPosition position);

  /**
   * Changes the home position and the distance of this mob entity.
   *
   * @param position The new home position.
   * @param distance The new distance.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setHomePositionAndDistance(BlockPosition position, int distance);

  /**
   * Retrieves the home position of this mob entity.
   *
   * @return The home position of this mob entity.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  BlockPosition getHomePosition();

  /**
   * Retrieves the maximum home distance of this mob entity.
   *
   * @return The maximum home distance.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  float getMaximumHomeDistance();

  /**
   * Whether the mob entity detach home.
   *
   * @return {@code true} if the mob entity detach home, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean detachHome();

  /**
   * Clears all leads from the mob entity.
   *
   * @param sendPacket {@code true} if the packet should be sent, otherwise {@code false}.
   * @param dropLead   {@code true} if the lead is dropped, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void clearLeashed(boolean sendPacket, boolean dropLead);

  /**
   * Whether the mob entity can be leashed to given {@link PlayerEntity}.
   *
   * @param playerEntity The player entity to be checked.
   * @return {@code true} if the mob entity can be leashed to given {@link PlayerEntity}, otherwise
   * {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean canBeLeashedTo(PlayerEntity playerEntity);

  /**
   * Whether the mob entity is leashed.
   *
   * @return {@link true} if the mob entity is leashed, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isLeashed();

  /**
   * Retrieves the leash holder of this mob entity.
   *
   * @return The leash holder.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  Entity getLeashHolder();

  /**
   * Changes the leash holder of this mob entity.
   *
   * @param entity      The new leash holder.
   * @param leashHolder {@code true} if the new leash holder, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setLeashHolder(Entity entity, boolean leashHolder);

  /**
   * Changes the vehicle entity identifier of this mob entity.
   *
   * @param vehicleEntityId The new vehicle entity identifier.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setVehicleEntityId(int vehicleEntityId);

  /**
   * Whether the given {@link ItemStack} is in the slot type.
   *
   * @param slotType  The slot type of the item stack.
   * @param itemStack The item stack to be checked.
   * @return {@code true} if the given item stack is in the slot type, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isItemStackInSlot(EquipmentSlotType slotType, ItemStack itemStack);

  /**
   * Changes the mob entity no AI.
   *
   * @param noAI {@code true} if the entity should have no AI, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setNoAI(boolean noAI);

  /**
   * Whether the AI is disabled.
   *
   * @return {@code true} if the entity AI is disabled, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isAIDisabled();

  /**
   * Whether the entity is left handed.
   *
   * @return {@code true} if the entity is left handed, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isLeftHanded();

  /**
   * Changes whether the entity is left-handed.
   *
   * @param leftHanded {@code true} if the entity is left-handed, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  void setLeftHanded(boolean leftHanded);

  /**
   * Whether the entity is aggressive.
   *
   * @return {@code true} if the entity is aggressive, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
   */
  boolean isAggressive();

  /**
   * Changes the aggressive of this mob entity.
   *
   * @param aggressive {@code true} if the entity should be aggressive, otherwise {@code false}.
   * @throws EntityNotLoadedException If this method is being called when no world is loaded in the
   *                                  client
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
    MobEntity create(
        @Assisted("mobEntity") Object entity, @Assisted("entityType") EntityType entityType);
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
