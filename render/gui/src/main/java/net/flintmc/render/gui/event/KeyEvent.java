package net.flintmc.render.gui.event;

import net.flintmc.render.gui.event.input.InputState;
import net.flintmc.render.gui.event.input.Key;
import net.flintmc.render.gui.event.input.ModifierKey;

import java.util.Set;

/** Event indicating that a key state has changed or is still being hold. */
public class KeyEvent extends EventWithModifierKeys {
  private final Key key;
  private final int scancode;
  private final InputState state;

  /**
   * Constructs a new {@link KeyEvent} with the given key, scancode, state and modifier keys.
   *
   * @param key The key that has changed state
   * @param scancode The (system specific) scancode of the key that has changed
   * @param state The new state of the key
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public KeyEvent(Key key, int scancode, InputState state, Set<ModifierKey> modifierKeys) {
    super(modifierKeys);
    this.key = key;
    this.scancode = scancode;
    this.state = state;
  }

  /**
   * Retrieves the key that has changed state.
   *
   * @return The key that has changed state
   */
  public Key getKey() {
    return key;
  }

  /**
   * Retrieves the (system specific) scancode of the key that has changed state.
   *
   * @return The scancode of the key that has changed state
   */
  public int getScancode() {
    return scancode;
  }

  /**
   * Retrieves the new state of the key.
   *
   * @return The new state of the key
   */
  public InputState getState() {
    return state;
  }
}
