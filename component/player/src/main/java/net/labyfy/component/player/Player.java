package net.labyfy.component.player;

import groovyjarjarantlr4.v4.runtime.misc.ObjectEqualityComparator;
import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.util.CooldownTracking;
import net.labyfy.component.player.util.EntityPose;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.util.PlayerClothing;
import net.labyfy.component.player.util.sound.Sound;
import net.labyfy.component.player.util.sound.SoundCategory;
import net.labyfy.component.player.world.World;

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
     * @return the player.
     */
    T getPlayer();

    /**
     * Retrieves the world of this player.
     *
     * @return the world of this player.
     */
    World getWorld();

    /**
     * Retrieves the game profile of this player.
     *
     * @return the game profile of this player.
     */
    GameProfile getGameProfile();

    /**
     * Retrieves the name of this player.
     *
     * @return the name of this player
     */
    // TODO: 31.08.2020 Replaces the Object to TextComponent when the Chat API is ready
    Object getName();

    /**
     * Retrieves the display name of this player
     *
     * @return the display name of this player
     */
    // TODO: 01.09.2020 Replaces the Object to TextComponent when the Chat API is ready
    Object getDisplayName();

    /**
     * Retrieves the display name and the unique identifier of this player.
     *
     * @return the display name and the unique identiifer of this player.
     */
    // TODO: 05.09.2020 Replaces the Object to TextComponent when the Chat API is ready
    Object getDisplayNameAndUniqueId();

    /**
     * Retrieves the unique identifier of this player.
     *
     * @return the unique identifier of this player
     */
    UUID getUniqueId();

    /**
     * Retrieves the health of this player.
     *
     * @return the health of this player
     */
    float getHealth();

    /**
     * Retrieves the list name of this player.
     *
     * @return the list name of this player.
     */
    String getPlayerListName();

    /**
     * Retrieves the world time of this player.
     *
     * @return the world time of this player.
     */
    long getPlayerTime();

    /**
     * Retrieves the x position of this player.
     *
     * @return the x position of this player
     */
    double getX();

    /**
     * Retrieves the y position of this player.
     *
     * @return the y position of this player
     */
    double getY();

    /**
     * Retrieves the z position of this player.
     *
     * @return the z position of this playe
     */
    double getZ();

    /**
     * Retrieves the pitch of this player.
     *
     * @return the pitch of this player.
     */
    float getPitch();

    /**
     * Retrieves the pitch of this player.
     *
     * @param partialTicks The period of time, in fractions of a tick,
     *                     that has passed since the last full tick
     * @return the pitch of this player.
     */
    float getPitch(float partialTicks);

    /**
     * Retrieves the yaw of this player.
     *
     * @return the yaw of this player.
     */
    float getYaw();

    /**
     * Retrieves the yaw of this player.
     *
     * @param partialTicks The period of time, in fractions of a tick,
     *                     that has passed since the last full tick
     * @return the yaw of this player.
     */
    float getYaw(float partialTicks);

    /**
     * Retrieves the rotation yaw head of this player.
     *
     * @return the rotation yaw head of this player.
     */
    float getRotationYawHead();

    /**
     * Retrieves the eye height of this player.
     *
     * @return the eye height of this player.
     */
    float getEyeHeight();

    /**
     * Retrieves the eye height of this player.
     *
     * @param entityPose The current pose for the eye height
     * @return the eye height of this player
     */
    float getEyeHeight(EntityPose entityPose);

    /**
     * Retrieves the y position of the eyes of this player.
     *
     * @return the y position of the eyes of this player
     */
    double getPosYEye();

    /**
     * Retrieves the pose of this player.
     *
     * @return the pose of this player.
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
     * @return the active hand of this player
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
     * @return the active item stack of this player
     */
    // TODO: 01.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    Object getActiveItemStack();

    /**
     * Retrieves use count of the item.
     *
     * @return the use count of the item
     */
    int getItemInUseCount();

    /**
     * Retrieves the maximal use count of the item.
     *
     * @return the maximal use count of this item.
     */
    int getItemInUseMaxCount();

    /**
     * Retrieves the food level of this player.
     *
     * @return the food level of this player.
     */
    int getFoodLevel();

    /**
     * Retrieves the saturation of this player.
     *
     * @return the saturation of this player.
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
     * @return the absorption amount of this player.
     */
    float getAbsorptionAmount();

    /**
     * Retrieves the total armor value of this player.
     *
     * @return the total armor value of this player.
     */
    int getTotalArmorValue();

    /**
     * Prints a message in this player chat
     *
     * @param component The message to print
     */
    // TODO: 31.08.2020 Replaces the Object to TextComponent when the Chat API is ready
    void sendMessage(Object component);

    /**
     * Retrieves the network player info of this player.
     *
     * @return the network player info of this  player
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
     * @return the sound category of this player.
     */
    default SoundCategory getSoundCategory() {
        return SoundCategory.PLAYER;
    }

    /**
     * Retrieves the fall distance of this player.
     *
     * @return the fall distance of this player.
     */
    float getFallDistance();

    /**
     * Retrieves the maximal fall distance of this player.
     *
     * @return the maximal fall distance of this player.
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
     * @return the maximal in portal time of this player.
     */
    int getMaxInPortalTime();

    /**
     * Retrieves the fly speed of this player.
     *
     * @return the fly speed of this player.
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
     * @return the walk speed of this player.
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
     * @return the luck of this player.
     */
    float getLuck();

    /**
     * Retrieves the primary hand this player.
     *
     * @return the primary hand this player.
     */
    Hand.Side getPrimaryHand();

    /**
     * Sets the primary hand of this player.
     *
     * @param side the primary hand of this player.
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
     * @return the cooldown period of this player.
     */
    float getCooldownPeriod();

    /**
     * Retrieves the cooled attack strength of this player.
     *
     * @param adjustTicks The ticks to adjust the cooled strength of the attack.
     * @return the cooled attack strength of this player.
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
     * @return an iterable collection of the equipment held by that player.
     */
    // TODO: 05.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    Iterable<Object> getHeldEquipment();

    /**
     * Retrieves an iterable inventory of that player's armor.
     *
     * @return an iterable inventory of that player's armor.
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
     * @return the experience bar cap of this player.
     */
    int experienceBarCap();

    /**
     * Retrieves the experience speed of this player.
     *
     * @return the experience speed of this player.
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
     * @return the AI move speed of this player.
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
     * @return the sleep timer of this player.
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
     * @return the score of this player.
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
     * @return the dropped item as an entity, or {@code null}
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
     * @return the dropped item as an entity, or {@code null}
     */
    // TODO: 05.09.2020 (Method Type) Replaces the Object to ItemEntity when the (Entity API?) is ready
    // TODO: 05.09.2020 (Parameter 1) Replaces the Object to ItemStack when the (Item API?) is ready
    Object dropItem(Object droppedItem, boolean dropAround, boolean traceItem);


    /**
     * Retrieves the digging speed of the given block state for this player.
     *
     * @param blockState The block state that is to receive the dig speed.
     * @return the digging speed of the block state for this player.
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
    // TODO: 05.09.2020 Replaces the Object to TextComponent when the Chat API is ready
    void sendStatusMessage(Object component, boolean actionBar);

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
     * @return the entity as a compound nbt.
     */
    // TODO: 05.09.2020 Replaces the Object to CompoundNBT when the NBT API is ready
    Object getLeftShoulderEntity();

    /**
     * Retrieves the entity which is on the right shoulder.
     *
     * @return the entity as a compound nbt.
     */
    // TODO: 05.09.2020 Replaces the Object to CompoundNBT when the NBT API is ready
    Object getRightShoulderEntity();

    /**
     * Retrieves the world scoreboard of this player.
     *
     * @return the world scoreboard of this player.
     */
    // TODO: 05.09.2020 Replaces the Object to Scoreboard when the Scoreboard API is ready
    default Object getWorldScoreboard() {
        return this.getWorld().getScoreboard();
    }

    /**
     * Retrieves the fire timer of this player.
     *
     * @return the fire timer of this player.
     */
    int getFireTimer();

    /**
     * Retrieves the step height of this player.
     *
     * @return the step height of this player.
     */
    float getStepHeight();

    /**
     * Retrieves the x rotate elytra of this player.
     *
     * @return the x rotate elytra of this player.
     */
    float getRotateElytraX();

    /**
     * Retrieves the y rotate elytra of this player.
     *
     * @return the y rotate elytra of this player.
     */
    float getRotateElytraY();

    /**
     * Retrieves the z rotate elytra of this player.
     *
     * @return the z rotate elytra of this player.
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
     * A factory class for {@link Player}
     *
     * @param <T> The type of this player
     */
    interface Factory<T> {

        /**
         * Creates a new {@link Player} with the given type.
         *
         * @param player A type to create this player
         * @return the created player
         */
        Player create(T player);

    }

}
