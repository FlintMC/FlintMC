package net.flintmc.mcapi.v1_15_2.world.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.world.event.WorldUnloadEvent;
import net.flintmc.mcapi.world.event.WorldUnloadEvent.Factory;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.gui.screen.Screen;

@Singleton
public class VersionedWorldUnloadEventInjector {

  private final EventBus eventBus;
  private final WorldUnloadEvent worldUnloadEvent;

  @Inject
  private VersionedWorldUnloadEventInjector(EventBus eventBus,
      Factory worldUnloadEventFactory) {
    this.eventBus = eventBus;
    this.worldUnloadEvent = worldUnloadEventFactory.create();
  }

  @Hook(className = "net.minecraft.client.Minecraft", methodName = "unloadWorld", parameters = {
      @Type(reference = Screen.class)})
  public void hookUnloadWorld(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.worldUnloadEvent, executionTime);
  }

}
