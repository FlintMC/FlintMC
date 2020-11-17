package net.flintmc.mcapi.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.name.Nameable;
import net.flintmc.mcapi.entity.reason.MoverType;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityPose;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.block.material.PushReaction;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.scoreboad.team.Team;
import net.flintmc.render.model.RenderContextAware;

import java.util.*;
import java.util.stream.Stream;

/** Represents the Minecraft entity. */
public interface Entity extends Nameable, RenderContextAware<EntityRenderContext> {


  /**
   * Retrieves the team color as an {@link Integer} of this entity.
   *
   * @return The team color as an {@link Integer}.
   */
  int getTeamColor();

  /**
   * Whether the entity is a spectator.
   *
   * @return {@code true} if the entity is a spectator, otherwise {@code false}.
   */
  default boolean isSpectator() {
    return false;
  }

  /**
   * Detach all passengers from this entity and stop the riding if the entity itself is a passenger.
   */
  void detach();

  /**
   * Sets the packet coordinates of this entity.
   *
   * @param x The new `x` position for the packet.
   * @param y The new `y` position for the packet.
   * @param z The new `z` position for the packet.
   */
  void setPacketCoordinates(double x, double y, double z);

  /**
   * Retrieves the type of this entity.
   *
   * @return The entity type.
   */
  EntityType getType();

  /**
   * Retrieves the identifier of this entity.
   *
   * @return The entity identifier.
   */
  int getIdentifier();

  /**
   * Changes the identifier of this entity.
   *
   * @param identifier The new identifier.
   */
  void setIdentifier(int identifier);

  /**
   * Retrieves a collection with all tags of this entity.
   *
   * @return A collection with all tags.
   */
  Set<String> getTags();

  /**
   * Adds a tag to the tags collection.
   *
   * @param tag The tag to be added.
   * @return {@code true} if the tag was added, otherwise {@code false}.
   */
  boolean addTag(String tag);

  /**
   * Removes a tag form the tag collection.
   *
   * @param tag The tag to be removed.
   * @return {@code true} if the tag was removed, otherwise {@code false}.
   */
  boolean removeTag(String tag);

  /**
   * Retrieves the `x` position of this entity.
   *
   * @return The `x` position.
   */
  double getPosX();

  /**
   * Retrieves the `y` position of this entity.
   *
   * @return The `y` position.
   */
  double getPosY();

  /**
   * Retrieves the `z` position of this entity.
   *
   * @return The `z` position.
   */
  double getPosZ();

  /** Removes this entity from the world. */
  void remove();

  /**
   * Retrieves the pose of this entity.
   *
   * @return The entity pose.
   */
  EntityPose getPose();

  /**
   * Changes the position of this entity.
   *
   * @param x The new `x` position.
   * @param y The new `y` position.
   * @param z The new `z` position.
   */
  void setPosition(double x, double y, double z);

  /**
   * Changes the position and rotation of this entity.
   *
   * @param x The new `x` position.
   * @param y The new `y` position.
   * @param z The new `z` position.
   * @param yaw The new `yaw` rotation.
   * @param pitch The new `pitch` rotation.
   */
  void setPositionAndRotation(double x, double y, double z, float yaw, float pitch);

  /**
   * Moves the entity ot the block position and the angeles.
   *
   * @param position The block position for the entity.
   * @param rotationYaw The yaw rotation.
   * @param rotationPitch The pitch rotation.
   */
  void moveToBlockPosAndAngles(BlockPosition position, float rotationYaw, float rotationPitch);

  /**
   * Changes the location and the angles of this entity.
   *
   * @param x The new `x` location.
   * @param y The new `y` location.
   * @param z The new `z` location.
   * @param yaw The new `yaw` angle.
   * @param pitch The new `pitch` angle.
   */
  void setLocationAndAngles(double x, double y, double z, float yaw, float pitch);

  /**
   * Forces change the position of this entity.
   *
   * @param x The forced `x` position.
   * @param y The forced `y` position.
   * @param z The forced `z` position.
   */
  void forceSetPosition(double x, double y, double z);

  /**
   * Retrieves the distance to the given {@link Entity}.
   *
   * @param entity The entity to calculate the distance.
   * @return The calculated distance to the given {@link Entity}.
   */
  float getDistance(Entity entity);

  /**
   * Retrieves the squared distance of the given parameters.
   *
   * @param x The `x` position.
   * @param y The `y` position.
   * @param z The `z` position.
   * @return The calculated squared distance.
   */
  double getDistanceSq(double x, double y, double z);

  /**
   * Retrieves the squared distance of the given entity.
   *
   * @param entity The entity to calculate the squared distance.
   * @return The calcuatled squared distance.
   */
  double getDistanceSq(Entity entity);

  /**
   * Applies the entity collision to the given entity.
   *
   * @param entity The entity to apply the collision.
   */
  void applyEntityCollision(Entity entity);

  /**
   * Adds velocity to this entity.
   *
   * @param x The `x` velocity.
   * @param y The `y` velocity.
   * @param z The `z` velocity.
   */
  void addVelocity(double x, double y, double z);

  /**
   * Rotates this entity in a direction.
   *
   * @param yaw The `yaw` of this rotation.
   * @param pitch The `pitch` of this rotation.
   */
  void rotateTowards(double yaw, double pitch);

  /**
   * Retrieves the pitch of this entity.
   *
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *     full tick.
   * @return The entity pitch.
   */
  float getPitch(float partialTicks);

  /**
   * Retrieves the pitch of this entity.
   *
   * @return The entity pitch.
   */
  float getPitch();

  /**
   * Changes the pitch of this entity.
   *
   * @param pitch The new entity pitch.
   */
  void setPitch(float pitch);

  /**
   * Retrieves the yaw of this entity.
   *
   * @param partialTicks The period of time, in fractions of a tick, that has passed since the last
   *     full tick.
   * @return The entity yaw.
   */
  float getYaw(float partialTicks);

  /**
   * Retrieves the pitch of this entity.
   *
   * @return The entity yaw.
   */
  float getYaw();

  /**
   * Changes the yaw of this entity.
   *
   * @param yaw The new entity yaw.
   */
  void setYaw(float yaw);

  /**
   * Retrieves the maximal in portal time of this entity.
   *
   * @return The entity maximal in portal time.
   */
  int getMaxInPortalTime();

  /**
   * Changes the seconds of this fire timer.
   *
   * @param seconds The new seconds for this fire time.
   */
  void setFire(int seconds);

  /**
   * Retrieves the fire tick timer.
   *
   * @return The fire tick timer.
   */
  int getFireTimer();

  /**
   * Changes the ticks of this fire timer.
   *
   * @param ticks The new ticks for the fire timer.
   */
  void setFireTimer(int ticks);

  /** Sets the fire timer to {@code 0}. */
  void extinguish();

  /** Resets the position to the bounding box of this entity. */
  void resetPositionToBB();

  /**
   * Plays a sound at the position of this entity.
   *
   * @param sound The sound to be played.
   * @param volume The volume of this sound.
   * @param pitch The pitch of this sound.
   */
  void playSound(Sound sound, float volume, float pitch);

  /**
   * Whether the entity is silent.
   *
   * @return {@code true} if the entity is silent, otherwise {@code false}.
   */
  boolean isSilent();

  /**
   * Changes the silent of this entity.
   *
   * @param silent {@code true} if the entity should be silenced, otherwise {@code false}.
   */
  void setSilent(boolean silent);

  /**
   * Whether the entity has no gravity.
   *
   * @return {@code true} if the entity has no gravity, otherwise {@code false}.
   */
  boolean hasNoGravity();

  /**
   * Changes the no gravity of this entity.
   *
   * @param noGravity {@code true} if the entity should have no gravity, otherwise {@code false}.
   */
  void setNoGravity(boolean noGravity);

  /**
   * Whether the entity is immune to fire.
   *
   * @return {@code true} if the entity is immune to fire, otherwise {@code false}.
   */
  boolean isImmuneToFire();

  /**
   * Whether the entity offset position is in liquid.
   *
   * @param x The x position.
   * @param y The y position.
   * @param z The z position.
   * @return {@code true} if the entity offset position is in liquid.
   */
  boolean isOffsetPositionInLiquid(double x, double y, double z);

  /**
   * Whether the entity is wet.
   *
   * @return {@code true} if the entity is wet, otherwise {@code false}.
   */
  boolean isWet();

  /**
   * Whether the entity is in water rain or bubble column.
   *
   * @return {@code true} if the entity is in water rain or bubble column, otherwise {@code false}.
   */
  boolean isInWaterRainOrBubbleColumn();

  /**
   * Whether the entity is in water or bubble column.
   *
   * @return {@code true} if the entity is in water or bubble column, otherwise {@code false}.
   */
  boolean isInWaterOrBubbleColumn();

  /**
   * Whether the entity can swim.
   *
   * @return {@code true} if the entity can swim, otherwise {@code false}.
   */
  boolean canSwim();

  /** Updates the swim state of this entity. */
  void updateSwim();

  /**
   * Whether the entity handles water movement.
   *
   * @return {@code true} if the entity handles water movement, otherwise {@code false}.
   */
  boolean handleWaterMovement();

  /** Spawn running particles at the entity position. */
  void spawnRunningParticles();

  /**
   * Whether the entity is in water.
   *
   * @return {@code true} if the entity is in water, otherwise {@code false}.
   */
  boolean isInWater();

  /** Sets the entity in lava. */
  void setInLava();

  /**
   * Whether the entity is in lava.
   *
   * @return {@code true} if the entity is in lava, otherwise {@code false}.
   */
  boolean isInLava();

  /**
   * Whether the entity is burning.
   *
   * @return {@code true} if the entity is burning, otherwise {@code false}.
   */
  boolean isBurning();

  /**
   * Whether the entity is a passenger.
   *
   * @return {@code true} if the entity is a passenger, otherwise {@code false}.
   */
  boolean isPassenger();

  /**
   * Whether the entity is being ridden.
   *
   * @return {@code true} if the entity is being ridden, otherwise {@code false}.
   */
  boolean isBeingRidden();

  /**
   * Whether the entity is sneaking.
   *
   * @return {@code true} if the entity is sneaking, otherwise {@code false}.
   */
  boolean isSneaking();

  /**
   * Changes the sneaking of this entity.
   *
   * @param sneaking {@code true} if the entity should be sneaked, otherwise {@code false}.
   */
  void setSneaking(boolean sneaking);

  /**
   * Whether the entity can be ridden in water.
   *
   * @return {@code true} if the entity can be ridden in water, otherwise {@code false}.
   */
  default boolean canBeRiddenInWater() {
    return false;
  }

  /**
   * Whether the entity is stepping carefully.
   *
   * @return {@code true} entity is stepping carefully, otherwise {@code false}.
   */
  boolean isSteppingCarefully();

  /**
   * Whether the entity is suppressing bounce.
   *
   * @return {@code true} if the entity is suppressing bounce.
   */
  boolean isSuppressingBounce();

  /**
   * Whether the entity is discrete.
   *
   * @return {@code true} if the entity is discrete, otherwise {@code false}.
   */
  boolean isDiscrete();

  /**
   * Whether the entity is descending.
   *
   * @return {@code true} if the entity is descending, otherwise {@code false}.
   */
  boolean isDescending();

  /**
   * Whether the entity is crouching.
   *
   * @return {@code true} if the entity is crouching, otherwise {@code false}.
   */
  boolean isCrouching();

  /**
   * Whether the entity is sprinting.
   *
   * @return {@code true} if the entity is sprinting, otherwise {@code false}.
   */
  boolean isSprinting();

  /**
   * Changes the sprinting of this entity.
   *
   * @param sprinting {@code true} if the entity should be sprint, otherwise {@code false}.
   */
  void setSprinting(boolean sprinting);

  /**
   * Whether the entity is swimming.
   *
   * @return {@code true} if the entity is swimming, otherwise {@code false}.
   */
  boolean isSwimming();

  /**
   * Changes the swimming of this entity.
   *
   * @param swimming {@code true} if the entity should be swim, otherwise {@code false}.
   */
  void setSwimming(boolean swimming);

  /**
   * Whether the entity is actually swimming.
   *
   * @return {@code true} if the entity is actually swimming, otherwise {@code false}.
   */
  boolean isActuallySwimming();

  /**
   * Whether the entity is visually swimming.
   *
   * @return {@code true} if the entity is visually swimming, otherwise {@code false}.
   */
  boolean isVisuallySwimming();

  /**
   * Whether the entity is glowing.
   *
   * @return {@code true} if the entity is glowing, otherwise {@code false}.
   */
  boolean isGlowing();

  /**
   * Changes the glowing of this entity.
   *
   * @param glowing {@code true} if the entity should be glow, otherwise {@code false}.
   */
  void setGlowing(boolean glowing);

  /**
   * Whether the entity is invisible.
   *
   * @return {@code true} if the entity is invisible, otherwise {@code false}.
   */
  boolean isInvisible();

  /**
   * Whether the entity is invisible to given player.
   *
   * @param player The player to be checked.
   * @return {@code true} if the entity is invisible to the given player.
   */
  boolean isInvisibleToPlayer(PlayerEntity player);

  /**
   * Whether the entity is in fluid.
   *
   * @return {@code true} if the entity is in fluid, otherwise {@code false}.
   */
  default boolean isInFluid() {
    return this.isInWater() || this.isInLava();
  }

  /**
   * Whether the entity can render on fire.
   *
   * @return {@code true} if the entity can render on fire, otherwise {@code false}.
   */
  boolean canRenderOnFire();

  /**
   * Retrieves the unique identifier of this entity.
   *
   * @return The unique identifier of this entity.
   */
  UUID getUniqueId();

  /**
   * Changes the unique identifier of this entity.
   *
   * @param uniqueId The new unique identifier.
   */
  void setUniqueId(UUID uniqueId);

  /**
   * Retrieves the cached unique identifier as a {@link String} of this entity.
   *
   * @return The cached unique identifier.
   */
  String getCachedUniqueId();

  /**
   * Retrieves the scoreboard name of this entity.
   *
   * @return The scoreboard name.
   */
  String getScoreboardName();

  /**
   * Whether if the entity is pushed by water.
   *
   * @return {@code true} if the entity is pushed by water, otherwise {@code false}.
   */
  default boolean isPushedByWater() {
    return true;
  }

  /**
   * Whether the custom name is visible.
   *
   * @return {@code true} if the custom name is visible, otherwise {@code false}.
   */
  boolean isCustomNameVisible();

  /**
   * Changes the visibility of the custom name of this entity.
   *
   * @param alwaysRenderNameTag {@code true} if the custom name should be rendered, otherwise {@code
   *     false}.
   */
  void setCustomNameVisible(boolean alwaysRenderNameTag);

  /**
   * Sends a message to the player chat.
   *
   * @param component The component to be sent.
   * @param senderUUID The sender unique identifier of this entity.
   */
  default void sendMessage(ChatComponent component, UUID senderUUID) {}

  /**
   * Retrieves the eye height of this entity by the given {@link EntityPose}.
   *
   * @param pose The pose of this entity.
   * @return The entity eye height with the pose.
   */
  float getEyeHeight(EntityPose pose);

  /**
   * Retrieves the eye height of this entity.
   *
   * @return The entity eye height.
   */
  float getEyeHeight();

  /**
   * Retrieves the brightness of this entity.
   *
   * @return The entity brightness.
   */
  float getBrightness();

  /**
   * Retrieves the world of this entity.
   *
   * @return The entity world.
   */
  World getWorld();

  /**
   * Retrieves the riding entity of this entity.
   *
   * @return The riding entity.
   */
  Entity getRidingEntity();

  /**
   * Retrievse the push reaction of this entity.
   *
   * @return The entity push reaction.
   */
  default PushReaction getPushReaction() {
    return PushReaction.NORMAL;
  }

  /**
   * Retrieves the sound category of this entity.
   *
   * @return The entity sound category.
   */
  default SoundCategory getSoundCategory() {
    return SoundCategory.NEUTRAL;
  }

  /**
   * Changes the motion of this entity.
   *
   * @param x The new `x` motion.
   * @param y The new `y` motion.
   * @param z The new `z` motion.
   */
  void setMotion(double x, double y, double z);

  /**
   * Teleport the entity to the keep loaded position.
   *
   * @param x The x position.
   * @param y The x position.
   * @param z The x position.
   */
  void teleportKeepLoaded(double x, double y, double z);

  /**
   * Changes the position of this entity and updates it.
   *
   * @param x The new `x` position.
   * @param y The new `y` position.
   * @param z The new `z` position.
   */
  void setPositionAndUpdate(double x, double y, double z);

  /**
   * Whether the name tag for the entity is always render.
   *
   * @return {@code true} if the name tag for the entity is always render, otherwise {@code false}.
   */
  boolean isAlwaysRenderNameTagForRender();

  /** Recalculates the size of this entity. */
  void recalculateSize();

  /**
   * Whether replaces item in the entity inventory.
   *
   * @param slot The slot to replaced.
   * @param itemStack The new item stack.
   * @return {@code true} if was the item stack replaced, otherwise {@code false}.
   */
  boolean replaceItemInInventory(int slot, ItemStack itemStack);

  /**
   * Whether the entity is immune to explosions.
   *
   * @return {@code true} if the entity is immune to explosions, otherwise {@code false}.
   */
  boolean isImmuneToExplosions();

  /**
   * Whether the entity is ignoring the item entity data.
   *
   * @return {@code true} if the entity is ignoring the item entity data, otherwise {@code false}.
   */
  boolean ignoreItemEntityData();

  /**
   * Retrieves the controlling passenger.
   *
   * @return The controlling passenger.
   */
  Entity getControllingPassenger();

  /**
   * Retrieves a collection with all passengers of this entity.
   *
   * @return A collection with all passengers of this entity.
   */
  List<Entity> getPassengers();

  /**
   * Whether the given entity is a passenger of this entity.
   *
   * @param entity The entity to be checked.
   * @return {@code true} if the given entity is a passenger, otherwise {@code false}.
   */
  boolean isPassenger(Entity entity);

  /**
   * Retrieves a collection with all recursive passengers of this entity.
   *
   * @return A collection with al recursive passengers.
   */
  Collection<Entity> getRecursivePassengers();

  /**
   * Retrieves a sequence of the entity and their passengers.
   *
   * @return A sequence of the entity and their passengers.
   */
  Stream<Entity> getSelfAndPassengers();

  /**
   * Whether one player is riding this entity.
   *
   * @return {@code true} if one player is riding this entity, otherwise {@code false}.
   */
  boolean isOnePlayerRiding();

  /**
   * Retrieves the lowest riding entity of this entity.
   *
   * @return The lowest riding entity.
   */
  Entity getLowestRidingEntity();

  /**
   * Whether the entity is riding the same entity.
   *
   * @param entity The entity to be checked.
   * @return {@code true} if the entity is riding the same entity, otherwise {@code false}.
   */
  boolean isRidingSameEntity(Entity entity);

  /**
   * Whether the entity is riding or being ridden by the given entity.
   *
   * @param entity The entity to be checked.
   * @return {@code true} if the entity is riding or being ridden by the given entity, otherwise
   *     {@code false}.
   */
  boolean isRidingOrBeingRiddenBy(Entity entity);

  /**
   * Whether the passenger can steer this entity.
   *
   * @return {@code true} if the passenger can steer this entity, otherwise {@code false}.
   */
  boolean canPassengerSteer();

  /**
   * Whether the entity has the permission level.
   *
   * @param level The level to be checked.
   * @return {@code true} if the entity hash the permission level, otherwise {@code false}.
   */
  boolean hasPermissionLevel(int level);

  /**
   * Retrieves the width of this entity.
   *
   * @return The entity width.
   */
  float getWidth();

  /**
   * Retrieves the height of this entity.
   *
   * @return The entity height.
   */
  float getHeight();

  /**
   * Retrieves the size of this entity.
   *
   * @return The entity size.
   */
  EntitySize getSize();

  /**
   * Retrieves the current block position of this entity.
   *
   * @return The current block position.
   */
  BlockPosition getPosition();

  /**
   * Retrieves the `x` width position of this entity.
   *
   * @param width The width to multiply.
   * @return The `x` width position.
   */
  double getPosXWidth(double width);

  /**
   * Retrieves a random `x` position of this entity.
   *
   * @param factor The factor to multiply.
   * @return A random `x` position.
   */
  double getPosXRandom(double factor);

  /**
   * Retrieves the `y` height position of this entity.
   *
   * @param height The height to multiply.
   * @return The `y` height position.
   */
  double getPosYHeight(double height);

  /**
   * Retrieves a random `y` position of this entity.
   *
   * @return A random `y` position.
   */
  double getPosYRandom();

  /**
   * Retrieves the `y` position of the entity eyes.
   *
   * @return The `y` position of the eyes.
   */
  double getPosYEye();

  /**
   * Retrieves the `z` width position of this entity.
   *
   * @param width The width to multiply.
   * @return The `z` width position.
   */
  double getPosZWidth(double width);

  /**
   * Retrieves a random `z` position of this entity.
   *
   * @param factor The factor to multiply.
   * @return A random `z` position.
   */
  double getPosZRandom(double factor);

  /**
   * Changes the raw position of this entity.
   *
   * @param x The new `x` position.
   * @param y The new `y` position.
   * @param z The new `z` position.
   */
  void setRawPosition(double x, double y, double z);

  /**
   * Whether the entity is invulnerable.
   *
   * @return {@code true} if the entity is invulnerable, otherwise {@code false}.
   */
  boolean isInvulnerable();

  /**
   * Changes the invulnerable of this entity.
   *
   * @param invulnerable {@code true} if the entity should be invulnerable, otherwise {@code false}.
   */
  void setInvulnerable(boolean invulnerable);

  /**
   * Retrieves the team of this entity.
   *
   * @return The entity team or {@code null}.
   */
  Team getTeam();

  /**
   * Whether the entity is in the same team.
   *
   * @param entity The entity to be checked.
   * @return {@code true} if the entity is in the same team, otherwise {@code false}.
   */
  boolean isInSameTeam(Entity entity);

  /**
   * Whether the team is in the same scoreboard team.
   *
   * @param team The team to be checked.
   * @return {@code true} if the team is in the same scoreboard team.
   */
  boolean isInScoreboardTeam(Team team);

  /**
   * Whether the entity is alive.
   *
   * @return {@code true} if the entity is alive, otherwise {@code false}.
   */
  boolean isAlive();

  /**
   * Moves the entity.
   *
   * @param moverType The type for the move.
   * @param vector3D The vector for the move.
   */
  void move(MoverType moverType, Vector3D vector3D);

  /**
   * Whether the entity is collided horizontally or vertically.
   *
   * @return {@code true} if the entity is collided horizontally or vertically, otherwise {@code
   *     false}.
   */
  default boolean isCollided() {
    return this.isCollidedHorizontally() || this.isCollidedVertically();
  }

  /**
   * Whether the entity is collided horizontally.
   *
   * @return {@code true} if the entity is collided horizontally, otherwise {@code false}.
   */
  boolean isCollidedHorizontally();

  /**
   * Changes whether the entity is collided horizontally.
   *
   * @param horizontally {@code true} if the entity should be collided horizontally, otherwise
   *     {@code false}.
   */
  void setCollidedHorizontally(boolean horizontally);

  /**
   * Whether the entity is collided vertically.
   *
   * @return {@code true} if the entity is collided vertically, otherwise {@code false}.
   */
  boolean isCollidedVertically();

  /**
   * Changes whether the entity is collied vertically.
   *
   * @param vertically {@code true} if the etity should be collided vertically, otherwise {@code
   *     false}.
   */
  void setCollidedVertically(boolean vertically);

  /**
   * Retrieves the chunk coordinate x of this entity.
   *
   * @return The x chunk coordinate.
   */
  int getChunkCoordinateX();

  /**
   * Retrieves the chunk coordinate y of this entity.
   *
   * @return The y chunk coordinate.
   */
  int getChunkCoordinateY();

  /**
   * Retrieves the chunk coordinate z of this entity.
   *
   * @return The z chunk coordinate.
   */
  int getChunkCoordinateZ();

  /**
   * Whether the entity is on ground.
   *
   * @return {@code true} if the entity is on ground, otherwise {@code false}.
   */
  boolean isOnGround();

  /**
   * Changes whether the entity is on ground.
   *
   * @param onGround {@code true} if the entity should be on ground, otherwise {@code false}.
   */
  void setOnGround(boolean onGround);

  /** Checks if the entity must be removed. */
  default void checkDespawn() {}

  /**
   * Reads additional named binary compound tag.
   *
   * @param compound The named binary compound to read.
   */
  default void readAdditional(NBTCompound compound) {}

  /**
   * Writes additional named binary compound tag.
   *
   * @param compound The named binary compound to write.
   */
  default void writeAdditional(NBTCompound compound) {}

  /**
   * Retrieves the random of this entity.
   *
   * @return The random of this entity.
   */
  Random getRandom();

  /**
   * Retrieves an entity mapper to map entity relevant things.
   *
   * @return An entity mapper.
   */
  EntityFoundationMapper getEntityFoundationMapper();

  /** An enumeration representing all classifications for entities. */
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

    /**
     * Retrieves the category name of this classificaiton.
     *
     * @return The classification category name.
     */
    public String getName() {
      return name;
    }

    /**
     * Retrieves the maximal number of the creature.
     *
     * @return The maximal number of the creature.
     */
    public int getMaxNumberOfCreature() {
      return maxNumberOfCreature;
    }

    /**
     * Whether the classification is a peaceful creature.
     *
     * @return {@code true} if the classification is a peaceful creature.
     */
    public boolean isPeacefulCreature() {
      return peacefulCreature;
    }

    /**
     * Whether the classification is an animal.
     *
     * @return {@code true} if the classification is an animal, otherwise {@code false}.
     */
    public boolean isAnimal() {
      return animal;
    }
  }

  /** A factory class for the {@link Entity}. */
  @AssistedFactory(Entity.class)
  interface Factory {

    /**
     * Creates an {@link Entity} with the given parameters.
     *
     * @param entity The non-null minecraft entity.
     * @param entityType The type of the entity.
     * @return A created entity.
     */
    Entity create(@Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);
  }
}
