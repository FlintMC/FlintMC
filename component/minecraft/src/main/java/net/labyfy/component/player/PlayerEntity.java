package net.labyfy.component.player;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.entity.Entity;
import net.labyfy.component.entity.LivingEntity;
import net.labyfy.component.entity.item.ItemEntity;
import net.labyfy.component.entity.type.EntityType;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.items.ItemStack;
import net.labyfy.component.items.inventory.Inventory;
import net.labyfy.component.nbt.NBTCompound;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.type.CooldownTracking;
import net.labyfy.component.player.type.GameMode;
import net.labyfy.component.player.type.hand.Hand;
import net.labyfy.component.player.type.model.PlayerClothing;
import net.labyfy.component.player.type.sound.Sound;
import net.labyfy.component.player.type.sound.SoundCategory;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.tileentity.SignTileEntity;
import net.labyfy.component.world.World;
import net.labyfy.component.world.math.BlockPosition;
import net.labyfy.component.world.scoreboad.Scoreboard;

import java.util.UUID;

public interface PlayerEntity extends LivingEntity, CooldownTracking {

  /**
   * Whether block action restricted.
   *
   * @param world    The world to be checked.
   * @param position The block position to be checked.
   * @param mode     The game mode for the block action restricted
   * @return {@code true} if the block action restricted, otherwise {@code false}.
   */
  boolean blockActionRestricted(World world, BlockPosition position, GameMode mode);

  /**
   * Whether secondary use is active.
   *
   * @return {@code true} if the secondary use is active, otherwise {@code false}.
   */
  boolean isSecondaryUseActive();

  /**
   * Plays a sound at the position of this player.
   *
   * @param sound    The sound to be played.
   * @param category The category for this sound.
   * @param volume   The volume of this sound.
   * @param pitch    The pitch of this sound.
   */
  void playSound(Sound sound, SoundCategory category, float volume, float pitch);

  /**
   * Retrieves the score of this player.
   *
   * @return The score of this player.
   */
  int getScore();

  /**
   * Sets the score of this player.
   *
   * @param score The new score.
   */
  void setScore(int score);

  /**
   * Adds the score to this player.
   *
   * @param score The score to be added.
   */
  void addScore(int score);

  /**
   * Whether the selected item can be dropped.
   *
   * @param dropEntireStack Whether the entries stack can eb dropped.
   * @return {@code true} if the selected item can be dropped, otherwise {@code false}.
   */
  boolean drop(boolean dropEntireStack);

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param itemStack The dropped item.
   * @param traceItem Whether the item can be traced.
   * @return The dropped item as an entity, or {@code null}.
   */
  ItemEntity dropItem(ItemStack itemStack, boolean traceItem);

  /**
   * Retrieves the dropped item as an entity.
   *
   * @param itemStack  The dropped item.
   * @param dropAround If {@code true}, the item will be thrown in a random direction from the entity regardless
   *                   of which direction the entity is facing.
   * @param traceItem  Whether the item can be traced.
   * @return The dropped ite mas an entity, or {@code null}.
   */
  ItemEntity dropItem(ItemStack itemStack, boolean dropAround, boolean traceItem);

  /**
   * Whether the player can attack another player.
   *
   * @param playerEntity The player to be attacked.
   * @return {@code true} if can the player be attacked, otherwise {@code false}.
   */
  boolean canAttackPlayer(PlayerEntity playerEntity);

  /**
   * Opens a sign editor.
   *
   * @param signTileEntity The sign to be edited.
   */
  void openSignEditor(SignTileEntity signTileEntity);

  /**
   * Opens a minecart command block.
   *
   * @param commandBlockLogic The minecart command block to be opened.
   */
  void openMinecartCommandBlock(Object commandBlockLogic);

  /**
   * Opens a command block.
   *
   * @param commandBlockTileEntity The command block to be opened.
   */
  void openCommandBlock(Object commandBlockTileEntity);

  /**
   * Opens a structure block.
   *
   * @param structureBlockTileEntity The structure block to be opened.
   */
  void openStructureBlock(Object structureBlockTileEntity);

  /**
   * Opens a jigsaw.
   *
   * @param jigsawTileEntity The jigsaw to be opened.
   */
  void openJigsaw(Object jigsawTileEntity);

  /**
   * Opens a horse inventory.
   *
   * @param abstractHorseEntity The horse that has an inventory.
   * @param inventory           Inventory of this horse.
   */
  void openHorseInventory(Object abstractHorseEntity, Inventory inventory);

  /**
   * Opens a merchant inventory.
   *
   * @param merchantOffers  The offers of the merchant
   * @param container       The container identifier for this merchant
   * @param levelProgress   The level progress of this merchant.<br>
   *                        <b>Note:</b><br>
   *                        1 = Novice<br>
   *                        2 = Apprentice<br>
   *                        3 = Journeyman<br>
   *                        4 = Expert<br>
   *                        5 = Master
   * @param experience      The total experience for this villager (Always 0 for the wandering trader)
   * @param regularVillager {@code True} if this is a regular villager,
   *                        otherwise {@code false} for the wandering trader. When {@code false},
   *                        hides the villager level  and some other GUI elements
   * @param refreshable     {@code True} for regular villagers and {@code false} for the wandering trader.
   *                        If {@code true}, the "Villagers restock up to two times per day".
   */
  void openMerchantContainer(
          int container,
          Object merchantOffers,
          int levelProgress,
          int experience,
          boolean regularVillager,
          boolean refreshable
  );

  /**
   * Opens a book.
   *
   * @param stack The item stack which should be a book.
   * @param hand  The hand of this player.
   */
  void openBook(ItemStack stack, Hand hand);

  /**
   * Attacks the target entity with the current item.
   *
   * @param entity The entity to be attacked.
   */
  void attackTargetEntityWithCurrentItem(Entity entity);

  /**
   * Disables the shield of this player.
   *
   * @param disable Whether the shield should be deactivated.
   */
  void disableShield(boolean disable);

  /**
   * Spawns the sweep particles.
   */
  void spawnSweepParticles();

  /**
   * Sends the client status packet to respawn the player.
   */
  void respawnPlayer();

  /**
   * Whether the player is an user.
   *
   * @return {@code true} if the player is an user, otherwise {@code false}.
   */
  boolean isUser();

  /**
   * Retrieves the game player profile of this player.
   *
   * @return The game player profile of this player.
   */
  GameProfile getGameProfile();

  /**
   * Wakes up this player or updates all sleeping players.
   *
   * @param updateTimer           Updates the sleep timer.
   * @param updateSleepingPlayers Updates all sleeping players.
   */
  void stopSleepInBed(boolean updateTimer, boolean updateSleepingPlayers);

  /**
   * Whether the player is fully asleep.
   *
   * @return {@code true} if the player is fully asleep, otherwise {@code false}.
   */
  boolean isPlayerFullyAsleep();

  /**
   * Retrieves the sleep timer of this player.
   *
   * @return The sleep timer of this player.
   */
  int getSleepTimer();

  /**
   * Sends a status message to this player.
   *
   * @param component The message for this status.
   * @param actionbar Whether to send to the action bar.
   */
  void sendStatusMessage(ChatComponent component, boolean actionbar);

  /**
   * Retrieves the bed location of this player.
   *
   * @return The player's bed location.
   */
  BlockPosition getBedLocation();

  /**
   * Adds a custom stat to this player.
   *
   * @param resourceLocation The resource location for the stat.
   */
  void addStat(ResourceLocation resourceLocation);

  /**
   * Adds a custom stat to this player.
   *
   * @param resourceLocation The resource location for the stat.
   * @param state            The stat of the stat.
   */
  void addStat(ResourceLocation resourceLocation, int state);

  /**
   * Adds movement stats to this player.
   *
   * @param x The `x` position to be added.
   * @param y The `y` position to be added.
   * @param z The `z` position to be added.
   */
  void addMovementStat(double x, double y, double z);

  /**
   * Whether can be tried to start the fall flying of this player.
   *
   * @return {@code true} if can be tried to start the fall flying, otherwise {@code false}.
   */
  boolean tryToStartFallFlying();

  /**
   * Starts the fall flying of this player.
   */
  void startFallFlying();

  /**
   * Stops the fall flying of this player.
   */
  void stopFallFlying();

  /**
   * Gives experience points to this player.
   *
   * @param experiencePoints The points to be assigned.
   */
  void giveExperiencePoints(int experiencePoints);

  /**
   * Retrieves the experience seed of this player.
   *
   * @return The experience seed.
   */
  int getExperienceSeed();

  /**
   * Adds the experience level to this player.
   *
   * @param experienceLevel The levels to be added.
   */
  void addExperienceLevel(int experienceLevel);

  /**
   * Retrieves the experience bar cap of this player.
   *
   * @return The experience bar cap of this player.
   */
  int getExperienceBarCap();

  /**
   * Adds the exhaustion of this player.
   *
   * @param exhaustion The exhaustion to be added.
   */
  void addExhaustion(float exhaustion);

  /**
   * Whether the player can eat.
   *
   * @param ignoreHunger Whether hunger should be ignored.
   * @return {@code true} if the player can eat, otherwise {@code false}.
   */
  boolean canEat(boolean ignoreHunger);

  /**
   * Whether the player should heal.
   *
   * @return {@code true} if the player should heal, otherwise {@code false}.
   */
  boolean shouldHeal();

  /**
   * Whether the player is allowed to edit.
   *
   * @return {@code true} if the player is allowed to edit, otherwise {@code false}.
   */
  boolean isAllowEdit();

  /**
   * Sends the abilities of this player to the server.
   */
  void sendPlayerAbilities();

  /**
   * Changes the game mode of this player.
   *
   * @param gameMode The new game mode.
   */
  void setGameMode(GameMode gameMode);

  /**
   * Whether the item stack was added to this main inventory.
   *
   * @param itemStack The item stack to be added.
   * @return {@code true} if was the item stack added.
   */
  boolean addItemStackToInventory(ItemStack itemStack);

  /**
   * Whether the player is in creative mode.
   *
   * @return {@code true} if the player is in the creative mode, otherwise {@code false}.
   */
  boolean isCreative();

  /**
   * Retrieves the scoreboard of this player.
   *
   * @return The player's scoreboard.
   */
  Scoreboard getScoreboard();

  /**
   * Retrieves the unique identifier
   *
   * @param profile The game profile of this player.
   * @return The unique identifier of the given game profile.
   */
  UUID getUniqueId(GameProfile profile);

  /**
   * Retrieves the offline unique identifier for this player.
   *
   * @param username The username of this player.
   * @return THe offline unique identifier.
   */
  UUID getOfflineUniqueId(String username);

  /**
   * Whether the player is wearing the given clothing.
   *
   * @param clothing The clothing that should be worn.
   * @return {@code true} if the player is wearing the clothing, otherwise {@code false}.
   */
  boolean isWearing(PlayerClothing clothing);

  /**
   * Whether the player has reduced debug.
   *
   * @return {@code true} if the player has reduced debug, otherwise {@code false}.
   */
  boolean hasReducedDebug();

  /**
   * Sets the reduced debug for this player.
   *
   * @param reducedDebug The new reduced debug state.
   */
  void setReducedDebug(boolean reducedDebug);

  /**
   * Sets the primary hand of this player.
   *
   * @param primaryHand The new primary hand of this player.
   */
  void setPrimaryHand(Hand.Side primaryHand);

  /**
   * Retrieves the cooldown period of this player.
   *
   * @return The cooldown period of this player.
   */
  float getCooldownPeriod();

  /**
   * Retrieves the cooled attack strength of this player.
   *
   * @param strength the ticks to adjust the cooled strength of the attack.
   * @return The cooled attack strength of this player.
   */
  float getCooledAttackStrength(float strength);

  /**
   * Resets the cooldown of the player.
   */
  void resetCooldown();

  /**
   * Retrieves the luck of the player.
   *
   * @return The luck of the player.
   */
  float getLuck();

  /**
   * Whether the player can use a command block.
   *
   * @return {@code true} if the player can use a command block, otherwise {@code false}.
   */
  boolean canUseCommandBlock();

  /**
   * Retrieves the left shoulder entity as a {@link NBTCompound}.
   *
   * @return The left shoulder entity.
   */
  NBTCompound getLeftShoulderEntity();

  /**
   * Retrieves the right shoulder entity as a {@link NBTCompound}.
   *
   * @return The right shoulder entity.
   */
  NBTCompound getRightShoulderEntity();

  /**
   * A factory class for the {@link PlayerEntity}.
   */
  @AssistedFactory(PlayerEntity.class)
  interface Factory {

    /**
     * Creates a new {@link PlayerEntity} with the given parameters.
     *
     * @param entity     The non-null Minecraft entity.
     * @param entityType The entity type.
     * @return A created {@link PlayerEntity}.
     */
    PlayerEntity create(@Assisted("entity") Object entity, @Assisted("entityType") EntityType entityType);

  }

  /**
   * Service interface for creating {@link PlayerEntity}.
   */
  interface Provider {

    /**
     * Creates a new {@link PlayerEntity} with the given parameter.
     *
     * @param entity The non-null Minecraft entity.
     * @return A created {@link PlayerEntity}.
     */
    PlayerEntity get(Object entity);

  }


}
