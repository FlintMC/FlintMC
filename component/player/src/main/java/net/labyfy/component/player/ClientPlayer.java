package net.labyfy.component.player;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.overlay.TabOverlay;
import net.labyfy.component.player.inventory.PlayerInventory;

import java.util.List;

/**
 * Represents the client player
 */
@Implement(Player.class)
public interface ClientPlayer<T> extends Player<T> {

    /**
     * Retrieves the inventory of this player.
     *
     * @return the inventory of this player
     */
    PlayerInventory getPlayerInventory();

    /**
     * Retrieves the armor contents of this player.
     *
     * @return the armor contents of this player.
     */
    // TODO: 04.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    List<Object> getArmorContents();

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
     * Retrieves the tab overlay of this player.
     *
     * @return the tab overlay of this player.
     */
    TabOverlay getTabOverlay();

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
     * Whether auto jump is enabled.
     *
     * @return {@code true} if auto jump is enabled, otherwise {@code false}
     */
    boolean isAutoJumpEnabled();

    /**
     * Whether the player rides a horse
     *
     * @return {@code true} if the player rides a horse, otherwise {@code false}
     */
    boolean isRidingHorse();

    /**
     * Whether the player rows a boat
     *
     * @return {@code true} if the player rows a boat, otherwise {@code false}
     */
    boolean isRowingBoat();

    /**
     * Whether the player's death screen is shown
     *
     * @return {@code true} if the player's death screen is shown, otherwise {@code false}
     */
    boolean isShowDeathScreen();

    /**
     * Retrieves the water brightness of this player.
     *
     * @return the water brightness of this player.
     */
    String getServerBrand();

    /**
     * Retrieves the water brightness of this player.
     *
     * @return the water brightness of this player.
     */
    float getWaterBrightness();

    /**
     * Retrieves the previous time in portal of this player.
     *
     * @return the previous time in portal of this player.
     */
    float getPrevTimeInPortal();

    /**
     * Retrieves the time in portal of this player.
     *
     * @return the time in portal of this player.
     */
    float getTimeInPortal();

    /**
     * Performs a command
     *
     * @param command The command to perform
     * @return {@code true} was the command successful performed, otherwise {@code false}
     */
    boolean performCommand(String command);

    /**
     * A factory class for {@link ClientPlayer}
     */
    interface Factory {

        /**
         * Creates a new {@link ClientPlayer}.
         *
         * @return the created client player
         */
        ClientPlayer create();

    }

}
