package net.labyfy.component.player;

import net.labyfy.component.player.gameprofile.GameProfile;
import net.labyfy.component.player.gui.TabOverlay;
import net.labyfy.component.player.inventory.PlayerInventory;
import net.labyfy.component.player.network.NetworkPlayerInfo;
import net.labyfy.component.player.util.Hand;

import java.util.UUID;

/**
 * Represents a player
 */
public interface Player extends PlayerSkinProfile {

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
     * Retrieves the inventory of this player.
     *
     * @return the inventory of this player
     */
    PlayerInventory getPlayerInventory();

    /**
     * Retrieves the health of this player.
     *
     * @return the health of this player
     */
    float getHealth();

    /**
     * Retrieves the experience level of this player.
     *
     * @return the experience level of this player
     */
    int getExperienceLevel();

    /**
     * Retrieves the experience total of this player.
     *
     * @return the experience total of this player
     */
    int getExperienceTotal();

    /**
     * Retrieves the experience of this player.
     *
     * @return the experience of this player.
     */
    float getExperience();

    /**
     * Retrieves the language of this player.
     *
     * @return the language of this player
     */
    String getLocale();

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
     * Retrieves the tab overlay of this player.
     *
     * @return the tab overlay of this player.
     */
    TabOverlay getTabOverlay();

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
     * Retrieves the yaw of this player.
     *
     * @return the yaw of this player.
     */
    float getYaw();

    /**
     * Retrieves the rotation yaw head of this player.
     *
     * @return the rotation yaw head of this player.
     */
    float getRotationYawHead();

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
     * Whether the player has any information
     *
     * @return {@code true} if the player has any information, otherwise {@code false}
     */
    boolean hasPlayerInfo();

    /**
     * Retrieves the <b>FOV</b> modifier of this player.
     *
     * @return the fov modifier of this player
     */
    float getFovModifier();

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
     * Retrieves the food statistics of this player
     *
     * @return the food statistics of this player
     */
    FoodStats getFoodStats();

    /**
     * Retrieves the network player info of this player.
     *
     * @return the network player info of this  player
     */
    NetworkPlayerInfo getNetworkPlayerInfo();

    /**
     * Prints a message in this player chat
     *
     * @param component The message to print
     */
    // TODO: 31.08.2020 Replaces the Object to TextComponent when the Chat API is ready
    void sendMessage(Object component);

    /**
     * Sends a message to the chat
     *
     * @param content The message content
     */
    void sendMessage(String content);

}
