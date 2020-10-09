package net.labyfy.internal.component.player.v1_15_2;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.player.PlayerEntity;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.player.type.model.ModelMapper;
import net.labyfy.component.player.type.model.PlayerClothing;
import net.labyfy.component.player.type.sound.Sound;
import net.labyfy.component.player.type.sound.SoundCategory;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.World;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.util.BlockPosition;
import net.labyfy.internal.component.entity.v1_15_2.VersionedLivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.Item;
import net.minecraft.item.MerchantOffers;
import net.minecraft.tileentity.*;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class VersionedPlayerEntity extends VersionedLivingEntity implements PlayerEntity {

  private final net.minecraft.entity.player.PlayerEntity entity;
  private final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileGameProfileSerializer;
  private final ModelMapper modelMapper;


  public VersionedPlayerEntity(
          Object entity,
          EntityType entityType,
          ClientWorld world,
          EntityMapper entityMapper,
          GameProfileSerializer gameProfileGameProfileSerializer,
          ModelMapper modelMapper
  ) {
    super(entity, entityType, world, entityMapper);
    this.gameProfileGameProfileSerializer = gameProfileGameProfileSerializer;
    this.modelMapper = modelMapper;

    if (!(entity instanceof net.minecraft.entity.player.PlayerEntity)) {
      throw new IllegalArgumentException("");
    }

    this.entity = (net.minecraft.entity.player.PlayerEntity) entity;
  }

  @Override
  public boolean blockActionRestricted(World world, BlockPosition position, GameMode mode) {
    return false;
  }

  @Override
  public boolean isSecondaryUseActive() {
    return this.entity.isSecondaryUseActive();
  }

  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    this.entity.playSound(
            (SoundEvent) this.getEntityMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            (net.minecraft.util.SoundCategory) this.getEntityMapper().getSoundMapper().toMinecraftSoundCategory(category),
            volume,
            pitch
    );
  }

  @Override
  public int getScore() {
    return this.entity.getScore();
  }

  @Override
  public void setScore(int score) {
    this.entity.setScore(score);
  }

  @Override
  public void addScore(int score) {
    this.entity.addScore(score);
  }

  @Override
  public boolean drop(boolean dropEntireStack) {
    return this.entity.drop(dropEntireStack);
  }

  @Override
  public Object dropItem(ItemStack itemStack, boolean traceItem) {
    return this.entity.dropItem(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack),
            traceItem
    );
  }

  @Override
  public Object dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem) {
    return this.entity.dropItem(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack),
            dropAround,
            traceItem
    );
  }

  @Override
  public boolean canAttackPlayer(PlayerEntity playerEntity) {
    // TODO: 08.10.2020 Implement
    return false;
  }

  @Override
  public void openSignEditor(Object signTileEntity) {
    this.entity.openSignEditor((SignTileEntity) signTileEntity);
  }

  @Override
  public void openMinecartCommandBlock(Object commandBlockLogic) {
    this.entity.openMinecartCommandBlock((CommandBlockLogic) commandBlockLogic);

  }

  @Override
  public void openCommandBlock(Object commandBlockTileEntity) {
    this.entity.openCommandBlock((CommandBlockTileEntity) commandBlockTileEntity);

  }

  @Override
  public void openStructureBlock(Object structureBlockTileEntity) {
    this.entity.openStructureBlock((StructureBlockTileEntity) structureBlockTileEntity);

  }

  @Override
  public void openJigsaw(Object jigsawTileEntity) {
    this.entity.openJigsaw((JigsawTileEntity) jigsawTileEntity);
  }

  @Override
  public void openHorseInventory(Object abstractHorseEntity, Inventory inventory) {
    // TODO: 08.10.2020 Implement
  }

  @Override
  public void openMerchantContainer(int containerId, Object merchantOffers, int level, int experience, boolean regularVillager, boolean refreshable) {
    this.entity.openMerchantContainer(
            containerId,
            (MerchantOffers) merchantOffers,
            level,
            experience,
            regularVillager,
            refreshable
    );
  }

  @Override
  public void openBook(ItemStack stack, Hand hand) {
    this.entity.openBook(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(stack),
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand)
    );
  }

  @Override
  public void attackTargetEntityWithCurrentItem(Entity entity) {
    this.entity.attackTargetEntityWithCurrentItem(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity)
    );
  }

  @Override
  public void disableShield(boolean disable) {
    this.entity.disableShield(disable);
  }

  @Override
  public void spawnSweepParticles() {
    this.entity.spawnSweepParticles();
  }

  @Override
  public void respawnPlayer() {
    this.entity.respawnPlayer();
  }

  @Override
  public boolean isUser() {
    return this.entity.isUser();
  }

  @Override
  public GameProfile getGameProfile() {
    return this.gameProfileGameProfileSerializer.deserialize(this.entity.getGameProfile());
  }

  @Override
  public void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers) {
    this.entity.stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  @Override
  public boolean isPlayerFullyAsleep() {
    return this.entity.isPlayerFullyAsleep();
  }

  @Override
  public int getSleepTimer() {
    return this.entity.getSleepTimer();
  }

  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionbar) {
    this.entity.sendStatusMessage(
            (ITextComponent) this.getEntityMapper().getComponentMapper().toMinecraft(component),
            actionbar
    );
  }

  @Override
  public BlockPosition getBedLocation() {
    return this.getWorld().fromMinecraftBlockPos(
            this.entity.getBedLocation()
    );
  }

  @Override
  public boolean isSpawnForced() {
    return this.entity.isSpawnForced();
  }

  @Override
  public void setRespawnPosition(BlockPosition position, boolean spawnForced, boolean bedSpawn) {
    this.entity.setRespawnPosition(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            spawnForced,
            bedSpawn
    );
  }

  @Override
  public void addStat(ResourceLocation resourceLocation) {
    this.entity.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle());
  }

  @Override
  public void addStat(ResourceLocation resourceLocation, int state) {
    this.entity.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle(), state);
  }

  @Override
  public void addMovementStat(double x, double y, double z) {
    this.entity.addMovementStat(x, y, z);
  }

  @Override
  public boolean tryToStartFallFlying() {
    return this.entity.tryToStartFallFlying();
  }

  @Override
  public void startFallFlying() {
    this.entity.startFallFlying();
  }

  @Override
  public void stopFallFlying() {
    this.entity.stopFallFlying();
  }

  @Override
  public void giveExperiencePoints(int experiencePoints) {
    this.entity.giveExperiencePoints(experiencePoints);
  }

  @Override
  public int getXPSeed() {
    return this.entity.getXPSeed();
  }

  @Override
  public void addExperienceLevel(int experienceLevel) {
    this.entity.addExperienceLevel(experienceLevel);
  }

  @Override
  public int getXpBarCap() {
    return this.entity.xpBarCap();
  }

  @Override
  public void addExhaustion(float exhaustion) {
    this.entity.addExhaustion(exhaustion);
  }

  @Override
  public boolean canEat(boolean ignoreHunger) {
    return this.entity.canEat(ignoreHunger);
  }

  @Override
  public boolean shouldHeal() {
    return this.entity.shouldHeal();
  }

  @Override
  public boolean isAllowEdit() {
    return this.entity.isAllowEdit();
  }

  @Override
  public void sendPlayerAbilities() {
    this.entity.sendPlayerAbilities();
  }

  @Override
  public void setGameMode(GameMode gameMode) {
    this.entity.setGameType((GameType) this.getEntityMapper().toMinecraftGameType(gameMode));
  }

  @Override
  public boolean addItemStackToInventory(ItemStack itemStack) {
    return this.entity.addItemStackToInventory(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack)
    );
  }

  @Override
  public boolean isCreative() {
    return this.entity.isCreative();
  }

  @Override
  public Scoreboard getScoreboard() {
    return this.getWorld().getScoreboard();
  }

  @Override
  public UUID getUniqueId(GameProfile profile) {
    UUID uniqueId = profile.getUniqueId();
    if (uniqueId == null) {
      uniqueId = this.getOfflineUniqueId(profile.getName());
    }

    return uniqueId;
  }

  @Override
  public UUID getOfflineUniqueId(String username) {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public boolean isWearing(PlayerClothing clothing) {
    return this.entity.isWearing((PlayerModelPart) this.modelMapper.toMinecraftPlayerModelPart(clothing));
  }

  @Override
  public boolean hasReducedDebug() {
    return this.entity.hasReducedDebug();
  }

  @Override
  public void setReducedDebug(boolean reducedDebug) {
    this.entity.setReducedDebug(reducedDebug);
  }

  @Override
  public void setPrimaryHand(Hand.Side primaryHand) {
    this.entity.setPrimaryHand(
            (HandSide) this.getEntityMapper().getHandMapper().toMinecraftHandSide(primaryHand)
    );
  }

  @Override
  public float getCooldownPeriod() {
    return this.entity.getCooldownPeriod();
  }

  @Override
  public float getCooledAttackStrength(float strength) {
    return this.entity.getCooledAttackStrength(strength);
  }

  @Override
  public void resetCooldown() {
    this.entity.resetCooldown();
  }

  @Override
  public float getLuck() {
    return this.entity.getLuck();
  }

  @Override
  public boolean canUseCommandBlock() {
    return this.entity.canUseCommandBlock();
  }

  @Override
  public boolean hasCooldown(Object item) {
    return this.entity.getCooldownTracker().hasCooldown((Item) item);
  }

  @Override
  public float getCooldown(Object item, float partialTicks) {
    return this.entity.getCooldownTracker().getCooldown((Item) item, partialTicks);
  }

  @Override
  public void setCooldown(Object item, int ticks) {
    this.entity.getCooldownTracker().setCooldown((Item) item, ticks);
  }

  @Override
  public void removeCooldown(Object item) {
    this.entity.getCooldownTracker().removeCooldown((Item) item);
  }
}
