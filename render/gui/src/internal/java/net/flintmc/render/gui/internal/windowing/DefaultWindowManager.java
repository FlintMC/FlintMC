package net.flintmc.render.gui.internal.windowing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.tasks.Task;
import net.flintmc.framework.tasks.Tasks;
import net.flintmc.render.gui.event.GuiEvent;
import net.flintmc.render.gui.windowing.MinecraftWindow;
import net.flintmc.render.gui.windowing.Window;
import net.flintmc.render.gui.windowing.WindowManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Default implementation of the Labyfy {@link WindowManager}. */
@Singleton
@Implement(WindowManager.class)
public class DefaultWindowManager implements WindowManager {
  private final Map<Long, InternalWindow> windows;

  private MinecraftWindow minecraftWindow;

  @Inject
  private DefaultWindowManager() {
    this.windows = new HashMap<>();
  }

  @Override
  public Collection<Window> allWindows() {
    return Collections.unmodifiableCollection(windows.values());
  }

  /**
   * Registers a window with this window manager.
   *
   * @param window The window to register
   */
  public void registerWindow(InternalWindow window) {
    windows.put(window.getHandle(), window);
  }

  /**
   * Unregisters a window from this window manager.
   *
   * @param window The window to unregister
   */
  public void unregisterWindow(InternalWindow window) {
    windows.remove(window.getHandle(), window);
  }

  /**
   * Registers the minecraft window after it has been initialized with OpenGL.
   *
   * @param window The main minecraft window
   */
  @Task(Tasks.POST_OPEN_GL_INITIALIZE)
  private void postOpenGLInitialize(MinecraftWindow window) {
    this.minecraftWindow = window;
    registerWindow((InternalWindow) window);
  }

  /**
   * Fires an event and automatically select the target window based on the event source handle.
   *
   * @param targetHandle The handle of the window to, or {@code -1} for the minecraft window
   * @param event The event to fire
   * @return {@code true} if the event has been handled, {@code false} otherwise
   */
  public boolean fireEvent(long targetHandle, GuiEvent event) {
    Window window = targetHandle == -1 ? minecraftWindow : windows.get(targetHandle);
    return window != null && window.sendEvent(event);
  }

  /**
   * Determines whether the minecraft window is rendered intrusively.
   *
   * @return {@code true} if the window is rendered intrusively, {@code false} otherwise
   */
  public boolean isMinecraftWindowRenderedIntrusively() {
    return ((InternalWindow) minecraftWindow).isRenderedIntrusively();
  }

  /** Renders the minecraft window now. */
  public void renderMinecraftWindow() {
    ((InternalWindow) minecraftWindow).render();
  }

  /** Renders all windows except the minecraft window itself. */
  public void renderSideWindows() {
    for (InternalWindow window : windows.values()) {
      if (window == minecraftWindow) {
        // The minecraft window is rendered at another point, skip it
        continue;
      }

      window.render();
    }
  }
}
