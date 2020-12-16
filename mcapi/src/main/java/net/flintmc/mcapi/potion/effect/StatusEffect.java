package net.flintmc.mcapi.potion.effect;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents a Minecraft effect.
 */
public interface StatusEffect {

  /**
   * Retrieves the potion is beneficial to the player. Beneficial potions are shown on the first row
   * the HUD.
   *
   * @return {@code true} if the potion is beneficial to the player, otherwise {@code false}.
   */
  boolean isBeneficial();

  /**
   * Retrieves the color of the potion.
   *
   * @return The potion's color.
   */
  int getLiquidColor();

  /**
   * Retrieve the effect of the potion.
   *
   * @return The potion's effect type.
   */
  Type getEffectType();

  /**
   * Retrieves the name of the potion.
   *
   * @return The potion's name.
   */
  String getName();

  /**
   * Whether the potion has an instant effect.
   *
   * @return {@code true} if the potion has an instant effect, otherwise {@code false}.
   */
  boolean isInstant();

  /**
   * An enumeration of all available effect types.
   */
  enum Type {
    BENEFICIAL,
    HARMFUL,
    NEUTRAL
  }

  /**
   * A factory class for creating {@link StatusEffect}'s.
   */
  @AssistedFactory(StatusEffect.class)
  interface Factory {

    /**
     * Creates a new {@link StatusEffect} with the given {@code effectType} and the {@code
     * liquidColor}
     *
     * @param name        The name of the status effect.
     * @param instant     {@code true} if the effect instant, otherwise {@code false}.
     * @param effectType  The type of the effect.
     * @param liquidColor The liquid color for the effect.
     * @return A created effect.
     */
    StatusEffect create(
        @Assisted("name") String name,
        @Assisted("instant") boolean instant,
        @Assisted("effectType") Type effectType,
        @Assisted("liquidColor") int liquidColor);
  }
}
