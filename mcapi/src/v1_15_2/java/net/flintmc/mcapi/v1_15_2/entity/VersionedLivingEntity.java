package net.flintmc.mcapi.v1_15_2.entity;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.v1_15_2.entity.render.LivingRendererAccessor;
import net.flintmc.mcapi.v1_15_2.entity.render.QuadrupedModelAccessor;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.render.model.ModelBoxHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Implement(value = LivingEntity.class, version = "1.15.2")
public class VersionedLivingEntity extends VersionedEntity<net.minecraft.entity.LivingEntity>
    implements LivingEntity {

  @AssistedInject
  public VersionedLivingEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    super(entity, entityType, world, entityFoundationMapper, entityRenderContextFactory);

    if (!(entity instanceof net.minecraft.entity.LivingEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.LivingEntity.class.getName());
    }
  }

  protected Map<String, ModelBoxHolder<Entity, EntityRenderContext>> createModelRenderers() {
    EntityModel<? extends net.minecraft.entity.LivingEntity> entityModel =
        ((LivingRendererAccessor)
            Minecraft.getInstance().getRenderManager().getRenderer(this.getHandle()))
            .getEntityModel();

    Map<String, ModelBoxHolder<Entity, EntityRenderContext>> modelBoxHolders = new HashMap<>();

    if (entityModel instanceof BipedModel) {
      BipedModel<? extends net.minecraft.entity.LivingEntity> bipedModel =
          (BipedModel<? extends net.minecraft.entity.LivingEntity>) entityModel;
      modelBoxHolders.put("body", this.createModelBox(bipedModel.bipedBody));
      modelBoxHolders.put("head", this.createModelBox(bipedModel.bipedHead));
      modelBoxHolders.put("headWear", this.createModelBox(bipedModel.bipedHeadwear));
      modelBoxHolders.put("leftArm", this.createModelBox(bipedModel.bipedLeftArm));
      modelBoxHolders.put("rightArm", this.createModelBox(bipedModel.bipedRightArm));
      modelBoxHolders.put("rightLeg", this.createModelBox(bipedModel.bipedRightLeg));
      modelBoxHolders.put("leftLeg", this.createModelBox(bipedModel.bipedLeftLeg));
    }

    if (entityModel instanceof PlayerModel) {
      modelBoxHolders.put(
          "leftArmWear", this.createModelBox(((PlayerModel<?>) entityModel).bipedLeftArmwear));
      modelBoxHolders.put(
          "rightArmWear",
          this.createModelBox(((PlayerModel<?>) entityModel).bipedRightArmwear));
      modelBoxHolders.put(
          "leftLegWear", this.createModelBox(((PlayerModel<?>) entityModel).bipedLeftLegwear));
      modelBoxHolders.put(
          "rightLegWear",
          this.createModelBox(((PlayerModel<?>) entityModel).bipedRightLegwear));
      modelBoxHolders.put(
          "bodyWear", this.createModelBox(((PlayerModel<?>) entityModel).bipedBodyWear));
    }

    if (entityModel instanceof QuadrupedModelAccessor) {
      QuadrupedModelAccessor quadrupedModelAccessor = (QuadrupedModelAccessor) entityModel;
      modelBoxHolders.put("body", this.createModelBox(quadrupedModelAccessor.getBody()));
      modelBoxHolders.put("head", this.createModelBox(quadrupedModelAccessor.getHead()));
      modelBoxHolders.put(
          "legBackLeft", this.createModelBox(quadrupedModelAccessor.getLegBackLeft()));
      modelBoxHolders.put(
          "legBackRight", this.createModelBox(quadrupedModelAccessor.getLegBackRight()));
      modelBoxHolders.put(
          "legFrontRight", this.createModelBox(quadrupedModelAccessor.getLegFrontRight()));
      modelBoxHolders.put(
          "legFrontLeft", this.createModelBox(quadrupedModelAccessor.getLegFrontLeft()));
    }
    return modelBoxHolders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBreathUnderwater() {
    return this.getHandle().canBreatheUnderwater();
  }

  /** {@inheritDoc} */
  @Override
  public float getSwimAnimation(float partialTicks) {
    return this.getHandle().getSwimAnimation(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getRenderScale() {
    return this.getHandle().getRenderScale();
  }

  /** {@inheritDoc} */
  @Override
  public Random getRandom() {
    return this.getHandle().getRNG();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getRevengeTarget() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.getHandle().getRevengeTarget());
  }

  /** {@inheritDoc} */
  @Override
  public void setRevengeTarget(LivingEntity entity) {
    this.getHandle()
        .setRevengeTarget(
            (net.minecraft.entity.LivingEntity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getRevengeTimer() {
    return this.getHandle().getRevengeTimer();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getLastAttackedEntity() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.getHandle().getRevengeTarget());
  }

  /** {@inheritDoc} */
  @Override
  public void setLastAttackedEntity(Entity entity) {
    this.getHandle()
        .setLastAttackedEntity(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getLastAttackedEntityTime() {
    return this.getHandle().getLastAttackedEntityTime();
  }

  /** {@inheritDoc} */
  @Override
  public int getIdleTime() {
    return this.getHandle().getIdleTime();
  }

  /** {@inheritDoc} */
  @Override
  public void setIdleTime(int idleTime) {
    this.getHandle().setIdleTime(idleTime);
  }

  /** {@inheritDoc} */
  @Override
  public double getVisibilityMultiplier(Entity entity) {
    return this.getHandle()
        .getVisibilityMultiplier(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canAttack(LivingEntity entity) {
    return this.getHandle()
        .canAttack(
            (net.minecraft.entity.EntityType<?>)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean clearActivePotions() {
    return this.getHandle().clearActivePotions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEntityUndead() {
    return this.getHandle().isEntityUndead();
  }

  /** {@inheritDoc} */
  @Override
  public void heal(float health) {
    this.getHandle().heal(health);
  }

  /** {@inheritDoc} */
  @Override
  public float getHealth() {
    return this.getHandle().getHealth();
  }

  /** {@inheritDoc} */
  @Override
  public void setHealth(float health) {
    this.getHandle().setHealth(health);
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityFoundationMapper()
        .getResourceLocationProvider()
        .get(
            this.getHandle().getLootTableResourceLocation().getPath(),
            this.getHandle().getLootTableResourceLocation().getNamespace());
  }

  /** {@inheritDoc} */
  @Override
  public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
    this.getHandle()
        .knockBack(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity),
            strength,
            xRatio,
            zRatio);
  }

  @Override
  public Sound getEatSound(ItemStack itemStack) {
    return this.getEntityFoundationMapper()
        .getSoundMapper()
        .fromMinecraftSoundEvent(
            this.getHandle()
                .getEatSound(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack)));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnLadder() {
    return this.getHandle().isOnLadder();
  }

  /** {@inheritDoc} */
  @Override
  public int getTotalArmorValue() {
    return this.getHandle().getTotalArmorValue();
  }

  /** {@inheritDoc} */
  @Override
  public float getMaxHealth() {
    return this.getHandle().getMaxHealth();
  }

  /** {@inheritDoc} */
  @Override
  public int getArrowCountInEntity() {
    return this.getHandle().getArrowCountInEntity();
  }

  /** {@inheritDoc} */
  @Override
  public void setArrowCountInEntity(int count) {
    this.getHandle().setArrowCountInEntity(count);
  }

  /** {@inheritDoc} */
  @Override
  public int getBeeStingCount() {
    return this.getHandle().getBeeStingCount();
  }

  @Override
  public void setBeeStingCount(int stingCount) {
    this.getHandle().setBeeStingCount(stingCount);
  }

  /** {@inheritDoc} */
  @Override
  public void swingArm(Hand hand) {
    this.getHandle()
        .swingArm(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void swing(Hand hand, boolean sendToAll) {
    this.getHandle()
        .swing(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand),
            sendToAll);
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getHeldItem(Hand hand) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.getHandle()
                .getHeldItem(
                    (net.minecraft.util.Hand)
                        this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand)));
  }

  /** {@inheritDoc} */
  @Override
  public void setHeldItem(Hand hand, ItemStack heldItem) {
    this.getHandle()
        .setHeldItem(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand),
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(heldItem));
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasItemInSlot(EquipmentSlotType slotType) {
    return this.getHandle()
        .hasItemInSlot(
            (net.minecraft.inventory.EquipmentSlotType)
                this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.getHandle()
                .getItemStackFromSlot(
                    (net.minecraft.inventory.EquipmentSlotType)
                        this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType)));
  }

  /** {@inheritDoc} */
  @Override
  public float getArmorCoverPercentage() {
    return this.getHandle().getArmorCoverPercentage();
  }

  /** {@inheritDoc} */
  @Override
  public float getAIMoveSpeed() {
    return this.getHandle().getAIMoveSpeed();
  }

  /** {@inheritDoc} */
  @Override
  public void setAIMoveSpeed(float speed) {
    this.getHandle().setAIMoveSpeed(speed);
  }

  /** {@inheritDoc} */
  @Override
  public void applyEntityCollision(Entity entity) {
    this.getHandle()
        .applyEntityCollision(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw(float partialTicks) {
    return this.getHandle().getYaw(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public void attackEntityAsMob(Entity entity) {
    this.getHandle()
        .attackEntityAsMob(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void setSprinting(boolean sprinting) {
    this.getHandle().setSprinting(sprinting);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActuallySwimming() {
    return this.getHandle().isActualySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void startSpinAttack(int duration) {
    this.getHandle().startSpinAttack(duration);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSpinAttacking() {
    return this.getHandle().isSpinAttacking();
  }

  /** {@inheritDoc} */
  @Override
  public void setJumping(boolean jumping) {
    this.getHandle().setJumping(jumping);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canEntityBeSeen(Entity entity) {
    return this.getHandle()
        .canEntityBeSeen(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getSwingProgress(float partialTicks) {
    return this.getHandle().getSwingProgress(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isServerWorld() {
    return this.getHandle().isServerWorld();
  }

  /** {@inheritDoc} */
  @Override
  public float getAbsorptionAmount() {
    return this.getHandle().getAbsorptionAmount();
  }

  /** {@inheritDoc} */
  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    this.getHandle().setAbsorptionAmount(absorptionAmount);
  }

  /** {@inheritDoc} */
  @Override
  public void sendEnterCombat() {
    this.getHandle().sendEnterCombat();
  }

  /** {@inheritDoc} */
  @Override
  public void sendEndCombat() {
    this.getHandle().sendEndCombat();
  }

  /** {@inheritDoc} */
  @Override
  public Hand.Side getPrimaryHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHandSide(this.getHandle().getPrimaryHand());
  }

  /** {@inheritDoc} */
  @Override
  public Hand getActiveHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHand(this.getHandle().getActiveHand());
  }

  /** {@inheritDoc} */
  @Override
  public void setActiveHand(Hand hand) {
    this.getHandle()
        .setActiveHand(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getActiveItemStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.getHandle().getActiveItemStack());
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseCount() {
    return this.getHandle().getItemInUseCount();
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseMaxCount() {
    return this.getHandle().getItemInUseMaxCount();
  }

  /** {@inheritDoc} */
  @Override
  public void stopActiveHand() {
    this.getHandle().stopActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public void resetActiveHand() {
    this.getHandle().resetActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActiveItemStackBlocking() {
    return this.getHandle().isActiveItemStackBlocking();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSuppressingSlidingDownLadder() {
    return this.getHandle().isSuppressingSlidingDownLadder();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isElytraFlying() {
    return this.getHandle().isElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public int getTicksElytraFlying() {
    return this.getHandle().getTicksElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public boolean attemptTeleport(double x, double y, double z, boolean particleEffects) {
    return this.getHandle().attemptTeleport(x, y, z, particleEffects);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeHitWithPotion() {
    return this.getHandle().canBeHitWithPotion();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeRiddenInWater() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public boolean attackable() {
    return this.getHandle().attackable();
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveStrafing() {
    return this.getHandle().moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveStrafing(float moveStrafing) {
    this.getHandle().moveStrafing = moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveVertical() {
    return this.getHandle().moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveVertical(float moveVertical) {
    this.getHandle().moveVertical = moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveForward() {
    return this.getHandle().moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveForward(float moveForward) {
    this.getHandle().moveForward = moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setPartying(BlockPosition position, boolean isPartying) {
    this.getHandle()
        .setPartying((BlockPos) this.getWorld().toMinecraftBlockPos(position), isPartying);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return this.getHandle()
        .canPickUpItem(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack));
  }

  /** {@inheritDoc} */
  @Override
  public Optional<BlockPosition> getBedPosition() {
    Optional<BlockPosition> optional = Optional.empty();

    if (this.getHandle().getBedPosition().isPresent()) {
      optional =
          Optional.of(
              this.getWorld().fromMinecraftBlockPos(this.getHandle().getBedPosition().get()));
    }

    return optional;
  }

  /** {@inheritDoc} */
  @Override
  public void setBedPosition(BlockPosition position) {
    this.getHandle().setBedPosition((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void clearBedPosition() {
    this.getHandle().clearBedPosition();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSleeping() {
    return this.getHandle().isSleeping();
  }

  /** {@inheritDoc} */
  @Override
  public void startSleeping(BlockPosition position) {
    this.getHandle().startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void wakeUp() {
    this.getHandle().wakeUp();
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.getHandle()
                .findAmmo(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(shootable)));
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(EquipmentSlotType slotType) {
    this.getHandle()
        .sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType)
                this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(Hand hand) {
    this.getHandle()
        .sendBreakAnimation(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.getHandle()
        .readAdditional(
            (CompoundNBT)
                this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.getHandle()
        .writeAdditional(
            (CompoundNBT)
                this.getEntityFoundationMapper().getNbtMapper().fromMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlive() {
    return this.getHandle().isAlive();
  }
}
