package net.labyfy.component.player;

/**
 * Represents the food stats of a player.
 */
public interface FoodStats {

    /**
     * Updates the food statistics of this player
     *
     * @param foodLevel              The new food level
     * @param foodSaturationModifier The new food saturation modifier
     */
    void updateStats(int foodLevel, int foodSaturationModifier);

    /**
     * Updates the exhaustion of this player
     *
     * @param exhaustion The new exhaustion
     */
    void updateExhaustion(float exhaustion);

    /**
     * Whether the player can consume the food.
     *
     * @param food The item that should be food
     */
    // TODO: 01.09.2020 Replaces the Object to Item when the (Item API?) is ready
    void consume(Object food);

    /**
     * Retrieves the food level of this player
     *
     * @return the food level
     */
    int getFoodLevel();

    /**
     * Whether the player needs food
     *
     * @return {@code true} if the player needs food, otherwise {@code false}
     */
    boolean needFood();

    /**
     * Retrieves the saturation level of this player
     *
     * @return the saturation level
     */
    float getSaturationLevel();

}
