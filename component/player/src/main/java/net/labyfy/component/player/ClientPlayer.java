package net.labyfy.component.player;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.overlay.TabOverlay;

import java.util.List;

/**
 * Represents the client player
 */
@Implement(Player.class)
public interface ClientPlayer<T> extends Player<T> {

    /**
     * Retrieves the inventory of this player.
     *
     * @return The inventory of this player
     */
    // TODO: 07.09.2020 Replaces the Object to PlayerInventory when the Inventory API is ready
    Object getPlayerInventory();

    /**
     * Retrieves the armor contents of this player.
     *
     * @return The armor contents of this player.
     */
    // TODO: 04.09.2020 Replaces the Object to ItemStack when the (Item API?) is ready
    List<Object> getArmorContents();

    /**
     * Retrieves the experience level of this player.
     *
     * @return The experience level of this player
     */
    int getExperienceLevel();

    /**
     * Retrieves the experience total of this player.
     *
     * @return The experience total of this player
     */
    int getExperienceTotal();

    /**
     * Retrieves the experience of this player.
     *
     * @return The experience of this player.
     */
    float getExperience();

    /**
     * Retrieves the language of this player.
     *
     * @return The language of this player
     */
    String getLocale();

    /**
     * Retrieves the tab overlay of this player.
     *
     * @return The tab overlay of this player.
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
     * @return The fov modifier of this player
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
     * Retrieves the server brand of this player.
     *
     * @return The server brand of this player.
     */
    String getServerBrand();

    /**
     * Sets the server brand of this player.
     *
     * @param brand The new server brand.
     */
    void setServerBrand(String brand);

    /**
     * Retrieves the water brightness of this player.
     *
     * @return The water brightness of this player.
     */
    float getWaterBrightness();

    /**
     * Retrieves the previous time in portal of this player.
     *
     * @return The previous time in portal of this player.
     */
    float getPrevTimeInPortal();

    /**
     * Retrieves the time in portal of this player.
     *
     * @return The time in portal of this player.
     */
    float getTimeInPortal();

    /**
     * Sends a message to the chat
     *
     * @param content The message content
     */
    void sendMessage(String content);

    /**
     * Performs a command
     *
     * @param command The command to perform
     * @return {@code true} was the command successful performed, otherwise {@code false}
     */
    boolean performCommand(String command);

    /**
     * Closes the screen and drop the item stack
     */
    void closeScreenAndDropStack();

    /**
     * Whether the player can swim.
     *
     * @return {@code true} if the player can swim, otherwise {@code false}
     */
    boolean canSwim();

    /**
     * Sends the horse inventory to the server.
     */
    void sendHorseInventory();

    /**
     * Retrieves the horse jump power.
     *
     * @return The horse jump power.
     */
    float getHorseJumpPower();

    /**
     * Sets the experience stats of this player.
     *
     * @param currentExperience The current experience of this player.
     * @param maxExperience     The max experience of this  player.
     * @param level             The level of this player.
     */
    void setExperienceStats(int currentExperience, int maxExperience, int level);

    /**
     * Whether the player is holding the sneak key.
     *
     * @return {@code true} if the player is holding the sneak key, otherwise {@code false}
     */
    boolean isHoldingSneakKey();

    /**
     * Retrieves the statistics manager of this player.
     *
     * @return The statistics manager of this player.
     */
    // TODO: 06.09.2020 Replaces the Object to StatisticsManager when the Statistics API is ready.
    Object getStatistics();

    /**
     * Retrieves the recipe book of this player.
     *
     * @return The recipe book of this player.
     */
    // TODO: 06.09.2020 Replaces the Object to ClientRecipeBook/RecipeBook when the Item API is ready.
    Object getRecipeBook();

    /**
     * Removes the highlight a recipe.
     *
     * @param recipe The recipe to removing the highlighting.
     */
    // TODO: 06.09.2020 Replaces the Object to Recipe when the Item API is ready.
    void removeRecipeHighlight(Object recipe);

    /**
     * Updates the health of this player.
     * <br><br>
     * <b>Note:</b> This is only on the client side.
     *
     * @param health The new health of this player.
     */
    void updatePlayerHealth(float health);

    /**
     * Updates the entity action state of this player.
     */
    void updateEntityActionState();

    /**
     * Sends a plugin message to the server.
     *
     * @param channel The name of this channel
     * @param data    The data to be written into the channel
     */
    void sendPluginMessage(String channel, byte[] data);

    /**
     * Retrieves the network communicator of this player.<br>
     * The network communicator allows this player to send packets to the server.
     *
     * @return The network communicator of this player.
     */
    Object getConnection();

    /**
     * Retrieves the current biome name of this player.
     *
     * @return The current biome name or {@code null}
     */
    String getBiome();

    /**
     * A factory class for {@link ClientPlayer}
     */
    interface Factory {

        /**
         * Creates a new {@link ClientPlayer}.
         *
         * @return The created client player
         */
        ClientPlayer create();

    }

}
