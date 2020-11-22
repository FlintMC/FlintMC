package net.flintmc.render.gui.event;

import net.flintmc.render.gui.event.input.ModifierKey;
import net.flintmc.render.gui.windowing.Window;

import java.util.Set;

/** Event indicating that the user has typed input */
public class UnicodeTypedEvent extends EventWithModifierKeys {
  private final int codepoint;

  /**
   * Constructs a new {@link UnicodeTypedEvent} with the given code point
   *
   * @param window The non-null window where this event has happened
   * @param codepoint The unicode code point the user has typed
   * @param modifierKeys The modifier keys which were active while the event was fired
   */
  public UnicodeTypedEvent(Window window, int codepoint, Set<ModifierKey> modifierKeys) {
    super(window, modifierKeys);
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
