package net.labyfy.component.gui.event;

public interface GuiInputEventProcessor {
  void beginInput();
  boolean process(GuiInputEvent event);
  void endInput();

  default void inputOnlyIterationDone() {}
}
