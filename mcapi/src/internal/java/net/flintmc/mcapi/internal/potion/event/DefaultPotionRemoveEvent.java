package net.flintmc.mcapi.internal.potion.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.event.PotionRemoveEvent;

@Implement(PotionRemoveEvent.class)
public class DefaultPotionRemoveEvent extends DefaultPotionEvent implements PotionRemoveEvent {

  private final StatusEffect statusEffect;

  @AssistedInject
  private DefaultPotionRemoveEvent(
      @Assisted("livingEntity") LivingEntity livingEntity,
      @Assisted("statusEffect") StatusEffect statusEffect) {
    super(Type.REMOVE, livingEntity);
    this.statusEffect = statusEffect;
  }

  /** {@inheritDoc} */
  @Override
  public StatusEffect getStatusEffect() {
    return this.statusEffect;
  }
}
