package net.labyfy.internal.player.v1_15_2;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.player.FoodStats;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

/**
 * 1.15.2 implementation of the player's food statistics.
 */
@Implement(value = FoodStats.class, version = "1.15.2")
public class VersionedFoodStats implements FoodStats {

    /**
     * Updates the food statistics of this player
     *
     * @param foodLevel              The new food level
     * @param foodSaturationModifier The new food saturation modifier
     */
    @Override
    public void updateStats(int foodLevel, int foodSaturationModifier) {
        Minecraft.getInstance().player.getFoodStats().addStats(foodLevel, foodSaturationModifier);
    }

    /**
     * Updates the exhaustion of this player
     *
     * @param exhaustion The new exhaustion
     */
    @Override
    public void updateExhaustion(float exhaustion) {
        Minecraft.getInstance().player.getFoodStats().addExhaustion(exhaustion);
    }

    /**
     * Whether the player can consume the food.
     *
     * @param food The item that should be food
     */
    @Override
    public void consume(Object food) {
        Minecraft.getInstance().player.getFoodStats().consume((Item) food, null);
    }

    /**
     * Retrieves the food level of this player
     *
     * @return the food level
     */
    @Override
    public int getFoodLevel() {
        return Minecraft.getInstance().player.getFoodStats().getFoodLevel();
    }

    /**
     * Whether the player needs food
     *
     * @return {@code true} if the player needs food, otherwise {@code false}
     */
    @Override
    public boolean needFood() {
        return Minecraft.getInstance().player.getFoodStats().needFood();
    }

    /**
     * Retrieves the saturation level of this player
     *
     * @return the saturation level
     */
    @Override
    public float getSaturationLevel() {
        return Minecraft.getInstance().player.getFoodStats().getSaturationLevel();
    }
}
