package net.flintmc.mcapi.potion;

import java.util.Map;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.resources.ResourceLocation;

/**
 * Represents a potion register to registers potions or effects.
 */
public interface PotionRegister {

  /**
   * Adds a new {@link StatusEffect} to the register.
   *
   * @param location The location of the status effect.
   * @param effect   The status effect to be added.
   */
  void addEffect(ResourceLocation location, StatusEffect effect);

  /**
   * Removes a status effect from the register.
   *
   * @param location The location of the status effect.
   */
  void removeEffect(ResourceLocation location);

  /**
   * Removes a status effect from the register.
   *
   * @param location The location of the status effect.
   * @param effect   The effect to be removed.
   */
  void removeEffect(ResourceLocation location, StatusEffect effect);

  /**
   * Retrieves a status effect with the given {@code location}.
   *
   * @param location The location of the status effect.
   * @return A status effect or {@code null}.
   */
  StatusEffect getEffect(ResourceLocation location);

  /**
   * Retrieves a key-value system with the registered {@link StatusEffect}'s.
   *
   * @return A key-value system.
   */
  Map<ResourceLocation, StatusEffect> getEffects();

  /**
   * Adds a new {@link Potion} to the register.
   *
   * @param location The location of the potion.
   * @param potion   The potion to be added.
   */
  void addPotion(ResourceLocation location, Potion potion);

  /**
   * Removes a potion from the register.
   *
   * @param location The location of the potion.
   */
  void removePotion(ResourceLocation location);

  /**
   * Removes a potion from the register.
   *
   * @param location The location of the potion.
   * @param potion   The potion to be removed.
   */
  void removePotion(ResourceLocation location, Potion potion);

  /**
   * Retrieves a potion with the given {@code location}.
   *
   * @param location The location of the potion.
   * @return A potion with the location or {@code null}.
   */
  Potion getPotion(ResourceLocation location);

  /**
   * Retrieves a key-value system with the registered {@link Potion}'s.
   *
   * @return A key-value system.
   */
  Map<ResourceLocation, Potion> getPotions();
}
