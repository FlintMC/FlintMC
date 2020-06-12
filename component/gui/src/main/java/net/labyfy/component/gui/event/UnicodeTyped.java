package net.labyfy.component.gui.event;

public class UnicodeTyped implements GuiInputEvent<Character> {
  private final char value;

  public UnicodeTyped(char value) {
    this.value = value;
  }

  @Override
  public Character value() {
    return value;
  }
}
