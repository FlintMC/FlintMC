package net.flintmc.render.gui.internal.windowing;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.render.gui.event.GuiEvent;
import net.flintmc.render.gui.windowing.MinecraftWindow;
import net.flintmc.render.gui.windowing.Window;
import net.flintmc.render.gui.windowing.WindowManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/** Default implementation of the Flint {@link WindowManager}. */
@Singleton
@Implement(WindowManager.class)
public class DefaultWindowManager implements WindowManager {
  private final Map<Long, InternalWindow> windows;

  private final EventBus eventBus;
  protected MinecraftWindow minecraftWindow;

  @Inject
  private DefaultWindowManager(EventBus eventBus) {
    this.eventBus = eventBus;

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
   * Retrieves the window with a specific handle that should be used for a {@link GuiEvent}.
   *
   * @param handle The handle of the window, or {@code -1} for the minecraft window
   * @return The window or {@code null} if there is no window with the given handle
   */
  public Window getTargetWindowForEvent(long handle) {
    return handle == -1 ? this.minecraftWindow : this.windows.get(handle);
  }

  /**
   * Fires an event and automatically select the target window based on the event source handle.
   *
   * @param windowHandle The handle of the window to, or {@code -1} for the minecraft window
   * @param eventFunction The function to create the event with the window for the given handle
   * @return {@code true} if the event has been handled, {@code false} otherwise
   */
  public boolean fireEvent(long windowHandle, Function<Window, GuiEvent> eventFunction) {
    Window window = this.getTargetWindowForEvent(windowHandle);
    if (window == null) {
      return false;
    }

    GuiEvent event = eventFunction.apply(window);
    return this.eventBus.fireEvent(event, Subscribe.Phase.PRE).isCancelled();
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
