package net.labyfy.internal.component.entity.v1_15_2;

import com.google.common.collect.Sets;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.EntitySize;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.player.type.sound.Sound;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.World;
import net.labyfy.component.world.util.BlockPosition;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.*;
import java.util.stream.Stream;

@Implement(value = Entity.class, version = "1.15.2")
public class VersionedEntity implements Entity {

  private final EntityType entityType;
  private final World world;
  private final EntityMapper entityMapper;

  private final net.minecraft.entity.Entity entity;

  @AssistedInject
  public VersionedEntity(
          @Assisted("entity") Object entity,
          @Assisted("entityType") EntityType entityType,
          @Assisted("world") ClientWorld world,
          @Assisted("entityMapper") EntityMapper entityMapper) {
    this.entityType = entityType;
    this.world = world;
    this.entityMapper = entityMapper;

    if (!(entity instanceof net.minecraft.entity.Entity)) {
      throw new IllegalArgumentException("");
    }

    this.entity = (net.minecraft.entity.Entity) entity;
  }

  @Override
  public int getTeamColor() {
    return this.entity.getTeamColor();
  }

  @Override
  public void detach() {
    this.entity.detach();
  }

  @Override
  public void setPacketCoordinates(double x, double y, double z) {
    this.entity.setPacketCoordinates(x, y, z);
  }

  @Override
  public EntityType getType() {
    return this.entityType;
  }

  @Override
  public int getIdentifier() {
    return this.entity.getEntityId();
  }

  @Override
  public void setIdentifier(int identifier) {
    this.entity.setEntityId(identifier);
  }

  @Override
  public Set<String> getTags() {
    return this.entity.getTags();
  }

  @Override
  public boolean addTag(String tag) {
    return this.entity.addTag(tag);
  }

  @Override
  public boolean removeTag(String tag) {
    return this.entity.removeTag(tag);
  }

  @Override
  public double getPosX() {
    return this.entity.getPosX();
  }

  @Override
  public double getPosY() {
    return this.entity.getPosY();
  }

  @Override
  public double getPosZ() {
    return this.entity.getPosZ();
  }

  @Override
  public void remove() {
    this.entity.remove();
  }

  @Override
  public EntityPose getPose() {
    return this.entityMapper.fromMinecraftPose(
            this.entity.getPose()
    );
  }

  @Override
  public void setPosition(double x, double y, double z) {
    this.entity.setPosition(x, y, z);
  }

  @Override
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    this.entity.setPositionAndRotation(x, y, z, yaw, pitch);
  }

  @Override
  public void moveToBlockPosAndAngles(BlockPosition position, float rotationYaw, float rotationPitch) {
    this.entity.moveToBlockPosAndAngles(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            rotationYaw,
            rotationPitch
    );
  }

  @Override
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    this.entity.setLocationAndAngles(x, y, z, yaw, pitch);
  }

  @Override
  public void forceSetPosition(double x, double y, double z) {
    this.entity.forceSetPosition(x, y, z);
  }

  @Override
  public float getDistance(Entity entity) {
    float distanceX = (float) (this.getPosX() - entity.getPosX());
    float distanceY = (float) (this.getPosY() - entity.getPosY());
    float distanceZ = (float) (this.getPosZ() - entity.getPosZ());
    return MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
  }

  @Override
  public double getDistanceSq(double x, double y, double z) {
    return this.entity.getDistanceSq(x, y, z);
  }

  @Override
  public double getDistanceSq(Entity entity) {
    return this.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ());
  }

  @Override
  public void applyEntityCollision(Entity entity) {
    this.entity.applyEntityCollision((net.minecraft.entity.Entity) this.entityMapper.toMinecraftEntity(entity));
  }

  @Override
  public void addVelocity(double x, double y, double z) {
    this.entity.addVelocity(x, y, z);
  }

  @Override
  public void rotateTowards(double yaw, double pitch) {
    this.entity.rotateTowards(yaw, pitch);
  }

  @Override
  public float getPitch(float partialTicks) {
    return this.entity.getPitch(partialTicks);
  }

  @Override
  public float getYaw(float partialTicks) {
    return this.entity.getYaw(partialTicks);
  }

  @Override
  public int getMaxInPortalTime() {
    return this.entity.getMaxInPortalTime();
  }

  @Override
  public void setFire(int seconds) {
    this.entity.setFire(seconds);
  }

  @Override
  public int getFireTimer() {
    return this.entity.getFireTimer();
  }

  @Override
  public void setFireTimer(int ticks) {
    this.entity.setFireTimer(ticks);
  }

  @Override
  public void extinguish() {
    this.entity.extinguish();
  }

  @Override
  public void resetPositionToBB() {
    this.entity.resetPositionToBB();
  }

  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.entity.playSound(
            (SoundEvent) this.getEntityMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            volume,
            pitch
    );
  }

  @Override
  public boolean isSilent() {
    return this.entity.isSilent();
  }

  @Override
  public void setSilent(boolean silent) {
    this.entity.setSilent(silent);
  }

  @Override
  public boolean hasNoGravity() {
    return this.entity.hasNoGravity();
  }

  @Override
  public void setNoGravity(boolean noGravity) {
    this.entity.setNoGravity(noGravity);
  }

  @Override
  public boolean isImmuneToFire() {
    return this.entity.isImmuneToFire();
  }

  @Override
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    return this.entity.isOffsetPositionInLiquid(x, y, z);
  }

  @Override
  public boolean isWet() {
    return this.entity.isWet();
  }

  @Override
  public boolean isInWaterRainOrBubbleColumn() {
    return this.entity.isInWaterRainOrBubbleColumn();
  }

  @Override
  public boolean isInWaterOrBubbleColumn() {
    return this.entity.isInWaterOrBubbleColumn();
  }

  @Override
  public boolean canSwim() {
    return this.entity.canSwim();
  }

  @Override
  public void updateSwim() {
    this.entity.updateSwimming();
  }

  @Override
  public boolean handleWaterMovement() {
    return this.entity.handleWaterMovement();
  }

  @Override
  public void spawnRunningParticles() {
    this.entity.spawnRunningParticles();
  }

  @Override
  public boolean isInWater() {
    return this.entity.isInWater();
  }

  @Override
  public void setInLava() {
    this.entity.setInLava();
  }

  @Override
  public boolean isInLava() {
    return this.entity.isInLava();
  }

  @Override
  public boolean isBurning() {
    return this.entity.isBurning();
  }

  @Override
  public boolean isPassenger() {
    return this.entity.isPassenger();
  }

  @Override
  public boolean isBeingRidden() {
    return this.entity.isBeingRidden();
  }

  @Override
  public boolean isSneaking() {
    return this.entity.isSneaking();
  }

  @Override
  public void setSneaking(boolean sneaking) {
    this.entity.setSneaking(sneaking);
  }

  @Override
  public boolean isSteppingCarefully() {
    return this.entity.isSteppingCarefully();
  }

  @Override
  public boolean isSuppressingBounce() {
    return this.entity.isSuppressingBounce();
  }

  @Override
  public boolean isDiscrete() {
    return this.entity.isDiscrete();
  }

  @Override
  public boolean isDescending() {
    return this.entity.isDescending();
  }

  @Override
  public boolean isCrouching() {
    return this.entity.isCrouching();
  }

  @Override
  public boolean isSprinting() {
    return this.entity.isSprinting();
  }

  @Override
  public void setSprinting(boolean sprinting) {
    this.entity.setSprinting(sprinting);
  }

  @Override
  public boolean isSwimming() {
    return this.entity.isSwimming();
  }

  @Override
  public void setSwimming(boolean swimming) {
    this.entity.setSwimming(swimming);
  }

  @Override
  public boolean isActuallySwimming() {
    return this.entity.isActualySwimming();
  }

  @Override
  public boolean isVisuallySwimming() {
    return this.entity.isVisuallySwimming();
  }

  @Override
  public boolean isGlowing() {
    return this.entity.isGlowing();
  }

  @Override
  public void setGlowing(boolean glowing) {
    this.entity.setGlowing(glowing);
  }

  @Override
  public boolean isInvisible() {
    return this.entity.isInvisible();
  }

  @Override
  public boolean isInvisibleToPlayer(Object player) {
    return this.entity.isInvisibleToPlayer((PlayerEntity) player);
  }

  @Override
  public boolean canRenderOnFire() {
    return this.entity.canRenderOnFire();
  }

  @Override
  public UUID getUniqueId() {
    return this.entity.getUniqueID();
  }

  @Override
  public void setUniqueId(UUID uniqueId) {
    this.entity.setUniqueId(uniqueId);
  }

  @Override
  public String getCachedUniqueId() {
    return this.entity.getCachedUniqueIdString();
  }

  @Override
  public String getScoreboardName() {
    return this.entity.getScoreboardName();
  }

  @Override
  public boolean isCustomNameVisible() {
    return this.entity.isCustomNameVisible();
  }

  @Override
  public void setCustomNameVisible(boolean alwaysRenderNameTag) {
    this.entity.setCustomNameVisible(alwaysRenderNameTag);
  }

  @Override
  public float getEyeHeight(EntityPose pose) {
    return this.entity.getEyeHeight((Pose) this.entityMapper.toMinecraftPose(pose));
  }

  @Override
  public float getEyeHeight() {
    return this.entity.getEyeHeight();
  }

  @Override
  public float getBrightness() {
    return this.entity.getBrightness();
  }

  @Override
  public World getWorld() {
    return this.world;
  }

  @Override
  public Entity getRidingEntity() {
    return this.entityMapper.fromMinecraftEntity(this.entity.getRidingEntity());
  }


  @Override
  public void setMotion(double x, double y, double z) {
    this.entity.setMotion(x, y, z);
  }

  @Override
  public void teleportKeepLoaded(double x, double y, double z) {
    this.entity.teleportKeepLoaded(x, y, z);
  }

  @Override
  public void setPositionAndUpdate(double x, double y, double z) {
    this.entity.setPositionAndUpdate(x, y, z);
  }

  @Override
  public boolean getAlwaysRenderNameTagForRender() {
    return this.entity.getAlwaysRenderNameTagForRender();
  }

  @Override
  public void recalculateSize() {
    this.entity.recalculateSize();
  }

  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return this.entity.replaceItemInInventory(
            slot,
            (net.minecraft.item.ItemStack) this.entityMapper.getItemMapper().toMinecraft(itemStack)
    );
  }

  @Override
  public boolean isImmuneToExplosions() {
    return this.entity.isImmuneToExplosions();
  }

  @Override
  public boolean ignoreItemEntityData() {
    return this.entity.ignoreItemEntityData();
  }

  @Override
  public Entity getControllingPassenger() {
    return this.entityMapper.fromMinecraftEntity(this.entity.getControllingPassenger());
  }

  @Override
  public List<Entity> getPassengers() {
    List<Entity> passengers = new ArrayList<>();

    for (net.minecraft.entity.Entity passenger : this.entity.getPassengers()) {
      passengers.add(this.entityMapper.fromMinecraftEntity(passenger));
    }

    return passengers;
  }

  @Override
  public boolean isPassenger(Entity entity) {
    return this.entity.isPassenger((net.minecraft.entity.Entity) this.entityMapper.toMinecraftEntity(entity));
  }

  @Override
  public boolean isPassenger(Class<? extends Entity> passenger) {
    // TODO: 08.10.2020 Implement
    return false;
  }

  @Override
  public Collection<Entity> getRecursivePassengers() {
    Set<Entity> entities = Sets.newHashSet();
    for (net.minecraft.entity.Entity passenger : this.entity.getPassengers()) {
      entities.add(this.entityMapper.fromMinecraftEntity(passenger));
    }

    return entities;
  }

  @Override
  public Stream<Entity> getSelfAndPassengers() {
    return Stream.concat(Stream.of(this), this.getPassengers().stream().flatMap(Entity::getSelfAndPassengers));
  }

  @Override
  public boolean isOnePlayerRiding() {
    return this.entity.isOnePlayerRiding();
  }

  @Override
  public Entity getLowestRidingEntity() {
    return this.entityMapper.fromMinecraftEntity(this.entity.getLowestRidingEntity());
  }

  @Override
  public boolean isRidingSameEntity(Entity entity) {
    return this.entity.isRidingSameEntity((net.minecraft.entity.Entity) this.entityMapper.toMinecraftEntity(entity));
  }

  @Override
  public boolean isRidingOrBeingRiddenBy(Entity entity) {
    return this.entity.isRidingOrBeingRiddenBy((net.minecraft.entity.Entity) this.entityMapper.toMinecraftEntity(entity));
  }

  @Override
  public boolean canPassengerSteer() {
    return this.entity.canPassengerSteer();
  }

  @Override
  public boolean hasPermissionLevel(int level) {
    return this.entity.hasPermissionLevel(level);
  }

  @Override
  public float getWidth() {
    return this.entity.getWidth();
  }

  @Override
  public float getHeight() {
    return this.entity.getHeight();
  }

  @Override
  public EntitySize getSize() {
    return this.getType().getSize();
  }

  @Override
  public BlockPosition getPosition() {
    return this.getWorld().fromMinecraftBlockPos(this.entity.getPosition());
  }

  @Override
  public double getPosXWidth(double width) {
    return this.entity.getPosXWidth(width);
  }

  @Override
  public double getPosXRandom(double factor) {
    return this.entity.getPosXRandom(factor);
  }

  @Override
  public double getPosYHeight(double height) {
    return this.entity.getPosYHeight(height);
  }

  @Override
  public double getPosYRandom() {
    return this.entity.getPosYRandom();
  }

  @Override
  public double getPosYEye() {
    return this.entity.getPosYEye();
  }

  @Override
  public double getPosZWidth(double width) {
    return this.entity.getPosZWidth(width);
  }

  @Override
  public double getPosZRandom(double factor) {
    return this.entity.getPosZRandom(factor);
  }

  @Override
  public void setRawPosition(double x, double y, double z) {
    this.entity.setRawPosition(x, y, z);
  }

  @Override
  public EntityMapper getEntityMapper() {
    return this.entityMapper;
  }

  @Override
  public ChatComponent getName() {
    return this.entityMapper.getComponentMapper().fromMinecraft(
            this.entity.getName()
    );
  }
}
