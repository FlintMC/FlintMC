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

package net.flintmc.mcapi.v1_16_5.player;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.type.EntityType;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.model.ModelMapper;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.tileentity.mapper.TileEntityMapper;
import net.flintmc.mcapi.v1_16_5.entity.VersionedLivingEntity;
import net.flintmc.mcapi.world.World;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.scoreboad.Scoreboard;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.Item;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.tileentity.JigsawTileEntity;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

@Implement(value = PlayerEntity.class, version = "1.16.5")
public class VersionedPlayerEntity extends VersionedLivingEntity implements PlayerEntity {

  private final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;
  private final ModelMapper modelMapper;
  private final ItemEntityMapper itemEntityMapper;
  private final TileEntityMapper tileEntityMapper;

  @AssistedInject
  public VersionedPlayerEntity(
      @Assisted("entity") Object entity,
      @Assisted("entityType") EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      GameProfileSerializer gameProfileSerializer,
      ModelMapper modelMapper,
      ItemEntityMapper itemEntityMapper,
      TileEntityMapper tileEntityMapper) {
    super(entity, entityType, world, entityFoundationMapper);
    this.gameProfileSerializer = gameProfileSerializer;
    this.modelMapper = modelMapper;
    this.itemEntityMapper = itemEntityMapper;
    this.tileEntityMapper = tileEntityMapper;
  }

  public VersionedPlayerEntity(
      Supplier<Object> entitySupplier,
      EntityType entityType,
      World world,
      EntityFoundationMapper entityFoundationMapper,
      GameProfileSerializer gameProfileSerializer,
      ModelMapper modelMapper,
      ItemEntityMapper itemEntityMapper,
      TileEntityMapper tileEntityMapper) {
    super(entitySupplier, entityType, world, entityFoundationMapper);
    this.gameProfileSerializer = gameProfileSerializer;
    this.modelMapper = modelMapper;
    this.itemEntityMapper = itemEntityMapper;
    this.tileEntityMapper = tileEntityMapper;
  }

  @Override
  protected net.minecraft.entity.player.PlayerEntity wrapped() {
    net.minecraft.entity.Entity entity = super.wrapped();

    if (!(entity instanceof net.minecraft.entity.player.PlayerEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.player.PlayerEntity.class.getName());
    }

    return (net.minecraft.entity.player.PlayerEntity) entity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean blockActionRestricted(World world, BlockPosition position, GameMode mode) {
    return this.wrapped()
        .blockActionRestricted(
            this.wrapped().world,
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            (GameType) this.getEntityFoundationMapper().toMinecraftGameType(mode));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSecondaryUseActive() {
    return this.wrapped().isSecondaryUseActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    this.wrapped()
        .playSound(
            (SoundEvent)
                this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            (net.minecraft.util.SoundCategory)
                this.getEntityFoundationMapper()
                    .getSoundMapper()
                    .toMinecraftSoundCategory(category),
            volume,
            pitch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScore() {
    return this.wrapped().getScore();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setScore(int score) {
    this.wrapped().setScore(score);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addScore(int score) {
    this.wrapped().addScore(score);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean drop(boolean dropEntireStack) {
    return this.wrapped().drop(dropEntireStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean traceItem) {
    return this.itemEntityMapper.fromMinecraftItemEntity(
        this.wrapped()
            .dropItem(
                (net.minecraft.item.ItemStack)
                    this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack),
                traceItem));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem) {
    return this.itemEntityMapper.fromMinecraftItemEntity(
        this.wrapped()
            .dropItem(
                (net.minecraft.item.ItemStack)
                    this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack),
                dropAround,
                traceItem));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAttackPlayer(PlayerEntity playerEntity) {
    return this.wrapped()
        .canAttackPlayer(
            (net.minecraft.entity.player.PlayerEntity)
                this.getEntityFoundationMapper()
                    .getEntityMapper()
                    .toMinecraftPlayerEntity(playerEntity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openSignEditor(SignTileEntity signTileEntity) {
    this.wrapped()
        .openSignEditor(
            (net.minecraft.tileentity.SignTileEntity)
                this.tileEntityMapper.toMinecraftSignTileEntity(signTileEntity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openMinecartCommandBlock(Object commandBlockLogic) {
    this.wrapped().openMinecartCommandBlock((CommandBlockLogic) commandBlockLogic);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openCommandBlock(Object commandBlockTileEntity) {
    this.wrapped().openCommandBlock((CommandBlockTileEntity) commandBlockTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openStructureBlock(Object structureBlockTileEntity) {
    this.wrapped().openStructureBlock((StructureBlockTileEntity) structureBlockTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openJigsaw(Object jigsawTileEntity) {
    this.wrapped().openJigsaw((JigsawTileEntity) jigsawTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openHorseInventory(Object abstractHorseEntity, Inventory inventory) {
    // TODO: 08.10.2020 Implement
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openMerchantContainer(
      int container,
      Object merchantOffers,
      int levelProgress,
      int experience,
      boolean regularVillager,
      boolean refreshable) {
    this.wrapped()
        .openMerchantContainer(
            container,
            (MerchantOffers) merchantOffers,
            levelProgress,
            experience,
            regularVillager,
            refreshable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openBook(ItemStack stack, Hand hand) {
    this.wrapped()
        .openBook(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack),
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attackTargetEntityWithCurrentItem(Entity entity) {
    this.wrapped()
        .attackTargetEntityWithCurrentItem(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disableShield(boolean disable) {
    this.wrapped().disableShield(disable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spawnSweepParticles() {
    this.wrapped().spawnSweepParticles();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void respawnPlayer() {
    this.wrapped().respawnPlayer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUser() {
    return this.wrapped().isUser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile getGameProfile() {
    return this.gameProfileSerializer.deserialize(this.wrapped().getGameProfile());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers) {
    this.wrapped().stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPlayerFullyAsleep() {
    return this.wrapped().isPlayerFullyAsleep();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSleepTimer() {
    return this.wrapped().getSleepTimer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionbar) {
    this.wrapped()
        .sendStatusMessage(
            (ITextComponent)
                this.getEntityFoundationMapper().getComponentMapper().toMinecraft(component),
            actionbar);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getBedLocation() {
    Optional<BlockPos> bedPosition = this.wrapped().getBedPosition();

    return bedPosition
        .map(blockPos -> this.getWorld().fromMinecraftBlockPos(blockPos))
        .orElse(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStat(ResourceLocation resourceLocation) {
    this.wrapped().addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStat(ResourceLocation resourceLocation, int state) {
    this.wrapped()
        .addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle(), state);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addMovementStat(double x, double y, double z) {
    this.wrapped().addMovementStat(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean tryToStartFallFlying() {
    return this.wrapped().tryToStartFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startFallFlying() {
    this.wrapped().startFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopFallFlying() {
    this.wrapped().stopFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void giveExperiencePoints(int experiencePoints) {
    this.wrapped().giveExperiencePoints(experiencePoints);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getExperienceSeed() {
    return this.wrapped().getXPSeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addExperienceLevel(int experienceLevel) {
    this.wrapped().addExperienceLevel(experienceLevel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getExperienceBarCap() {
    return this.wrapped().xpBarCap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addExhaustion(float exhaustion) {
    this.wrapped().addExhaustion(exhaustion);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canEat(boolean ignoreHunger) {
    return this.wrapped().canEat(ignoreHunger);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean shouldHeal() {
    return this.wrapped().shouldHeal();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAllowEdit() {
    return this.wrapped().isAllowEdit();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPlayerAbilities() {
    this.wrapped().sendPlayerAbilities();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGameMode(GameMode gameMode) {
    this.wrapped()
        .setGameType((GameType) this.getEntityFoundationMapper().toMinecraftGameType(gameMode));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addItemStackToInventory(ItemStack itemStack) {
    return this.wrapped()
        .addItemStackToInventory(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCreative() {
    return this.wrapped().isCreative();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Scoreboard getScoreboard() {
    return this.getWorld().getScoreboard();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueId(GameProfile profile) {
    UUID uniqueId = profile.getUniqueId();
    if (uniqueId == null) {
      uniqueId = this.getOfflineUniqueId(profile.getName());
    }

    return uniqueId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getOfflineUniqueId(String username) {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWearing(PlayerClothing clothing) {
    return this.wrapped()
        .isWearing((PlayerModelPart) this.modelMapper.toMinecraftPlayerModelPart(clothing));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasReducedDebug() {
    return this.wrapped().hasReducedDebug();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setReducedDebug(boolean reducedDebug) {
    this.wrapped().setReducedDebug(reducedDebug);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrimaryHand(Hand.Side primaryHand) {
    this.wrapped()
        .setPrimaryHand(
            (HandSide)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHandSide(primaryHand));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooldownPeriod() {
    return this.wrapped().getCooldownPeriod();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooledAttackStrength(float strength) {
    return this.wrapped().getCooledAttackStrength(strength);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetCooldown() {
    this.wrapped().resetCooldown();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getLuck() {
    return this.wrapped().getLuck();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canUseCommandBlock() {
    return this.wrapped().canUseCommandBlock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTCompound getLeftShoulderEntity() {
    return (NBTCompound)
        this.getEntityFoundationMapper()
            .getNbtMapper()
            .fromMinecraftNBT(this.wrapped().getLeftShoulderEntity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NBTCompound getRightShoulderEntity() {
    return (NBTCompound)
        this.getEntityFoundationMapper()
            .getNbtMapper()
            .fromMinecraftNBT(this.wrapped().getRightShoulderEntity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFoodStatistics(int foodLevel, float modifier) {
    this.wrapped().getFoodStats().addStats(foodLevel, modifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFoodLevel() {
    return this.wrapped().getFoodStats().getFoodLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFoodLevel(int foodLevel) {
    this.wrapped().getFoodStats().setFoodLevel(foodLevel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean needFood() {
    return this.wrapped().getFoodStats().needFood();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSaturationLevel() {
    return this.wrapped().getFoodStats().getSaturationLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSaturationLevel(float saturationLevel) {
    this.wrapped().getFoodStats().setFoodSaturationLevel(saturationLevel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCooldown(Object item) {
    return this.wrapped().getCooldownTracker().hasCooldown((Item) item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooldown(Object item, float partialTicks) {
    return this.wrapped().getCooldownTracker().getCooldown((Item) item, partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCooldown(Object item, int ticks) {
    this.wrapped().getCooldownTracker().setCooldown((Item) item, ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeCooldown(Object item) {
    this.wrapped().getCooldownTracker().removeCooldown((Item) item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.wrapped()
                .findAmmo(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(shootable)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return this.wrapped()
        .canPickUpItem(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return this.wrapped()
        .replaceItemInInventory(
            slot,
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getAbsorptionAmount() {
    return this.wrapped().getAbsorptionAmount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    this.wrapped().setAbsorptionAmount(absorptionAmount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getScoreboardName() {
    return this.wrapped().getScoreboardName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPushedByWater() {
    return this.wrapped().isPushedByWater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSwimming() {
    return this.wrapped().isSwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.wrapped()
                .getItemStackFromSlot(
                    (net.minecraft.inventory.EquipmentSlotType)
                        this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getAIMoveSpeed() {
    return this.wrapped().getAIMoveSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateSwim() {
    this.wrapped().updateSwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void wakeUp() {
    this.wrapped().wakeUp();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startSleeping(BlockPosition position) {
    this.wrapped().startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove() {
    this.wrapped().remove();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.wrapped()
        .readAdditional(
            (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.wrapped()
        .writeAdditional(
            (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SoundCategory getSoundCategory() {
    return SoundCategory.PLAYER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.wrapped()
        .playSound(
            (SoundEvent)
                this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            volume,
            pitch);
  }
}
