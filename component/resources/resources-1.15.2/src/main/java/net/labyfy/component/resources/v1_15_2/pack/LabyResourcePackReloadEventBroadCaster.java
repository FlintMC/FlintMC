package net.labyfy.component.resources.v1_15_2.pack;

import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.component.inject.event.EventService;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import net.labyfy.component.resources.pack.ResourcePackReloadEventBroadcaster;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@AutoLoad
public class LabyResourcePackReloadEventBroadCaster implements ResourcePackReloadEventBroadcaster {

  private final ResourcePackReloadEvent.Factory resourcePackReloadEventFactory;
  private final EventService eventService;

  @Inject
  private LabyResourcePackReloadEventBroadCaster(
      ResourcePackReloadEvent.Factory resourcePackReloadEventFactory, EventService eventService) {
    this.resourcePackReloadEventFactory = resourcePackReloadEventFactory;
    this.eventService = eventService;
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  @TaskBody
  public void broadcast() {
    ((SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager())
        .addReloadListener(
            new IResourceManagerReloadListener() {
              public void onResourceManagerReload(IResourceManager iResourceManager) {
                eventService.broadcast(resourcePackReloadEventFactory.create());
              }
            });
  }
}
