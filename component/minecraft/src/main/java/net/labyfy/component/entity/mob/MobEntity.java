package net.labyfy.component.entity.mob;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.ai.EntitySenses;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.world.util.BlockPosition;

/**
 * Represents the Minecraft mob.
 */
public interface MobEntity extends LivingEntity {

  Object getArmorByChance(EquipmentSlotType slotType, float chance);

  EntitySenses getEntitySenses();

  LivingEntity getAttackTarget();

  void setAttackTarget(LivingEntity entity);

  void eatGrassBonus();

  int getTalkInterval();

  void playAmbientSound();

  void spawnExplosionParticle();

  void setMoveForward(float forward);

  void setMoveVertical(float vertical);

  void setMoveStrafing(float strafing);

  boolean canDespawn(double factor);

  boolean preventDespawn();

  int getVerticalFaceSpeed();

  int getHorizontalFaceSpeed();

  int getFaceRotationSpeed();

  void faceEntity(Entity entity, float pitch, float yaw);

  int getMaxSpawnedInChunk();

  boolean isMaxSpawnedInChunk();

  boolean canBeSteered();

  void enablePersistence();

  void setDropChange(EquipmentSlotType slotType, float chance);

  boolean canPickUpLoot();

  void setCanPickUpLoot(boolean canPickUpLoot);

  boolean isNoDespawnRequired();

  boolean isWithinHomeDistanceCurrentPosition();

  boolean isWithinHomeDistanceFromPosition(BlockPosition position);

  void setHomePositionAndDistance(BlockPosition position, int distance);

  BlockPosition getHomePosition();

  float getMaximumHomeDistance();

  boolean detachHome();

  void clearLeashed(boolean flag, boolean flag2);

  boolean canBeLeashedTo(PlayerEntity playerEntity);

  boolean isLeashed();

  Entity getLeashHolder();

  void setLeashHolder(Entity entity, boolean leashHolder);

  void setVehicleEntityId(int vehicleEntityId);

  boolean isItemStackInSlot(EquipmentSlotType slotType, ItemStack itemStack);

  void setNoAI(boolean noAI);

  void setAggroed(boolean aggroed);

  boolean isAIDisabled();

  boolean isLeftHanded();

  void setLeftHanded(boolean leftHanded);

  boolean isAggressive();

}
