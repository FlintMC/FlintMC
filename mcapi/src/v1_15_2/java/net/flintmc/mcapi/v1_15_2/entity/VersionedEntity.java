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

package net.flintmc.mcapi.v1_15_2.entity;

import com.google.common.collect.Sets;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.EntityNotLoadedException;
import net.flintmc.mcapi.entity.EntitySize;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.reason.MoverType;
import net.flintmc.mcapi.entity.type.EntityPose;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.scoreboad.team.Team;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Implement(value = Entity.class, version = "1.15.2")
public class VersionedEntity implements Entity {

  private final EntityType entityType;
  private final World world;
  private final EntityFoundationMapper entityFoundationMapper;

  private final Supplier<Object> entitySupplier;
  private final Random random;

  @AssistedInject
  public VersionedEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    this(() -> entity, entityType, world, entityFoundationMapper);
  }

  protected VersionedEntity(
      Supplier<Object> entitySupplier,
      EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    this.entityType = entityType;
    this.world = world;
    this.entityFoundationMapper = entityFoundationMapper;

    this.random = new Random();
    this.entitySupplier = entitySupplier;
  }

  protected net.minecraft.entity.Entity wrapped() {
    Object entity = this.entitySupplier.get();

    if (entity == null) {
      throw EntityNotLoadedException.INSTANCE;
    }

    if (!(entity instanceof net.minecraft.entity.Entity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.Entity.class.getName());
    }

    return (net.minecraft.entity.Entity) entity;
  }

  /** {@inheritDoc} */
  @Override
  public int getTeamColor() {
    return this.wrapped().getTeamColor();
  }

  /** {@inheritDoc} */
  @Override
  public void detach() {
    this.wrapped().detach();
  }

  /** {@inheritDoc} */
  @Override
  public void setPacketCoordinates(double x, double y, double z) {
    this.wrapped().setPacketCoordinates(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public EntityType getType() {
    return this.entityType;
  }

  /** {@inheritDoc} */
  @Override
  public int getIdentifier() {
    return this.wrapped().getEntityId();
  }

  /** {@inheritDoc} */
  @Override
  public void setIdentifier(int identifier) {
    this.wrapped().setEntityId(identifier);
  }

  /** {@inheritDoc} */
  @Override
  public Set<String> getTags() {
    return this.wrapped().getTags();
  }

  /** {@inheritDoc} */
  @Override
  public boolean addTag(String tag) {
    return this.wrapped().addTag(tag);
  }

  /** {@inheritDoc} */
  @Override
  public boolean removeTag(String tag) {
    return this.wrapped().removeTag(tag);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosX() {
    return this.wrapped().getPosX();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosY() {
    return this.wrapped().getPosY();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZ() {
    return this.wrapped().getPosZ();
  }

  /** {@inheritDoc} */
  @Override
  public void remove() {
    this.wrapped().remove();
  }

  /** {@inheritDoc} */
  @Override
  public EntityPose getPose() {
    return this.entityFoundationMapper.fromMinecraftPose(this.wrapped().getPose());
  }

  /** {@inheritDoc} */
  @Override
  public void setPosition(double x, double y, double z) {
    this.wrapped().setPosition(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    this.wrapped().setPositionAndRotation(x, y, z, yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public void moveToBlockPosAndAngles(
      BlockPosition position, float rotationYaw, float rotationPitch) {
    this.wrapped()
        .moveToBlockPosAndAngles(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position), rotationYaw, rotationPitch);
  }

  /** {@inheritDoc} */
  @Override
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    this.wrapped().setLocationAndAngles(x, y, z, yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public void forceSetPosition(double x, double y, double z) {
    this.wrapped().forceSetPosition(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public float getDistance(Entity entity) {
    float distanceX = (float) (this.getPosX() - entity.getPosX());
    float distanceY = (float) (this.getPosY() - entity.getPosY());
    float distanceZ = (float) (this.getPosZ() - entity.getPosZ());
    return MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
  }

  /** {@inheritDoc} */
  @Override
  public double getDistanceSq(double x, double y, double z) {
    return this.wrapped().getDistanceSq(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public double getDistanceSq(Entity entity) {
    return this.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ());
  }

  /** {@inheritDoc} */
  @Override
  public void applyEntityCollision(Entity entity) {
    this.wrapped()
        .applyEntityCollision(
            (net.minecraft.entity.Entity)
                this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void addVelocity(double x, double y, double z) {
    this.wrapped().addVelocity(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void rotateTowards(double yaw, double pitch) {
    this.wrapped().rotateTowards(yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch(float partialTicks) {
    return this.wrapped().getPitch(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch() {
    return this.wrapped().rotationPitch;
  }

  /** {@inheritDoc} */
  @Override
  public void setPitch(float pitch) {
    this.wrapped().rotationPitch = pitch;
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw(float partialTicks) {
    return this.wrapped().getYaw(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw() {
    return this.wrapped().rotationYaw;
  }

  /** {@inheritDoc} */
  @Override
  public void setYaw(float yaw) {
    this.wrapped().rotationYaw = yaw;
  }

  /** {@inheritDoc} */
  @Override
  public int getMaxInPortalTime() {
    return this.wrapped().getMaxInPortalTime();
  }

  /** {@inheritDoc} */
  @Override
  public void setFire(int seconds) {
    this.wrapped().setFire(seconds);
  }

  /** {@inheritDoc} */
  @Override
  public int getFireTimer() {
    return this.wrapped().getFireTimer();
  }

  /** {@inheritDoc} */
  @Override
  public void setFireTimer(int ticks) {
    this.wrapped().setFireTimer(ticks);
  }

  /** {@inheritDoc} */
  @Override
  public void extinguish() {
    this.wrapped().extinguish();
  }

  /** {@inheritDoc} */
  @Override
  public void resetPositionToBB() {
    this.wrapped().resetPositionToBB();
  }

  /** {@inheritDoc} */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.wrapped()
        .playSound(
            (SoundEvent)
                this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            volume,
            pitch);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSilent() {
    return this.wrapped().isSilent();
  }

  /** {@inheritDoc} */
  @Override
  public void setSilent(boolean silent) {
    this.wrapped().setSilent(silent);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasNoGravity() {
    return this.wrapped().hasNoGravity();
  }

  /** {@inheritDoc} */
  @Override
  public void setNoGravity(boolean noGravity) {
    this.wrapped().setNoGravity(noGravity);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isImmuneToFire() {
    return this.wrapped().isImmuneToFire();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    return this.wrapped().isOffsetPositionInLiquid(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isWet() {
    return this.wrapped().isWet();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWaterRainOrBubbleColumn() {
    return this.wrapped().isInWaterRainOrBubbleColumn();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWaterOrBubbleColumn() {
    return this.wrapped().isInWaterOrBubbleColumn();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canSwim() {
    return this.wrapped().canSwim();
  }

  /** {@inheritDoc} */
  @Override
  public void updateSwim() {
    this.wrapped().updateSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean handleWaterMovement() {
    return this.wrapped().handleWaterMovement();
  }

  /** {@inheritDoc} */
  @Override
  public void spawnRunningParticles() {
    this.wrapped().spawnRunningParticles();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWater() {
    return this.wrapped().isInWater();
  }

  /** {@inheritDoc} */
  @Override
  public void setInLava() {
    this.wrapped().setInLava();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInLava() {
    return this.wrapped().isInLava();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isBurning() {
    return this.wrapped().isBurning();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPassenger() {
    return this.wrapped().isPassenger();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isBeingRidden() {
    return this.wrapped().isBeingRidden();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSneaking() {
    return this.wrapped().isSneaking();
  }

  /** {@inheritDoc} */
  @Override
  public void setSneaking(boolean sneaking) {
    this.wrapped().setSneaking(sneaking);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSteppingCarefully() {
    return this.wrapped().isSteppingCarefully();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSuppressingBounce() {
    return this.wrapped().isSuppressingBounce();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDiscrete() {
    return this.wrapped().isDiscrete();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDescending() {
    return this.wrapped().isDescending();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCrouching() {
    return this.wrapped().isCrouching();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSprinting() {
    return this.wrapped().isSprinting();
  }

  /** {@inheritDoc} */
  @Override
  public void setSprinting(boolean sprinting) {
    this.wrapped().setSprinting(sprinting);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSwimming() {
    return this.wrapped().isSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void setSwimming(boolean swimming) {
    this.wrapped().setSwimming(swimming);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActuallySwimming() {
    return this.wrapped().isActualySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isVisuallySwimming() {
    return this.wrapped().isVisuallySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isGlowing() {
    return this.wrapped().isGlowing();
  }

  /** {@inheritDoc} */
  @Override
  public void setGlowing(boolean glowing) {
    this.wrapped().setGlowing(glowing);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInvisible() {
    return this.wrapped().isInvisible();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInvisibleToPlayer(net.flintmc.mcapi.player.PlayerEntity player) {
    return this.wrapped().isInvisibleToPlayer((PlayerEntity) player);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canRenderOnFire() {
    return this.wrapped().canRenderOnFire();
  }

  /** {@inheritDoc} */
  @Override
  public UUID getUniqueId() {
    return this.wrapped().getUniqueID();
  }

  /** {@inheritDoc} */
  @Override
  public void setUniqueId(UUID uniqueId) {
    this.wrapped().setUniqueId(uniqueId);
  }

  /** {@inheritDoc} */
  @Override
  public String getCachedUniqueId() {
    return this.wrapped().getCachedUniqueIdString();
  }

  /** {@inheritDoc} */
  @Override
  public String getScoreboardName() {
    return this.wrapped().getScoreboardName();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCustomNameVisible() {
    return this.wrapped().isCustomNameVisible();
  }

  /** {@inheritDoc} */
  @Override
  public void setCustomNameVisible(boolean alwaysRenderNameTag) {
    this.wrapped().setCustomNameVisible(alwaysRenderNameTag);
  }

  /** {@inheritDoc} */
  @Override
  public float getEyeHeight(EntityPose pose) {
    return this.wrapped().getEyeHeight((Pose) this.entityFoundationMapper.toMinecraftPose(pose));
  }

  /** {@inheritDoc} */
  @Override
  public float getEyeHeight() {
    return this.wrapped().getEyeHeight();
  }

  /** {@inheritDoc} */
  @Override
  public float getBrightness() {
    return this.wrapped().getBrightness();
  }

  /** {@inheritDoc} */
  @Override
  public World getWorld() {
    return this.world;
  }

  /** {@inheritDoc} */
  @Override
  public Entity getRidingEntity() {
    return this.entityFoundationMapper
        .getEntityMapper()
        .fromMinecraftEntity(this.wrapped().getRidingEntity());
  }

  /** {@inheritDoc} */
  @Override
  public void setMotion(double x, double y, double z) {
    this.wrapped().setMotion(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void teleportKeepLoaded(double x, double y, double z) {
    this.wrapped().teleportKeepLoaded(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void setPositionAndUpdate(double x, double y, double z) {
    this.wrapped().setPositionAndUpdate(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlwaysRenderNameTagForRender() {
    return this.wrapped().getAlwaysRenderNameTagForRender();
  }

  /** {@inheritDoc} */
  @Override
  public void recalculateSize() {
    this.wrapped().recalculateSize();
  }

  /** {@inheritDoc} */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return this.wrapped()
        .replaceItemInInventory(
            slot,
            (net.minecraft.item.ItemStack)
                this.entityFoundationMapper.getItemMapper().toMinecraft(itemStack));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isImmuneToExplosions() {
    return this.wrapped().isImmuneToExplosions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean ignoreItemEntityData() {
    return this.wrapped().ignoreItemEntityData();
  }

  /** {@inheritDoc} */
  @Override
  public Entity getControllingPassenger() {
    return this.entityFoundationMapper
        .getEntityMapper()
        .fromMinecraftEntity(this.wrapped().getControllingPassenger());
  }

  /** {@inheritDoc} */
  @Override
  public List<Entity> getPassengers() {
    List<Entity> passengers = new ArrayList<>();

    for (net.minecraft.entity.Entity passenger : this.wrapped().getPassengers()) {
      passengers.add(this.entityFoundationMapper.getEntityMapper().fromMinecraftEntity(passenger));
    }

    return passengers;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPassenger(Entity entity) {
    return this.wrapped()
        .isPassenger(
            (net.minecraft.entity.Entity)
                this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Entity> getRecursivePassengers() {
    Set<Entity> entities = Sets.newHashSet();
    for (net.minecraft.entity.Entity passenger : this.wrapped().getPassengers()) {
      entities.add(this.entityFoundationMapper.getEntityMapper().fromMinecraftEntity(passenger));
    }

    return entities;
  }

  /** {@inheritDoc} */
  @Override
  public Stream<Entity> getSelfAndPassengers() {
    return Stream.concat(
        Stream.of(this), this.getPassengers().stream().flatMap(Entity::getSelfAndPassengers));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnePlayerRiding() {
    return this.wrapped().isOnePlayerRiding();
  }

  /** {@inheritDoc} */
  @Override
  public Entity getLowestRidingEntity() {
    return this.entityFoundationMapper
        .getEntityMapper()
        .fromMinecraftEntity(this.wrapped().getLowestRidingEntity());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingSameEntity(Entity entity) {
    return this.wrapped()
        .isRidingSameEntity(
            (net.minecraft.entity.Entity)
                this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingOrBeingRiddenBy(Entity entity) {
    return this.wrapped()
        .isRidingOrBeingRiddenBy(
            (net.minecraft.entity.Entity)
                this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPassengerSteer() {
    return this.wrapped().canPassengerSteer();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasPermissionLevel(int level) {
    return this.wrapped().hasPermissionLevel(level);
  }

  /** {@inheritDoc} */
  @Override
  public float getWidth() {
    return this.wrapped().getWidth();
  }

  /** {@inheritDoc} */
  @Override
  public float getHeight() {
    return this.wrapped().getHeight();
  }

  /** {@inheritDoc} */
  @Override
  public EntitySize getSize() {
    return this.getType().getSize();
  }

  /** {@inheritDoc} */
  @Override
  public BlockPosition getPosition() {
    return this.getWorld().fromMinecraftBlockPos(this.wrapped().getPosition());
  }

  /** {@inheritDoc} */
  @Override
  public double getPosXWidth(double width) {
    return this.wrapped().getPosXWidth(width);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosXRandom(double factor) {
    return this.wrapped().getPosXRandom(factor);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYHeight(double height) {
    return this.wrapped().getPosYHeight(height);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYRandom() {
    return this.wrapped().getPosYRandom();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYEye() {
    return this.wrapped().getPosYEye();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZWidth(double width) {
    return this.wrapped().getPosZWidth(width);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZRandom(double factor) {
    return this.wrapped().getPosZRandom(factor);
  }

  /** {@inheritDoc} */
  @Override
  public void setRawPosition(double x, double y, double z) {
    this.wrapped().setRawPosition(x, y, z);
  }

  @Override
  public boolean isInvulnerable() {
    return this.wrapped().isInvulnerable();
  }

  /** {@inheritDoc} */
  @Override
  public void setInvulnerable(boolean invulnerable) {
    this.wrapped().setInvulnerable(invulnerable);
  }

  /** {@inheritDoc} */
  @Override
  public Team getTeam() {
    return this.getWorld().getScoreboard().getPlayerTeam(this.getScoreboardName());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInSameTeam(Entity entity) {
    return this.isInScoreboardTeam(entity.getTeam());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInScoreboardTeam(Team team) {
    return this.getTeam() != null && this.getTeam().isSameTeam(team);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlive() {
    return this.wrapped().isAlive();
  }

  /** {@inheritDoc} */
  @Override
  public void move(MoverType moverType, Vector3D vector3D) {
    this.wrapped()
        .move(
            (net.minecraft.entity.MoverType)
                this.entityFoundationMapper.toMinecraftMoverType(moverType),
            new Vec3d(vector3D.getX(), vector3D.getY(), vector3D.getZ()));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCollidedHorizontally() {
    return this.wrapped().collidedHorizontally;
  }

  /** {@inheritDoc} */
  @Override
  public void setCollidedHorizontally(boolean horizontally) {
    this.wrapped().collidedHorizontally = horizontally;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCollidedVertically() {
    return this.wrapped().collidedVertically;
  }

  /** {@inheritDoc} */
  @Override
  public void setCollidedVertically(boolean vertically) {
    this.wrapped().collidedVertically = vertically;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateX() {
    return this.wrapped().chunkCoordX;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateY() {
    return this.wrapped().chunkCoordY;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateZ() {
    return this.wrapped().chunkCoordZ;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnGround() {
    return this.wrapped().onGround;
  }

  /** {@inheritDoc} */
  @Override
  public void setOnGround(boolean onGround) {
    this.wrapped().onGround = onGround;
  }

  /** {@inheritDoc} */
  @Override
  public Random getRandom() {
    return this.random;
  }

  /** {@inheritDoc} */
  @Override
  public EntityFoundationMapper getEntityFoundationMapper() {
    return this.entityFoundationMapper;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getName() {
    return this.entityFoundationMapper.getComponentMapper().fromMinecraft(this.wrapped().getName());
  }
}
