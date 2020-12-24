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

package net.flintmc.mcapi.v1_15_2.player;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.EntityNotLoadedException;
import net.flintmc.mcapi.entity.EntitySize;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.reason.MoverType;
import net.flintmc.mcapi.entity.type.EntityPose;
import net.flintmc.mcapi.entity.type.EntityTypeRegister;
import net.flintmc.mcapi.items.ItemStack;
import net.flintmc.mcapi.items.inventory.EquipmentSlotType;
import net.flintmc.mcapi.items.inventory.Inventory;
import net.flintmc.mcapi.items.inventory.InventoryController;
import net.flintmc.mcapi.items.inventory.player.PlayerInventory;
import net.flintmc.mcapi.nbt.NBTCompound;
import net.flintmc.mcapi.player.ClientPlayer;
import net.flintmc.mcapi.player.PlayerEntity;
import net.flintmc.mcapi.player.gameprofile.GameProfile;
import net.flintmc.mcapi.player.network.NetworkPlayerInfo;
import net.flintmc.mcapi.player.network.NetworkPlayerInfoRegistry;
import net.flintmc.mcapi.player.overlay.TabOverlay;
import net.flintmc.mcapi.player.serializer.gameprofile.GameProfileSerializer;
import net.flintmc.mcapi.player.type.GameMode;
import net.flintmc.mcapi.player.type.hand.Hand;
import net.flintmc.mcapi.player.type.hand.Hand.Side;
import net.flintmc.mcapi.player.type.model.ModelMapper;
import net.flintmc.mcapi.player.type.model.PlayerClothing;
import net.flintmc.mcapi.player.type.model.SkinModel;
import net.flintmc.mcapi.player.type.sound.Sound;
import net.flintmc.mcapi.player.type.sound.SoundCategory;
import net.flintmc.mcapi.resources.ResourceLocation;
import net.flintmc.mcapi.tileentity.SignTileEntity;
import net.flintmc.mcapi.tileentity.mapper.TileEntityMapper;
import net.flintmc.mcapi.world.math.BlockPosition;
import net.flintmc.mcapi.world.math.Vector3D;
import net.flintmc.mcapi.world.scoreboad.team.Team;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@Singleton
@Implement(value = ClientPlayer.class, version = "1.15.2")
public class VersionedClientPlayer extends VersionedPlayerEntity implements ClientPlayer {

  private static final String UNKNOWN_BIOME = "Unknown";

  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;
  private final InventoryController inventoryController;
  private final TabOverlay tabOverlay;
  private final TileEntityMapper tileEntityMapper;
  private final ModelMapper modelMapper;
  private final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;

  @Inject
  private VersionedClientPlayer(
      EntityTypeRegister entityTypeRegister,
      net.flintmc.mcapi.world.World world,
      EntityFoundationMapper entityFoundationMapper,
      GameProfileSerializer gameProfileSerializer,
      ModelMapper modelMapper,
      ItemEntityMapper itemEntityMapper,
      TileEntityMapper tileEntityMapper,
      NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
      InventoryController inventoryController,
      TabOverlay tabOverlay) {
    super(
        () -> Minecraft.getInstance().player,
        entityTypeRegister.getEntityType("player"),
        world,
        entityFoundationMapper,
        gameProfileSerializer,
        modelMapper,
        itemEntityMapper,
        tileEntityMapper);
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
    this.inventoryController = inventoryController;
    this.tabOverlay = tabOverlay;
    this.tileEntityMapper = tileEntityMapper;
    this.modelMapper = modelMapper;
    this.gameProfileSerializer = gameProfileSerializer;
  }

  @Override
  protected ClientPlayerEntity wrapped() {
    ClientPlayerEntity entity = Minecraft.getInstance().player;
    if (entity == null) {
      throw EntityNotLoadedException.INSTANCE;
    }
    return entity;
  }

  /** {@inheritDoc} */
  @Override
  public float getElytraPitch() {
    return this.wrapped().rotateElytraX;
  }

  /** {@inheritDoc} */
  @Override
  public void setElytraPitch(float elytraPitch) {
    this.wrapped().rotateElytraX = elytraPitch;
  }

  /** {@inheritDoc} */
  @Override
  public float getElytraYaw() {
    return this.wrapped().rotateElytraY;
  }

  /** {@inheritDoc} */
  @Override
  public void setElytraYaw(float elytraYaw) {
    this.wrapped().rotateElytraY = elytraYaw;
  }

  /** {@inheritDoc} */
  @Override
  public float getElytraRoll() {
    return this.wrapped().rotateElytraZ;
  }

  /** {@inheritDoc} */
  @Override
  public void setElytraRoll(float elytraRoll) {
    this.wrapped().rotateElytraZ = elytraRoll;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSpectator() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.SPECTATOR;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCreative() {
    NetworkPlayerInfo networkPlayerInfo = this.getNetworkPlayerInfo();
    return networkPlayerInfo != null && networkPlayerInfo.getGameMode() == GameMode.CREATIVE;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasPlayerInfo() {
    return this.getNetworkPlayerInfo() != null;
  }

  /** {@inheritDoc} */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(this.wrapped().getGameProfile().getId());
  }

  /** {@inheritDoc} */
  @Override
  public float getFovModifier() {
    return this.wrapped().getFovModifier();
  }

  /** {@inheritDoc} */
  @Override
  public PlayerInventory getInventoryController() {
    return this.inventoryController.getPlayerInventory();
  }

  /** {@inheritDoc} */
  @Override
  public Inventory getOpenInventory() {
    return this.inventoryController.getOpenInventory();
  }

  /** {@inheritDoc} */
  @Override
  public void sendChatMessage(String message) {
    this.wrapped().sendChatMessage(message);
  }

  /** {@inheritDoc} */
  @Override
  public void closeScreenAndDropStack() {
    this.wrapped().closeScreenAndDropStack();
  }

  /** {@inheritDoc} */
  @Override
  public void setPlayerSPHealth(float health) {
    this.wrapped().setPlayerSPHealth(health);
  }

  /** {@inheritDoc} */
  @Override
  public void sendHorseInventory() {
    this.wrapped().sendHorseInventory();
  }

  /** {@inheritDoc} */
  @Override
  public String getServerBrand() {
    return this.wrapped().getServerBrand();
  }

  /** {@inheritDoc} */
  @Override
  public void setServerBrand(String serverBrand) {
    this.wrapped().setServerBrand(serverBrand);
  }

  /** {@inheritDoc} */
  @Override
  public void setPermissionLevel(int level) {
    this.wrapped().setPermissionLevel(level);
  }

  /** {@inheritDoc} */
  @Override
  public void setExperienceStats(float currentExperience, int maxExperience, int level) {
    this.wrapped().setXPStats(currentExperience, maxExperience, level);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isShowDeathScreen() {
    return this.wrapped().isShowDeathScreen();
  }

  /** {@inheritDoc} */
  @Override
  public void setShowDeathScreen(boolean showDeathScreen) {
    this.wrapped().setShowDeathScreen(showDeathScreen);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingHorse() {
    return this.wrapped().isRidingHorse();
  }

  /** {@inheritDoc} */
  @Override
  public float getHorseJumpPower() {
    return this.wrapped().getHorseJumpPower();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRowingBoat() {
    return this.wrapped().isRowingBoat();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAutoJumpEnabled() {
    return this.wrapped().isAutoJumpEnabled();
  }

  /** {@inheritDoc} */
  @Override
  public float getWaterBrightness() {
    return this.wrapped().getWaterBrightness();
  }

  /** {@inheritDoc} */
  @Override
  public TabOverlay getTabOverlay() {
    return this.tabOverlay;
  }

  /** {@inheritDoc} */
  @Override
  public float getRenderArmYaw() {
    return this.wrapped().renderArmYaw;
  }

  /** {@inheritDoc} */
  @Override
  public void setRenderArmYaw(float renderArmYaw) {
    this.wrapped().renderArmYaw = renderArmYaw;
  }

  /** {@inheritDoc} */
  @Override
  public float getPreviousRenderArmYaw() {
    return this.wrapped().prevRenderArmYaw;
  }

  /** {@inheritDoc} */
  @Override
  public void setPreviousArmYaw(float previousRenderArmYaw) {
    this.wrapped().prevRenderArmYaw = previousRenderArmYaw;
  }

  /** {@inheritDoc} */
  @Override
  public float getRenderArmPitch() {
    return this.wrapped().renderArmPitch;
  }

  /** {@inheritDoc} */
  @Override
  public void setRenderArmPitch(float renderArmPitch) {
    this.wrapped().renderArmPitch = renderArmPitch;
  }

  /** {@inheritDoc} */
  @Override
  public float getPreviousRenderArmPitch() {
    return this.wrapped().prevRenderArmPitch;
  }

  /** {@inheritDoc} */
  @Override
  public void setPreviousRenderArmPitch(float previousRenderArmPitch) {
    this.wrapped().prevRenderArmPitch = previousRenderArmPitch;
  }

  /** {@inheritDoc} */
  @Override
  public String getBiome() {
    World world = Minecraft.getInstance().world;
    Entity renderViewEntity = Minecraft.getInstance().getRenderViewEntity();

    if (renderViewEntity == null) {
      return UNKNOWN_BIOME;
    }

    BlockPos blockPos = new BlockPos(renderViewEntity);

    if (world != null) {
      String biomePath = Registry.BIOME.getKey(world.getBiome(blockPos)).getPath();
      String[] split = biomePath.split("_");

      StringBuilder builder = new StringBuilder();

      for (int i = 0; i < split.length; i++) {
        String biomeName = split[i];
        biomeName = biomePath.substring(0, 1).toUpperCase() + biomeName.substring(1).toLowerCase();

        if (i == split.length - 1) {
          builder.append(biomeName);
          break;
        }

        builder.append(biomeName).append(" ");
      }
      return builder.toString();
    }

    return UNKNOWN_BIOME;
  }

  /** {@inheritDoc} */
  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }

  /** {@inheritDoc} */
  @Override
  public boolean blockActionRestricted(
      net.flintmc.mcapi.world.World world, BlockPosition position, GameMode mode) {
    return Minecraft.getInstance()
        .player
        .blockActionRestricted(
            this.wrapped().world,
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            (GameType) this.getEntityFoundationMapper().toMinecraftGameType(mode));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSecondaryUseActive() {
    return this.wrapped().isSecondaryUseActive();
  }

  /** {@inheritDoc} */
  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    Minecraft.getInstance()
        .player
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

  /** {@inheritDoc} */
  @Override
  public int getScore() {
    return this.wrapped().getScore();
  }

  /** {@inheritDoc} */
  @Override
  public void setScore(int score) {
    this.wrapped().setScore(score);
  }

  /** {@inheritDoc} */
  @Override
  public void addScore(int score) {
    this.wrapped().addScore(score);
  }

  /** {@inheritDoc} */
  @Override
  public boolean drop(boolean dropEntireStack) {
    return this.wrapped().drop(dropEntireStack);
  }

  /** {@inheritDoc} */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean traceItem) {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .getItemEntityMapper()
        .fromMinecraftItemEntity(
            Minecraft.getInstance()
                .player
                .dropItem(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack),
                    traceItem));
  }

  /** {@inheritDoc} */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem) {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .getItemEntityMapper()
        .fromMinecraftItemEntity(
            Minecraft.getInstance()
                .player
                .dropItem(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack),
                    dropAround,
                    traceItem));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canAttackPlayer(PlayerEntity playerEntity) {
    return Minecraft.getInstance()
        .player
        .canAttackPlayer(
            (net.minecraft.entity.player.PlayerEntity)
                this.getEntityFoundationMapper()
                    .getEntityMapper()
                    .toMinecraftPlayerEntity(playerEntity));
  }

  /** {@inheritDoc} */
  @Override
  public void openSignEditor(SignTileEntity signTileEntity) {
    Minecraft.getInstance()
        .player
        .openSignEditor(
            (net.minecraft.tileentity.SignTileEntity)
                this.tileEntityMapper.toMinecraftSignTileEntity(signTileEntity));
  }

  /** {@inheritDoc} */
  @Override
  public void openMinecartCommandBlock(Object commandBlockLogic) {
    this.wrapped().openMinecartCommandBlock((CommandBlockLogic) commandBlockLogic);
  }

  /** {@inheritDoc} */
  @Override
  public void openCommandBlock(Object commandBlockTileEntity) {
    Minecraft.getInstance()
        .player
        .openCommandBlock((CommandBlockTileEntity) commandBlockTileEntity);
  }

  /** {@inheritDoc} */
  @Override
  public void openStructureBlock(Object structureBlockTileEntity) {
    Minecraft.getInstance()
        .player
        .openStructureBlock((StructureBlockTileEntity) structureBlockTileEntity);
  }

  /** {@inheritDoc} */
  @Override
  public void openJigsaw(Object jigsawTileEntity) {
    this.wrapped().openJigsaw((JigsawTileEntity) jigsawTileEntity);
  }

  /** {@inheritDoc} */
  @Override
  public void openHorseInventory(Object abstractHorseEntity, Inventory inventory) {
    // TODO: 08.10.2020 Implement
  }

  /** {@inheritDoc} */
  @Override
  public void openMerchantContainer(
      int container,
      Object merchantOffers,
      int levelProgress,
      int experience,
      boolean regularVillager,
      boolean refreshable) {
    Minecraft.getInstance()
        .player
        .openMerchantContainer(
            container,
            (MerchantOffers) merchantOffers,
            levelProgress,
            experience,
            regularVillager,
            refreshable);
  }

  /** {@inheritDoc} */
  @Override
  public void openBook(ItemStack stack, Hand hand) {
    Minecraft.getInstance()
        .player
        .openBook(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack),
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void attackTargetEntityWithCurrentItem(net.flintmc.mcapi.entity.Entity entity) {
    Minecraft.getInstance()
        .player
        .attackTargetEntityWithCurrentItem(
            (net.minecraft.entity.Entity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void disableShield(boolean disable) {
    this.wrapped().disableShield(disable);
  }

  /** {@inheritDoc} */
  @Override
  public void spawnSweepParticles() {
    this.wrapped().spawnSweepParticles();
  }

  /** {@inheritDoc} */
  @Override
  public void respawnPlayer() {
    this.wrapped().respawnPlayer();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isUser() {
    return this.wrapped().isUser();
  }

  /** {@inheritDoc} */
  @Override
  public GameProfile getGameProfile() {
    try {
      return super.getGameProfile();
    } catch (EntityNotLoadedException ignored) {
      return this.gameProfileSerializer.deserialize(
          Minecraft.getInstance().getSession().getProfile());
    }
  }

  /** {@inheritDoc} */
  @Override
  public void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers) {
    this.wrapped().stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPlayerFullyAsleep() {
    return this.wrapped().isPlayerFullyAsleep();
  }

  /** {@inheritDoc} */
  @Override
  public int getSleepTimer() {
    return this.wrapped().getSleepTimer();
  }

  /** {@inheritDoc} */
  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionbar) {
    Minecraft.getInstance()
        .player
        .sendStatusMessage(
            (ITextComponent)
                this.getEntityFoundationMapper().getComponentMapper().toMinecraft(component),
            actionbar);
  }

  /** {@inheritDoc} */
  @Override
  public BlockPosition getBedLocation() {
    return this.getWorld().fromMinecraftBlockPos(this.wrapped().getBedLocation());
  }

  /** {@inheritDoc} */
  @Override
  public void addStat(ResourceLocation resourceLocation) {
    Minecraft.getInstance()
        .player
        .addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle());
  }

  /** {@inheritDoc} */
  @Override
  public void addStat(ResourceLocation resourceLocation, int state) {
    Minecraft.getInstance()
        .player
        .addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle(), state);
  }

  /** {@inheritDoc} */
  @Override
  public void addMovementStat(double x, double y, double z) {
    this.wrapped().addMovementStat(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean tryToStartFallFlying() {
    return this.wrapped().tryToStartFallFlying();
  }

  /** {@inheritDoc} */
  @Override
  public void startFallFlying() {
    this.wrapped().startFallFlying();
  }

  /** {@inheritDoc} */
  @Override
  public void stopFallFlying() {
    this.wrapped().stopFallFlying();
  }

  /** {@inheritDoc} */
  @Override
  public void giveExperiencePoints(int experiencePoints) {
    this.wrapped().giveExperiencePoints(experiencePoints);
  }

  /** {@inheritDoc} */
  @Override
  public int getExperienceSeed() {
    return this.wrapped().getXPSeed();
  }

  /** {@inheritDoc} */
  @Override
  public void addExperienceLevel(int experienceLevel) {
    this.wrapped().addExperienceLevel(experienceLevel);
  }

  /** {@inheritDoc} */
  @Override
  public int getExperienceBarCap() {
    return this.wrapped().xpBarCap();
  }

  /** {@inheritDoc} */
  @Override
  public void addExhaustion(float exhaustion) {
    this.wrapped().addExhaustion(exhaustion);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canEat(boolean ignoreHunger) {
    return this.wrapped().canEat(ignoreHunger);
  }

  /** {@inheritDoc} */
  @Override
  public boolean shouldHeal() {
    return this.wrapped().shouldHeal();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAllowEdit() {
    return this.wrapped().isAllowEdit();
  }

  /** {@inheritDoc} */
  @Override
  public void sendPlayerAbilities() {
    this.wrapped().sendPlayerAbilities();
  }

  /** {@inheritDoc} */
  @Override
  public void setGameMode(GameMode gameMode) {
    Minecraft.getInstance()
        .player
        .setGameType((GameType) this.getEntityFoundationMapper().toMinecraftGameType(gameMode));
  }

  /** {@inheritDoc} */
  @Override
  public boolean addItemStackToInventory(ItemStack itemStack) {
    return Minecraft.getInstance()
        .player
        .addItemStackToInventory(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /** {@inheritDoc} */
  @Override
  public UUID getUniqueId(GameProfile profile) {
    return ClientPlayerEntity.getUUID(this.gameProfileSerializer.serialize(profile));
  }

  /** {@inheritDoc} */
  @Override
  public UUID getOfflineUniqueId(String username) {
    return ClientPlayerEntity.getOfflineUUID(username);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isWearing(PlayerClothing clothing) {
    return Minecraft.getInstance()
        .player
        .isWearing((PlayerModelPart) this.modelMapper.toMinecraftPlayerModelPart(clothing));
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasReducedDebug() {
    return this.wrapped().hasReducedDebug();
  }

  /** {@inheritDoc} */
  @Override
  public void setReducedDebug(boolean reducedDebug) {
    this.wrapped().setReducedDebug(reducedDebug);
  }

  /** {@inheritDoc} */
  @Override
  public float getCooldownPeriod() {
    return this.wrapped().getCooldownPeriod();
  }

  /** {@inheritDoc} */
  @Override
  public float getCooledAttackStrength(float strength) {
    return this.wrapped().getCooledAttackStrength(strength);
  }

  /** {@inheritDoc} */
  @Override
  public void resetCooldown() {
    this.wrapped().resetCooldown();
  }

  /** {@inheritDoc} */
  @Override
  public float getLuck() {
    return this.wrapped().getLuck();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canUseCommandBlock() {
    return this.wrapped().canUseCommandBlock();
  }

  /** {@inheritDoc} */
  @Override
  public NBTCompound getLeftShoulderEntity() {
    return (NBTCompound)
        this.getEntityFoundationMapper()
            .getNbtMapper()
            .fromMinecraftNBT(this.wrapped().getLeftShoulderEntity());
  }

  /** {@inheritDoc} */
  @Override
  public NBTCompound getRightShoulderEntity() {
    return (NBTCompound)
        this.getEntityFoundationMapper()
            .getNbtMapper()
            .fromMinecraftNBT(this.wrapped().getRightShoulderEntity());
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasCooldown(Object item) {
    return this.wrapped().getCooldownTracker().hasCooldown((Item) item);
  }

  /** {@inheritDoc} */
  @Override
  public float getCooldown(Object item, float partialTicks) {
    return Minecraft.getInstance()
        .player
        .getCooldownTracker()
        .getCooldown((Item) item, partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public void setCooldown(Object item, int ticks) {
    this.wrapped().getCooldownTracker().setCooldown((Item) item, ticks);
  }

  /** {@inheritDoc} */
  @Override
  public void removeCooldown(Object item) {
    this.wrapped().getCooldownTracker().removeCooldown((Item) item);
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            Minecraft.getInstance()
                .player
                .findAmmo(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(shootable)));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return Minecraft.getInstance()
        .player
        .canPickUpItem(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack));
  }

  /** {@inheritDoc} */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return Minecraft.getInstance()
        .player
        .replaceItemInInventory(
            slot,
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /** {@inheritDoc} */
  @Override
  public float getAbsorptionAmount() {
    return this.wrapped().getAbsorptionAmount();
  }

  /** {@inheritDoc} */
  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    this.wrapped().setAbsorptionAmount(absorptionAmount);
  }

  /** {@inheritDoc} */
  @Override
  public String getScoreboardName() {
    return this.wrapped().getScoreboardName();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPushedByWater() {
    return this.wrapped().isPushedByWater();
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
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            Minecraft.getInstance()
                .player
                .getItemStackFromSlot(
                    (net.minecraft.inventory.EquipmentSlotType)
                        this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType)));
  }

  /** {@inheritDoc} */
  @Override
  public float getAIMoveSpeed() {
    return this.wrapped().getAIMoveSpeed();
  }

  /** {@inheritDoc} */
  @Override
  public void setAIMoveSpeed(float speed) {
    this.wrapped().setAIMoveSpeed(speed);
  }

  /** {@inheritDoc} */
  @Override
  public void updateSwim() {
    this.wrapped().updateSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void wakeUp() {
    this.wrapped().wakeUp();
  }

  /** {@inheritDoc} */
  @Override
  public void startSleeping(BlockPosition position) {
    Minecraft.getInstance()
        .player
        .startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void remove() {
    this.wrapped().remove();
  }

  /** {@inheritDoc} */
  @Override
  public void readAdditional(NBTCompound compound) {
    Minecraft.getInstance()
        .player
        .readAdditional(
            (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void writeAdditional(NBTCompound compound) {
    Minecraft.getInstance()
        .player
        .writeAdditional(
            (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    Minecraft.getInstance()
        .player
        .playSound(
            (SoundEvent)
                this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            volume,
            pitch);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBreathUnderwater() {
    return this.wrapped().canBreatheUnderwater();
  }

  /** {@inheritDoc} */
  @Override
  public float getSwimAnimation(float partialTicks) {
    return this.wrapped().getSwimAnimation(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public float getRenderScale() {
    return this.wrapped().getRenderScale();
  }

  /** {@inheritDoc} */
  @Override
  public Random getRandom() {
    return this.wrapped().getRNG();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getRevengeTarget() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.wrapped().getRevengeTarget());
  }

  /** {@inheritDoc} */
  @Override
  public void setRevengeTarget(LivingEntity entity) {
    Minecraft.getInstance()
        .player
        .setRevengeTarget(
            (net.minecraft.entity.LivingEntity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getRevengeTimer() {
    return this.wrapped().getRevengeTimer();
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getLastAttackedEntity() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftLivingEntity(this.wrapped().getLastAttackedEntity());
  }

  /** {@inheritDoc} */
  @Override
  public void setLastAttackedEntity(net.flintmc.mcapi.entity.Entity entity) {
    Minecraft.getInstance()
        .player
        .setLastAttackedEntity(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public int getLastAttackedEntityTime() {
    return this.wrapped().getLastAttackedEntityTime();
  }

  /** {@inheritDoc} */
  @Override
  public int getIdleTime() {
    return this.wrapped().getIdleTime();
  }

  /** {@inheritDoc} */
  @Override
  public void setIdleTime(int idleTime) {
    this.wrapped().setIdleTime(idleTime);
  }

  /** {@inheritDoc} */
  @Override
  public double getVisibilityMultiplier(net.flintmc.mcapi.entity.Entity entity) {
    return Minecraft.getInstance()
        .player
        .getVisibilityMultiplier(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canAttack(LivingEntity entity) {
    return Minecraft.getInstance()
        .player
        .canAttack(
            (EntityType<?>)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftLivingEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean clearActivePotions() {
    return this.wrapped().clearActivePotions();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEntityUndead() {
    return this.wrapped().isEntityUndead();
  }

  /** {@inheritDoc} */
  @Override
  public void heal(float health) {
    this.wrapped().heal(health);
  }

  /** {@inheritDoc} */
  @Override
  public float getHealth() {
    return this.wrapped().getHealth();
  }

  /** {@inheritDoc} */
  @Override
  public void setHealth(float health) {
    this.wrapped().setHealth(health);
  }

  /** {@inheritDoc} */
  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityFoundationMapper()
        .getResourceLocationProvider()
        .get(this.wrapped().getLootTableResourceLocation().getPath());
  }

  /** {@inheritDoc} */
  @Override
  public void knockBack(
      net.flintmc.mcapi.entity.Entity entity, float strength, double xRatio, double zRatio) {
    Minecraft.getInstance()
        .player
        .knockBack(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity),
            strength,
            xRatio,
            zRatio);
  }

  /** {@inheritDoc} */
  @Override
  public Sound getEatSound(ItemStack itemStack) {
    return this.getEntityFoundationMapper()
        .getSoundMapper()
        .fromMinecraftSoundEvent(
            Minecraft.getInstance()
                .player
                .getEatSound(
                    (net.minecraft.item.ItemStack)
                        this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack)));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnLadder() {
    return this.wrapped().isOnLadder();
  }

  /** {@inheritDoc} */
  @Override
  public int getTotalArmorValue() {
    return this.wrapped().getTotalArmorValue();
  }

  /** {@inheritDoc} */
  @Override
  public float getMaxHealth() {
    return this.wrapped().getMaxHealth();
  }

  /** {@inheritDoc} */
  @Override
  public int getArrowCountInEntity() {
    return this.wrapped().getArrowCountInEntity();
  }

  /** {@inheritDoc} */
  @Override
  public void setArrowCountInEntity(int count) {
    this.wrapped().setArrowCountInEntity(count);
  }

  /** {@inheritDoc} */
  @Override
  public int getBeeStingCount() {
    return this.wrapped().getBeeStingCount();
  }

  /** {@inheritDoc} */
  @Override
  public void setBeeStingCount(int stingCount) {
    this.wrapped().setBeeStingCount(stingCount);
  }

  /** {@inheritDoc} */
  @Override
  public void swingArm(Hand hand) {
    Minecraft.getInstance()
        .player
        .swingArm(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void swing(Hand hand, boolean sendToAll) {
    Minecraft.getInstance()
        .player
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
            Minecraft.getInstance()
                .player
                .getHeldItem(
                    (net.minecraft.util.Hand)
                        this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand)));
  }

  /** {@inheritDoc} */
  @Override
  public void setHeldItem(Hand hand, ItemStack heldItem) {
    Minecraft.getInstance()
        .player
        .setHeldItem(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand),
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(heldItem));
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasItemInSlot(EquipmentSlotType slotType) {
    return Minecraft.getInstance()
        .player
        .hasItemInSlot(
            (net.minecraft.inventory.EquipmentSlotType)
                this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public float getArmorCoverPercentage() {
    return this.wrapped().getArmorCoverPercentage();
  }

  /** {@inheritDoc} */
  @Override
  public void applyEntityCollision(net.flintmc.mcapi.entity.Entity entity) {
    Minecraft.getInstance()
        .player
        .applyEntityCollision(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getYaw(float partialTicks) {
    return this.wrapped().getYaw(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public void attackEntityAsMob(net.flintmc.mcapi.entity.Entity entity) {
    Minecraft.getInstance()
        .player
        .attackEntityAsMob(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActuallySwimming() {
    return this.wrapped().isActualySwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void startSpinAttack(int duration) {
    this.wrapped().startSpinAttack(duration);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSpinAttacking() {
    return this.wrapped().isSpinAttacking();
  }

  /** {@inheritDoc} */
  @Override
  public void setJumping(boolean jumping) {
    this.wrapped().setJumping(jumping);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canEntityBeSeen(net.flintmc.mcapi.entity.Entity entity) {
    return Minecraft.getInstance()
        .player
        .canEntityBeSeen(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public float getSwingProgress(float partialTicks) {
    return this.wrapped().getSwingProgress(partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isServerWorld() {
    return this.wrapped().isServerWorld();
  }

  /** {@inheritDoc} */
  @Override
  public void sendEnterCombat() {
    this.wrapped().sendEnterCombat();
  }

  /** {@inheritDoc} */
  @Override
  public void sendEndCombat() {
    this.wrapped().sendEndCombat();
  }

  /** {@inheritDoc} */
  @Override
  public Side getPrimaryHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHandSide(this.wrapped().getPrimaryHand());
  }

  /** {@inheritDoc} */
  @Override
  public void setPrimaryHand(Side primaryHand) {
    Minecraft.getInstance()
        .player
        .setPrimaryHand(
            (HandSide)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHandSide(primaryHand));
  }

  /** {@inheritDoc} */
  @Override
  public Hand getActiveHand() {
    return this.getEntityFoundationMapper()
        .getHandMapper()
        .fromMinecraftHand(this.wrapped().getActiveHand());
  }

  /** {@inheritDoc} */
  @Override
  public void setActiveHand(Hand hand) {
    Minecraft.getInstance()
        .player
        .setActiveHand(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getActiveItemStack() {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(this.wrapped().getActiveItemStack());
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseCount() {
    return this.wrapped().getItemInUseCount();
  }

  /** {@inheritDoc} */
  @Override
  public int getItemInUseMaxCount() {
    return this.wrapped().getItemInUseMaxCount();
  }

  /** {@inheritDoc} */
  @Override
  public void stopActiveHand() {
    this.wrapped().stopActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public void resetActiveHand() {
    this.wrapped().resetActiveHand();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isActiveItemStackBlocking() {
    return this.wrapped().isActiveItemStackBlocking();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSuppressingSlidingDownLadder() {
    return this.wrapped().isSuppressingSlidingDownLadder();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isElytraFlying() {
    return this.wrapped().isElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public int getTicksElytraFlying() {
    return this.wrapped().getTicksElytraFlying();
  }

  /** {@inheritDoc} */
  @Override
  public boolean attemptTeleport(double x, double y, double z, boolean particleEffects) {
    return this.wrapped().attemptTeleport(x, y, z, particleEffects);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeHitWithPotion() {
    return this.wrapped().canBeHitWithPotion();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canBeRiddenInWater() {
    return this.wrapped().canBeRiddenInWater();
  }

  /** {@inheritDoc} */
  @Override
  public boolean attackable() {
    return this.wrapped().attackable();
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveStrafing() {
    return this.wrapped().moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveStrafing(float moveStrafing) {
    this.wrapped().moveStrafing = moveStrafing;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveVertical() {
    return this.wrapped().moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveVertical(float moveVertical) {
    this.wrapped().moveVertical = moveVertical;
  }

  /** {@inheritDoc} */
  @Override
  public float getMoveForward() {
    return this.wrapped().moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setMoveForward(float moveForward) {
    this.wrapped().moveForward = moveForward;
  }

  /** {@inheritDoc} */
  @Override
  public void setPartying(BlockPosition position, boolean isPartying) {
    Minecraft.getInstance()
        .player
        .setPartying((BlockPos) this.getWorld().toMinecraftBlockPos(position), isPartying);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<BlockPosition> getBedPosition() {
    Optional<BlockPosition> optional = Optional.empty();

    if (this.wrapped().getBedPosition().isPresent()) {
      optional =
          Optional.of(this.getWorld().fromMinecraftBlockPos(this.wrapped().getBedPosition().get()));
    }

    return optional;
  }

  /** {@inheritDoc} */
  @Override
  public void setBedPosition(BlockPosition position) {
    Minecraft.getInstance()
        .player
        .setBedPosition((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void clearBedPosition() {
    this.wrapped().clearBedPosition();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSleeping() {
    return this.wrapped().isSleeping();
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(EquipmentSlotType slotType) {
    Minecraft.getInstance()
        .player
        .sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType)
                this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType));
  }

  /** {@inheritDoc} */
  @Override
  public void sendBreakAnimation(Hand hand) {
    Minecraft.getInstance()
        .player
        .sendBreakAnimation(
            (net.minecraft.util.Hand)
                this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAlive() {
    return this.wrapped().isAlive();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPlayer() {
    return true;
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
  public EntityPose getPose() {
    return this.getEntityFoundationMapper().fromMinecraftPose(this.wrapped().getPose());
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
    Minecraft.getInstance()
        .player
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
  public float getDistance(net.flintmc.mcapi.entity.Entity entity) {
    return Minecraft.getInstance()
        .player
        .getDistance(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public double getDistanceSq(double x, double y, double z) {
    return this.wrapped().getDistanceSq(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public double getDistanceSq(net.flintmc.mcapi.entity.Entity entity) {
    return Minecraft.getInstance()
        .player
        .getDistanceSq(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
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
  public boolean isInvisibleToPlayer(PlayerEntity player) {
    return Minecraft.getInstance()
        .player
        .isInvisibleToPlayer(
            (net.minecraft.entity.player.PlayerEntity)
                this.getEntityFoundationMapper().getEntityMapper().toMinecraftPlayerEntity(player));
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
    return Minecraft.getInstance()
        .player
        .getEyeHeight((Pose) this.getEntityFoundationMapper().toMinecraftPose(pose));
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
  public net.flintmc.mcapi.entity.Entity getRidingEntity() {
    return this.getEntityFoundationMapper()
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
  public net.flintmc.mcapi.entity.Entity getControllingPassenger() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftEntity(this.wrapped().getControllingPassenger());
  }

  /** {@inheritDoc} */
  @Override
  public List<net.flintmc.mcapi.entity.Entity> getPassengers() {
    List<net.flintmc.mcapi.entity.Entity> passengers = new ArrayList<>();

    for (net.minecraft.entity.Entity passenger : this.wrapped().getPassengers()) {
      passengers.add(
          this.getEntityFoundationMapper().getEntityMapper().fromMinecraftEntity(passenger));
    }

    return passengers;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPassenger(net.flintmc.mcapi.entity.Entity entity) {
    return Minecraft.getInstance()
        .player
        .isPassenger(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public Collection<net.flintmc.mcapi.entity.Entity> getRecursivePassengers() {
    Set<net.flintmc.mcapi.entity.Entity> entities = Sets.newHashSet();
    for (net.minecraft.entity.Entity passenger : this.wrapped().getPassengers()) {
      entities.add(
          this.getEntityFoundationMapper().getEntityMapper().fromMinecraftEntity(passenger));
    }

    return entities;
  }

  /** {@inheritDoc} */
  @Override
  public Stream<net.flintmc.mcapi.entity.Entity> getSelfAndPassengers() {
    return Stream.concat(
        Stream.of(this),
        this.getPassengers().stream()
            .flatMap(net.flintmc.mcapi.entity.Entity::getSelfAndPassengers));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOnePlayerRiding() {
    return this.wrapped().isOnePlayerRiding();
  }

  /** {@inheritDoc} */
  @Override
  public net.flintmc.mcapi.entity.Entity getLowestRidingEntity() {
    return this.getEntityFoundationMapper()
        .getEntityMapper()
        .fromMinecraftEntity(this.wrapped().getLowestRidingEntity());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingSameEntity(net.flintmc.mcapi.entity.Entity entity) {
    return Minecraft.getInstance()
        .player
        .isRidingSameEntity(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRidingOrBeingRiddenBy(net.flintmc.mcapi.entity.Entity entity) {
    return Minecraft.getInstance()
        .player
        .isRidingOrBeingRiddenBy(
            (Entity) this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
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

  /** {@inheritDoc} */
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
  public boolean isInSameTeam(net.flintmc.mcapi.entity.Entity entity) {
    return this.isInScoreboardTeam(entity.getTeam());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInScoreboardTeam(Team team) {
    return this.getTeam() != null && this.getTeam().isSameTeam(team);
  }

  /** {@inheritDoc} */
  @Override
  public void move(MoverType moverType, Vector3D vector3D) {
    Minecraft.getInstance()
        .player
        .move(
            (net.minecraft.entity.MoverType)
                this.getEntityFoundationMapper().toMinecraftMoverType(moverType),
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
  public ChatComponent getName() {
    return this.getEntityFoundationMapper()
        .getComponentMapper()
        .fromMinecraft(this.wrapped().getName());
  }

  /** {@inheritDoc} */
  @Override
  public boolean isInFluid() {
    return this.isInLava() || this.isInWater();
  }

  /** {@inheritDoc} */
  @Override
  public void sendMessage(ChatComponent component, UUID senderUUID) {
    Minecraft.getInstance()
        .player
        .sendMessage(
            (ITextComponent)
                this.getEntityFoundationMapper().getComponentMapper().toMinecraft(component));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCollided() {
    return this.isCollidedHorizontally() || this.isCollidedVertically();
  }

  /** {@inheritDoc} */
  @Override
  public void checkDespawn() {
    this.wrapped().checkDespawn();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasCustomName() {
    return this.wrapped().hasCustomName();
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getDisplayName() {
    return this.getEntityFoundationMapper()
        .getComponentMapper()
        .fromMinecraft(this.wrapped().getDisplayName());
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getCustomName() {
    return this.getEntityFoundationMapper()
        .getComponentMapper()
        .fromMinecraft(this.wrapped().getCustomName());
  }
}
