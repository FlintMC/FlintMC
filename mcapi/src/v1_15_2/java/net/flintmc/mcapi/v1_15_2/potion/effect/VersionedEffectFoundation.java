package net.flintmc.mcapi.v1_15_2.potion.effect;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.entity.mapper.EntityMapper;
import net.flintmc.mcapi.potion.effect.Effect;
import net.flintmc.mcapi.potion.effect.EffectFoundation;
import net.minecraft.potion.EffectInstance;

/**
 * 1.15.2 implementation of the {@link EffectFoundation}.
 */
@Implement(value = EffectFoundation.class, version = "1.15.2")
public class VersionedEffectFoundation implements EffectFoundation {

  private final EffectInstance effectInstance;
  private final Effect potion;
  private final EntityMapper entityMapper;
  private final int duration;
  private final int amplifier;
  private final boolean ambient;
  private final boolean showParticles;
  private final boolean showIcon;
  private boolean durationMaximum;

  @AssistedInject
  private VersionedEffectFoundation(
          @Assisted("effectInstance") Object effectInstance,
          @Assisted("potion") Effect potion,
          @Assisted("duration") int duration,
          @Assisted("amplifier") int amplifier,
          @Assisted("ambient") boolean ambient,
          @Assisted("durationMaximum") boolean durationMaximum,
          @Assisted("showParticles") boolean showParticles,
          @Assisted("showIcon") boolean showIcon,
          EntityMapper entityMapper) {
    this.potion = potion;
    this.duration = duration;
    this.amplifier = amplifier;
    this.ambient = ambient;
    this.durationMaximum = durationMaximum;
    this.showParticles = showParticles;
    this.showIcon = showIcon;
    this.entityMapper = entityMapper;

    if (!(effectInstance instanceof EffectInstance)) {
      throw new IllegalArgumentException(
              effectInstance.getClass().getName()
                      + " is not an instance of "
                      + net.minecraft.potion.EffectInstance.class.getName());
    }

    this.effectInstance = (EffectInstance) effectInstance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Effect getPotion() {
    return this.potion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getDuration() {
    return this.duration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAmplifier() {
    return this.amplifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAmbient() {
    return this.ambient;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean doesShowParticles() {
    return this.showParticles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShowIcon() {
    return this.showIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void performEffect(LivingEntity entity) {
    this.effectInstance.performEffect((net.minecraft.entity.LivingEntity) this.entityMapper.toMinecraftLivingEntity(entity));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEffectName() {
    return this.potion.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDurationMaximum() {
    return this.durationMaximum;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDurationMaximum(boolean durationMaximal) {
    this.durationMaximum = durationMaximal;
  }
}
