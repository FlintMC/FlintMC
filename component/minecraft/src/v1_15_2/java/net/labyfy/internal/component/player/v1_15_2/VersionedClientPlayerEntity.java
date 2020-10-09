package net.labyfy.internal.component.player.v1_15_2;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.player.ClientPlayerEntity;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.network.NetworkPlayerInfoRegistry;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.serializer.gameprofile.GameProfileSerializer;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.player.type.model.ModelMapper;
import net.labyfy.component.player.type.model.PlayerClothing;
import net.labyfy.component.player.type.model.SkinModel;
import net.labyfy.component.player.type.sound.Sound;
import net.labyfy.component.player.type.sound.SoundCategory;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.ClientWorld;
import net.labyfy.component.world.World;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.util.BlockPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.Item;
import net.minecraft.item.MerchantOffers;
import net.minecraft.tileentity.*;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameType;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

@Implement(value = ClientPlayerEntity.class, version = "1.15.2")
public class VersionedClientPlayerEntity extends VersionedAbstractClientPlayer implements ClientPlayerEntity {

  private final GameProfileSerializer<com.mojang.authlib.GameProfile> gameProfileSerializer;
  private final ModelMapper modelMapper;
  private final NetworkPlayerInfoRegistry networkPlayerInfoRegistry;

  private final TabOverlay tabOverlay;

  @Inject
  private VersionedClientPlayerEntity(
          ClientWorld world,
          EntityMapper entityMapper,
          GameProfileSerializer gameProfileSerializer,
          ModelMapper modelMapper,
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          EntityTypeRegister entityTypeRegister,
          TabOverlay tabOverlay
  ) {
    super(
            Minecraft.getInstance().player,
            entityTypeRegister.getEntityType("player"),
            world,
            entityMapper,
            gameProfileSerializer,
            modelMapper,
            networkPlayerInfoRegistry
    );
    this.gameProfileSerializer = gameProfileSerializer;
    this.modelMapper = modelMapper;
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
    this.tabOverlay = tabOverlay;
  }

  @Override
  public boolean hasPlayerInfo() {
    return Minecraft.getInstance().player.hasPlayerInfo();
  }

  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(this.getGameProfile().getUniqueId());
  }

  @Override
  public float getFovModifier() {
    return Minecraft.getInstance().player.getFovModifier();
  }

  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasCloak();
  }

  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }

  @Override
  public boolean blockActionRestricted(World world, BlockPosition position, GameMode mode) {
    return false;
  }

  @Override
  public boolean isSecondaryUseActive() {
    return Minecraft.getInstance().player.isSecondaryUseActive();
  }

  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    Minecraft.getInstance().player.playSound(
            (SoundEvent) this.getEntityMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            (net.minecraft.util.SoundCategory) this.getEntityMapper().getSoundMapper().toMinecraftSoundCategory(category),
            volume,
            pitch
    );
  }

  @Override
  public int getScore() {
    return Minecraft.getInstance().player.getScore();
  }

  @Override
  public void setScore(int score) {
    Minecraft.getInstance().player.setScore(score);
  }

  @Override
  public void addScore(int score) {
    Minecraft.getInstance().player.addScore(score);
  }

  @Override
  public boolean drop(boolean dropEntireStack) {
    return Minecraft.getInstance().player.drop(dropEntireStack);
  }

  @Override
  public Object dropItem(ItemStack itemStack, boolean traceItem) {
    return Minecraft.getInstance().player.dropItem(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack),
            traceItem
    );
  }

  @Override
  public Object dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem) {
    return Minecraft.getInstance().player.dropItem(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack),
            dropAround,
            traceItem
    );
  }

  @Override
  public boolean canAttackPlayer(net.labyfy.component.player.PlayerEntity playerEntity) {
    return Minecraft.getInstance().player.canAttackPlayer((PlayerEntity) playerEntity);
  }

  @Override
  public void openSignEditor(Object signTileEntity) {
    Minecraft.getInstance().player.openSignEditor((SignTileEntity) signTileEntity);
  }

  @Override
  public void openMinecartCommandBlock(Object commandBlockLogic) {
    Minecraft.getInstance().player.openMinecartCommandBlock((CommandBlockLogic) commandBlockLogic);
  }

  @Override
  public void openCommandBlock(Object commandBlockTileEntity) {
    Minecraft.getInstance().player.openCommandBlock((CommandBlockTileEntity) commandBlockTileEntity);
  }

  @Override
  public void openStructureBlock(Object structureBlockTileEntity) {
    Minecraft.getInstance().player.openStructureBlock((StructureBlockTileEntity) structureBlockTileEntity);
  }

  @Override
  public void openJigsaw(Object jigsawTileEntity) {
    Minecraft.getInstance().player.openJigsaw((JigsawTileEntity) jigsawTileEntity);
  }

  @Override
  public void openHorseInventory(Object abstractHorseEntity, Inventory inventory) {
    // TODO: 09.10.2020 Implement
  }

  @Override
  public void openMerchantContainer(
          int containerId,
          Object merchantOffers,
          int level,
          int experience,
          boolean regularVillager,
          boolean refreshable
  ) {
    Minecraft.getInstance().player.openMerchantContainer(
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
    Minecraft.getInstance().player.openBook(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(stack),
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand)
    );
  }

  @Override
  public void attackTargetEntityWithCurrentItem(Entity entity) {
    Minecraft.getInstance().player.attackTargetEntityWithCurrentItem((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  @Override
  public void disableShield(boolean disable) {
    Minecraft.getInstance().player.disableShield(disable);
  }

  @Override
  public void spawnSweepParticles() {
    Minecraft.getInstance().player.spawnSweepParticles();
  }

  @Override
  public GameProfile getGameProfile() {
    return this.gameProfileSerializer.deserialize(Minecraft.getInstance().player.getGameProfile());
  }

  @Override
  public void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers) {
    Minecraft.getInstance().player.stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  @Override
  public boolean isPlayerFullyAsleep() {
    return Minecraft.getInstance().player.isPlayerFullyAsleep();
  }

  @Override
  public int getSleepTimer() {
    return Minecraft.getInstance().player.getSleepTimer();
  }

  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionbar) {
    Minecraft.getInstance().player.sendStatusMessage(
            (ITextComponent) this.getEntityMapper().getComponentMapper().toMinecraft(component),
            actionbar
    );
  }

  @Override
  public BlockPosition getBedLocation() {
    return this.getWorld().fromMinecraftBlockPos(Minecraft.getInstance().player.getBedLocation());
  }

  @Override
  public boolean isSpawnForced() {
    return Minecraft.getInstance().player.isSpawnForced();
  }

  @Override
  public void setRespawnPosition(BlockPosition position, boolean spawnForced, boolean bedSpawn) {
    Minecraft.getInstance().player.setRespawnPosition(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            spawnForced,
            bedSpawn
    );
  }

  @Override
  public void addStat(ResourceLocation resourceLocation) {
    Minecraft.getInstance().player.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle());
  }

  @Override
  public void addStat(ResourceLocation resourceLocation, int state) {
    Minecraft.getInstance().player.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle(), state);
  }

  @Override
  public void addMovementStat(double x, double y, double z) {
    Minecraft.getInstance().player.addMovementStat(x, y, z);
  }

  @Override
  public boolean tryToStartFallFlying() {
    return Minecraft.getInstance().player.tryToStartFallFlying();
  }

  @Override
  public void startFallFlying() {
    Minecraft.getInstance().player.startFallFlying();
  }

  @Override
  public void stopFallFlying() {
    Minecraft.getInstance().player.stopFallFlying();
  }

  @Override
  public void giveExperiencePoints(int experiencePoints) {
    Minecraft.getInstance().player.giveExperiencePoints(experiencePoints);
  }

  @Override
  public int getXPSeed() {
    return Minecraft.getInstance().player.getXPSeed();
  }

  @Override
  public void addExperienceLevel(int experienceLevel) {
    Minecraft.getInstance().player.addExperienceLevel(experienceLevel);
  }

  @Override
  public int getXpBarCap() {
    return Minecraft.getInstance().player.xpBarCap();
  }

  @Override
  public void addExhaustion(float exhaustion) {
    Minecraft.getInstance().player.addExhaustion(exhaustion);
  }

  @Override
  public boolean canEat(boolean ignoreHunger) {
    return Minecraft.getInstance().player.canEat(ignoreHunger);
  }

  @Override
  public boolean shouldHeal() {
    return Minecraft.getInstance().player.shouldHeal();
  }

  @Override
  public boolean isAllowEdit() {
    return Minecraft.getInstance().player.isAllowEdit();
  }

  @Override
  public void setGameMode(GameMode gameMode) {
    Minecraft.getInstance().player.setGameType(
            (GameType) this.getEntityMapper().toMinecraftGameType(gameMode)
    );
  }

  @Override
  public boolean addItemStackToInventory(ItemStack itemStack) {
    return Minecraft.getInstance().player.addItemStackToInventory(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack)
    );
  }

  @Override
  public boolean isCreative() {
    return Minecraft.getInstance().player.isCreative();
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
    return Minecraft.getInstance().player.isWearing((PlayerModelPart) this.modelMapper.toMinecraftPlayerModelPart(clothing));
  }

  @Override
  public boolean hasReducedDebug() {
    return Minecraft.getInstance().player.hasReducedDebug();
  }

  @Override
  public void setReducedDebug(boolean reducedDebug) {
    Minecraft.getInstance().player.setReducedDebug(reducedDebug);
  }

  @Override
  public float getCooldownPeriod() {
    return Minecraft.getInstance().player.getCooldownPeriod();
  }

  @Override
  public float getCooledAttackStrength(float strength) {
    return Minecraft.getInstance().player.getCooledAttackStrength(strength);
  }

  @Override
  public void resetCooldown() {
    Minecraft.getInstance().player.resetCooldown();
  }

  @Override
  public float getLuck() {
    return Minecraft.getInstance().player.getLuck();
  }

  @Override
  public boolean canUseCommandBlock() {
    return Minecraft.getInstance().player.canUseCommandBlock();
  }

  @Override
  public boolean hasCooldown(Object item) {
    return Minecraft.getInstance().player.getCooldownTracker().hasCooldown((Item) item);
  }

  @Override
  public float getCooldown(Object item, float partialTicks) {
    return Minecraft.getInstance().player.getCooldownTracker().getCooldown((Item) item, partialTicks);
  }

  @Override
  public void setCooldown(Object item, int ticks) {
    Minecraft.getInstance().player.getCooldownTracker().setCooldown((Item) item, ticks);
  }

  @Override
  public void removeCooldown(Object item) {
    Minecraft.getInstance().player.getCooldownTracker().removeCooldown((Item) item);
  }

  @Override
  public boolean canBreathUnderwater() {
    return Minecraft.getInstance().player.canBreatheUnderwater();
  }

  @Override
  public float getSwimAnimation(float partialTicks) {
    return Minecraft.getInstance().player.getSwimAnimation(partialTicks);
  }

  @Override
  public float getRenderScale() {
    return Minecraft.getInstance().player.getRenderScale();
  }

  @Override
  public Random getRandom() {
    return Minecraft.getInstance().player.getRNG();
  }

  @Override
  public LivingEntity getRevengeTarget() {
    return null;
  }

  @Override
  public void setRevengeTarget(LivingEntity entity) {
    Minecraft.getInstance().player.setRevengeTarget(null);
  }

  @Override
  public int getRevengeTimer() {
    return Minecraft.getInstance().player.getRevengeTimer();
  }

  @Override
  public LivingEntity getLastAttackedEntity() {
    return null;
  }

  @Override
  public void setLastAttackedEntity(Entity entity) {
    Minecraft.getInstance().player.setLastAttackedEntity((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  @Override
  public int getLastAttackedEntityTime() {
    return Minecraft.getInstance().player.getLastAttackedEntityTime();
  }

  @Override
  public int getIdleTime() {
    return Minecraft.getInstance().player.getIdleTime();
  }

  @Override
  public void setIdleTime(int idleTime) {
    Minecraft.getInstance().player.setIdleTime(idleTime);
  }

  @Override
  public double getVisibilityMultiplier(Entity entity) {
    return Minecraft.getInstance().player.getVisibilityMultiplier((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  @Override
  public boolean canAttack(LivingEntity entity) {
    return Minecraft.getInstance().player.canAttack((EntityType<?>) null);
  }

  @Override
  public boolean clearActivePotions() {
    return Minecraft.getInstance().player.clearActivePotions();
  }

  @Override
  public boolean isEntityUndead() {
    return Minecraft.getInstance().player.isEntityUndead();
  }

  @Override
  public float getHealth() {
    return Minecraft.getInstance().player.getHealth();
  }

  @Override
  public void setHealth(float health) {
    Minecraft.getInstance().player.setHealth(health);
  }

  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityMapper().getResourceLocationProvider().get(Minecraft.getInstance().player.getLootTableResourceLocation().getPath());
  }

  @Override
  public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
    Minecraft.getInstance().player.knockBack(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity),
            strength,
            xRatio,
            zRatio
    );
  }

  @Override
  public Sound getEatSound(ItemStack itemStack) {
    return this.getEntityMapper().getSoundMapper().fromMinecraftSoundEvent(Minecraft.getInstance().player.getEatSound(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack))
    );
  }

  @Override
  public boolean isOnLadder() {
    return Minecraft.getInstance().player.isOnLadder();
  }

  @Override
  public int getTotalArmorValue() {
    return Minecraft.getInstance().player.getTotalArmorValue();
  }

  @Override
  public float getMaxHealth() {
    return Minecraft.getInstance().player.getMaxHealth();
  }

  @Override
  public int getArrowCountInEntity() {
    return Minecraft.getInstance().player.getArrowCountInEntity();
  }

  @Override
  public void setArrowCountInEntity(int count) {
    Minecraft.getInstance().player.setArrowCountInEntity(count);
  }

  @Override
  public int getBeeStingCount() {
    return Minecraft.getInstance().player.getBeeStingCount();
  }

  @Override
  public void setBeeStingCount(int stingCount) {
    Minecraft.getInstance().player.setBeeStingCount(stingCount);
  }

  @Override
  public void swing(Hand hand, boolean swing) {
    Minecraft.getInstance().player.swing(
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand),
            swing
    );
  }

  @Override
  public ItemStack getHeldItem(Hand hand) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(Minecraft.getInstance().player.getHeldItem(
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand)));
  }

  @Override
  public void setHeldItem(Hand hand, ItemStack heldItem) {
    Minecraft.getInstance().player.setHeldItem(
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand),
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(heldItem));
  }

  @Override
  public boolean hasItemInSlot(EquipmentSlotType slotType) {
    return Minecraft.getInstance().player.hasItemInSlot(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
    );
  }

  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(
            Minecraft.getInstance().player.getItemStackFromSlot(
                    (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
            )
    );
  }

  @Override
  public float getArmorCoverPercentage() {
    return Minecraft.getInstance().player.getArmorCoverPercentage();
  }

  @Override
  public float getAIMoveSpeed() {
    return Minecraft.getInstance().player.getAIMoveSpeed();
  }

  @Override
  public void setAIMoveSpeed(float speed) {
    Minecraft.getInstance().player.setAIMoveSpeed(speed);
  }

  @Override
  public void attackEntityAsMob(Entity entity) {
    Minecraft.getInstance().player.attackEntityAsMob((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  @Override
  public void startSpinAttack(int duration) {
    Minecraft.getInstance().player.startSpinAttack(duration);
  }

  @Override
  public boolean isSpinAttacking() {
    return Minecraft.getInstance().player.isSpinAttacking();
  }

  @Override
  public void setJumping(boolean jumping) {
    Minecraft.getInstance().player.setJumping(jumping);
  }

  @Override
  public boolean canEntityBeSeen(Entity entity) {
    return Minecraft.getInstance().player.canEntityBeSeen(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity)
    );
  }

  @Override
  public float getSwingProgress(float partialTicks) {
    return Minecraft.getInstance().player.getSwingProgress(partialTicks);
  }

  @Override
  public boolean isServerWorld() {
    return Minecraft.getInstance().player.isServerWorld();
  }

  @Override
  public float getAbsorptionAmount() {
    return Minecraft.getInstance().player.getAbsorptionAmount();
  }

  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    Minecraft.getInstance().player.setAbsorptionAmount(absorptionAmount);
  }

  @Override
  public void sendEnterCombat() {
    Minecraft.getInstance().player.sendEnterCombat();
  }

  @Override
  public void sendEndCombat() {
    Minecraft.getInstance().player.sendEndCombat();
  }

  @Override
  public Hand.Side getPrimaryHand() {
    return this.getEntityMapper().getHandMapper().fromMinecraftHandSide(Minecraft.getInstance().player.getPrimaryHand());
  }

  @Override
  public void setPrimaryHand(Hand.Side primaryHand) {
    Minecraft.getInstance().player.setPrimaryHand(
            (HandSide) this.getEntityMapper().getHandMapper().toMinecraftHandSide(primaryHand)
    );
  }

  @Override
  public Hand getActiveHand() {
    return this.getEntityMapper().getHandMapper().fromMinecraftHand(Minecraft.getInstance().player.getActiveHand());
  }

  @Override
  public void setActiveHand(Hand hand) {
    Minecraft.getInstance().player.setActiveHand((net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand));
  }

  @Override
  public ItemStack getActiveItemStack() {
    return this.getEntityMapper().getItemMapper().fromMinecraft(Minecraft.getInstance().player.getActiveItemStack());
  }

  @Override
  public int getItemInUseCount() {
    return Minecraft.getInstance().player.getItemInUseCount();
  }

  @Override
  public int getItemInUseMaxCount() {
    return Minecraft.getInstance().player.getItemInUseMaxCount();
  }

  @Override
  public void stopActiveHand() {
    Minecraft.getInstance().player.stopActiveHand();
  }

  @Override
  public void resetActiveHand() {
    Minecraft.getInstance().player.resetActiveHand();
  }

  @Override
  public boolean isActiveItemStackBlocking() {
    return Minecraft.getInstance().player.isActiveItemStackBlocking();
  }

  @Override
  public boolean isSuppressingSlidingDownLadder() {
    return Minecraft.getInstance().player.isSuppressingSlidingDownLadder();
  }

  @Override
  public boolean isElytraFlying() {
    return Minecraft.getInstance().player.isElytraFlying();
  }

  @Override
  public int getTicksElytraFlying() {
    return Minecraft.getInstance().player.getTicksElytraFlying();
  }

  @Override
  public boolean attemptTeleport(double x, double y, double z, boolean teleportState) {
    return Minecraft.getInstance().player.attemptTeleport(x, y, z, teleportState);
  }

  @Override
  public boolean canBeHitWithPotion() {
    return Minecraft.getInstance().player.canBeHitWithPotion();
  }

  @Override
  public boolean attackable() {
    return Minecraft.getInstance().player.attackable();
  }

  @Override
  public void setPartying(BlockPosition position, boolean isPartying) {
    Minecraft.getInstance().player.setPartying(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            isPartying
    );
  }

  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return Minecraft.getInstance().player.canPickUpItem(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(stack)
    );
  }

  @Override
  public Optional<BlockPosition> getBedPosition() {
    Optional<BlockPosition> optional = Optional.empty();

    if (Minecraft.getInstance().player.getBedPosition().isPresent()) {
      optional = Optional.of(this.getWorld().fromMinecraftBlockPos(Minecraft.getInstance().player.getBedPosition().get()));
    }

    return optional;
  }

  @Override
  public void setBedPosition(BlockPosition position) {
    Minecraft.getInstance().player.setBedPosition(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position)
    );
  }

  @Override
  public void clearBedPosition() {
    Minecraft.getInstance().player.clearBedPosition();
  }

  @Override
  public boolean isSleeping() {
    return Minecraft.getInstance().player.isSleeping();
  }

  @Override
  public void startSleeping(BlockPosition position) {
    Minecraft.getInstance().player.startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  @Override
  public void wakeUp() {
    Minecraft.getInstance().player.wakeUp();
  }

  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(Minecraft.getInstance().player.findAmmo(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(shootable)
    ));
  }

  @Override
  public void sendBreakAnimation(EquipmentSlotType slotType) {
    Minecraft.getInstance().player.sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
    );
  }

  @Override
  public void sendBreakAnimation(Hand hand) {
    Minecraft.getInstance().player.sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().getHandMapper().toMinecraftHand(hand)
    );
  }

  @Override
  public int getTeamColor() {
    return Minecraft.getInstance().player.getTeamColor();
  }

  @Override
  public void detach() {
    Minecraft.getInstance().player.detach();
  }

  @Override
  public void setPacketCoordinates(double x, double y, double z) {
    Minecraft.getInstance().player.setPacketCoordinates(x, y, z);
  }

  @Override
  public int getIdentifier() {
    return Minecraft.getInstance().player.getEntityId();
  }

  @Override
  public void setIdentifier(int identifier) {
    Minecraft.getInstance().player.setEntityId(identifier);
  }

  @Override
  public Set<String> getTags() {
    return Minecraft.getInstance().player.getTags();
  }

  @Override
  public boolean addTag(String tag) {
    return Minecraft.getInstance().player.addTag(tag);
  }

  @Override
  public boolean removeTag(String tag) {
    return Minecraft.getInstance().player.removeTag(tag);
  }

  @Override
  public double getPosX() {
    return Minecraft.getInstance().player.getPosX();
  }

  @Override
  public double getPosY() {
    return Minecraft.getInstance().player.getPosY();
  }

  @Override
  public double getPosZ() {
    return Minecraft.getInstance().player.getPosZ();
  }

  @Override
  public void remove() {
    Minecraft.getInstance().player.remove();
  }

  @Override
  public EntityPose getPose() {
    return this.getEntityMapper().fromMinecraftPose(Minecraft.getInstance().player.getPose());
  }

  @Override
  public void setPosition(double x, double y, double z) {
    Minecraft.getInstance().player.setPosition(x, y, z);
  }

  @Override
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    Minecraft.getInstance().player.setPositionAndRotation(x, y, z, yaw, pitch);
  }

  @Override
  public void moveToBlockPosAndAngles(BlockPosition position, float rotationYaw, float rotationPitch) {
    Minecraft.getInstance().player.moveToBlockPosAndAngles(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            rotationYaw,
            rotationPitch
    );
  }

  @Override
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    Minecraft.getInstance().player.setLocationAndAngles(x, y, z, yaw, pitch);
  }

  @Override
  public void forceSetPosition(double x, double y, double z) {
    Minecraft.getInstance().player.forceSetPosition(x, y, z);
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
    return Minecraft.getInstance().player.getDistanceSq(x, y, z);
  }

  @Override
  public double getDistanceSq(Entity entity) {
    return this.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ());
  }

  @Override
  public void applyEntityCollision(Entity entity) {
    Minecraft.getInstance().player.applyEntityCollision(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity)
    );
  }

  @Override
  public void addVelocity(double x, double y, double z) {
    Minecraft.getInstance().player.addVelocity(x, y, z);
  }

  @Override
  public void rotateTowards(double yaw, double pitch) {
    Minecraft.getInstance().player.rotateTowards(yaw, pitch);
  }

  @Override
  public int getMaxInPortalTime() {
    return Minecraft.getInstance().player.getMaxInPortalTime();
  }

  @Override
  public void setFire(int seconds) {
    Minecraft.getInstance().player.setFire(seconds);
  }

  @Override
  public int getFireTimer() {
    return Minecraft.getInstance().player.getFireTimer();
  }

  @Override
  public void setFireTimer(int ticks) {
    Minecraft.getInstance().player.setFireTimer(ticks);
  }

  @Override
  public void extinguish() {
    Minecraft.getInstance().player.extinguish();
  }

  @Override
  public void resetPositionToBB() {
    Minecraft.getInstance().player.resetPositionToBB();
  }

  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    Minecraft.getInstance().player.playSound(
            (SoundEvent) this.getEntityMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            volume,
            pitch
    );
  }

  @Override
  public boolean isSilent() {
    return Minecraft.getInstance().player.isSilent();
  }

  @Override
  public void setSilent(boolean silent) {
    Minecraft.getInstance().player.setSilent(silent);
  }

  @Override
  public boolean hasNoGravity() {
    return Minecraft.getInstance().player.hasNoGravity();
  }

  @Override
  public void setNoGravity(boolean noGravity) {
    Minecraft.getInstance().player.setNoGravity(noGravity);
  }

  @Override
  public boolean isImmuneToFire() {
    return Minecraft.getInstance().player.isImmuneToFire();
  }

  @Override
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    return Minecraft.getInstance().player.isOffsetPositionInLiquid(x, y, z);
  }

  @Override
  public boolean isWet() {
    return Minecraft.getInstance().player.isWet();
  }

  @Override
  public boolean isInWaterRainOrBubbleColumn() {
    return Minecraft.getInstance().player.isInWaterRainOrBubbleColumn();
  }

  @Override
  public boolean isInWaterOrBubbleColumn() {
    return Minecraft.getInstance().player.isInWaterOrBubbleColumn();
  }

  @Override
  public boolean canSwim() {
    return Minecraft.getInstance().player.canSwim();
  }

  @Override
  public void updateSwim() {
    Minecraft.getInstance().player.updateSwimming();
  }

  @Override
  public boolean handleWaterMovement() {
    return Minecraft.getInstance().player.handleWaterMovement();
  }

  @Override
  public void spawnRunningParticles() {
    Minecraft.getInstance().player.spawnRunningParticles();
  }

  @Override
  public boolean isInWater() {
    return Minecraft.getInstance().player.isInWater();
  }

  @Override
  public void setInLava() {
    Minecraft.getInstance().player.setInLava();
  }

  @Override
  public boolean isInLava() {
    return Minecraft.getInstance().player.isInLava();
  }

  @Override
  public boolean isBurning() {
    return Minecraft.getInstance().player.isBurning();
  }

  @Override
  public boolean isPassenger() {
    return Minecraft.getInstance().player.isPassenger();
  }

  @Override
  public boolean isBeingRidden() {
    return Minecraft.getInstance().player.isBeingRidden();
  }

  @Override
  public boolean isSneaking() {
    return Minecraft.getInstance().player.isSneaking();
  }

  @Override
  public void setSneaking(boolean sneaking) {
    Minecraft.getInstance().player.setSneaking(sneaking);
  }

  @Override
  public boolean isSteppingCarefully() {
    return Minecraft.getInstance().player.isSteppingCarefully();
  }

  @Override
  public boolean isSuppressingBounce() {
    return Minecraft.getInstance().player.isSuppressingBounce();
  }

  @Override
  public boolean isDiscrete() {
    return Minecraft.getInstance().player.isDiscrete();
  }

  @Override
  public boolean isDescending() {
    return Minecraft.getInstance().player.isDescending();
  }

  @Override
  public boolean isCrouching() {
    return Minecraft.getInstance().player.isCrouching();
  }

  @Override
  public boolean isSprinting() {
    return Minecraft.getInstance().player.isSprinting();
  }

  @Override
  public void setSprinting(boolean sprinting) {
    Minecraft.getInstance().player.setSprinting(sprinting);
  }

  @Override
  public boolean isSwimming() {
    return Minecraft.getInstance().player.isSwimming();
  }

  @Override
  public void setSwimming(boolean swimming) {
    Minecraft.getInstance().player.setSwimming(swimming);
  }

  @Override
  public boolean isActuallySwimming() {
    return Minecraft.getInstance().player.isActualySwimming();
  }

  @Override
  public boolean isVisuallySwimming() {
    return Minecraft.getInstance().player.isVisuallySwimming();
  }

  @Override
  public boolean isGlowing() {
    return Minecraft.getInstance().player.isGlowing();
  }

  @Override
  public void setGlowing(boolean glowing) {
    Minecraft.getInstance().player.setGlowing(glowing);
  }

  @Override
  public boolean isInvisible() {
    return Minecraft.getInstance().player.isInvisible();
  }

  @Override
  public boolean isInvisibleToPlayer(Object player) {
    return Minecraft.getInstance().player.isInvisibleToPlayer((PlayerEntity) player);
  }

  @Override
  public boolean canRenderOnFire() {
    return Minecraft.getInstance().player.canRenderOnFire();
  }

  @Override
  public UUID getUniqueId() {
    return Minecraft.getInstance().player.getUniqueID();
  }

  @Override
  public void setUniqueId(UUID uniqueId) {
    Minecraft.getInstance().player.setUniqueId(uniqueId);
  }

  @Override
  public String getCachedUniqueId() {
    return Minecraft.getInstance().player.getCachedUniqueIdString();
  }

  @Override
  public String getScoreboardName() {
    return Minecraft.getInstance().player.getScoreboardName();
  }

  @Override
  public boolean isCustomNameVisible() {
    return Minecraft.getInstance().player.isCustomNameVisible();
  }

  @Override
  public void setCustomNameVisible(boolean alwaysRenderNameTag) {
    Minecraft.getInstance().player.setCustomNameVisible(alwaysRenderNameTag);
  }

  @Override
  public float getEyeHeight(EntityPose pose) {
    return Minecraft.getInstance().player.getEyeHeight(
            (Pose) this.getEntityMapper().toMinecraftPose(pose)
    );
  }

  @Override
  public float getEyeHeight() {
    return Minecraft.getInstance().player.getEyeHeight();
  }

  @Override
  public float getBrightness() {
    return Minecraft.getInstance().player.getBrightness();
  }

  @Override
  public Entity getRidingEntity() {
    return this.getEntityMapper().fromMinecraftEntity(Minecraft.getInstance().player.getRidingEntity());
  }

  @Override
  public void setMotion(double x, double y, double z) {
    Minecraft.getInstance().player.setMotion(x, y, z);
  }

  @Override
  public void teleportKeepLoaded(double x, double y, double z) {
    Minecraft.getInstance().player.teleportKeepLoaded(x, y, z);
  }

  @Override
  public void setPositionAndUpdate(double x, double y, double z) {
    Minecraft.getInstance().player.setPositionAndUpdate(x, y, z);
  }

  @Override
  public boolean getAlwaysRenderNameTagForRender() {
    return Minecraft.getInstance().player.getAlwaysRenderNameTagForRender();
  }

  @Override
  public void recalculateSize() {
    Minecraft.getInstance().player.recalculateSize();
  }

  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return Minecraft.getInstance().player.replaceItemInInventory(
            slot,
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack)
    );
  }

  @Override
  public boolean isImmuneToExplosions() {
    return Minecraft.getInstance().player.isImmuneToExplosions();
  }

  @Override
  public boolean ignoreItemEntityData() {
    return Minecraft.getInstance().player.ignoreItemEntityData();
  }

  @Override
  public Entity getControllingPassenger() {
    return this.getEntityMapper().fromMinecraftEntity(Minecraft.getInstance().player.getControllingPassenger());
  }

  @Override
  public List<Entity> getPassengers() {
    List<Entity> passengers = new ArrayList<>();

    for (net.minecraft.entity.Entity passenger : Minecraft.getInstance().player.getPassengers()) {
      passengers.add(this.getEntityMapper().fromMinecraftEntity(passenger));
    }

    return passengers;
  }

  @Override
  public boolean isPassenger(Entity entity) {
    return Minecraft.getInstance().player.isPassenger((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  @Override
  public boolean isPassenger(Class<? extends Entity> passenger) {
    return false;
  }

  @Override
  public Collection<Entity> getRecursivePassengers() {
    Set<Entity> entities = Sets.newHashSet();
    for (net.minecraft.entity.Entity passenger : Minecraft.getInstance().player.getPassengers()) {
      entities.add(this.getEntityMapper().fromMinecraftEntity(passenger));
    }

    return entities;
  }

  @Override
  public Stream<Entity> getSelfAndPassengers() {
    return Stream.concat(Stream.of(this), this.getPassengers().stream().flatMap(Entity::getSelfAndPassengers));
  }

  @Override
  public boolean isOnePlayerRiding() {
    return Minecraft.getInstance().player.isOnePlayerRiding();
  }

  @Override
  public Entity getLowestRidingEntity() {
    return this.getEntityMapper().fromMinecraftEntity(Minecraft.getInstance().player.getLowestRidingEntity());
  }

  @Override
  public boolean isRidingSameEntity(Entity entity) {
    // TODO: 09.10.2020 Implement 
    return false;
  }

  @Override
  public boolean isRidingOrBeingRiddenBy(Entity entity) {
    // TODO: 09.10.2020 Implement 
    return false;
  }

  @Override
  public boolean canPassengerSteer() {
    return Minecraft.getInstance().player.canPassengerSteer();
  }

  @Override
  public boolean hasPermissionLevel(int level) {
    return Minecraft.getInstance().player.hasPermissionLevel(level);
  }

  @Override
  public float getWidth() {
    return Minecraft.getInstance().player.getWidth();
  }

  @Override
  public float getHeight() {
    return Minecraft.getInstance().player.getHeight();
  }

  @Override
  public BlockPosition getPosition() {
    return this.getWorld().fromMinecraftBlockPos(Minecraft.getInstance().player.getPosition());
  }

  @Override
  public double getPosXWidth(double width) {
    return Minecraft.getInstance().player.getPosXWidth(width);
  }

  @Override
  public double getPosXRandom(double factor) {
    return Minecraft.getInstance().player.getPosXRandom(factor);
  }

  @Override
  public double getPosYHeight(double height) {
    return Minecraft.getInstance().player.getPosYHeight(height);
  }

  @Override
  public double getPosYRandom() {
    return Minecraft.getInstance().player.getPosYRandom();
  }

  @Override
  public double getPosYEye() {
    return Minecraft.getInstance().player.getPosYEye();
  }

  @Override
  public double getPosZWidth(double width) {
    return Minecraft.getInstance().player.getPosZWidth(width);
  }

  @Override
  public double getPosZRandom(double factor) {
    return Minecraft.getInstance().player.getPosZRandom(factor);
  }

  @Override
  public void setRawPosition(double x, double y, double z) {
    Minecraft.getInstance().player.setRawPosition(x, y, z);
  }

  @Override
  public ChatComponent getName() {
    return this.getEntityMapper().getComponentMapper().fromMinecraft(Minecraft.getInstance().player.getName());
  }

  @Override
  public void heal(float health) {
  }

  @Override
  public float getPitch(float partialTicks) {
    return Minecraft.getInstance().player.rotationPitch;
  }

  @Override
  public float getYaw(float partialTicks) {
    return Minecraft.getInstance().player.rotationYaw;
  }

  @Override
  public void sendChatMessage(String message) {
    Minecraft.getInstance().player.sendChatMessage(message);
  }

  @Override
  public void swingArm(Hand hand) {
    Minecraft.getInstance().player.swingArm((net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand));
  }

  @Override
  public void respawnPlayer() {
    Minecraft.getInstance().player.respawnPlayer();
  }

  @Override
  public void closeScreenAndDropStack() {
    Minecraft.getInstance().player.closeScreenAndDropStack();
  }

  @Override
  public void sendPlayerAbilities() {
    Minecraft.getInstance().player.sendPlayerAbilities();
  }

  @Override
  public boolean isUser() {
    return true;
  }

  @Override
  public void setPlayerSPHealth(float health) {
    Minecraft.getInstance().player.setPlayerSPHealth(health);
  }

  @Override
  public void sendHorseInventory() {
    Minecraft.getInstance().player.sendHorseInventory();
  }

  @Override
  public String getServerBrand() {
    return Minecraft.getInstance().player.getServerBrand();
  }

  @Override
  public void setServerBrand(String serverBrand) {
    Minecraft.getInstance().player.setServerBrand(serverBrand);
  }

  @Override
  public void setPermissionLevel(int level) {
    Minecraft.getInstance().player.setPermissionLevel(level);
  }

  @Override
  public void setXPStats(float currentExperience, int maxExperience, int level) {
    Minecraft.getInstance().player.setXPStats(currentExperience, maxExperience, level);
  }

  @Override
  public boolean isShowDeathScreen() {
    return Minecraft.getInstance().player.isShowDeathScreen();
  }

  @Override
  public void setShowDeathScreen(boolean showDeathScreen) {
    Minecraft.getInstance().player.setShowDeathScreen(showDeathScreen);
  }

  @Override
  public boolean isRidingHorse() {
    return Minecraft.getInstance().player.isRidingHorse();
  }

  @Override
  public float getHorseJumpPower() {
    return Minecraft.getInstance().player.getHorseJumpPower();
  }

  @Override
  public boolean isRowingBoat() {
    return Minecraft.getInstance().player.isRowingBoat();
  }

  @Override
  public boolean isAutoJumpEnabled() {
    return Minecraft.getInstance().player.isAutoJumpEnabled();
  }

  @Override
  public float getWaterBrightness() {
    return Minecraft.getInstance().player.getWaterBrightness();
  }

  @Override
  public TabOverlay getTabOverlay() {
    return this.tabOverlay;
  }

  @Override
  public void sendMessage(ChatComponent component, UUID senderUUID) {
    Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(
            (ITextComponent) this.getEntityMapper().getComponentMapper().toMinecraft(component)
    );
  }

  @Override
  public boolean isChild() {
    return Minecraft.getInstance().player.isChild();
  }

  @Override
  public boolean isSpectator() {
    return Minecraft.getInstance().player.isSpectator();
  }

  @Override
  public boolean canBeRiddenInWater() {
    return Minecraft.getInstance().player.canBeRiddenInWater();
  }

  @Override
  public boolean isPushedByWater() {
    return Minecraft.getInstance().player.isPushedByWater();
  }

  @Override
  public SoundCategory getSoundCategory() {
    return this.getEntityMapper().getSoundMapper().fromMinecraftSoundCategory(
            Minecraft.getInstance().player.getSoundCategory()
    );
  }

  @Override
  public void checkDespawn() {
    Minecraft.getInstance().player.checkDespawn();
  }

  @Override
  public boolean hasCustomName() {
    return Minecraft.getInstance().player.hasCustomName();
  }

  @Override
  public ChatComponent getDisplayName() {
    return this.getEntityMapper().getComponentMapper().fromMinecraft(
            Minecraft.getInstance().player.getDisplayName()
    );
  }

  @Override
  public ChatComponent getCustomName() {
    return this.getEntityMapper().getComponentMapper().fromMinecraft(
            Minecraft.getInstance().player.getCustomName()
    );
  }
}
