package net.labyfy.component.entity;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.name.Nameable;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.World;
import net.labyfy.component.world.block.material.PushReaction;
import net.labyfy.component.world.util.BlockPosition;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Represents the Minecraft entity.
 */
public interface Entity extends Nameable {

  /**
   * Retrieves the team color as an {@link Integer} of this entity.
   *
   * @return The team color as an {@link Integer}.
   */
  int getTeamColor();

  default boolean isSpectator() {
    return false;
  }

  /**
   * Detach all passengers from this entity and stop the riding if the entity itself is a passenger.
   */
  void detach();

  void setPacketCoordinates(double x, double y, double z);

  EntityType getType();

  int getIdentifier();

  void setIdentifier(int identifier);

  Set<String> getTags();

  boolean addTag(String tag);

  boolean removeTag(String tag);

  double getPosX();

  double getPosY();

  double getPosZ();

  void remove();

  EntityPose getPose();

  void setPosition(double x, double y, double z);

  void setPositionAndRotation(double x, double y, double z, float yaw, float pitch);

  void moveToBlockPosAndAngles(BlockPosition position, float rotationYaw, float rotationPitch);

  void setLocationAndAngles(double x, double y, double z, float yaw, float pitch);

  void forceSetPosition(double x, double y, double z);

  float getDistance(Entity entity);

  double getDistanceSq(double x, double y, double z);

  double getDistanceSq(Entity entity);

  void applyEntityCollision(Entity entity);

  void addVelocity(double x, double y, double z);

  void rotateTowards(double yaw, double pitch);

  float getPitch(float partialTicks);

  float getYaw(float partialTicks);

  int getMaxInPortalTime();

  void setFire(int seconds);

  int getFireTimer();

  void setFireTimer(int ticks);

  void extinguish();

  void resetPositionToBB();

  void playSound(Sound sound, float volume, float pitch);

  boolean isSilent();

  void setSilent(boolean silent);

  boolean hasNoGravity();

  void setNoGravity(boolean noGravity);

  boolean isImmuneToFire();

  boolean isOffsetPositionInLiquid(double x, double y, double z);

  boolean isWet();

  boolean isInWaterRainOrBubbleColumn();

  boolean isInWaterOrBubbleColumn();

  boolean canSwim();

  void updateSwim();

  boolean handleWaterMovement();

  void spawnRunningParticles();

  boolean isInWater();

  void setInLava();

  boolean isInLava();

  boolean isBurning();

  boolean isPassenger();

  boolean isBeingRidden();

  boolean isSneaking();

  void setSneaking(boolean sneaking);

  default boolean canBeRiddenInWater() {
    return false;
  }

  boolean isSteppingCarefully();

  boolean isSuppressingBounce();

  boolean isDiscrete();

  boolean isDescending();

  boolean isCrouching();

  boolean isSprinting();

  void setSprinting(boolean sprinting);

  boolean isSwimming();

  void setSwimming(boolean swimming);

  boolean isActuallySwimming();

  boolean isVisuallySwimming();

  boolean isGlowing();

  void setGlowing(boolean glowing);

  boolean isInvisible();

  // TODO: 08.10.2020 PlayerEntity
  boolean isInvisibleToPlayer(Object player);

  default boolean isInFluid() {
    return this.isInWater() || this.isInLava();
  }

  boolean canRenderOnFire();

  UUID getUniqueId();

  void setUniqueId(UUID uniqueId);

  String getCachedUniqueId();

  String getScoreboardName();

  default boolean isPushedByWater() {
    return true;
  }

  boolean isCustomNameVisible();

  void setCustomNameVisible(boolean alwaysRenderNameTag);

  default void sendMessage(ChatComponent component, UUID senderUUID) {

  }

  float getEyeHeight(EntityPose pose);

  float getEyeHeight();

  float getBrightness();

  World getWorld();

  Entity getRidingEntity();

  default PushReaction getPushReaction() {
    return PushReaction.NORMAL;
  }

  default SoundCategory getSoundCategory() {
    return SoundCategory.NEUTRAL;
  }

  void setMotion(double x, double y, double z);

  void teleportKeepLoaded(double x, double y, double z);

  void setPositionAndUpdate(double x, double y, double z);

  boolean getAlwaysRenderNameTagForRender();

  void recalculateSize();

  boolean replaceItemInInventory(int slot, ItemStack itemStack);

  boolean isImmuneToExplosions();

  boolean ignoreItemEntityData();

  Entity getControllingPassenger();

  List<Entity> getPassengers();

  boolean isPassenger(Entity entity);

  boolean isPassenger(Class<? extends Entity> passenger);

  Collection<Entity> getRecursivePassengers();

  Stream<Entity> getSelfAndPassengers();

  boolean isOnePlayerRiding();

  Entity getLowestRidingEntity();

  boolean isRidingSameEntity(Entity entity);

  boolean isRidingOrBeingRiddenBy(Entity entity);

  boolean canPassengerSteer();

  boolean hasPermissionLevel(int level);

  float getWidth();

  float getHeight();

  EntitySize getSize();

  BlockPosition getPosition();

  double getPosXWidth(double width);

  double getPosXRandom(double factor);

  double getPosYHeight(double height);

  double getPosYRandom();

  double getPosYEye();

  double getPosZWidth(double width);

  double getPosZRandom(double factor);

  void setRawPosition(double x, double y, double z);

  default void checkDespawn() {
  }

  EntityMapper getEntityMapper();

  /**
   * An enumeration representing all classifications for entities.
   */
  enum Classification {

    MONSTER("monster", 70, false, false),
    CREATURE("creature", 10, true, true),
    AMBIENT("ambient", 15, true, false),
    WATER_CREATURE("water_creature", 15, true, false),
    MISC("misc", 15, true, true);

    private final String name;
    private final int maxNumberOfCreature;
    private final boolean peacefulCreature;
    private final boolean animal;

    Classification(String name, int maxNumberOfCreature, boolean peacefulCreature, boolean animal) {
      this.name = name;
      this.maxNumberOfCreature = maxNumberOfCreature;
      this.peacefulCreature = peacefulCreature;
      this.animal = animal;
    }

    public String getName() {
      return name;
    }

    public int getMaxNumberOfCreature() {
      return maxNumberOfCreature;
    }

    public boolean isPeacefulCreature() {
      return peacefulCreature;
    }

    public boolean isAnimal() {
      return animal;
    }
  }

  @AssistedFactory(Entity.class)
  interface Factory {

    Entity create(
            @Assisted("entity") Object entity,
            @Assisted("entityType") EntityType entityType,
            @Assisted("world") ClientWorld world,
            @Assisted("entityMapper") EntityMapper entityMapper
    );

  }

  interface Provider {

    Entity get(Object entity, EntityType entityType);

  }

}
