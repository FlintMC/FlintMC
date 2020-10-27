package net.flintmc.render.gui.event;

import net.flintmc.render.gui.event.input.ModifierKey;

import java.util.Set;

/**
 * Event indicating that the user has typed input
 */
public class UnicodeTypedEvent extends EventWithModifierKeys {
  private final int codepoint;

  /**
   * Constructs a new {@link UnicodeTypedEvent} with the given code point
   *
   * @param codepoint The unicode code point the user has typed
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public UnicodeTypedEvent(int codepoint, Set<ModifierKey> modifierKeys) {
    super(modifierKeys);
    this.codepoint = codepoint;
  }

  /**
   * Retrieves the code point the user has typed
   *
   * @return Code point the user has typed
   */
  public int getCodepoint() {
    return codepoint;
  }
}
