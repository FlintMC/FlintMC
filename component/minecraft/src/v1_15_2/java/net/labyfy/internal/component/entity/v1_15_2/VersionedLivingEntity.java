package net.labyfy.internal.component.entity.v1_15_2;

import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.util.BlockPosition;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.Random;

@Implement(value = LivingEntity.class, version = "1.15.2")
public class VersionedLivingEntity extends VersionedEntity implements LivingEntity {

  private final net.minecraft.entity.LivingEntity livingEntity;

  public VersionedLivingEntity(Object entity, EntityType entityType, ClientWorld world, EntityMapper entityMapper) {
    super(entity, entityType, world, entityMapper);

    if (!(entity instanceof net.minecraft.entity.LivingEntity)) {
      throw new IllegalArgumentException("");
    }

    this.livingEntity = (net.minecraft.entity.LivingEntity) entity;
  }

  @Override
  public boolean canBreathUnderwater() {
    return this.livingEntity.canBreatheUnderwater();
  }

  @Override
  public float getSwimAnimation(float partialTicks) {
    return this.livingEntity.getSwimAnimation(partialTicks);
  }

  @Override
  public float getRenderScale() {
    return this.livingEntity.getRenderScale();
  }

  @Override
  public Random getRandom() {
    return this.livingEntity.getRNG();
  }

  @Override
  public LivingEntity getRevengeTarget() {
    // TODO: 08.10.2020 Implement
    return null;
  }

  @Override
  public void setRevengeTarget(LivingEntity entity) {
    // TODO: 08.10.2020 Implement

  }

  @Override
  public int getRevengeTimer() {
    return this.livingEntity.getRevengeTimer();
  }

  @Override
  public LivingEntity getLastAttackedEntity() {
    // TODO: 08.10.2020 Implement
    return null;
  }

  @Override
  public void setLastAttackedEntity(Entity entity) {
    // TODO: 08.10.2020 Implement

  }

  @Override
  public int getLastAttackedEntityTime() {
    return this.livingEntity.getLastAttackedEntityTime();
  }

  @Override
  public int getIdleTime() {
    return this.livingEntity.getIdleTime();
  }

  @Override
  public void setIdleTime(int idleTime) {
    this.livingEntity.setIdleTime(idleTime);
  }

  @Override
  public double getVisibilityMultiplier(Entity entity) {
    // TODO: 08.10.2020 Implement
    return 0;
  }

  @Override
  public boolean canAttack(LivingEntity entity) {
    // TODO: 08.10.2020 Implement
    return false;
  }

  @Override
  public boolean clearActivePotions() {
    return this.livingEntity.clearActivePotions();
  }

  @Override
  public boolean isEntityUndead() {
    return this.livingEntity.isEntityUndead();
  }

  @Override
  public void heal(float health) {
    this.livingEntity.heal(health);
  }

  @Override
  public float getHealth() {
    return this.livingEntity.getHealth();
  }

  @Override
  public void setHealth(float health) {
    this.livingEntity.setHealth(health);
  }

  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityMapper().getResourceLocationProvider().get(
            this.livingEntity.getLootTableResourceLocation().getPath(),
            this.livingEntity.getLootTableResourceLocation().getNamespace()
    );
  }

  @Override
  public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
    // TODO: 08.10.2020 Implement

  }

  @Override
  public Sound getEatSound(ItemStack itemStack) {
    return this.getEntityMapper().fromMinecraftSound(
            this.livingEntity.getEatSound(
                    (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack)
            )
    );
  }

  @Override
  public boolean isOnLadder() {
    return this.livingEntity.isOnLadder();
  }

  @Override
  public int getTotalArmorValue() {
    return this.livingEntity.getTotalArmorValue();
  }

  @Override
  public float getMaxHealth() {
    return this.livingEntity.getMaxHealth();
  }

  @Override
  public int getArrowCountInEntity() {
    return this.livingEntity.getArrowCountInEntity();
  }

  @Override
  public void setArrowCountInEntity(int count) {
    this.livingEntity.setArrowCountInEntity(count);
  }

  @Override
  public int getBeeStringCount() {
    return this.livingEntity.getBeeStingCount();
  }

  @Override
  public void setBeeStingCount(int stingCount) {
    this.livingEntity.setBeeStingCount(stingCount);
  }

  @Override
  public void swingArm(Hand hand) {
    this.livingEntity.swingArm(
            (net.minecraft.util.Hand) this.getEntityMapper().toMinecraftHand(hand)
    );
  }

  @Override
  public void swing(Hand hand, boolean swing) {
    this.livingEntity.swing(
            (net.minecraft.util.Hand) this.getEntityMapper().toMinecraftHand(hand),
            swing
    );
  }

  @Override
  public ItemStack getHeldItem(Hand hand) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(
            this.livingEntity.getHeldItem(
                    (net.minecraft.util.Hand) this.getEntityMapper().toMinecraftHand(hand)
            )
    );
  }

  @Override
  public void setHeldItem(Hand hand, ItemStack heldItem) {
    this.livingEntity.setHeldItem(
            (net.minecraft.util.Hand) this.getEntityMapper().toMinecraftHand(hand),
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(heldItem)
    );
  }

  @Override
  public boolean hasItemInSlot(EquipmentSlotType slotType) {
    return this.livingEntity.hasItemInSlot(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
    );
  }

  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(
            this.livingEntity.getItemStackFromSlot(
                    (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
            )
    );
  }

  @Override
  public float getArmorCoverPercentage() {
    return this.livingEntity.getArmorCoverPercentage();
  }

  @Override
  public float getAIMoveSpeed() {
    return this.livingEntity.getAIMoveSpeed();
  }

  @Override
  public void setAIMoveSpeed(float speed) {
    this.livingEntity.setAIMoveSpeed(speed);
  }

  @Override
  public void attackEntityAsMob(Entity entity) {
    // TODO: 08.10.2020 Implement
  }

  @Override
  public void startSpinAttack(int duration) {
    this.livingEntity.startSpinAttack(duration);
  }

  @Override
  public boolean isSpinAttacking() {
    return this.livingEntity.isSpinAttacking();
  }

  @Override
  public void setJumping(boolean jumping) {
    this.livingEntity.setJumping(jumping);
  }

  @Override
  public boolean canEntityBeSeen(Entity entity) {
    // TODO: 08.10.2020 Implement
    return false;
  }

  @Override
  public float getSwingProgress(float partialTicks) {
    return this.livingEntity.getSwingProgress(partialTicks);
  }

  @Override
  public boolean isServerWorld() {
    return this.livingEntity.isServerWorld();
  }

  @Override
  public float getAbsorptionAmount() {
    return this.livingEntity.getAbsorptionAmount();
  }

  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    this.livingEntity.setAbsorptionAmount(absorptionAmount);
  }

  @Override
  public void sendEnterCombat() {
    this.livingEntity.sendEnterCombat();
  }

  @Override
  public void sendEndCombat() {
    this.livingEntity.sendEndCombat();
  }

  @Override
  public Hand.Side getPrimaryHand() {
    return this.getEntityMapper().fromMinecraftHandSide(this.livingEntity.getPrimaryHand());
  }

  @Override
  public Hand getActiveHand() {
    return this.getEntityMapper().fromMinecraftHand(this.livingEntity.getActiveHand());
  }

  @Override
  public void setActiveHand(Hand hand) {
    this.livingEntity.setActiveHand(
            (net.minecraft.util.Hand) this.getEntityMapper().toMinecraftHand(hand)
    );
  }

  @Override
  public ItemStack getActiveItemStack() {
    return this.getEntityMapper().getItemMapper().fromMinecraft(this.livingEntity.getActiveItemStack());
  }

  @Override
  public int getItemInUseCount() {
    return this.livingEntity.getItemInUseCount();
  }

  @Override
  public int getItemInUseMaxCount() {
    return this.livingEntity.getItemInUseMaxCount();
  }

  @Override
  public void stopActiveHand() {
    this.livingEntity.stopActiveHand();
  }

  @Override
  public void resetActiveHand() {
    this.livingEntity.resetActiveHand();
  }

  @Override
  public boolean isActiveItemStackBlocking() {
    return this.livingEntity.isActiveItemStackBlocking();
  }

  @Override
  public boolean isSuppressingSlidingDownLadder() {
    return this.livingEntity.isSuppressingSlidingDownLadder();
  }

  @Override
  public boolean isElytraFlying() {
    return this.livingEntity.isElytraFlying();
  }

  @Override
  public int getTicksElytraFlying() {
    return this.livingEntity.getTicksElytraFlying();
  }

  @Override
  public boolean attemptTeleport(double x, double y, double z, boolean teleportState) {
    return this.livingEntity.attemptTeleport(x, y, z, teleportState);
  }

  @Override
  public boolean canBeHitWithPotion() {
    return this.livingEntity.canBeHitWithPotion();
  }

  @Override
  public boolean attackable() {
    return this.livingEntity.attackable();
  }

  @Override
  public void setPartying(BlockPosition position, boolean isPartying) {
    this.livingEntity.setPartying(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            isPartying
    );
  }

  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return this.livingEntity.canPickUpItem(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(stack)
    );
  }

  @Override
  public Optional<BlockPosition> getBedPosition() {
    Optional<BlockPosition> optional = Optional.empty();

    if (this.livingEntity.getBedPosition().isPresent()) {
      optional = Optional.of(this.getWorld().fromMinecraftBlockPos(this.livingEntity.getBedPosition().get()));
    }

    return optional;
  }

  @Override
  public void setBedPosition(BlockPosition position) {
    this.livingEntity.setBedPosition((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  @Override
  public void clearBedPosition() {
    this.livingEntity.clearBedPosition();
  }

  @Override
  public boolean isSleeping() {
    return this.livingEntity.isSleeping();
  }

  @Override
  public void startSleeping(BlockPosition position) {
    this.livingEntity.startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  @Override
  public void wakeUp() {
    this.livingEntity.wakeUp();
  }

  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(
            this.livingEntity.findAmmo(
                    (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(shootable)
            )
    );
  }

  @Override
  public void sendBreakAnimation(EquipmentSlotType slotType) {
    this.livingEntity.sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
    );
  }

  @Override
  public void sendBreakAnimation(Hand hand) {
    this.livingEntity.sendBreakAnimation(
            (net.minecraft.util.Hand) this.getEntityMapper().toMinecraftHand(hand)
    );
  }
}
