package net.flintmc.mcapi.v1_15_2.player;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.item.ItemEntity;
import net.flintmc.mcapi.entity.item.ItemEntityMapper;
import net.flintmc.mcapi.entity.mapper.EntityFoundationMapper;
import net.flintmc.mcapi.entity.render.EntityRenderContext;
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
import net.flintmc.mcapi.v1_15_2.entity.VersionedLivingEntity;
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

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Implement(value = PlayerEntity.class, version = "1.15.2")
public class VersionedPlayerEntity extends VersionedLivingEntity implements PlayerEntity {

  private final net.minecraft.entity.player.PlayerEntity entity;
  private final GameProfileSerializer<com.mojang.authlib.GameProfile>
      gameProfileGameProfileSerializer;
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
      TileEntityMapper tileEntityMapper,
      EntityRenderContext.Factory entityRenderContextFactory) {
    super(entity, entityType, world, entityFoundationMapper,entityRenderContextFactory);
    this.gameProfileGameProfileSerializer = gameProfileSerializer;
    this.modelMapper = modelMapper;
    this.itemEntityMapper = itemEntityMapper;
    this.tileEntityMapper = tileEntityMapper;

    if (!(entity instanceof net.minecraft.entity.player.PlayerEntity)) {
      throw new IllegalArgumentException(
          entity.getClass().getName()
              + " is not an instance of "
              + net.minecraft.entity.player.PlayerEntity.class.getName());
    }

    this.entity = (net.minecraft.entity.player.PlayerEntity) entity;
  }

  /** {@inheritDoc} */
  @Override
  public boolean blockActionRestricted(World world, BlockPosition position, GameMode mode) {
    return this.entity.blockActionRestricted(
        this.entity.world,
        (BlockPos) this.getWorld().toMinecraftBlockPos(position),
        (GameType) this.getEntityFoundationMapper().toMinecraftGameType(mode));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSecondaryUseActive() {
    return this.entity.isSecondaryUseActive();
  }

  /** {@inheritDoc} */
  @Override
  public void playSound(Sound sound, SoundCategory category, float volume, float pitch) {
    this.entity.playSound(
        (SoundEvent) this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
        (net.minecraft.util.SoundCategory)
            this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundCategory(category),
        volume,
        pitch);
  }

  /** {@inheritDoc} */
  @Override
  public int getScore() {
    return this.entity.getScore();
  }

  /** {@inheritDoc} */
  @Override
  public void setScore(int score) {
    this.entity.setScore(score);
  }

  /** {@inheritDoc} */
  @Override
  public void addScore(int score) {
    this.entity.addScore(score);
  }

  /** {@inheritDoc} */
  @Override
  public boolean drop(boolean dropEntireStack) {
    return this.entity.drop(dropEntireStack);
  }

  /** {@inheritDoc} */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean traceItem) {
    return this.itemEntityMapper.fromMinecraftItemEntity(
        this.entity.dropItem(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack),
            traceItem));
  }

  /** {@inheritDoc} */
  @Override
  public ItemEntity dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem) {
    return this.itemEntityMapper.fromMinecraftItemEntity(
        this.entity.dropItem(
            (net.minecraft.item.ItemStack)
                this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack),
            dropAround,
            traceItem));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canAttackPlayer(PlayerEntity playerEntity) {
    return this.entity.canAttackPlayer(
        (net.minecraft.entity.player.PlayerEntity)
            this.getEntityFoundationMapper()
                .getEntityMapper()
                .toMinecraftPlayerEntity(playerEntity));
  }

  /** {@inheritDoc} */
  @Override
  public void openSignEditor(SignTileEntity signTileEntity) {
    this.entity.openSignEditor(
        (net.minecraft.tileentity.SignTileEntity)
            this.tileEntityMapper.toMinecraftSignTileEntity(signTileEntity));
  }

  /** {@inheritDoc} */
  @Override
  public void openMinecartCommandBlock(Object commandBlockLogic) {
    this.entity.openMinecartCommandBlock((CommandBlockLogic) commandBlockLogic);
  }

  /** {@inheritDoc} */
  @Override
  public void openCommandBlock(Object commandBlockTileEntity) {
    this.entity.openCommandBlock((CommandBlockTileEntity) commandBlockTileEntity);
  }

  /** {@inheritDoc} */
  @Override
  public void openStructureBlock(Object structureBlockTileEntity) {
    this.entity.openStructureBlock((StructureBlockTileEntity) structureBlockTileEntity);
  }

  /** {@inheritDoc} */
  @Override
  public void openJigsaw(Object jigsawTileEntity) {
    this.entity.openJigsaw((JigsawTileEntity) jigsawTileEntity);
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
    this.entity.openMerchantContainer(
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
    this.entity.openBook(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack),
        (net.minecraft.util.Hand)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHand(hand));
  }

  /** {@inheritDoc} */
  @Override
  public void attackTargetEntityWithCurrentItem(Entity entity) {
    this.entity.attackTargetEntityWithCurrentItem(
        (net.minecraft.entity.Entity)
            this.getEntityFoundationMapper().getEntityMapper().toMinecraftEntity(entity));
  }

  /** {@inheritDoc} */
  @Override
  public void disableShield(boolean disable) {
    this.entity.disableShield(disable);
  }

  /** {@inheritDoc} */
  @Override
  public void spawnSweepParticles() {
    this.entity.spawnSweepParticles();
  }

  /** {@inheritDoc} */
  @Override
  public void respawnPlayer() {
    this.entity.respawnPlayer();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isUser() {
    return this.entity.isUser();
  }

  /** {@inheritDoc} */
  @Override
  public GameProfile getGameProfile() {
    return this.gameProfileGameProfileSerializer.deserialize(this.entity.getGameProfile());
  }

  /** {@inheritDoc} */
  @Override
  public void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers) {
    this.entity.stopSleepInBed(updateTimer, updateSleepingPlayers);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPlayerFullyAsleep() {
    return this.entity.isPlayerFullyAsleep();
  }

  /** {@inheritDoc} */
  @Override
  public int getSleepTimer() {
    return this.entity.getSleepTimer();
  }

  /** {@inheritDoc} */
  @Override
  public void sendStatusMessage(ChatComponent component, boolean actionbar) {
    this.entity.sendStatusMessage(
        (ITextComponent)
            this.getEntityFoundationMapper().getComponentMapper().toMinecraft(component),
        actionbar);
  }

  /** {@inheritDoc} */
  @Override
  public BlockPosition getBedLocation() {
    return this.getWorld().fromMinecraftBlockPos(this.entity.getBedLocation());
  }

  /** {@inheritDoc} */
  @Override
  public void addStat(ResourceLocation resourceLocation) {
    this.entity.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle());
  }

  /** {@inheritDoc} */
  @Override
  public void addStat(ResourceLocation resourceLocation, int state) {
    this.entity.addStat((net.minecraft.util.ResourceLocation) resourceLocation.getHandle(), state);
  }

  /** {@inheritDoc} */
  @Override
  public void addMovementStat(double x, double y, double z) {
    this.entity.addMovementStat(x, y, z);
  }

  /** {@inheritDoc} */
  @Override
  public boolean tryToStartFallFlying() {
    return this.entity.tryToStartFallFlying();
  }

  /** {@inheritDoc} */
  @Override
  public void startFallFlying() {
    this.entity.startFallFlying();
  }

  /** {@inheritDoc} */
  @Override
  public void stopFallFlying() {
    this.entity.stopFallFlying();
  }

  /** {@inheritDoc} */
  @Override
  public void giveExperiencePoints(int experiencePoints) {
    this.entity.giveExperiencePoints(experiencePoints);
  }

  /** {@inheritDoc} */
  @Override
  public int getExperienceSeed() {
    return this.entity.getXPSeed();
  }

  /** {@inheritDoc} */
  @Override
  public void addExperienceLevel(int experienceLevel) {
    this.entity.addExperienceLevel(experienceLevel);
  }

  /** {@inheritDoc} */
  @Override
  public int getExperienceBarCap() {
    return this.entity.xpBarCap();
  }

  /** {@inheritDoc} */
  @Override
  public void addExhaustion(float exhaustion) {
    this.entity.addExhaustion(exhaustion);
  }

  /** {@inheritDoc} */
  @Override
  public boolean canEat(boolean ignoreHunger) {
    return this.entity.canEat(ignoreHunger);
  }

  /** {@inheritDoc} */
  @Override
  public boolean shouldHeal() {
    return this.entity.shouldHeal();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAllowEdit() {
    return this.entity.isAllowEdit();
  }

  /** {@inheritDoc} */
  @Override
  public void sendPlayerAbilities() {
    this.entity.sendPlayerAbilities();
  }

  /** {@inheritDoc} */
  @Override
  public void setGameMode(GameMode gameMode) {
    this.entity.setGameType(
        (GameType) this.getEntityFoundationMapper().toMinecraftGameType(gameMode));
  }

  /** {@inheritDoc} */
  @Override
  public boolean addItemStackToInventory(ItemStack itemStack) {
    return this.entity.addItemStackToInventory(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCreative() {
    return this.entity.isCreative();
  }

  /** {@inheritDoc} */
  @Override
  public Scoreboard getScoreboard() {
    return this.getWorld().getScoreboard();
  }

  /** {@inheritDoc} */
  @Override
  public UUID getUniqueId(GameProfile profile) {
    UUID uniqueId = profile.getUniqueId();
    if (uniqueId == null) {
      uniqueId = this.getOfflineUniqueId(profile.getName());
    }

    return uniqueId;
  }

  /** {@inheritDoc} */
  @Override
  public UUID getOfflineUniqueId(String username) {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
  }

  /** {@inheritDoc} */
  @Override
  public boolean isWearing(PlayerClothing clothing) {
    return this.entity.isWearing(
        (PlayerModelPart) this.modelMapper.toMinecraftPlayerModelPart(clothing));
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasReducedDebug() {
    return this.entity.hasReducedDebug();
  }

  /** {@inheritDoc} */
  @Override
  public void setReducedDebug(boolean reducedDebug) {
    this.entity.setReducedDebug(reducedDebug);
  }

  /** {@inheritDoc} */
  @Override
  public void setPrimaryHand(Hand.Side primaryHand) {
    this.entity.setPrimaryHand(
        (HandSide)
            this.getEntityFoundationMapper().getHandMapper().toMinecraftHandSide(primaryHand));
  }

  /** {@inheritDoc} */
  @Override
  public float getCooldownPeriod() {
    return this.entity.getCooldownPeriod();
  }

  /** {@inheritDoc} */
  @Override
  public float getCooledAttackStrength(float strength) {
    return this.entity.getCooledAttackStrength(strength);
  }

  /** {@inheritDoc} */
  @Override
  public void resetCooldown() {
    this.entity.resetCooldown();
  }

  /** {@inheritDoc} */
  @Override
  public float getLuck() {
    return this.entity.getLuck();
  }

  /** {@inheritDoc} */
  @Override
  public boolean canUseCommandBlock() {
    return this.entity.canUseCommandBlock();
  }

  /** {@inheritDoc} */
  @Override
  public NBTCompound getLeftShoulderEntity() {
    return (NBTCompound)
        this.getEntityFoundationMapper()
            .getNbtMapper()
            .fromMinecraftNBT(this.entity.getLeftShoulderEntity());
  }

  /** {@inheritDoc} */
  @Override
  public NBTCompound getRightShoulderEntity() {
    return (NBTCompound)
        this.getEntityFoundationMapper()
            .getNbtMapper()
            .fromMinecraftNBT(this.entity.getRightShoulderEntity());
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasCooldown(Object item) {
    return this.entity.getCooldownTracker().hasCooldown((Item) item);
  }

  /** {@inheritDoc} */
  @Override
  public float getCooldown(Object item, float partialTicks) {
    return this.entity.getCooldownTracker().getCooldown((Item) item, partialTicks);
  }

  /** {@inheritDoc} */
  @Override
  public void setCooldown(Object item, int ticks) {
    this.entity.getCooldownTracker().setCooldown((Item) item, ticks);
  }

  /** {@inheritDoc} */
  @Override
  public void removeCooldown(Object item) {
    this.entity.getCooldownTracker().removeCooldown((Item) item);
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack findAmmo(ItemStack shootable) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.entity.findAmmo(
                (net.minecraft.item.ItemStack)
                    this.getEntityFoundationMapper().getItemMapper().toMinecraft(shootable)));
  }

  /** {@inheritDoc} */
  @Override
  public boolean canPickUpItem(ItemStack stack) {
    return this.entity.canPickUpItem(
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(stack));
  }

  /** {@inheritDoc} */
  @Override
  public boolean replaceItemInInventory(int slot, ItemStack itemStack) {
    return this.entity.replaceItemInInventory(
        slot,
        (net.minecraft.item.ItemStack)
            this.getEntityFoundationMapper().getItemMapper().toMinecraft(itemStack));
  }

  /** {@inheritDoc} */
  @Override
  public float getAbsorptionAmount() {
    return this.entity.getAbsorptionAmount();
  }

  /** {@inheritDoc} */
  @Override
  public void setAbsorptionAmount(float absorptionAmount) {
    this.entity.setAbsorptionAmount(absorptionAmount);
  }

  /** {@inheritDoc} */
  @Override
  public String getScoreboardName() {
    return this.entity.getScoreboardName();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isPushedByWater() {
    return this.entity.isPushedByWater();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSwimming() {
    return this.entity.isSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public ItemStack getItemStackFromSlot(EquipmentSlotType slotType) {
    return this.getEntityFoundationMapper()
        .getItemMapper()
        .fromMinecraft(
            this.entity.getItemStackFromSlot(
                (net.minecraft.inventory.EquipmentSlotType)
                    this.getEntityFoundationMapper().toMinecraftEquipmentSlotType(slotType)));
  }

  /** {@inheritDoc} */
  @Override
  public float getAIMoveSpeed() {
    return this.entity.getAIMoveSpeed();
  }

  /** {@inheritDoc} */
  @Override
  public void updateSwim() {
    this.entity.updateSwimming();
  }

  /** {@inheritDoc} */
  @Override
  public void wakeUp() {
    this.entity.wakeUp();
  }

  /** {@inheritDoc} */
  @Override
  public void startSleeping(BlockPosition position) {
    this.entity.startSleeping((BlockPos) this.getWorld().toMinecraftBlockPos(position));
  }

  /** {@inheritDoc} */
  @Override
  public void remove() {
    this.entity.remove();
  }

  /** {@inheritDoc} */
  @Override
  public void readAdditional(NBTCompound compound) {
    this.entity.readAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public void writeAdditional(NBTCompound compound) {
    this.entity.writeAdditional(
        (CompoundNBT) this.getEntityFoundationMapper().getNbtMapper().toMinecraftNBT(compound));
  }

  /** {@inheritDoc} */
  @Override
  public SoundCategory getSoundCategory() {
    return SoundCategory.PLAYER;
  }

  /** {@inheritDoc} */
  @Override
  public void playSound(Sound sound, float volume, float pitch) {
    this.entity.playSound(
        (SoundEvent) this.getEntityFoundationMapper().getSoundMapper().toMinecraftSoundEvent(sound),
        volume,
        pitch);
  }
}
