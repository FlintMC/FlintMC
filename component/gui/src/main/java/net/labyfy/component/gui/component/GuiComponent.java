package net.labyfy.component.gui.component;

import net.labyfy.component.gui.RenderExecution;
import net.labyfy.component.gui.screen.ScreenName;
import net.labyfy.component.transform.hook.Hook;

/**
 * Represents a GUI component. As a package developer, you usually don't want to use this directly, but rather
 * the JuklearMC components.
 */
public interface GuiComponent {
  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} when the minecraft screen changes.
   *
   * @param newScreen The new screen's name
   */
  void screenChanged(ScreenName newScreen);

  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} to determine if the components
   * {@link #render(RenderExecution)} method should be called.
   *
   * @param executionTime The time the hook is being executed
   * @param execution     The current execution, may be used for cancellation and such
   * @return {@code true} when the {@link #render(RenderExecution)} method should be called, {@code false} otherwise
   */
  boolean shouldRender(Hook.ExecutionTime executionTime, RenderExecution execution);

  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} to signal that the component
   * should be drawn now.
   *
   * @param execution The current execution, may be used for cancellation and such
   */
  void render(RenderExecution execution);

  /**
   * Called by the {@link net.labyfy.component.gui.GuiController} to signal that a frame has completed.
   * Can be used for state reset and similar tasks.
   */
  default void frameDone() {
  }

  ;
}
