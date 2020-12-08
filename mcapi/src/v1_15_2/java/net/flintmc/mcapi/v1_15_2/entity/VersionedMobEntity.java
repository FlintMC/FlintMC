package net.flintmc.mcapi.v1_15_2.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.MobEntity;
import net.flintmc.mcapi.entity.ai.EntitySenses;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

@Implement(value = MobEntity.class, version = "1.15.2")
public class VersionedMobEntity extends VersionedLivingEntity implements MobEntity {

  private final net.minecraft.entity.MobEntity mobEntity;
  private final EntitySenses.Factory entitySensesFactory;

  @AssistedInject
  public VersionedMobEntity(
      @Assisted("mobEntity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntitySenses.Factory entitySensesFactory) {
    super(entity, entityType, world, entityFoundationMapper);
    this.entitySensesFactory = entitySensesFactory;

    if (!(entity instanceof net.minecraft.entity.MobEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.MobEntity.class.getName());
    }
    this.mobEntity = (net.minecraft.entity.MobEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntitySenses getEntitySenses() {
    return this.entitySensesFactory.create(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LivingEntity getAttackTarget() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.mobEntity.getAttackTarget());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAttackTarget(LivingEntity entity) {
    this.mobEntity.setAttackTarget(
        (net.minecraft.entity.LivingEntity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void eatGrassBonus() {
    this.mobEntity.eatGrassBonus();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTalkInterval() {
    return this.mobEntity.getTalkInterval();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void playAmbientSound() {
    this.mobEntity.playAmbientSound();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spawnExplosionParticle() {
    this.mobEntity.spawnExplosionParticle();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMoveForward(float amount) {
    this.mobEntity.setMoveForward(amount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMoveVertical(float amount) {
    this.mobEntity.setMoveVertical(amount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMoveStrafing(float amount) {
    this.mobEntity.setMoveStrafing(amount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canDespawn(double distanceToClosestPlayer) {
    return this.mobEntity.canDespawn(distanceToClosestPlayer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean preventDespawn() {
    return this.mobEntity.preventDespawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getVerticalFaceSpeed() {
    return this.mobEntity.getVerticalFaceSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getHorizontalFaceSpeed() {
    return this.mobEntity.getHorizontalFaceSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFaceRotationSpeed() {
    return this.mobEntity.getFaceRotSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void faceEntity(Entity entity, float maxYawIncrease, float maxPitchIncrease) {
    this.mobEntity.faceEntity(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity),
        maxYawIncrease,
        maxPitchIncrease);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxSpawnedInChunk() {
    return this.mobEntity.getMaxSpawnedInChunk();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isMaxGroupSize(int size) {
    return this.mobEntity.isMaxGroupSize(size);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeSteered() {
    return this.mobEntity.canBeSteered();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enablePersistence() {
    this.mobEntity.enablePersistence();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDropChance(EquipmentSlotType slotType, float chance) {
    this.mobEntity.setDropChance(
        (net.minecraft.inventory.EquipmentSlotType)
            this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType),
        chance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canPickUpLoot() {
    return this.mobEntity.canPickUpLoot();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCanPickUpLoot(boolean canPickUpLoot) {
    this.mobEntity.setCanPickUpLoot(canPickUpLoot);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isNoDespawnRequired() {
    return this.mobEntity.isNoDespawnRequired();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWithinHomeDistanceCurrentPosition() {
    return this.mobEntity.isWithinHomeDistanceCurrentPosition();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWithinHomeDistanceFromPosition(BlockPosition position) {
    return this.mobEntity.isWithinHomeDistanceFromPosition(
        (BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHomePositionAndDistance(BlockPosition position, int distance) {
    this.mobEntity.setHomePosAndDistance(
        (BlockPos) this.getWorld().toMinecraftBlockPos(position), distance);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getHomePosition() {
    return this.getWorld().fromMinecraftBlockPos(this.mobEntity.getHomePosition());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getMaximumHomeDistance() {
    return this.mobEntity.getMaximumHomeDistance();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean detachHome() {
    return this.mobEntity.detachHome();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearLeashed(boolean sendPacket, boolean dropLead) {
    this.mobEntity.clearLeashed(sendPacket, dropLead);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeLeashedTo(PlayerEntity playerEntity) {
    return this.mobEntity.canBeLeashedTo(
        (net.minecraft.entity.player.PlayerEntity)
            this.getEntityFoundationMapper()
                .getEntityMapper()
                .toMinecraftPlayerEntity(playerEntity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLeashed() {
    return this.mobEntity.getLeashed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity getLeashHolder() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftEntity(this.mobEntity.getLeashHolder());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLeashHolder(Entity entity, boolean leashHolder) {
    this.mobEntity.setLeashHolder(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity),
        leashHolder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVehicleEntityId(int vehicleEntityId) {
    this.mobEntity.setVehicleEntityId(vehicleEntityId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isItemStackInSlot(EquipmentSlotType slotType, ItemStack itemStack) {
    return net.minecraft.entity.MobEntity.isItemStackInSlot(
        (net.minecraft.inventory.EquipmentSlotType)
            this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType),
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoAI(boolean noAI) {
    this.mobEntity.setNoAI(noAI);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAIDisabled() {
    return this.mobEntity.isAIDisabled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLeftHanded() {
    return this.mobEntity.isLeftHanded();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLeftHanded(boolean leftHanded) {
    this.mobEntity.setLeftHanded(leftHanded);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAggressive() {
    return this.mobEntity.isAggressive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAggressive(boolean aggressive) {
    this.mobEntity.setAggroed(aggressive);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityFoundationMapper()
        .getResourceLocationProvider()
        .get(this.mobEntity.getLootTableResourceLocation().getPath())
        .getHandle();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAIMoveSpeed(float speed) {
    this.mobEntity.setAIMoveSpeed(speed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkDespawn() {
    this.mobEntity.checkDespawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.mobEntity.getItemStackFromSlot(
                (net.minecraft.inventory.EquipmentSlotType)
                    this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return this.mobEntity.canPickUpItem(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return this.mobEntity.replaceItemInInventory(
        slot,
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canPassengerSteer() {
    return this.mobEntity.canPassengerSteer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isServerWorld() {
    return this.mobEntity.isServerWorld();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAttack(LivingEntity entity) {
    return this.mobEntity.canAttack(
        (net.minecraft.entity.LivingEntity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attackEntityAsMob(Entity entity) {
    this.mobEntity.attackEntityAsMob(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.mobEntity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.mobEntity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side getPrimaryHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHandSide(this.mobEntity.getPrimaryHand());
  }
}
