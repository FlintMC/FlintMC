package net.flintmc.render.gui.internal.windowing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.render.gui.event.OpenGLInitializeEvent;
import net.flintmc.render.gui.windowing.MinecraftWindow;

@Singleton
public class DefaultWindowManagerHandler {

  private final DefaultWindowManager windowManager;

  @Inject
  private DefaultWindowManagerHandler(DefaultWindowManager windowManager) {
    this.windowManager = windowManager;
  }

  /** Registers the minecraft window after it has been initialized with OpenGL. */
  @Subscribe(phase = Subscribe.Phase.POST)
  public void postOpenGLInitialize(MinecraftWindow window, OpenGLInitializeEvent event) {
    this.windowManager.minecraftWindow = window;
    this.windowManager.registerWindow((InternalWindow) window);
  }
}
