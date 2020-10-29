package net.flintmc.mcapi.potion.effect;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.mcapi.entity.LivingEntity;

/**
 * Represents the Minecraft effect instance.
 */
public interface EffectFoundation {

  /**
   * Retrieves the effect of the foundation.
   *
   * @return The foundation's effect.
   */
  Effect getPotion();

  /**
   * Retrieves the duration of the effect foundation.
   *
   * @return The effect foundation's duration.
   */
  int getDuration();

  /**
   * Retrieves the amplifier of the effect foundation.
   *
   * @return The effect foundation's amplifier.
   */
  int getAmplifier();

  /**
   * Whether the effect foundation is ambient.
   *
   * @return {@code true} if the effect foundation is ambient, otherwise {@code false}.
   */
  boolean isAmbient();

  /**
   * Whether the effect foundation shows particles.
   *
   * @return {@code true} if the effect foundation shows particles, otherwise {@code false}.
   */
  boolean doesShowParticles();

  /**
   * Whether the effect foundation shows an icon.
   *
   * @return {@code true} if the effect foundation shows an icon, otherwise {@code false}.
   */
  boolean isShowIcon();

  /**
   * Performs the effect at the given {@link LivingEntity}.
   *
   * @param entity The living entity to which the effect should be applied.
   */
  void performEffect(LivingEntity entity);

  /**
   * Retrieves the effect name of the foundation.
   *
   * @return The foundation effect name.
   */
  String getEffectName();

  /**
   * Whether the duration of the effect foundation is maximal.
   *
   * @return {@code true} if the duration of the effect foundation is maximal, otherwise {@code false}.
   */
  boolean isDurationMaximum();

  /**
   * Changes whether the duration of the effect foundation is maximal.
   *
   * @param maxDuration {@code true} if the duration of the effect foundation should be maximum, otherwise {@code false}.
   */
  void setDurationMaximum(boolean maxDuration);

  /**
   * A factory class for creating {@link EffectFoundation}'s.
   */
  @AssistedFactory(EffectFoundation.class)
  interface Factory {

    /**
     * Creates a new {@link EffectFoundation} with the given parameters.
     *
     * @param effectInstance  The non-null Minecraft effect instance.
     * @param potion          The potion effect of the foundation.
     * @param duration        The duration of the foundation.
     * @param amplifier       The amplifier of the foundation.
     * @param ambient         {@code true} if the foundation ambient, otherwise {@code false}.
     * @param durationMaximum {@code true} if the duration maximum, otherwise {@code false}.
     * @param showParticles   {@code true} if the foundation should be show particles, otherwise {@code false}.
     * @param showIcon        {@code true} if the foundation should be show an icon, otherwise {@code false}.
     * @return A created effect foundation.
     */
    EffectFoundation create(
            @Assisted("effectInstance") Object effectInstance,
            @Assisted("potion") Effect potion,
            @Assisted("duration") int duration,
            @Assisted("amplifier") int amplifier,
            @Assisted("ambient") boolean ambient,
            @Assisted("durationMaximum") boolean durationMaximum,
            @Assisted("showParticles") boolean showParticles,
            @Assisted("showIcon") boolean showIcon
    );

  }

}
