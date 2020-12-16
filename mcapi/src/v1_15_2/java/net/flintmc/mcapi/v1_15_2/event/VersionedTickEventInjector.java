package net.flintmc.mcapi.v1_15_2.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.mcapi.event.TickEvent;
import net.flintmc.transform.hook.Hook;
import net.flintmc.transform.hook.Hook.ExecutionTime;

@Singleton
public class VersionedTickEventInjector {

  private final EventBus eventBus;
  private final TickEvent generalTickEvent;
  private final TickEvent gameRenderTickEvent;
  private final TickEvent worldRenderTickEvent;

  @Inject
  private VersionedTickEventInjector(EventBus eventBus, TickEvent.Factory factory) {
    this.eventBus = eventBus;
    this.generalTickEvent = factory.create(TickEvent.Type.GENERAL);
    this.gameRenderTickEvent = factory.create(TickEvent.Type.GAME_RENDER);
    this.worldRenderTickEvent = factory.create(TickEvent.Type.WORLD_RENDER);
  }

  @Hook(
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      className = "net.minecraft.client.Minecraft",
      methodName = "runTick")
  public void handleGeneralTick(ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.generalTickEvent, executionTime);
  }

  @Hook(
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "tick")
  public void handleGameRenderTick(ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.gameRenderTickEvent, executionTime);
  }

  @Hook(
      executionTime = {ExecutionTime.BEFORE, ExecutionTime.AFTER},
      className = "net.minecraft.client.renderer.WorldRenderer",
      methodName = "tick")
  public void hookWorldRenderTick(ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.worldRenderTickEvent, executionTime);
  }
}
