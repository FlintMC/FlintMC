package net.flintmc.mcapi.entity.event;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.mcapi.entity.Entity;

public interface EntityAttackEvent extends Event {

  Entity getEntity();
}
