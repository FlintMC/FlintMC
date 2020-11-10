package net.flintmc.mcapi.v1_15_2.entity;

import com.google.common.collect.Sets;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
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

import java.util.*;
import java.util.stream.Stream;

@Implement(value = Entity.class, version = "1.15.2")
public class VersionedEntity implements Entity {

  private final EntityType entityType;
  private final World world;
  private final EntityFoundationMapper entityFoundationMapper;

  private final net.minecraft.entity.Entity entity;
  private final Random random;

  @AssistedInject
  public VersionedEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper) {
    this.entityType = entityType;
    this.world = world;
    this.entityFoundationMapper = entityFoundationMapper;

    if (!(entity instanceof net.minecraft.entity.Entity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.Entity.class.getName());
    }

    this.random = new Random();
    this.entity = (net.minecraft.entity.Entity) entity;
  }

  /** {@inheritDoc} */
  @Override
  public int getTeamColor() {
    return this.entity.getTeamColor();
  }

  /** {@inheritDoc} */
  @Override
  public void detach() {
    this.entity.detach();
  }

  /** {@inheritDoc} */
  @Override
  public void setPacketCoordinates(double x, double y, double z) {
    this.entity.setPacketCoordinates(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public EntityType getType() {
    return this.entityType;
  }

  /** {@inheritDoc} */
  @Override
  public int getIdentifier() {
    return this.entity.getEntityId();
  }

  /** {@inheritDoc} */
  @Override
  public void setIdentifier(int identifier) {
    this.entity.setEntityId(identifier);
  }

  /** {@inheritDoc} */
  @Override
  public Set<String> getTags() {
    return this.entity.getTags();
  }

  /** {@inheritDoc} */
  @Override
  public boolean addTag(String tag) {
    return this.entity.addTag(tag);
  }

  /** {@inheritDoc} */
  @Override
  public boolean removeTag(String tag) {
    return this.entity.removeTag(tag);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosX() {
    return this.entity.getPosX();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosY() {
    return this.entity.getPosY();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZ() {
    return this.entity.getPosZ();
  }

  /** {@inheritDoc} */
  @Override
  public void remove() {
    this.entity.remove();
  }

  /** {@inheritDoc} */
  @Override
  public EntityPose getPose() {
    return this.entityFoundationMapper.fromMinecraftPose(this.entity.getPose());
  }

  /** {@inheritDoc} */
  @Override
  public void setPosition(double x, double y, double z) {
    this.entity.setPosition(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    this.entity.setPositionAndRotation(x, y, z, yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public void moveToBlockPosAndAngles(
      BlockPosition position, float rotationYaw, float rotationPitch) {
    this.entity.moveToBlockPosAndAngles(
        (BlockPos) this.getWorld().toMinecraftBlockPos(position), rotationYaw, rotationPitch);
  }

  /** {@inheritDoc} */
  @Override
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    this.entity.setLocationAndAngles(x, y, z, yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public void forceSetPosition(double x, double y, double z) {
    this.entity.forceSetPosition(x, y, z);
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
    return this.entity.getDistanceSq(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public double getDistanceSq(Entity entity) {
    return this.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ());
  }

  /** {@inheritDoc} */
  @Override
  public void applyEntityCollision(Entity entity) {
    this.entity.applyEntityCollision(
        (net.minecraft.entity.Entity)
            this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void addVelocity(double x, double y, double z) {
    this.entity.addVelocity(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void rotateTowards(double yaw, double pitch) {
    this.entity.rotateTowards(yaw, pitch);
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch(float partialTicks) {
    return this.entity.getPitch(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getPitch() {
    return this.entity.rotationPitch;
  }

  /** {@inheritDoc} */
  @Override
  public void setPitch(float pitch) {
    this.entity.rotationPitch = pitch;
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw(float partialTicks) {
    return this.entity.getYaw(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw() {
    return this.entity.rotationYaw;
  }

  /** {@inheritDoc} */
  @Override
  public void setYaw(float yaw) {
    this.entity.rotationYaw = yaw;
  }

  /** {@inheritDoc} */
  @Override
  public int getMaxInPortalTime() {
    return this.entity.getMaxInPortalTime();
  }

  /** {@inheritDoc} */
  @Override
  public void setFire(int seconds) {
    this.entity.setFire(seconds);
  }

  /** {@inheritDoc} */
  @Override
  public int getFireTimer() {
    return this.entity.getFireTimer();
  }

  /** {@inheritDoc} */
  @Override
  public void setFireTimer(int ticks) {
    this.entity.setFireTimer(ticks);
  }

  /** {@inheritDoc} */
  @Override
  public void extinguish() {
    this.entity.extinguish();
  }

  /** {@inheritDoc} */
  @Override
  public void resetPositionToBB() {
    this.entity.resetPositionToBB();
  }

  /** {@inheritDoc} */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.entity.playSound(
        (SoundEvent) this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
        volume,
        pitch);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSilent() {
    return this.entity.isSilent();
  }

  /** {@inheritDoc} */
  @Override
  public void setSilent(boolean silent) {
    this.entity.setSilent(silent);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasNoGravity() {
    return this.entity.hasNoGravity();
  }

  /** {@inheritDoc} */
  @Override
  public void setNoGravity(boolean noGravity) {
    this.entity.setNoGravity(noGravity);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isImmuneToFire() {
    return this.entity.isImmuneToFire();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    return this.entity.isOffsetPositionInLiquid(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isWet() {
    return this.entity.isWet();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWaterRainOrBubbleColumn() {
    return this.entity.isInWaterRainOrBubbleColumn();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWaterOrBubbleColumn() {
    return this.entity.isInWaterOrBubbleColumn();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canSwim() {
    return this.entity.canSwim();
  }

  /** {@inheritDoc} */
  @Override
  public void updateSwim() {
    this.entity.updateSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean handleWaterMovement() {
    return this.entity.handleWaterMovement();
  }

  /** {@inheritDoc} */
  @Override
  public void spawnRunningParticles() {
    this.entity.spawnRunningParticles();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInWater() {
    return this.entity.isInWater();
  }

  /** {@inheritDoc} */
  @Override
  public void setInLava() {
    this.entity.setInLava();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInLava() {
    return this.entity.isInLava();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isBurning() {
    return this.entity.isBurning();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPassenger() {
    return this.entity.isPassenger();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isBeingRidden() {
    return this.entity.isBeingRidden();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSneaking() {
    return this.entity.isSneaking();
  }

  /** {@inheritDoc} */
  @Override
  public void setSneaking(boolean sneaking) {
    this.entity.setSneaking(sneaking);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSteppingCarefully() {
    return this.entity.isSteppingCarefully();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSuppressingBounce() {
    return this.entity.isSuppressingBounce();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDiscrete() {
    return this.entity.isDiscrete();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDescending() {
    return this.entity.isDescending();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCrouching() {
    return this.entity.isCrouching();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSprinting() {
    return this.entity.isSprinting();
  }

  /** {@inheritDoc} */
  @Override
  public void setSprinting(boolean sprinting) {
    this.entity.setSprinting(sprinting);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSwimming() {
    return this.entity.isSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void setSwimming(boolean swimming) {
    this.entity.setSwimming(swimming);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActuallySwimming() {
    return this.entity.isActualySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isVisuallySwimming() {
    return this.entity.isVisuallySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isGlowing() {
    return this.entity.isGlowing();
  }

  /** {@inheritDoc} */
  @Override
  public void setGlowing(boolean glowing) {
    this.entity.setGlowing(glowing);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInvisible() {
    return this.entity.isInvisible();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInvisibleToPlayer(net.flintmc.mcapi.player.PlayerEntity player) {
    return this.entity.isInvisibleToPlayer((PlayerEntity) player);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canRenderOnFire() {
    return this.entity.canRenderOnFire();
  }

  /** {@inheritDoc} */
  @Override
  public UUID getUniqueId() {
    return this.entity.getUniqueID();
  }

  /** {@inheritDoc} */
  @Override
  public void setUniqueId(UUID uniqueId) {
    this.entity.setUniqueId(uniqueId);
  }

  /** {@inheritDoc} */
  @Override
  public String getCachedUniqueId() {
    return this.entity.getCachedUniqueIdString();
  }

  /** {@inheritDoc} */
  @Override
  public String getScoreboardName() {
    return this.entity.getScoreboardName();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCustomNameVisible() {
    return this.entity.isCustomNameVisible();
  }

  /** {@inheritDoc} */
  @Override
  public void setCustomNameVisible(boolean alwaysRenderNameTag) {
    this.entity.setCustomNameVisible(alwaysRenderNameTag);
  }

  /** {@inheritDoc} */
  @Override
  public float getEyeHeight(EntityPose pose) {
    return this.entity.getEyeHeight((Pose) this.entityFoundationMapper.toMinecraftPose(pose));
  }

  /** {@inheritDoc} */
  @Override
  public float getEyeHeight() {
    return this.entity.getEyeHeight();
  }

  /** {@inheritDoc} */
  @Override
  public float getBrightness() {
    return this.entity.getBrightness();
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
        .fromMinecraftEntity(this.entity.getRidingEntity());
  }

  /** {@inheritDoc} */
  @Override
  public void setMotion(double x, double y, double z) {
    this.entity.setMotion(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void teleportKeepLoaded(double x, double y, double z) {
    this.entity.teleportKeepLoaded(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public void setPositionAndUpdate(double x, double y, double z) {
    this.entity.setPositionAndUpdate(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlwaysRenderNameTagForRender() {
    return this.entity.getAlwaysRenderNameTagForRender();
  }

  /** {@inheritDoc} */
  @Override
  public void recalculateSize() {
    this.entity.recalculateSize();
  }

  /** {@inheritDoc} */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return this.entity.replaceItemInInventory(
        slot,
        (net.minecraft.item.ItemStack)
            this.entityFoundationMapper.getItemMapper().toMinecraft(itemStack));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isImmuneToExplosions() {
    return this.entity.isImmuneToExplosions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean ignoreItemEntityData() {
    return this.entity.ignoreItemEntityData();
  }

  /** {@inheritDoc} */
  @Override
  public Entity getControllingPassenger() {
    return this.entityFoundationMapper
        .getEntityMapper()
        .fromMinecraftEntity(this.entity.getControllingPassenger());
  }

  /** {@inheritDoc} */
  @Override
  public List<Entity> getPassengers() {
    List<Entity> passengers = new ArrayList<>();

    for (net.minecraft.entity.Entity passenger : this.entity.getPassengers()) {
      passengers.add(this.entityFoundationMapper.getEntityMapper().fromMinecraftEntity(passenger));
    }

    return passengers;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPassenger(Entity entity) {
    return this.entity.isPassenger(
        (net.minecraft.entity.Entity)
            this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Entity> getRecursivePassengers() {
    Set<Entity> entities = Sets.newHashSet();
    for (net.minecraft.entity.Entity passenger : this.entity.getPassengers()) {
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
    return this.entity.isOnePlayerRiding();
  }

  /** {@inheritDoc} */
  @Override
  public Entity getLowestRidingEntity() {
    return this.entityFoundationMapper
        .getEntityMapper()
        .fromMinecraftEntity(this.entity.getLowestRidingEntity());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingSameEntity(Entity entity) {
    return this.entity.isRidingSameEntity(
        (net.minecraft.entity.Entity)
            this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingOrBeingRiddenBy(Entity entity) {
    return this.entity.isRidingOrBeingRiddenBy(
        (net.minecraft.entity.Entity)
            this.entityFoundationMapper.getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPassengerSteer() {
    return this.entity.canPassengerSteer();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasPermissionLevel(int level) {
    return this.entity.hasPermissionLevel(level);
  }

  /** {@inheritDoc} */
  @Override
  public float getWidth() {
    return this.entity.getWidth();
  }

  /** {@inheritDoc} */
  @Override
  public float getHeight() {
    return this.entity.getHeight();
  }

  /** {@inheritDoc} */
  @Override
  public EntitySize getSize() {
    return this.getType().getSize();
  }

  /** {@inheritDoc} */
  @Override
  public BlockPosition getPosition() {
    return this.getWorld().fromMinecraftBlockPos(this.entity.getPosition());
  }

  /** {@inheritDoc} */
  @Override
  public double getPosXWidth(double width) {
    return this.entity.getPosXWidth(width);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosXRandom(double factor) {
    return this.entity.getPosXRandom(factor);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYHeight(double height) {
    return this.entity.getPosYHeight(height);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYRandom() {
    return this.entity.getPosYRandom();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosYEye() {
    return this.entity.getPosYEye();
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZWidth(double width) {
    return this.entity.getPosZWidth(width);
  }

  /** {@inheritDoc} */
  @Override
  public double getPosZRandom(double factor) {
    return this.entity.getPosZRandom(factor);
  }

  /** {@inheritDoc} */
  @Override
  public void setRawPosition(double x, double y, double z) {
    this.entity.setRawPosition(x, y, z);
  }

  @Override
  public boolean isInvulnerable() {
    return this.entity.isInvulnerable();
  }

  /** {@inheritDoc} */
  @Override
  public void setInvulnerable(boolean invulnerable) {
    this.entity.setInvulnerable(invulnerable);
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
    return this.entity.isAlive();
  }

  /** {@inheritDoc} */
  @Override
  public void move(MoverType moverType, Vector3D vector3D) {
    this.entity.move(
        (net.minecraft.entity.MoverType)
            this.entityFoundationMapper.toMinecraftMoverType(moverType),
        new Vec3d(vector3D.getX(), vector3D.getY(), vector3D.getZ()));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCollidedHorizontally() {
    return this.entity.collidedHorizontally;
  }

  /** {@inheritDoc} */
  @Override
  public void setCollidedHorizontally(boolean horizontally) {
    this.entity.collidedHorizontally = horizontally;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCollidedVertically() {
    return this.entity.collidedVertically;
  }

  /** {@inheritDoc} */
  @Override
  public void setCollidedVertically(boolean vertically) {
    this.entity.collidedVertically = vertically;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateX() {
    return this.entity.chunkCoordX;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateY() {
    return this.entity.chunkCoordY;
  }

  /** {@inheritDoc} */
  @Override
  public int getChunkCoordinateZ() {
    return this.entity.chunkCoordZ;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnGround() {
    return this.entity.onGround;
  }

  /** {@inheritDoc} */
  @Override
  public void setOnGround(boolean onGround) {
    this.entity.onGround = onGround;
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
    return this.entityFoundationMapper.getComponentMapper().fromMinecraft(this.entity.getName());
  }
}
