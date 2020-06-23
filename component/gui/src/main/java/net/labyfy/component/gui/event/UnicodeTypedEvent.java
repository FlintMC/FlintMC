package net.labyfy.component.gui.event;

/**
 * Event indicating that the user has typed input
 */
public class UnicodeTypedEvent implements GuiInputEvent {
  private final int value;

  /**
   * Constructs a new {@link UnicodeTypedEvent} with the given code point
   *
   * @param value The unicode code point the user has typed
   */
  public UnicodeTypedEvent(int value) {
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
