package net.labyfy.component.player;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.util.*;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.player.world.ClientWorld;

import java.util.UUID;

/**
 * Represents a player
 */
public interface Player<T> extends PlayerSkinProfile, CooldownTracking {

    /**
     * Sets the player.
     *
     * @param player The new player.
     */
    void setPlayer(T player);

    /**
     * Retrieves the player.
     *
     * @return The player.
     */
    T getPlayer();

    /**
     * Retrieves the world of this player.
     *
     * @return The world of this player.
     */
    ClientWorld getWorld();

    /**
     * Retrieves the game profile of this player.
     *
     * @return The game profile of this player.
     */
    GameProfile getGameProfile();

    /**
     * Retrieves the name of this player.
     *
     * @return The name of this player
     */
    ChatComponent getName();

    /**
     * Retrieves the display name of this player
     *
     * @return The display name of this player
     */
    ChatComponent getDisplayName();

    /**
     * Retrieves the display name and the unique identifier of this player.
     *
     * @return The display name and the unique identiifer of this player.
     */
    ChatComponent getDisplayNameAndUniqueId();

    /**
     * Retrieves the unique identifier of this player.
     *
     * @return The unique identifier of this player
     */
    UUID getUniqueId();

    /**
     * Retrieves the health of this player.
     *
     * @return The health of this player
     */
    float getHealth();

    /**
     * Retrieves the list name of this player.
     *
     * @return The list name of this player.
     */
    String getPlayerListName();

    /**
     * Retrieves the world time of this player.
     *
     * @return The world time of this player.
     */
    long getPlayerTime();

    /**
     * Retrieves the x position of this player.
     *
     * @return The x position of this player
     */
    double getX();

    /**
     * Retrieves the y position of this player.
     *
     * @return The y position of this player
     */
    double getY();

    /**
     * Retrieves the z position of this player.
     *
     * @return The z position of this playe
     */
    double getZ();

    /**
     * Retrieves the pitch of this player.
     *
     * @return The pitch of this player.
     */
    float getPitch();

    /**
     * Retrieves the pitch of this player.
     *
     * @param partialTicks The period of time, in fractions of a tick,
     *                     that has passed since the last full tick
     * @return The pitch of this player.
     */
    float getPitch(float partialTicks);

    /**
     * Retrieves the yaw of this player.
     *
     * @return The yaw of this player.
     */
    float getYaw();

    /**
     * Retrieves the yaw of this player.
     *
     * @param partialTicks The period of time, in fractions of a tick,
     *                     that has passed since the last full tick
     * @return The yaw of this player.
     */
    float getYaw(float partialTicks);

    /**
     * Retrieves the rotation yaw head of this player.
     *
     * @return The rotation yaw head of this player.
     */
    float getRotationYawHead();

    /**
     * Retrieves the eye height of this player.
     *
     * @return The eye height of this player.
     */
    float getEyeHeight();

    /**
     * Retrieves the eye height of this player.
     *
     * @param entityPose The current pose for the eye height
     * @return The eye height of this player
     */
    float getEyeHeight(EntityPose entityPose);

    /**
     * Retrieves the y position of the eyes of this player.
     *
     * @return The y position of the eyes of this player
     */
    double getPosYEye();

    /**
     * Retrieves the pose of this player.
     *
     * @return The pose of this player.
     */
    EntityPose getPose();

    /**
     * Whether the player is in spectator mode.
     *
     * @return {@code true} if the player is in the spectator mode, otherwise {@code false}
     */
    boolean isSpectator();

    /**
     * Whether the player is in creative mode.
     *
     * @return {@code true} if the player is in the creative mode, otherwise {@code false}
     */
    boolean isCreative();

    /**
     * Retrieves the active hand of this player.
     *
     * @return The active hand of this player
     */
    Hand getActiveHand();

    /**
     * Whether a hand is active.
     *
     * @return {@code true} if a hand is active, otherwise {@code false}
     */
    boolean isHandActive();

    /**
     * Retrieves the active item stack of this player.
     *
     * @return The active item stack of this player
     */
    // TODO: 01.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    Object getActiveItemStack();

    /**
     * Retrieves use count of the item.
     *
     * @return The use count of the item
     */
    int getItemInUseCount();

    /**
     * Retrieves the maximal use count of the item.
     *
     * @return The maximal use count of this item.
     */
    int getItemInUseMaxCount();

    /**
     * Retrieves the food level of this player.
     *
     * @return The food level of this player.
     */
    int getFoodLevel();

    /**
     * Retrieves the saturation of this player.
     *
     * @return The saturation of this player.
     */
    float getSaturationLevel();

    /**
     * Whether the player needs food
     *
     * @return {@code true} if the player needs food, otherwise {@code false}
     */
    boolean needFood();

    /**
     * Whether the player is in a three dimensional room to render
     *
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @return {@code true} if the player is in the three dimensional room, otherwise {@code false}
     */
    boolean isRangeToRender3d(double x, double y, double z);

    /**
     * Whether the player is in the range to render.
     *
     * @param distance The distance
     * @return {@code true} if the player is in range, otherwise {@code false}
     */
    boolean isInRangeToRender(double distance);

    /**
     * Whether the player is alive
     *
     * @return {@code true} if the player is alive, otherwise {@code false}
     */
    boolean isAlive();

    /**
     * Whether the player is sprinting
     *
     * @return {@code true} if the player is sprinting, otherwise {@code false}
     */
    boolean isSprinting();

    /**
     * Whether the player is on a ladder
     *
     * @return {@code true} if the player is on ladder, otherwise {@code false}
     */
    boolean isOnLadder();

    /**
     * Whether the player is sleeping
     *
     * @return {@code true} if the player is sleeping, otherwise {@code false}
     */
    boolean isSleeping();

    /**
     * Whether the player is swimming
     *
     * @return {@code true} if the player is swimming, otherwise {@code false}
     */
    boolean isSwimming();

    /**
     * Whether the player is wet
     *
     * @return {@code true} if the player is wet, otherwise {@code false}
     */
    boolean isWet();

    /**
     * Whether the player is crouched
     *
     * @return {@code true} if the player is crouched, otherwise {@code false}
     */
    boolean isCrouching();

    /**
     * Whether the player glows
     *
     * @return {@code true} if the player glows, otherwise {@code false}
     */
    boolean isGlowing();

    /**
     * Whether the player is sneaked
     *
     * @return {@code true} if the player is sneaked, otherwise {@code false}
     */
    boolean isSneaking();

    /**
     * Whether the player is in water
     *
     * @return {@code true} if the player is in water, otherwise {@code false}
     */
    boolean isInWater();

    /**
     * Whether the player is in lava
     *
     * @return {@code true} if the player is in lava, otherwise {@code false}
     */
    boolean isInLava();

    /**
     * Whether the player is in fluid
     *
     * @return {@code true} if the player is in fluid, otherwise {@code false}
     */
    default boolean isInFluid() {
        return this.isInWater() || this.isInLava();
    }

    /**
     * Whether the player is wearing the given clothing.
     *
     * @param playerClothing The clothing that should be worn.
     * @return {@code true} if the player is wearing the clothing, otherwise {@code false}
     */
    boolean isWearing(PlayerClothing playerClothing);

    /**
     * Whether the player is invisible
     *
     * @return {@code true} if the player is invisible, otherwise {@code false}
     */
    boolean isInvisible();

    /**
     * Sets the player invisible
     *
     * @param invisible The new state for invisible
     */
    void setInvisible(boolean invisible);

    /**
     * Whether the player is burning
     *
     * @return {@code true} if the player is burning, otherwise {@code false}
     */
    boolean isBurning();

    /**
     * Whether the player is descending
     *
     * @return {@code true} if the player is descending, otherwise {@code false}
     */
    default boolean isDescending() {
        return this.isSneaking();
    }

    /**
     * Whether the player is flying with an elytra.
     *
     * @return {@code true} if the player is flying with an elytra, otherwise {@code false}
     */
    boolean isElytraFlying();

    /**
     * Sets the absorption amount of this player.
     *
     * @param amount The new absorption amount.
     */
    void setAbsorptionAmount(float amount);

    /**
     * Retrieves the absorption amount of this player.
     *
     * @return The absorption amount of this player.
     */
    float getAbsorptionAmount();

    /**
     * Retrieves the total armor value of this player.
     *
     * @return The total armor value of this player.
     */
    int getTotalArmorValue();

    /**
     * Prints a message in this player chat
     *
     * @param component The message to print
     */
    void sendMessage(ChatComponent component);

    /**
     * Retrieves the network player info of this player.
     *
     * @return The network player info of this  player
     */
    NetworkPlayerInfo getNetworkPlayerInfo();

    /**
     * Plays a sound to the player.
     *
     * @param sound  The sound to play.
     * @param volume The volume of this sound.
     * @param pitch  The pitch of this sound.
     */
    void playSound(Sound sound, float volume, float pitch);

    /**
     * Plays a sound to the player.
     *
     * @param sound    The sound to play.
     * @param category The category for this sound.
     * @param volume   The volume of this sound.
     * @param pitch    The pitch of this sound.
     */
    void playSound(Sound sound, SoundCategory category, float volume, float pitch);

    /**
     * Retrieves the sound category of this player.
     *
     * @return The sound category of this player.
     */
    default SoundCategory getSoundCategory() {
        return SoundCategory.PLAYER;
    }

    /**
     * Retrieves the fall distance of this player.
     *
     * @return The fall distance of this player.
     */
    float getFallDistance();

    /**
     * Retrieves the maximal fall distance of this player.
     *
     * @return The maximal fall distance of this player.
     */
    int getMaxFallDistance();

    /**
     * Swings the arm through the given hand.
     *
     * @param hand The hand to swing.
     */
    void swingArm(Hand hand);

    /**
     * Retrieves the maximal in portal time of this player.
     *
     * @return The maximal in portal time of this player.
     */
    int getMaxInPortalTime();

    /**
     * Retrieves the fly speed of this player.
     *
     * @return The fly speed of this player.
     */
    float getFlySpeed();

    /**
     * Sets the fly speed of this player.
     *
     * @param speed The new fly speed.
     */
    void setFlySpeed(float speed);

    /**
     * Retrieves the walk speed of this player.
     *
     * @return The walk speed of this player.
     */
    float getWalkSpeed();

    /**
     * Sets the walk speed of this player.
     *
     * @param speed The new walk speed.
     */
    void setWalkSpeed(float speed);

    /**
     * Whether the player is on the ground.
     *
     * @return {@code true} if the player is on the ground, otherwise {@code false}
     */
    boolean isOnGround();

    /**
     * Retrieves the luck of this player.
     *
     * @return The luck of this player.
     */
    float getLuck();

    /**
     * Retrieves the primary hand this player.
     *
     * @return The primary hand this player.
     */
    Hand.Side getPrimaryHand();

    /**
     * Sets the primary hand of this player.
     *
     * @param side The new primary hand of this player.
     */
    void setPrimaryHand(Hand.Side side);

    /**
     * Whether the player can use command block.
     *
     * @return {@code true} if the player can use command blocks, otherwise {@code false}
     */
    boolean canUseCommandBlock();

    /**
     * Retrieves the cooldown period of this player.
     *
     * @return The cooldown period of this player.
     */
    float getCooldownPeriod();

    /**
     * Retrieves the cooled attack strength of this player.
     *
     * @param adjustTicks The ticks to adjust the cooled strength of the attack.
     * @return The cooled attack strength of this player.
     */
    float getCooledAttackStrength(float adjustTicks);

    /**
     * Resets the cooldown of this player.
     */
    void resetCooldown();

    /**
     * Whether the player has reduced debug.
     *
     * @return {@code true} if the player has reduced debug, otherwise {@code false}
     */
    boolean hasReducedDebug();

    /**
     * Sets the reduced debug for this player.
     *
     * @param reducedDebug The new reduced debug.
     */
    void setReducedDebug(boolean reducedDebug);

    /**
     * Whether the item stack at the slot can be replaced.
     *
     * @param slot      The slot that should be replaced.
     * @param itemStack The item stack to be replaced.
     * @return {@code true} if the item stack can be replaced, otherwise {@code false}
     */
    boolean replaceItemInInventory(int slot, Object itemStack);

    /**
     * Whether the player is pushed by water.
     *
     * @return {@code true} if the player pushed by water, otherwise {@code false}
     */
    boolean isPushedByWater();

    /**
     * Retrieves an iterable collection of the equipment held by that player.
     *
     * @return An iterable collection of the equipment held by that player.
     */
    // TODO: 05.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    Iterable<Object> getHeldEquipment();

    /**
     * Retrieves an iterable inventory of that player's armor.
     *
     * @return An iterable inventory of that player's armor.
     */
    // TODO: 05.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    Iterable<Object> getArmorInventoryList();

    /**
     * Whether the item stack was added to this main inventory.
     *
     * @param itemStack The item stack to be added
     * @return {@code true} if was the item stack added, otherwise {@code false}
     */
    boolean addItemStackToInventory(Object itemStack);

    /**
     * Whether the player is allowed to edit.
     *
     * @return {@code true} if the player is allowed to edit, otherwise {@code false}
     */
    boolean isAllowEdit();

    /**
     * Whether the player should heal.
     *
     * @return {@code true} if the player should heal, otherwise {@code false}
     */
    boolean shouldHeal();

    /**
     * Whether the player can eat.
     *
     * @param ignoreHunger Whether hunger should be ignored.
     * @return {@code true} if the player can eat, otherwise {@code false}
     */
    boolean canEat(boolean ignoreHunger);

    /**
     * Adds the exhaustion of this player.
     *
     * @param exhaustion The exhaustion to be added.
     */
    void addExhaustion(float exhaustion);

    /**
     * Adds the experience level to this player.
     *
     * @param levels The levels to be added.
     */
    void addExperienceLevel(int levels);

    /**
     * Retrieves the experience bar cap of this player.
     *
     * @return The experience bar cap of this player.
     */
    int experienceBarCap();

    /**
     * Retrieves the experience speed of this player.
     *
     * @return The experience speed of this player.
     */
    int getExperienceSpeed();

    /**
     * Gives this player experience points.
     *
     * @param points The points to be assigned
     */
    void giveExperiencePoints(int points);

    /**
     * Whether can be tried to start the fall flying of this player.
     *
     * @return {@code true} if can be tried to start the fall flying, otherwise {@code false}
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
     * Lets the player jump.
     */
    void jump();

    /**
     * Updates the swimming of this player.
     */
    void updateSwimming();

    /**
     * Retrieves the AI move speed of this player.
     *
     * @return The AI move speed of this player.
     */
    float getAIMoveSpeed();

    /**
     * Adds movement stats to this player.
     *
     * @param x The x position to be added
     * @param y The y position to be added
     * @param z The z position to be added
     */
    void addMovementStat(double x, double y, double z);

    /**
     * Whether the spawn is forced.
     *
     * @return {@code true} if the spawn is forced, otherwise {@code false}
     */
    boolean isSpawnForced();

    /**
     * Whether the player is fully asleep.
     *
     * @return {@code true} if the player is fully  asleep, otherwise {@code false}
     */
    boolean isPlayerFullyAsleep();

    /**
     * Retrieves the sleep timer of this player.
     *
     * @return The sleep timer of this player.
     */
    int getSleepTimer();

    /**
     * Wakes up this player.
     */
    void wakeUp();

    /**
     * Wakes up this player or updates all sleeping players.
     *
     * @param updateTimer           Updates the sleep timer
     * @param updateSleepingPlayers Updates all sleeping players.
     */
    void wakeUp(boolean updateTimer, boolean updateSleepingPlayers);

    /**
     * Disables the shield of this player.
     *
     * @param sprinting Whether the player is sprinting.
     */
    void disableShield(boolean sprinting);

    /**
     * Stops the ride of this player.
     */
    void stopRiding();

    /**
     * Whether the player can attack another player.
     *
     * @param player The player to be attacked
     * @return {@code true} if can the player be attacked, otherwise {@code false}
     */
    boolean canAttackPlayer(Player<T> player);

    /**
     * Retrieves the score of this player.
     *
     * @return The score of this player.
     */
    int getScore();

    /**
     * Sets the score of this player.
     *
     * @param score The new score
     */
    void setScore(int score);

    /**
     * Adds the score to this player.
     *
     * @param score The score to be added
     */
    void addScore(int score);

    /**
     * Whether the selected item can be dropped.
     *
     * @param dropEntireStack Whether the entire stack can be dropped.
     * @return {@code true} if the selected item can be dropped, otherwise {@code false}
     */
    boolean drop(boolean dropEntireStack);

    /**
     * Retrieves the dropped item as an entity.
     *
     * @param droppedItem The dropped item
     * @param traceItem   Whether the item can be traced.
     * @return The dropped item as an entity, or {@code null}
     */
    // TODO: 05.09.2020 (Method Type) Replaces the Object to ItemEntity when the (Entity API?) is ready
    // TODO: 05.09.2020 (Parameter 1) Replaces the Object to ItemStack when the (Item API?) is ready
    Object dropItem(Object droppedItem, boolean traceItem);

    /**
     * Retrieves the dropped item as an entity.
     *
     * @param droppedItem The dropped item
     * @param dropAround  If {@code true}, the item will be thrown in a random direction
     *                    from the entity regardless of which direction the entity is facing
     * @param traceItem   Whether the item can be traced.
     * @return The dropped item as an entity, or {@code null}
     */
    // TODO: 05.09.2020 (Method Type) Replaces the Object to ItemEntity when the (Entity API?) is ready
    // TODO: 05.09.2020 (Parameter 1) Replaces the Object to ItemStack when the (Item API?) is ready
    Object dropItem(Object droppedItem, boolean dropAround, boolean traceItem);


    /**
     * Retrieves the digging speed of the given block state for this player.
     *
     * @param blockState The block state that is to receive the dig speed.
     * @return The digging speed of the block state for this player.
     */
    // TODO: 05.09.2020 Replaces the Object to BlockState when the Block API is ready
    float getDigSpeed(Object blockState);

    /**
     * Whether the player can harvest the block.
     *
     * @param blockState The block to be harvested
     * @return {@code true} if the player can harvest the block, otherwise {@code false}.
     */
    // TODO: 05.09.2020 Replaces the Object to BlockState when the Block API is ready
    boolean canHarvestBlock(Object blockState);

    /**
     * Reads the additional of the given compound nbt.
     *
     * @param compoundNBT The compound nbt to be read.
     */
    // TODO: 05.09.2020 Replaces the Object to CompoundNBT when the NBT API is ready
    void readAdditional(Object compoundNBT);

    /**
     * Writes into the additional of this player.
     *
     * @param compoundNBT The additional to be written.
     */
    // TODO: 05.09.2020 Replaces the Object to CompoundNBT when the NBT API is ready
    void writeAdditional(Object compoundNBT);

    /**
     * Sends a status message to this player.
     *
     * @param component The message for this status.
     * @param actionBar Whether to send to the action bar.
     */
    void sendStatusMessage(ChatComponent component, boolean actionBar);

    /**
     * Finds shootable items in the inventory of this player.
     *
     * @param shootable The item to be fired.
     * @return an item to be fired or an empty item.
     */
    // TODO: 05.09.2020 (Method Type) Replaces the Object to ItemStack when the Item API is ready
    // TODO: 05.09.2020 (Parameter 1) Replaces the Object to ItemStack when the Item API is ready
    Object findAmmo(Object shootable);

    /**
     * Whether the player can pick up the item.
     *
     * @param itemStack The item to be pick up
     * @return {@code true} if the player can pick up the item, otherwise {@code false}
     */
    // TODO: 05.09.2020 Replaces the Object to ItemStack when the Item API is ready
    boolean canPickUpItem(Object itemStack);

    /**
     * Adds should entity to this player.
     *
     * @param compoundNbt The entity as a compound nbt
     * @return {@code true} if an entity was added to the shoulder, otherwise {@code false}
     */
    // TODO: 05.09.2020 Replaces the Object to CompoundNBT when the NBT API is ready
    boolean addShoulderEntity(Object compoundNbt);

    /**
     * Retrieves the entity which is on the left shoulder.
     *
     * @return The entity as a compound nbt.
     */
    // TODO: 05.09.2020 Replaces the Object to CompoundNBT when the NBT API is ready
    Object getLeftShoulderEntity();

    /**
     * Retrieves the entity which is on the right shoulder.
     *
     * @return The entity as a compound nbt.
     */
    // TODO: 05.09.2020 Replaces the Object to CompoundNBT when the NBT API is ready
    Object getRightShoulderEntity();

    /**
     * Retrieves the world scoreboard of this player.
     *
     * @return The world scoreboard of this player.
     */
    // TODO: 05.09.2020 Replaces the Object to Scoreboard when the Scoreboard API is ready
    default Object getWorldScoreboard() {
        return this.getWorld().getScoreboard();
    }

    /**
     * Retrieves the fire timer of this player.
     *
     * @return The fire timer of this player.
     */
    int getFireTimer();

    /**
     * Retrieves the step height of this player.
     *
     * @return The step height of this player.
     */
    float getStepHeight();

    /**
     * Retrieves the x rotate elytra of this player.
     *
     * @return The x rotate elytra of this player.
     */
    float getRotateElytraX();

    /**
     * Retrieves the y rotate elytra of this player.
     *
     * @return The y rotate elytra of this player.
     */
    float getRotateElytraY();

    /**
     * Retrieves the z rotate elytra of this player.
     *
     * @return The z rotate elytra of this player.
     */
    float getRotateElytraZ();

    /**
     * Whether the player is collided.
     *
     * @return {@code true} if the player is collided, otherwise {@code false}
     */
    boolean isCollided();

    /**
     * Whether the player is collided horizontally.
     *
     * @return {@code true} if the player is collided horizontally, otherwise {@code false}
     */
    boolean isCollidedHorizontally();

    /**
     * Whether the player is collided vertically.
     *
     * @return {@code true} if the player is collided vertically, otherwise {@code false}
     */
    boolean isCollidedVertically();

    /**
     * Opens a sign editor.
     *
     * @param signTileEntity The sign to be edited.
     */
    // TODO: 06.09.2020 Replaces the Object to SignTileEntity when the Entity API is ready
    void openSignEditor(Object signTileEntity);

    /**
     * Opens a minecart command block.
     *
     * @param commandBlock The minecart command block to be opened.
     */
    // TODO: 06.09.2020 Replaces the Object to CommandBlockLogic when the Entity API / Command API is ready
    void openMinecartCommandBlock(Object commandBlock);

    /**
     * Opens a command block.
     *
     * @param commandBlock The command block to be opened.
     */
    // TODO: 06.09.2020 Replaces the Object to CommandBlockTileEntity when the Entity API / Command API is ready
    void openCommandBlock(Object commandBlock);

    /**
     * Opens a structure block.
     *
     * @param structureBlock The structure block to be opened.
     */
    // TODO: 06.09.2020 Replaces the Object to StructureBlockTileEntity when the Entity API is ready
    void openStructureBlock(Object structureBlock);

    /**
     * Opens a jigsaw.
     *
     * @param jigsaw The jigsaw to be opened.
     */
    // TODO: 06.09.2020 Replaces the Object to JigsawTileEntity when the Entity API is ready
    void openJigsaw(Object jigsaw);

    /**
     * Opens a horse inventory
     *
     * @param horse     The horse that has an inventory
     * @param inventory Inventory of the horse
     */
    // TODO: 06.09.2020 (Parameter 1) Replaces the Object to AbstractHorseEntity when the Entity API is ready
    // TODO: 06.09.2020 (Parameter 2) Replaces the Object to Inventory when the Inventory API is ready
    void openHorseInventory(Object horse, Object inventory);

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
    // TODO: 06.09.2020  (Parameter 1) Replaces the Object to MerchantOffers when the (Item API / Entity API)? is ready
    void openMerchantInventory(
            Object merchantOffers,
            int container,
            int levelProgress,
            int experience,
            boolean regularVillager,
            boolean refreshable
    );

    /**
     * Opens a book.
     *
     * @param itemStack The item stack which should be a book.
     * @param hand      The hand of this player.
     */
    // TODO: 06.09.2020 (Parameter 1) Replaces Object to ItemStack when the Item API is ready.
    void openBook(Object itemStack, Hand hand);

    /**
     * Prepare this player for spawning.
     */
    void preparePlayerToSpawn();

    /**
     * Whether block actions are restricted for this player.
     *
     * @param clientWorld This world of this player
     * @param blockPos    This position of this block
     * @param gameMode    This game mode of this player
     * @return {@code true} if this player has restricted block actions, otherwise {@code false}
     */
    // TODO: 06.09.2020 (Parameter 2) Replaces Object to BlockPos when the Block API is ready.
    boolean blockActionRestricted(ClientWorld clientWorld, Object blockPos, GameMode gameMode);

    /**
     * Spawns sweep particles.
     */
    void spawnSweepParticles();

    /**
     * Respawns this player.
     */
    void respawnPlayer();

    /**
     * Sends the abilities of this player to the server.
     */
    void sendPlayerAbilities();

    /**
     * Updates the game mode of this player.
     * <br>
     * <b>Note:</b> This is only on the server side.
     *
     * @param gameMode The new game of this player
     */
    void updateGameMode(GameMode gameMode);


    /**
     * Enchants the given item stack with the cost.
     *
     * @param itemStack The item stack to enchant
     * @param cost      The cost of the enchant
     */
    void enchantItem(Object itemStack, int cost);

    /**
     * Retrieves the opened container of this player.
     *
     * @return The opened container of this player.
     */
    // TODO: 09.09.2020 Replaces the Object to Container when the Inventory/Item API is ready
    Object getOpenedContainer();

    /**
     * Retrieves the container of this player.
     *
     * @return The container of this player.
     */
    // TODO: 09.09.2020 Replaces the Object to Container when the Inventory/Item API is ready
    Object getPlayerContainer();

    /**
     * Retrieves the previous camera yaw of this player.
     *
     * @return The previous camera yaw of this player.
     */
    float getPrevCameraYaw();

    /**
     * Retrieves the camera yaw of this player.
     *
     * @return The camera yaw of this player.
     */
    float getCameraYaw();

    /**
     * Retrieves the previous chasing position X-axis of this player.
     *
     * @return The previous chasing position X-axis  of this player.
     */
    double getPrevChasingPosX();

    /**
     * Retrieves the previous chasing position Y-axis of this player.
     *
     * @return The previous chasing position Y-axis  of this player.
     */
    double getPrevChasingPosY();

    /**
     * Retrieves the previous chasing position Z-axis of this player.
     *
     * @return The previous chasing position Z-axis  of this player.
     */
    double getPrevChasingPosZ();

    /**
     * Retrieves the chasing position X-axis of this player.
     *
     * @return The chasing position X-axis  of this player.
     */
    double getChasingPosX();

    /**
     * Retrieves the chasing position Y-axis of this player.
     *
     * @return The chasing position Y-axis  of this player.
     */
    double getChasingPosY();

    /**
     * Retrieves the  chasing position Z-axis of this player.
     *
     * @return The chasing position Z-axis  of this player.
     */
    double getChasingPosZ();

    /**
     * A factory class for {@link Player}
     *
     * @param <T> The type of this player
     */
    interface Factory<T> {

        /**
         * Creates a new {@link Player} with the given type.
         *
         * @param player A type to create this player
         * @return The created player
         */
        Player create(T player);

    }

}
