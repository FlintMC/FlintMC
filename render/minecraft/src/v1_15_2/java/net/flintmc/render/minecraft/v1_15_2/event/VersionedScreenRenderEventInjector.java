package net.flintmc.render.minecraft.v1_15_2.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.stereotype.type.Type;
import net.flintmc.render.minecraft.event.ScreenRenderEvent;
import net.flintmc.transform.hook.Hook;

@Singleton
public class VersionedScreenRenderEventInjector {

  private final EventBus eventBus;
  private final ScreenRenderEvent event;

  @Inject
  private VersionedScreenRenderEventInjector(EventBus eventBus) {
    this.eventBus = eventBus;
    this.event = new ScreenRenderEvent() {};
  }

  @Hook(
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "updateCameraAndRender",
      parameters = {
        @Type(reference = float.class),
        @Type(reference = long.class),
        @Type(reference = boolean.class)
      },
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER})
  public void renderScreen(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.event, executionTime);
  }
}
