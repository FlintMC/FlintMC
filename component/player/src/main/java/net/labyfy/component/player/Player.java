package net.labyfy.component.player;

import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.util.EntityPose;
import net.labyfy.component.player.util.Hand;
import net.labyfy.component.player.world.World;

import java.util.UUID;

/**
 * Represents a player
 */
public interface Player<T> extends PlayerSkinProfile {

    /**
     * Sets the player.
     *
     * @param player The new player.
     */
    void setPlayer(T player);

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
