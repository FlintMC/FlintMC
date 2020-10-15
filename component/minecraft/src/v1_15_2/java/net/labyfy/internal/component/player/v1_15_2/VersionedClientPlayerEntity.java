package net.labyfy.internal.component.player.v1_15_2;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.EntityMapper;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.item.ItemEntity;
import net.labyfy.component.entity.type.EntityPose;
import net.labyfy.component.entity.type.EntityTypeRegister;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.EquipmentSlotType;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.items.inventory.InventoryController;
import net.labyfy.component.items.inventory.player.PlayerInventory;
import net.labyfy.component.nbt.mapper.NBTMapper;
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
import net.labyfy.component.world.math.BlockPosition;
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

  private final InventoryController inventoryController;
  private final TabOverlay tabOverlay;

  @Inject
  private VersionedClientPlayerEntity(
          ClientWorld world,
          EntityMapper entityMapper,
          GameProfileSerializer gameProfileSerializer,
          ModelMapper modelMapper,
          NetworkPlayerInfoRegistry networkPlayerInfoRegistry,
          EntityTypeRegister entityTypeRegister,
          InventoryController inventoryController,
          TabOverlay tabOverlay,
          NBTMapper nbtMapper
  ) {
    super(
            Minecraft.getInstance().player,
            entityTypeRegister.getEntityType("player"),
            world,
            entityMapper,
            gameProfileSerializer,
            modelMapper,
            networkPlayerInfoRegistry,
            nbtMapper
    );
    this.gameProfileSerializer = gameProfileSerializer;
    this.modelMapper = modelMapper;
    this.networkPlayerInfoRegistry = networkPlayerInfoRegistry;
    this.inventoryController = inventoryController;
    this.tabOverlay = tabOverlay;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PlayerInventory getInventoryController() {
    return this.inventoryController.getPlayerInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Inventory getOpenInventory() {
    return this.inventoryController.getOpenInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPlayerInfo() {
    return Minecraft.getInstance().player.hasPlayerInfo();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NetworkPlayerInfo getNetworkPlayerInfo() {
    return this.networkPlayerInfoRegistry.getPlayerInfo(this.getGameProfile().getUniqueId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getFovModifier() {
    return Minecraft.getInstance().player.getFovModifier();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SkinModel getSkinModel() {
    return this.getNetworkPlayerInfo().getSkinModel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getSkinLocation() {
    return this.getNetworkPlayerInfo().getSkinLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getCloakLocation() {
    return this.getNetworkPlayerInfo().getCloakLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getElytraLocation() {
    return this.getNetworkPlayerInfo().getElytraLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasSkin() {
    return this.getNetworkPlayerInfo().hasSkin();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCloak() {
    return this.getNetworkPlayerInfo().hasCloak();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasElytra() {
    return this.getNetworkPlayerInfo().hasElytra();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean blockActionRestricted(World world, BlockPosition position, GameMode mode) {
    return Minecraft.getInstance().player.blockActionRestricted(
            Minecraft.getInstance().player.world,
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            (GameType) this.getEntityMapper().toMinecraftGameType(mode)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSecondaryUseActive() {
    return Minecraft.getInstance().player.isSecondaryUseActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    Minecraft.getInstance().player.playSound(
            (SoundEvent) this.getEntityMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            (net.minecraft.util.SoundCategory) this.getEntityMapper().getSoundMapper().toMinecraftSoundCategory(category),
            volume,
            pitch
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getScore() {
    return Minecraft.getInstance().player.getScore();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setScore(int score) {
    Minecraft.getInstance().player.setScore(score);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addScore(int score) {
    Minecraft.getInstance().player.addScore(score);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean drop(boolean dropEntireStack) {
    return Minecraft.getInstance().player.drop(dropEntireStack);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean traceItem) {
    return this.getEntityMapper().fromMinecraftItemEntity(
            Minecraft.getInstance().player.dropItem(
                    (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack),
                    traceItem
            )
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem) {
    return this.getEntityMapper().fromMinecraftItemEntity(
            Minecraft.getInstance().player.dropItem(
                    (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack),
                    dropAround,
                    traceItem
            )
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPitch() {
    return Minecraft.getInstance().player.rotationPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPitch(float pitch) {
    Minecraft.getInstance().player.rotationPitch = pitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getYaw() {
    return Minecraft.getInstance().player.rotationYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setYaw(float yaw) {
    Minecraft.getInstance().player.rotationPitch = yaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAttackPlayer(net.labyfy.component.player.PlayerEntity playerEntity) {
    return Minecraft.getInstance().player.canAttackPlayer((PlayerEntity) playerEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openSignEditor(Object signTileEntity) {
    Minecraft.getInstance().player.openSignEditor((SignTileEntity) signTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openMinecartCommandBlock(Object commandBlockLogic) {
    Minecraft.getInstance().player.openMinecartCommandBlock((CommandBlockLogic) commandBlockLogic);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openCommandBlock(Object commandBlockTileEntity) {
    Minecraft.getInstance().player.openCommandBlock((CommandBlockTileEntity) commandBlockTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openStructureBlock(Object structureBlockTileEntity) {
    Minecraft.getInstance().player.openStructureBlock((StructureBlockTileEntity) structureBlockTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openJigsaw(Object jigsawTileEntity) {
    Minecraft.getInstance().player.openJigsaw((JigsawTileEntity) jigsawTileEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openHorseInventory(Object abstractHorseEntity, Inventory inventory) {
    // TODO: 09.10.2020 Implement
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
          boolean refreshable
  ) {
    Minecraft.getInstance().player.openMerchantContainer(
            container,
            (MerchantOffers) merchantOffers,
            levelProgress,
            experience,
            regularVillager,
            refreshable
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void openBook(ItemStack stack, Hand hand) {
    Minecraft.getInstance().player.openBook(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(stack),
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attackTargetEntityWithCurrentItem(Entity entity) {
    Minecraft.getInstance().player.attackTargetEntityWithCurrentItem((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disableShield(boolean disable) {
    Minecraft.getInstance().player.disableShield(disable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spawnSweepParticles() {
    Minecraft.getInstance().player.spawnSweepParticles();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public GameProfile getGameProfile() {
    return this.gameProfileSerializer.deserialize(Minecraft.getInstance().player.getGameProfile());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers) {
    Minecraft.getInstance().player.stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPlayerFullyAsleep() {
    return Minecraft.getInstance().player.isPlayerFullyAsleep();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSleepTimer() {
    return Minecraft.getInstance().player.getSleepTimer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionbar) {
    Minecraft.getInstance().player.sendStatusMessage(
            (ITextComponent) this.getEntityMapper().getComponentMapper().toMinecraft(component),
            actionbar
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getBedLocation() {
    return this.getWorld().fromMinecraftBlockPos(Minecraft.getInstance().player.getBedLocation());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStat(ResourceLocation resourceLocation) {
    Minecraft.getInstance().player.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStat(ResourceLocation resourceLocation, int state) {
    Minecraft.getInstance().player.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle(), state);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addMovementStat(double x, double y, double z) {
    Minecraft.getInstance().player.addMovementStat(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean tryToStartFallFlying() {
    return Minecraft.getInstance().player.tryToStartFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startFallFlying() {
    Minecraft.getInstance().player.startFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopFallFlying() {
    Minecraft.getInstance().player.stopFallFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void giveExperiencePoints(int experiencePoints) {
    Minecraft.getInstance().player.giveExperiencePoints(experiencePoints);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getExperienceSeed() {
    return Minecraft.getInstance().player.getXPSeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addExperienceLevel(int experienceLevel) {
    Minecraft.getInstance().player.addExperienceLevel(experienceLevel);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getExperienceBarCap() {
    return Minecraft.getInstance().player.xpBarCap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addExhaustion(float exhaustion) {
    Minecraft.getInstance().player.addExhaustion(exhaustion);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canEat(boolean ignoreHunger) {
    return Minecraft.getInstance().player.canEat(ignoreHunger);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean shouldHeal() {
    return Minecraft.getInstance().player.shouldHeal();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAllowEdit() {
    return Minecraft.getInstance().player.isAllowEdit();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGameMode(GameMode gameMode) {
    Minecraft.getInstance().player.setGameType(
            (GameType) this.getEntityMapper().toMinecraftGameType(gameMode)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addItemStackToInventory(ItemStack itemStack) {
    return Minecraft.getInstance().player.addItemStackToInventory(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCreative() {
    return Minecraft.getInstance().player.isCreative();
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
    return Minecraft.getInstance().player.isWearing((PlayerModelPart) this.modelMapper.toMinecraftPlayerModelPart(clothing));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasReducedDebug() {
    return Minecraft.getInstance().player.hasReducedDebug();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setReducedDebug(boolean reducedDebug) {
    Minecraft.getInstance().player.setReducedDebug(reducedDebug);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooldownPeriod() {
    return Minecraft.getInstance().player.getCooldownPeriod();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooledAttackStrength(float strength) {
    return Minecraft.getInstance().player.getCooledAttackStrength(strength);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetCooldown() {
    Minecraft.getInstance().player.resetCooldown();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getLuck() {
    return Minecraft.getInstance().player.getLuck();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canUseCommandBlock() {
    return Minecraft.getInstance().player.canUseCommandBlock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCooldown(Object item) {
    return Minecraft.getInstance().player.getCooldownTracker().hasCooldown((Item) item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getCooldown(Object item, float partialTicks) {
    return Minecraft.getInstance().player.getCooldownTracker().getCooldown((Item) item, partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCooldown(Object item, int ticks) {
    Minecraft.getInstance().player.getCooldownTracker().setCooldown((Item) item, ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeCooldown(Object item) {
    Minecraft.getInstance().player.getCooldownTracker().removeCooldown((Item) item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBreathUnderwater() {
    return Minecraft.getInstance().player.canBreatheUnderwater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSwimAnimation(float partialTicks) {
    return Minecraft.getInstance().player.getSwimAnimation(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getRenderScale() {
    return Minecraft.getInstance().player.getRenderScale();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Random getRandom() {
    return Minecraft.getInstance().player.getRNG();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LivingEntity getRevengeTarget() {
    return this.getEntityMapper().fromMinecraftLivingEntity(
            Minecraft.getInstance().player.getRevengeTarget()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRevengeTarget(LivingEntity entity) {
    Minecraft.getInstance().player.setRevengeTarget(
            (net.minecraft.entity.LivingEntity) this.getEntityMapper().toMinecraftLivingEntity(entity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getRevengeTimer() {
    return Minecraft.getInstance().player.getRevengeTimer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LivingEntity getLastAttackedEntity() {
    return this.getEntityMapper().fromMinecraftLivingEntity(
            Minecraft.getInstance().player.getLastAttackedEntity()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastAttackedEntity(Entity entity) {
    Minecraft.getInstance().player.setLastAttackedEntity((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getLastAttackedEntityTime() {
    return Minecraft.getInstance().player.getLastAttackedEntityTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getIdleTime() {
    return Minecraft.getInstance().player.getIdleTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIdleTime(int idleTime) {
    Minecraft.getInstance().player.setIdleTime(idleTime);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getVisibilityMultiplier(Entity entity) {
    return Minecraft.getInstance().player.getVisibilityMultiplier((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAttack(LivingEntity entity) {
    return Minecraft.getInstance().player.canAttack(
            (EntityType<?>) this.getEntityMapper().toMinecraftLivingEntity(entity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean clearActivePotions() {
    return Minecraft.getInstance().player.clearActivePotions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEntityUndead() {
    return Minecraft.getInstance().player.isEntityUndead();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHealth() {
    return Minecraft.getInstance().player.getHealth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHealth(float health) {
    Minecraft.getInstance().player.setHealth(health);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResourceLocation getLootTableResourceLocation() {
    return this.getEntityMapper().getResourceLocationProvider().get(Minecraft.getInstance().player.getLootTableResourceLocation().getPath());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
    Minecraft.getInstance().player.knockBack(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity),
            strength,
            xRatio,
            zRatio
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Sound getEatSound(ItemStack itemStack) {
    return this.getEntityMapper().getSoundMapper().fromMinecraftSoundEvent(Minecraft.getInstance().player.getEatSound(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack))
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOnLadder() {
    return Minecraft.getInstance().player.isOnLadder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTotalArmorValue() {
    return Minecraft.getInstance().player.getTotalArmorValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getMaxHealth() {
    return Minecraft.getInstance().player.getMaxHealth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getArrowCountInEntity() {
    return Minecraft.getInstance().player.getArrowCountInEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setArrowCountInEntity(int count) {
    Minecraft.getInstance().player.setArrowCountInEntity(count);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getBeeStingCount() {
    return Minecraft.getInstance().player.getBeeStingCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBeeStingCount(int stingCount) {
    Minecraft.getInstance().player.setBeeStingCount(stingCount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void swing(Hand hand, boolean sendToAll) {
    Minecraft.getInstance().player.swing(
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand),
            sendToAll
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getHeldItem(Hand hand) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(Minecraft.getInstance().player.getHeldItem(
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHeldItem(Hand hand, ItemStack heldItem) {
    Minecraft.getInstance().player.setHeldItem(
            (net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand),
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(heldItem));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasItemInSlot(EquipmentSlotType slotType) {
    return Minecraft.getInstance().player.hasItemInSlot(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(
            Minecraft.getInstance().player.getItemStackFromSlot(
                    (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
            )
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getArmorCoverPercentage() {
    return Minecraft.getInstance().player.getArmorCoverPercentage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getAIMoveSpeed() {
    return Minecraft.getInstance().player.getAIMoveSpeed();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAIMoveSpeed(float speed) {
    Minecraft.getInstance().player.setAIMoveSpeed(speed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attackEntityAsMob(Entity entity) {
    Minecraft.getInstance().player.attackEntityAsMob((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startSpinAttack(int duration) {
    Minecraft.getInstance().player.startSpinAttack(duration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSpinAttacking() {
    return Minecraft.getInstance().player.isSpinAttacking();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setJumping(boolean jumping) {
    Minecraft.getInstance().player.setJumping(jumping);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canEntityBeSeen(Entity entity) {
    return Minecraft.getInstance().player.canEntityBeSeen(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getSwingProgress(float partialTicks) {
    return Minecraft.getInstance().player.getSwingProgress(partialTicks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isServerWorld() {
    return Minecraft.getInstance().player.isServerWorld();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getAbsorptionAmount() {
    return Minecraft.getInstance().player.getAbsorptionAmount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    Minecraft.getInstance().player.setAbsorptionAmount(absorptionAmount);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendEnterCombat() {
    Minecraft.getInstance().player.sendEnterCombat();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendEndCombat() {
    Minecraft.getInstance().player.sendEndCombat();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand.Side getPrimaryHand() {
    return this.getEntityMapper().getHandMapper().fromMinecraftHandSide(Minecraft.getInstance().player.getPrimaryHand());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPrimaryHand(Hand.Side primaryHand) {
    Minecraft.getInstance().player.setPrimaryHand(
            (HandSide) this.getEntityMapper().getHandMapper().toMinecraftHandSide(primaryHand)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Hand getActiveHand() {
    return this.getEntityMapper().getHandMapper().fromMinecraftHand(Minecraft.getInstance().player.getActiveHand());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActiveHand(Hand hand) {
    Minecraft.getInstance().player.setActiveHand((net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack getActiveItemStack() {
    return this.getEntityMapper().getItemMapper().fromMinecraft(Minecraft.getInstance().player.getActiveItemStack());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getItemInUseCount() {
    return Minecraft.getInstance().player.getItemInUseCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getItemInUseMaxCount() {
    return Minecraft.getInstance().player.getItemInUseMaxCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stopActiveHand() {
    Minecraft.getInstance().player.stopActiveHand();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetActiveHand() {
    Minecraft.getInstance().player.resetActiveHand();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActiveItemStackBlocking() {
    return Minecraft.getInstance().player.isActiveItemStackBlocking();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSuppressingSlidingDownLadder() {
    return Minecraft.getInstance().player.isSuppressingSlidingDownLadder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isElytraFlying() {
    return Minecraft.getInstance().player.isElytraFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTicksElytraFlying() {
    return Minecraft.getInstance().player.getTicksElytraFlying();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean attemptTeleport(double x, double y, double z, boolean teleportState) {
    return Minecraft.getInstance().player.attemptTeleport(x, y, z, teleportState);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeHitWithPotion() {
    return Minecraft.getInstance().player.canBeHitWithPotion();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean attackable() {
    return Minecraft.getInstance().player.attackable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPartying(BlockPosition position, boolean isPartying) {
    Minecraft.getInstance().player.setPartying(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            isPartying
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return Minecraft.getInstance().player.canPickUpItem(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(stack)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<BlockPosition> getBedPosition() {
    Optional<BlockPosition> optional = Optional.empty();

    if (Minecraft.getInstance().player.getBedPosition().isPresent()) {
      optional = Optional.of(this.getWorld().fromMinecraftBlockPos(Minecraft.getInstance().player.getBedPosition().get()));
    }

    return optional;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBedPosition(BlockPosition position) {
    Minecraft.getInstance().player.setBedPosition(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearBedPosition() {
    Minecraft.getInstance().player.clearBedPosition();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSleeping() {
    return Minecraft.getInstance().player.isSleeping();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startSleeping(BlockPosition position) {
    Minecraft.getInstance().player.startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void wakeUp() {
    Minecraft.getInstance().player.wakeUp();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityMapper().getItemMapper().fromMinecraft(Minecraft.getInstance().player.findAmmo(
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(shootable)
    ));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendBreakAnimation(EquipmentSlotType slotType) {
    Minecraft.getInstance().player.sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().toMinecraftEquipmentSlotType(slotType)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendBreakAnimation(Hand hand) {
    Minecraft.getInstance().player.sendBreakAnimation(
            (net.minecraft.inventory.EquipmentSlotType) this.getEntityMapper().getHandMapper().toMinecraftHand(hand)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTeamColor() {
    return Minecraft.getInstance().player.getTeamColor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detach() {
    Minecraft.getInstance().player.detach();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPacketCoordinates(double x, double y, double z) {
    Minecraft.getInstance().player.setPacketCoordinates(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getIdentifier() {
    return Minecraft.getInstance().player.getEntityId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIdentifier(int identifier) {
    Minecraft.getInstance().player.setEntityId(identifier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> getTags() {
    return Minecraft.getInstance().player.getTags();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean addTag(String tag) {
    return Minecraft.getInstance().player.addTag(tag);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeTag(String tag) {
    return Minecraft.getInstance().player.removeTag(tag);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosX() {
    return Minecraft.getInstance().player.getPosX();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosY() {
    return Minecraft.getInstance().player.getPosY();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosZ() {
    return Minecraft.getInstance().player.getPosZ();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove() {
    Minecraft.getInstance().player.remove();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityPose getPose() {
    return this.getEntityMapper().fromMinecraftPose(Minecraft.getInstance().player.getPose());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPosition(double x, double y, double z) {
    Minecraft.getInstance().player.setPosition(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
    Minecraft.getInstance().player.setPositionAndRotation(x, y, z, yaw, pitch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveToBlockPosAndAngles(BlockPosition position, float rotationYaw, float rotationPitch) {
    Minecraft.getInstance().player.moveToBlockPosAndAngles(
            (BlockPos) this.getWorld().toMinecraftBlockPos(position),
            rotationYaw,
            rotationPitch
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
    Minecraft.getInstance().player.setLocationAndAngles(x, y, z, yaw, pitch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void forceSetPosition(double x, double y, double z) {
    Minecraft.getInstance().player.forceSetPosition(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getDistance(Entity entity) {
    float distanceX = (float) (this.getPosX() - entity.getPosX());
    float distanceY = (float) (this.getPosY() - entity.getPosY());
    float distanceZ = (float) (this.getPosZ() - entity.getPosZ());
    return MathHelper.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDistanceSq(double x, double y, double z) {
    return Minecraft.getInstance().player.getDistanceSq(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getDistanceSq(Entity entity) {
    return this.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void applyEntityCollision(Entity entity) {
    Minecraft.getInstance().player.applyEntityCollision(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addVelocity(double x, double y, double z) {
    Minecraft.getInstance().player.addVelocity(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rotateTowards(double yaw, double pitch) {
    Minecraft.getInstance().player.rotateTowards(yaw, pitch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxInPortalTime() {
    return Minecraft.getInstance().player.getMaxInPortalTime();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFire(int seconds) {
    Minecraft.getInstance().player.setFire(seconds);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getFireTimer() {
    return Minecraft.getInstance().player.getFireTimer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFireTimer(int ticks) {
    Minecraft.getInstance().player.setFireTimer(ticks);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void extinguish() {
    Minecraft.getInstance().player.extinguish();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetPositionToBB() {
    Minecraft.getInstance().player.resetPositionToBB();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    Minecraft.getInstance().player.playSound(
            (SoundEvent) this.getEntityMapper().getSoundMapper().toMinecraftSoundEvent(sound),
            volume,
            pitch
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSilent() {
    return Minecraft.getInstance().player.isSilent();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSilent(boolean silent) {
    Minecraft.getInstance().player.setSilent(silent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasNoGravity() {
    return Minecraft.getInstance().player.hasNoGravity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNoGravity(boolean noGravity) {
    Minecraft.getInstance().player.setNoGravity(noGravity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isImmuneToFire() {
    return Minecraft.getInstance().player.isImmuneToFire();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOffsetPositionInLiquid(double x, double y, double z) {
    return Minecraft.getInstance().player.isOffsetPositionInLiquid(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWet() {
    return Minecraft.getInstance().player.isWet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInWaterRainOrBubbleColumn() {
    return Minecraft.getInstance().player.isInWaterRainOrBubbleColumn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInWaterOrBubbleColumn() {
    return Minecraft.getInstance().player.isInWaterOrBubbleColumn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canSwim() {
    return Minecraft.getInstance().player.canSwim();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateSwim() {
    Minecraft.getInstance().player.updateSwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleWaterMovement() {
    return Minecraft.getInstance().player.handleWaterMovement();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void spawnRunningParticles() {
    Minecraft.getInstance().player.spawnRunningParticles();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInWater() {
    return Minecraft.getInstance().player.isInWater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInLava() {
    Minecraft.getInstance().player.setInLava();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInLava() {
    return Minecraft.getInstance().player.isInLava();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBurning() {
    return Minecraft.getInstance().player.isBurning();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPassenger() {
    return Minecraft.getInstance().player.isPassenger();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBeingRidden() {
    return Minecraft.getInstance().player.isBeingRidden();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSneaking() {
    return Minecraft.getInstance().player.isSneaking();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSneaking(boolean sneaking) {
    Minecraft.getInstance().player.setSneaking(sneaking);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSteppingCarefully() {
    return Minecraft.getInstance().player.isSteppingCarefully();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSuppressingBounce() {
    return Minecraft.getInstance().player.isSuppressingBounce();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDiscrete() {
    return Minecraft.getInstance().player.isDiscrete();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDescending() {
    return Minecraft.getInstance().player.isDescending();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCrouching() {
    return Minecraft.getInstance().player.isCrouching();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSprinting() {
    return Minecraft.getInstance().player.isSprinting();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSprinting(boolean sprinting) {
    Minecraft.getInstance().player.setSprinting(sprinting);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSwimming() {
    return Minecraft.getInstance().player.isSwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSwimming(boolean swimming) {
    Minecraft.getInstance().player.setSwimming(swimming);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActuallySwimming() {
    return Minecraft.getInstance().player.isActualySwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isVisuallySwimming() {
    return Minecraft.getInstance().player.isVisuallySwimming();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isGlowing() {
    return Minecraft.getInstance().player.isGlowing();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setGlowing(boolean glowing) {
    Minecraft.getInstance().player.setGlowing(glowing);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInvisible() {
    return Minecraft.getInstance().player.isInvisible();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInvisibleToPlayer(net.labyfy.component.player.PlayerEntity player) {
    return Minecraft.getInstance().player.isInvisibleToPlayer((PlayerEntity) player);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canRenderOnFire() {
    return Minecraft.getInstance().player.canRenderOnFire();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueId() {
    return Minecraft.getInstance().player.getUniqueID();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUniqueId(UUID uniqueId) {
    Minecraft.getInstance().player.setUniqueId(uniqueId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCachedUniqueId() {
    return Minecraft.getInstance().player.getCachedUniqueIdString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getScoreboardName() {
    return Minecraft.getInstance().player.getScoreboardName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCustomNameVisible() {
    return Minecraft.getInstance().player.isCustomNameVisible();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCustomNameVisible(boolean alwaysRenderNameTag) {
    Minecraft.getInstance().player.setCustomNameVisible(alwaysRenderNameTag);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getEyeHeight(EntityPose pose) {
    return Minecraft.getInstance().player.getEyeHeight(
            (Pose) this.getEntityMapper().toMinecraftPose(pose)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getEyeHeight() {
    return Minecraft.getInstance().player.getEyeHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getBrightness() {
    return Minecraft.getInstance().player.getBrightness();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity getRidingEntity() {
    return this.getEntityMapper().fromMinecraftEntity(Minecraft.getInstance().player.getRidingEntity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setMotion(double x, double y, double z) {
    Minecraft.getInstance().player.setMotion(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void teleportKeepLoaded(double x, double y, double z) {
    Minecraft.getInstance().player.teleportKeepLoaded(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPositionAndUpdate(double x, double y, double z) {
    Minecraft.getInstance().player.setPositionAndUpdate(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAlwaysRenderNameTagForRender() {
    return Minecraft.getInstance().player.getAlwaysRenderNameTagForRender();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void recalculateSize() {
    Minecraft.getInstance().player.recalculateSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return Minecraft.getInstance().player.replaceItemInInventory(
            slot,
            (net.minecraft.item.ItemStack) this.getEntityMapper().getItemMapper().toMinecraft(itemStack)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isImmuneToExplosions() {
    return Minecraft.getInstance().player.isImmuneToExplosions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean ignoreItemEntityData() {
    return Minecraft.getInstance().player.ignoreItemEntityData();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity getControllingPassenger() {
    return this.getEntityMapper().fromMinecraftEntity(Minecraft.getInstance().player.getControllingPassenger());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Entity> getPassengers() {
    List<Entity> passengers = new ArrayList<>();

    for (net.minecraft.entity.Entity passenger : Minecraft.getInstance().player.getPassengers()) {
      passengers.add(this.getEntityMapper().fromMinecraftEntity(passenger));
    }

    return passengers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPassenger(Entity entity) {
    return Minecraft.getInstance().player.isPassenger((net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Entity> getRecursivePassengers() {
    Set<Entity> entities = Sets.newHashSet();
    for (net.minecraft.entity.Entity passenger : Minecraft.getInstance().player.getPassengers()) {
      entities.add(this.getEntityMapper().fromMinecraftEntity(passenger));
    }

    return entities;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Stream<Entity> getSelfAndPassengers() {
    return Stream.concat(Stream.of(this), this.getPassengers().stream().flatMap(Entity::getSelfAndPassengers));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOnePlayerRiding() {
    return Minecraft.getInstance().player.isOnePlayerRiding();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Entity getLowestRidingEntity() {
    return this.getEntityMapper().fromMinecraftEntity(Minecraft.getInstance().player.getLowestRidingEntity());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRidingSameEntity(Entity entity) {
    return Minecraft.getInstance().player.isRidingSameEntity(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRidingOrBeingRiddenBy(Entity entity) {
    return Minecraft.getInstance().player.isRidingOrBeingRiddenBy(
            (net.minecraft.entity.Entity) this.getEntityMapper().toMinecraftEntity(entity)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canPassengerSteer() {
    return Minecraft.getInstance().player.canPassengerSteer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPermissionLevel(int level) {
    return Minecraft.getInstance().player.hasPermissionLevel(level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWidth() {
    return Minecraft.getInstance().player.getWidth();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHeight() {
    return Minecraft.getInstance().player.getHeight();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BlockPosition getPosition() {
    return this.getWorld().fromMinecraftBlockPos(Minecraft.getInstance().player.getPosition());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosXWidth(double width) {
    return Minecraft.getInstance().player.getPosXWidth(width);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosXRandom(double factor) {
    return Minecraft.getInstance().player.getPosXRandom(factor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosYHeight(double height) {
    return Minecraft.getInstance().player.getPosYHeight(height);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosYRandom() {
    return Minecraft.getInstance().player.getPosYRandom();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosYEye() {
    return Minecraft.getInstance().player.getPosYEye();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosZWidth(double width) {
    return Minecraft.getInstance().player.getPosZWidth(width);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getPosZRandom(double factor) {
    return Minecraft.getInstance().player.getPosZRandom(factor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRawPosition(double x, double y, double z) {
    Minecraft.getInstance().player.setRawPosition(x, y, z);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getName() {
    return this.getEntityMapper().getComponentMapper().fromMinecraft(Minecraft.getInstance().player.getName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void heal(float health) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getPitch(float partialTicks) {
    return Minecraft.getInstance().player.rotationPitch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getYaw(float partialTicks) {
    return Minecraft.getInstance().player.rotationYaw;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendChatMessage(String message) {
    Minecraft.getInstance().player.sendChatMessage(message);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void swingArm(Hand hand) {
    Minecraft.getInstance().player.swingArm((net.minecraft.util.Hand) this.getEntityMapper().getHandMapper().toMinecraftHand(hand));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void respawnPlayer() {
    Minecraft.getInstance().player.respawnPlayer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closeScreenAndDropStack() {
    Minecraft.getInstance().player.closeScreenAndDropStack();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPlayerAbilities() {
    Minecraft.getInstance().player.sendPlayerAbilities();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isUser() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlayerSPHealth(float health) {
    Minecraft.getInstance().player.setPlayerSPHealth(health);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendHorseInventory() {
    Minecraft.getInstance().player.sendHorseInventory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getServerBrand() {
    return Minecraft.getInstance().player.getServerBrand();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setServerBrand(String serverBrand) {
    Minecraft.getInstance().player.setServerBrand(serverBrand);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPermissionLevel(int level) {
    Minecraft.getInstance().player.setPermissionLevel(level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setExperienceStats(float currentExperience, int maxExperience, int level) {
    Minecraft.getInstance().player.setXPStats(currentExperience, maxExperience, level);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowDeathScreen() {
    return Minecraft.getInstance().player.isShowDeathScreen();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShowDeathScreen(boolean showDeathScreen) {
    Minecraft.getInstance().player.setShowDeathScreen(showDeathScreen);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRidingHorse() {
    return Minecraft.getInstance().player.isRidingHorse();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getHorseJumpPower() {
    return Minecraft.getInstance().player.getHorseJumpPower();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRowingBoat() {
    return Minecraft.getInstance().player.isRowingBoat();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAutoJumpEnabled() {
    return Minecraft.getInstance().player.isAutoJumpEnabled();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getWaterBrightness() {
    return Minecraft.getInstance().player.getWaterBrightness();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TabOverlay getTabOverlay() {
    return this.tabOverlay;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendMessage(ChatComponent component, UUID senderUUID) {
    Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(
            (ITextComponent) this.getEntityMapper().getComponentMapper().toMinecraft(component)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild() {
    return Minecraft.getInstance().player.isChild();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSpectator() {
    return Minecraft.getInstance().player.isSpectator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeRiddenInWater() {
    return Minecraft.getInstance().player.canBeRiddenInWater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPushedByWater() {
    return Minecraft.getInstance().player.isPushedByWater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SoundCategory getSoundCategory() {
    return this.getEntityMapper().getSoundMapper().fromMinecraftSoundCategory(
            Minecraft.getInstance().player.getSoundCategory()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkDespawn() {
    Minecraft.getInstance().player.checkDespawn();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasCustomName() {
    return Minecraft.getInstance().player.hasCustomName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getDisplayName() {
    return this.getEntityMapper().getComponentMapper().fromMinecraft(
            Minecraft.getInstance().player.getDisplayName()
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getCustomName() {
    return this.getEntityMapper().getComponentMapper().fromMinecraft(
            Minecraft.getInstance().player.getCustomName()
    );
  }
}
