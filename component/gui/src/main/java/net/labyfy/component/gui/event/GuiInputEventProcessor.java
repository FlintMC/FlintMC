package net.labyfy.component.gui.event;

public interface GuiInputEventProcessor {
  void beginInput();
  void process(GuiInputEvent<?> event);
  void endInput();
}
