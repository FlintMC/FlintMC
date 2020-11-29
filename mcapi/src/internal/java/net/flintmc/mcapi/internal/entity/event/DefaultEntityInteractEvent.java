package net.flintmc.mcapi.internal.entity.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.entity.Entity;
import net.flintmc.mcapi.entity.event.EntityInteractEvent;
import net.flintmc.mcapi.items.inventory.player.PlayerHand;

@Implement(EntityInteractEvent.class)
public class DefaultEntityInteractEvent implements EntityInteractEvent {

  private final Entity interacted;
  private final PlayerHand hand;

  @AssistedInject
  public DefaultEntityInteractEvent(@Assisted Entity interacted, @Assisted PlayerHand hand) {
    this.interacted = interacted;
    this.hand = hand;
  }

  @Override
  public Entity getInteracted() {
    return this.interacted;
  }

  @Override
  public PlayerHand getHand() {
    return this.hand;
  }
}
