package net.flintmc.mcapi.internal.potion.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent;

@Implement(PotionStateUpdateEvent.class)
public class DefaultPotionStateUpdateEvent implements PotionStateUpdateEvent {

  private final LivingEntity livingEntity;
  private final StatusEffectInstance statusEffectInstance;
  private final State state;

  @AssistedInject
  public DefaultPotionStateUpdateEvent(
      @Assisted("livingEntity") LivingEntity livingEntity,
      @Assisted("effect") StatusEffectInstance effect,
      @Assisted("state") State state) {
    this.livingEntity = livingEntity;
    this.statusEffectInstance = effect;
    this.state = state;
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getLivingEntity() {
    return this.livingEntity;
  }

  /** {@inheritDoc} */
  @Override
  public StatusEffectInstance getStatusEffectInstance() {
    return this.statusEffectInstance;
  }

  /** {@inheritDoc} */
  @Override
  public State getState() {
    return this.state;
  }
}
