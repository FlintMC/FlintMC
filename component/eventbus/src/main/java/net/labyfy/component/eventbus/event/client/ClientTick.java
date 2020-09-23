package net.labyfy.component.eventbus.event.client;

import net.labyfy.component.inject.implement.Implement;

/**
 * @author Robby
 */
@Implement(TickEvent.class)
public class ClientTick extends TickEvent {

  public ClientTick() {
    super(Type.CLIENT);
  }
}
