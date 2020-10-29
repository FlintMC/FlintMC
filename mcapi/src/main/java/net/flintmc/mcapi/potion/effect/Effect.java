package net.flintmc.mcapi.potion.effect;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.LivingEntity;

/**
 * Represents a Minecraft effect.
 */
public interface Effect {

  /**
   * Retrieves the potion is beneficial to the player. Beneficial potions are shown on the first row the HUD.
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
   * Checks if the potion effect is ready to be applied this tick.
   *
   * @param duration  The duration of the effect.
   * @param amplifier The amplifier of the effect.
   * @return {@code true} if the potion effect is read yto be applied this tick, otherwise {@code false}.
   */
  boolean isReady(int duration, int amplifier);

  void affectEntity(Entity source, Entity indirectSource, LivingEntity livingEntity, int amplifier, double health);

  void performEffect(LivingEntity livingEntity, int amplifier);

  /**
   * An enumeration of all available effect types.
   */
  enum Type {

    BENEFICIAL,
    HARMFUL,
    NEUTRAL

  }

  /**
   * A factory class for creating {@link Effect}'s.
   */
  @AssistedFactory(Effect.class)
  interface Factory {

    /**
     * Creates a new {@link Effect} with the given {@code effectType} and the {@code liquidColor}
     *
     * @param effectType  The type of the effect.
     * @param liquidColor The liquid color for the effect.
     * @return A created effect.
     */
    Effect create(
            @Assisted("effect") Object effect,
            @Assisted("effectType") Type effectType,
            @Assisted("liquidColor") int liquidColor
    );

  }

}
