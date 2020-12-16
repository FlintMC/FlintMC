package net.flintmc.mcapi.internal.potion.event;

import javax.annotation.Nullable;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.event.PotionUpdateEvent;

@Implement(PotionUpdateEvent.class)
public class DefaultPotionUpdateEvent implements PotionUpdateEvent {

  private final LivingEntity livingEntity;

  @AssistedInject
  public DefaultPotionUpdateEvent(@Assisted("livingEntity") @Nullable LivingEntity livingEntity) {
    this.livingEntity = livingEntity;
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getLivingEntity() {
    return this.livingEntity;
  }
}
