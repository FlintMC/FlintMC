package net.flintmc.mcapi.internal.potion.event;

import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.LivingEntity;
import net.flintmc.mcapi.potion.event.PotionEvent;

public class DefaultPotionEvent implements PotionEvent {

  private final Type type;
  private final LivingEntity livingEntity;

  protected DefaultPotionEvent(Type type, LivingEntity livingEntity) {
    this.type = type;
    this.livingEntity = livingEntity;
  }

  /** {@inheritDoc} */
  @Override
  public LivingEntity getLivingEntity() {
    return this.livingEntity;
  }

  /** {@inheritDoc} */
  @Override
  public Type getType() {
    return this.type;
  }
}
