package net.labyfy.component.resources.v1_15_1.pack;

import net.labyfy.component.inject.event.Event;
import net.labyfy.component.inject.event.EventService;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import net.labyfy.component.resources.pack.ResourcePackReloadEventBroadcaster;
import net.labyfy.component.transform.hook.Hook;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LabyResourcePackReloadEventBroadCaster implements ResourcePackReloadEventBroadcaster {

  private final ResourcePackReloadEvent.Factory resourcePackReloadEventFactory;
  private final EventService eventService;

  @Inject
  private LabyResourcePackReloadEventBroadCaster(
      ResourcePackReloadEvent.Factory resourcePackReloadEventFactory, EventService eventService) {
    this.resourcePackReloadEventFactory = resourcePackReloadEventFactory;
    this.eventService = eventService;
  }

  @Hook(
      className = "net.minecraft.resources.ResourcePackList",
      methodName = "reloadPacksFromFinders")
  public void broadcast() {
    this.eventService.broadcast(this.resourcePackReloadEventFactory.create());
  }

}
