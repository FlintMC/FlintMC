package net.labyfy.component.gui.event;

import net.labyfy.component.gui.event.input.ModifierKey;

import java.util.Set;

/**
 * Event indicating that the user has typed input
 */
public class UnicodeTypedEvent extends EventWithModifierKeys {
  private final int value;

  /**
   * Constructs a new {@link UnicodeTypedEvent} with the given code point
   *
   * @param value The unicode code point the user has typed
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public UnicodeTypedEvent(int value, Set<ModifierKey> modifierKeys) {
    super(modifierKeys);
    this.value = value;
  }

  /**
   * Retrieves the code point the user has typed
   *
   * @return Code point the user has typed
   */
  public int getValue() {
    return value;
  }
}
