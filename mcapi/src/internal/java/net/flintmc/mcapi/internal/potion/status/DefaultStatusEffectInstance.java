package net.flintmc.mcapi.internal.potion.status;

import java.util.Objects;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;

@Implement(StatusEffectInstance.class)
public class DefaultStatusEffectInstance implements StatusEffectInstance {

  private final StatusEffect potion;
  private final int amplifier;
  private final boolean ambient;
  private final boolean showParticles;
  private final boolean showIcon;
  private int duration;
  private boolean durationMaximum;

  @AssistedInject
  private DefaultStatusEffectInstance(
      @Assisted("potion") StatusEffect potion,
      @Assisted("duration") int duration,
      @Assisted("amplifier") int amplifier,
      @Assisted("ambient") boolean ambient,
      @Assisted("durationMaximum") boolean durationMaximum,
      @Assisted("showParticles") boolean showParticles,
      @Assisted("showIcon") boolean showIcon) {
    this.potion = potion;
    this.duration = duration;
    this.amplifier = amplifier;
    this.ambient = ambient;
    this.durationMaximum = durationMaximum;
    this.showParticles = showParticles;
    this.showIcon = showIcon;
  }

  /** {@inheritDoc} */
  @Override
  public StatusEffect getPotion() {
    return this.potion;
  }

  /** {@inheritDoc} */
  @Override
  public int getDuration() {
    return this.duration;
  }

  /** {@inheritDoc} */
  @Override
  public int getAmplifier() {
    return this.amplifier;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isAmbient() {
    return this.ambient;
  }

  /** {@inheritDoc} */
  @Override
  public boolean doesShowParticles() {
    return this.showParticles;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isShowIcon() {
    return this.showIcon;
  }

  /** {@inheritDoc} */
  @Override
  public String getEffectName() {
    return this.potion.getName();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDurationMaximum() {
    return this.durationMaximum;
  }

  /** {@inheritDoc} */
  @Override
  public void setDurationMaximum(boolean durationMaximal) {
    this.durationMaximum = durationMaximal;
  }

  @Override
  public boolean update() {
    if (this.duration > 0) {
      --this.duration;
    }

    return this.duration > 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!(obj instanceof StatusEffectInstance)) {
      return false;
    } else {
      StatusEffectInstance statusEffectInstance = (StatusEffectInstance) obj;
      return this.duration == statusEffectInstance.getDuration()
          && this.ambient == statusEffectInstance.isAmbient()
          && this.amplifier == statusEffectInstance.getAmplifier()
          && this.potion.equals(statusEffectInstance.getPotion());
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(potion, amplifier, ambient, potion);
  }
}
