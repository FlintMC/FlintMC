package net.labyfy.internal.component.resources.v1_15_2.pack;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.resources.pack.ResourcePackReloadEvent;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.resources.SimpleReloadableResourceManager;

@Singleton
@AutoLoad
public class DefaultResourcePackReloadEventBroadCaster {

  private final EventBus eventBus;
  private final ResourcePackReloadEvent resourcePackReloadEvent;

  @Inject
  public DefaultResourcePackReloadEventBroadCaster(EventBus eventBus, ResourcePackReloadEvent resourcePackReloadEvent) {
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
