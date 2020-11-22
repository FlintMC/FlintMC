package net.flintmc.render.gui.event;

import net.flintmc.render.gui.input.ModifierKey;
import net.flintmc.render.gui.windowing.Window;

import java.util.Collections;
import java.util.Set;

/** Base class for events with key modifiers. */
public abstract class EventWithModifierKeys extends DefaultGuiEvent implements GuiEvent {
  protected final Set<ModifierKey> modifierKeys;

  /**
   * Constructs a new {@link EventWithModifierKeys} with the specified modifier keys active.
   *
   * @param window The non-null window where this event has happened
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  protected EventWithModifierKeys(Window window, Set<ModifierKey> modifierKeys) {
    super(window);
    this.modifierKeys = modifierKeys;
  }

  /**
   * Retrieves a set of modifier keys which were active while the event was fired.
   *
   * @return An immutable set of modifier keys active during the event
   */
  public final Set<ModifierKey> getModifierKeys() {
    return Collections.unmodifiableSet(modifierKeys);
  }

  /**
   * Determines whether the shift key was down while the event was fired.
   *
   * @return {@code true} if the shift key was down, {@code false} otherwise
   */
  public boolean isShiftDown() {
    return modifierKeys.contains(ModifierKey.SHIFT);
  }

  /**
   * Determines whether the control key was down while the event was fired.
   *
   * @return {@code true} if the control key was down, {@code false} otherwise
   */
  public boolean isControlDown() {
    return modifierKeys.contains(ModifierKey.CONTROL);
  }

  /**
   * Determines whether the alt key was down while the event was fired.
   *
   * @return {@code true} if the alt key was down, {@code false} otherwise
   */
  public boolean isAltDown() {
    return modifierKeys.contains(ModifierKey.ALT);
  }

  /**
   * Determines whether the super key was down while the event was fired.
   *
   * @return {@code true} if the super key was down, {@code false} otherwise
   */
  public boolean isSuperDown() {
    return modifierKeys.contains(ModifierKey.SUPER);
  }
}
