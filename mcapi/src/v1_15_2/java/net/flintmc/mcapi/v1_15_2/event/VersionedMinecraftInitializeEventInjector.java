package net.flintmc.mcapi.v1_15_2.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.mcapi.event.MinecraftInitializeEvent;
import net.flintmc.transform.hook.Hook;
import net.minecraft.client.GameConfiguration;

@Singleton
public class VersionedMinecraftInitializeEventInjector {

  private final EventBus eventBus;

  private final MinecraftInitializeEvent event;

  @Inject
  public VersionedMinecraftInitializeEventInjector(EventBus eventBus) {
    this.eventBus = eventBus;
    this.event = new MinecraftInitializeEvent() {
    };
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "<init>",
      parameters = {@Type(reference = GameConfiguration.class)},
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER})
  public void minecraftInitialize(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.event, executionTime);
  }
}
