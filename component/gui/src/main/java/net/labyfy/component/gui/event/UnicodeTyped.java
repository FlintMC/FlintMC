package net.labyfy.component.gui.event;

public class UnicodeTyped implements GuiInputEvent {
  private final int value;

  public UnicodeTyped(int value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
