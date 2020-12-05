package net.flintmc.render.minecraft.v1_15_2.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.render.minecraft.event.OpenGLInitializeEvent;
import net.flintmc.transform.hook.Hook;

@Singleton
public class VersionedOpenGLInitializeEventInjector {

  private final EventBus eventBus;

  private final OpenGLInitializeEvent event;

  @Inject
  public VersionedOpenGLInitializeEventInjector(EventBus eventBus) {
    this.eventBus = eventBus;
    this.event = new OpenGLInitializeEvent() {};
  }

  @Hook(className = "net.minecraft.client.Minecraft", methodName = "startTimerHackThread")
  public void preInitialize() {
    this.eventBus.fireEvent(this.event, Subscribe.Phase.PRE);
  }

  @Hook(className = "net.minecraft.client.MainWindow", methodName = "setLogOnGlError")
  public void postInitialize() {
    this.eventBus.fireEvent(this.event, Subscribe.Phase.POST);
  }
}
