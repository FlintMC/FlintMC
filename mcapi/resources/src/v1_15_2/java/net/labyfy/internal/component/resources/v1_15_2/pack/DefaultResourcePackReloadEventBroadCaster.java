package net.labyfy.internal.component.resources.v1_15_2.pack;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;

@Singleton
public class DefaultResourcePackReloadEventBroadCaster {

  private final EventBus eventBus;
  private final ResourcePackReloadEvent resourcePackReloadEvent;

  @Inject
  private DefaultResourcePackReloadEventBroadCaster(EventBus eventBus, ResourcePackReloadEvent resourcePackReloadEvent) {
    this.eventBus = eventBus;
    this.resourcePackReloadEvent = resourcePackReloadEvent;
  }

  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  public void init() {
    // Install a hook on the minecraft resource manager
    ((SimpleReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(
        (IResourceManagerReloadListener) iResourceManager -> this.eventBus.fireEvent(this.resourcePackReloadEvent, Subscribe.Phase.POST)
    );
  }
}
