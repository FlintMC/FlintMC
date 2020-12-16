package net.flintmc.mcapi.potion;

import java.util.List;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

/** Represents a Minecraft potion. */
public interface Potion {

  /**
   * Retrieves the name of the potion.
   *
   * @return The potion's name.
   */
  String getName();

  /**
   * Retrieves a collection with all {@link StatusEffectInstance}'s of the potion.
   *
   * @return A collection with all effect foundations of the potion.
   */
  List<StatusEffectInstance> getStatusEffects();

  /**
   * Whether the potion has an instant effect.
   *
   * @return {@code true} if the potion has an instant effect, otherwise {@code false}.
   */
  boolean hasInstantly();

  /** A factory class for creating {@link Potion}'s. */
  @AssistedFactory(Potion.class)
  interface Factory {

    /**
     * Creates a new {@link Potion} with the given {@code effects}
     *
     * @param effects An array of effects for the potion.
     * @return A created potion.
     */
    Potion create(@Assisted("effects") StatusEffectInstance... effects);

    /**
     * Creates a new {@link Potion} with the given {@code name} and {@code effects}.
     *
     * @param name The name of the potion.
     * @param effects An array of effects for the potion.
     * @return A created potion.
     */
    Potion create(
        @Assisted("name") String name, @Assisted("effects") StatusEffectInstance... effects);
  }
}
