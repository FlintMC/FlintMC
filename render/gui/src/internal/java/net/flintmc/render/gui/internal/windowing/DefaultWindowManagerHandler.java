package net.flintmc.render.gui.internal.windowing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.flintmc.render.gui.windowing.MinecraftWindow;

@Singleton
public class DefaultWindowManagerHandler {

  private final DefaultWindowManager windowManager;

  @Inject
  public DefaultWindowManagerHandler(DefaultWindowManager windowManager) {
    this.windowManager = windowManager;
  }

  /** Registers the minecraft window after it has been initialized with OpenGL. */
  @Subscribe(phase = Subscribe.Phase.POST)
  private void postOpenGLInitialize(OpenGLInitializeEvent event) {
    this.windowManager.minecraftWindow = InjectionHolder.getInjectedInstance(MinecraftWindow.class);
    this.windowManager.registerWindow((InternalWindow) this.windowManager.minecraftWindow);
  }
}
