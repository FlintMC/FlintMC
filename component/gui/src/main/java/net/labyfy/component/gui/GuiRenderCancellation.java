package net.labyfy.component.gui;

public class GuiRenderCancellation {
  private boolean shouldCancel;

  public GuiRenderCancellation() {}

  public void cancel() {
    shouldCancel = true;
  }

  public boolean shouldCancel() {
    return shouldCancel;
  }
}
