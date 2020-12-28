package net.flintmc.render.gui.v1_16_4.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
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

  @Hook(
      className = "net.minecraft.client.Minecraft",
      methodName = "isMultiplayerEnabled",
      version = "1.16.4")
  public void preInitialize() {
    this.eventBus.fireEvent(this.event, Subscribe.Phase.PRE);
  }

  @Hook(
      className = "net.minecraft.client.MainWindow",
      methodName = "setLogOnGlError",
      version = "1.16.4")
  public void postInitialize() {
    this.eventBus.fireEvent(this.event, Subscribe.Phase.POST);
  }
}
