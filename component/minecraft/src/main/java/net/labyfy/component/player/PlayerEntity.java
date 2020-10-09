package net.labyfy.component.player;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.util.CooldownTracking;
import net.labyfy.component.player.util.GameMode;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.world.World;
import net.labyfy.component.world.scoreboad.Scoreboard;
import net.labyfy.component.world.util.BlockPosition;

import java.util.UUID;

public interface PlayerEntity extends LivingEntity, CooldownTracking {

  boolean blockActionRestricted(World world, BlockPosition position, GameMode mode);

  boolean isSecondaryUseActive();

  void playSound(Sound sound, SoundCategory category, float volume, float pitch);

  int getScore();

  void setScore(int score);

  void addScore(int score);

  boolean drop(boolean dropEntireStack);

  Object dropItem(ItemStack itemStack, boolean traceItem);

  Object dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem);

  boolean canAttackPlayer(PlayerEntity playerEntity);

  void openSignEditor(Object signTileEntity);

  void openMinecartCommandBlock(Object commandBlockLogic);

  void openCommandBlock(Object commandBlockTileEntity);

  void openStructureBlock(Object structureBlockTileEntity);

  void openJigsaw(Object jigsawTileEntity);

  void openHorseInventory(Object abstractHorseEntity, Inventory inventory);

  void openMerchantContainer(
          int containerId,
          Object merchantOffers,
          int level,
          int experience,
          boolean regularVillager,
          boolean refreshable
  );

  void openBook(ItemStack stack, Hand hand);

  void attackTargetEntityWithCurrentItem(Entity entity);

  void disableShield(boolean disable);

  void spawnSweepParticles();

  void respawnPlayer();

  boolean isUser();

  GameProfile getGameProfile();

  void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers);

  boolean isPlayerFullyAsleep();

  int getSleepTimer();

  void sendStatusMessage(ChatComponent component, boolean actionbar);

  BlockPosition getBedLocation();

  boolean isSpawnForced();

  void setRespawnPosition(BlockPosition position, boolean force, boolean unknown);

  void addStat(ResourceLocation resourceLocation);

  void addStat(ResourceLocation resourceLocation, int state);

  void addMovementStat(double x, double y, double z);

  boolean tryToStartFallFlying();

  void startFallFlying();

  void stopFallFlying();

  void giveExperiencePoints(int experiencePoints);

  int getXPSeed();

  void addExperienceLevel(int experienceLevel);

  int getXpBarCap();

  void addExhaustion(float exhaustion);

  boolean canEat(boolean ignoreHunger);

  boolean shouldHeal();

  boolean isAllowEdit();

  void sendPlayerAbilities();

  void setGameMode(GameMode gameMode);

  boolean addItemStackToInventory(ItemStack itemStack);

  boolean isCreative();

  Scoreboard getScoreboard();

  UUID getUniqueId(GameProfile profile);

  UUID getOfflineUniqueId(String username);

  boolean isWearing(PlayerClothing clothing);

  boolean hasReducedDebug();

  void setReducedDebug(boolean reducedDebug);

  void setPrimaryHand(Hand.Side primaryHand);

  float getCooldownPeriod();

  float getCooledAttackStrength(float strength);

  void resetCooldown();

  float getLuck();

  boolean canUseCommandBlock();

}
