package net.flintmc.mcapi.potion;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.potion.effect.EffectFoundation;

import java.util.List;

/**
 * Represents a Minecraft potion.
 */
public interface Potion {

  /**
   * Retrieves the name of the potion.
   *
   * @return The potion's name.
   */
  String getName();

  /**
   * Retrieves a collection with all {@link EffectFoundation}'s of the potion.
   *
   * @return A collection with all effect foundations of the potion.
   */
  List<EffectFoundation> getEffects();

  /**
   * Whether the potion has an instant effect.
   *
   * @return {@code true} if the potion has an instant effect, otherwise {@code false}.
   */
  boolean hasInstantEffect();

  /**
   * A factory class for creating {@link Potion}'s.
   */
  @AssistedFactory(Potion.class)
  interface Factory {

    /**
     * Creates a new {@link Potion} with the given {@code effectFoundations}
     *
     * @param effectFoundations The effect foundations for the potion.
     * @return A created potion.
     */
    Potion create(@Assisted("effects") List effectFoundations);

    /**
     * Creates a new {@link Potion} with the given {@code name} and {@code effectFoundations}.
     *
     * @param name              The name of the potion.
     * @param effectFoundations The effect foundations for the potion.
     * @return A created potion.
     */
    Potion create(@Assisted("name") String name, @Assisted("effects") List effectFoundations);
  }

}
