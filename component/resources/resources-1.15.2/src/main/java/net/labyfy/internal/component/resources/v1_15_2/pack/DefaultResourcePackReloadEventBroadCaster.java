package net.labyfy.internal.component.resources.v1_15_2.pack;

import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.resources.ResourceLocation;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import net.labyfy.component.resources.pack.ResourcePackReloadEventBroadcaster;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.internal.component.inject.EventService;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 1.15.2 implementation of the {@link ResourcePackReloadEventBroadcaster}.
 *
 * @deprecated See {@link ResourcePackReloadEventBroadcaster}
 */
@Singleton
@AutoLoad
@Deprecated
@Implement(value = ResourceLocation.class, version = "1.15.2")
public class DefaultResourcePackReloadEventBroadCaster implements ResourcePackReloadEventBroadcaster {

  private final EventService eventService;

  @Inject
  private DefaultResourcePackReloadEventBroadCaster(EventService eventService) {
    this.eventService = eventService;
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void broadcast() {
    // Install a hook on the minecraft resource manager
    ((SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager())
        .addReloadListener(
            (IResourceManagerReloadListener) iResourceManager ->
                eventService.broadcast(new ResourcePackReloadEvent()));
  }
}
