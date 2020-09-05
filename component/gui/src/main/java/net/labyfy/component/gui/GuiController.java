package net.labyfy.component.gui;

import net.labyfy.component.gui.component.GuiComponent;
import net.labyfy.component.gui.event.GuiInputEventProcessor;
import net.labyfy.component.gui.screen.ScreenName;

/**
 * Helper class for maintaining state and control of the entire minecraft GUI.
 */
public interface GuiController {
  /**
   * Adds an input event processor to the chain of event processors.
   *
   * @param processor The processor to add
   */
  void registerInputProcessor(GuiInputEventProcessor processor);

  /**
   * Removes an input event processor from the chain of event processors.
   *
   * @param processor The processor to remove
   * @return {@code true} if the processor has been removed, {@code false} otherwise
   */
  boolean removeInputProcessor(GuiInputEventProcessor processor);

  /**
   * Register a GUI component which will then receive callbacks from the GUI event processor.
   * Some callbacks may be called immediately, such as {@link GuiComponent#screenChanged(ScreenName)}.
   *
   * @param component The component to register
   */
  void registerComponent(GuiComponent component);

  /**
   * Removes a GUI component which will then no longer receive callbacks from the GUI event processor.
   *
   * @param component The component to remove
   * @return {@code true} if the component has been removed, {@code false} otherwise
   */
  boolean removeComponent(GuiComponent component);
}
