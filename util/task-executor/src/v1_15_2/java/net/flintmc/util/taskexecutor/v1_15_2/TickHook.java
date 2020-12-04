package net.flintmc.util.taskexecutor.v1_15_2;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.transform.hook.Hook;
import net.flintmc.util.taskexecutor.GameLoopEvent;
import net.flintmc.util.taskexecutor.TickEvent;

@Singleton
public class TickHook {

  private final EventBus eventBus;
  private final TickEvent tickEvent;
  private final GameLoopEvent gameLoopEvent;

  @Inject
  private TickHook(EventBus eventBus, TickEvent tickEvent, GameLoopEvent gameLoopEvent) {
    this.eventBus = eventBus;
    this.tickEvent = tickEvent;
    this.gameLoopEvent = gameLoopEvent;
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "runTick",
      version = "1.15.2",
      executionTime = Hook.ExecutionTime.BEFORE)
  public void beforeTick() {
    this.eventBus.fireEvent(this.tickEvent, Subscribe.Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "runTick",
      version = "1.15.2",
      executionTime = Hook.ExecutionTime.AFTER)
  public void afterTick() {
    this.eventBus.fireEvent(this.tickEvent, Subscribe.Phase.POST);
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "runGameLoop",
      parameters = {@Type(reference = boolean.class)},
      version = "1.15.2",
      executionTime = Hook.ExecutionTime.BEFORE)
  public void beforeGameLoop() {
    this.eventBus.fireEvent(this.gameLoopEvent, Subscribe.Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "runGameLoop",
      parameters = {@Type(reference = boolean.class)},
      version = "1.15.2",
      executionTime = Hook.ExecutionTime.AFTER)
  public void afterGameLoop() {
    this.eventBus.fireEvent(this.gameLoopEvent, Subscribe.Phase.POST);
  }
}
