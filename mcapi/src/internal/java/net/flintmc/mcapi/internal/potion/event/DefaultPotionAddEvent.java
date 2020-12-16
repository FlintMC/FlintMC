package net.flintmc.mcapi.internal.potion.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;
import net.flintmc.mcapi.potion.event.PotionAddEvent;

@Implement(PotionAddEvent.class)
public class DefaultPotionAddEvent extends DefaultPotionEvent implements PotionAddEvent {

  private final StatusEffectInstance statusEffectInstance;

  @AssistedInject
  private DefaultPotionAddEvent(
      @Assisted("livingEntity") LivingEntity livingEntity,
      @Assisted("statusEffectInstance") StatusEffectInstance statusEffectInstance) {
    super(Type.ADD, livingEntity);
    this.statusEffectInstance = statusEffectInstance;
  }

  /** {@inheritDoc} */
  @Override
  public StatusEffectInstance getStatusEffectInstance() {
    return this.statusEffectInstance;
  }
}
