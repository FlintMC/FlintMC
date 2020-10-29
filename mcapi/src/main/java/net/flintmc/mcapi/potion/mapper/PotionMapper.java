package net.flintmc.mcapi.potion.mapper;

import net.flintmc.mcapi.potion.Potion;
import net.flintmc.mcapi.potion.effect.Effect;
import net.flintmc.mcapi.potion.effect.EffectFoundation;

/**
 * Mapper between Minecraft potion and Flint potion.
 */
public interface PotionMapper {

  /**
   * Creates a new Minecraft potion by using the Flint {@link Potion} as the base.
   *
   * @param potion The non-null Flint {@link Potion}.
   * @return The new Minecraft potion or the default potion if the given potion was invalid.
   */
  Object toMinecraftPotion(Potion potion);

  /**
   * Creates a new {@link Potion} by using the given Minecraft potion as the base.
   *
   * @param handle The non-null Minecraft potion.
   * @return The new Flint {@link Potion} or {@code null} if the given potion was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft potion.
   */
  Potion fromMinecraftPotion(Object handle);

  /**
   * Creates a new Minecraft effect by using the Flint {@link Effect} as the base.
   *
   * @param effect The non-null Flint {@link Effect}.
   * @return The new Minecraft effect or the default effect if the given effect was invalid.
   */
  Object toMinecraftEffect(Effect effect);

  /**
   * Creates a new {@link Effect} by using the given Minecraft effect as the base.
   *
   * @param handle The non-null Minecraft effect.
   * @return The new Flint {@link Effect} or {@code null} if the given effect was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft effect.
   */
  Effect fromMinecraftEffect(Object handle);

  /**
   * Creates a new Minecraft effect by using the Flint {@link Effect.Type} as the base.
   *
   * @param effectType The non-null Flint {@link Effect.Type}.
   * @return The new Minecraft effect or the default effect if the given effect type was invalid.
   */
  Object toMinecraftEffectType(Effect.Type effectType);

  /**
   * Creates a new {@link Effect.Type} by using the given Minecraft effect type as the base.
   *
   * @param handle The non-null Minecraft effect type.
   * @return The new Flint {@link Effect.Type}.
   */
  Effect.Type fromMinecraftEffectType(Object handle);

  /**
   * Creates a new Minecraft effect instance by using the Flint {@link EffectFoundation} as the base.
   *
   * @param foundation The non-null Flint {@link EffectFoundation}.
   * @return The new Minecraft effect instance.
   */
  Object toMinecraftEffectInstance(EffectFoundation foundation);

  /**
   * Creates a new {@link EffectFoundation} by using the given Minecraft effect instance as the base.
   *
   * @param handle The non-null Minecraft effect instance.
   * @return The new Flint {@link EffectFoundation} or {@code null} if the given effect instance was invalid.
   * @throws IllegalArgumentException If the given object is no Minecraft effect instance.
   */
  EffectFoundation fromMinecraftEffectInstance(Object handle);

}
