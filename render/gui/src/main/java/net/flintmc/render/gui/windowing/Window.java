package net.flintmc.render.gui.windowing;

import com.google.inject.assistedinject.Assisted;
import net.flintmc.render.gui.event.GuiEvent;
import net.flintmc.render.gui.event.GuiEventListener;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * A generic, operating system window.
 */
public interface Window {
  /**
   * Retrieves the platform native handle.
   *
   * @return The platform native handle
   */
  long getHandle();

  /**
   * Retrieves the width of the window.
   *
   * @return The current width of the window
   */
  float getWidth();

  /**
   * Retrieves the height of the window.
   *
   * @return The current height of the window
   */
  float getHeight();

  /**
   * Retrieves the size of the window. This method should be preferred over 2 separate calls to {@link #getWidth()} and
   * {@link #getHeight()}, as implementations might be able to optimize this call into a single call into native code.
   *
   * @return The size of the window packaged as an array with the format {@code [width, height]}
   */
  float[] getSize();

  /**
   * Closes the window and disposed its resources.
   * <p>
   * Note that this might or might not take effect immediately, but the window should be considered invalid after this
   * method has been invoked!
   */
  void close();

  /**
   * Adds a renderer to this window and enables it. The renderer will be added to the end of the rendering chain.
   *
   * @param renderer The renderer to add to this window
   */
  void addRenderer(WindowRenderer renderer);

  /**
   * Disables and removes a renderer from this window.
   *
   * @param renderer The renderer to remove
   * @return {@code true} if the renderer had been added and was removed now, {@code false} otherwise
   */
  boolean removeRenderer(WindowRenderer renderer);

  /**
   * Inserts an event listener at the end of the listener chain of this window.
   *
   * @param listener The listener to insert
   */
  void addEventListener(GuiEventListener listener);

  /**
   * Removes an event listener from this window.
   *
   * @param listener The listener to remove
   * @return {@code true} if the listener had been added and was removed now, {@code false} otherwise
   */
  boolean removeEventListener(GuiEventListener listener);

  /**
   * Sends an event to this window.
   *
   * @param event The event to send
   * @return {@code true} if the event has been handled, {@code false} otherwise
   */
  boolean sendEvent(GuiEvent event);

  /**
   * Tests whether the window is currently focused.
   *
   * @return {@code true} if the window is focused currently, {@code false} otherwise
   */
  boolean isFocused();

  /**
   * Factory for {@link Window}s.
   */
  @AssistedFactory(Window.class)
  interface Factory {
    /**
     * Creates a new window and displays it. The OpenGL context of the window will a child context of the minecraft
     * context.
     *
     * @param title  The title of new the window
     * @param width  The width of new the window
     * @param height The height of the new window
     * @return The created window
     */
    Window create(@Assisted("title") String title, @Assisted("width") int width, @Assisted("height") int height);
  }
}
