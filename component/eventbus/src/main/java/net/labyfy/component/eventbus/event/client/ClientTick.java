package net.labyfy.component.eventbus.event.client;

public class ClientTick extends TickEvent {

  public ClientTick() {
    super(Type.CLIENT);
  }
}
