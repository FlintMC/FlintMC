package net.flintmc.mcapi.potion;

import net.flintmc.mcapi.potion.effect.Effect;

import java.util.Map;

public interface PotionRegister {

  /**
   * Retrieves a key-value system with the registered {@link Effect}'s.
   *
   * @return A key-value system.
   */
  Map<String, Effect> getEffects();

  /**
   * Retrieves a key-value system with the registered {@link Potion}'s.
   *
   * @return A key-value system.
   */
  Map<String, Potion> getPotions();

}
