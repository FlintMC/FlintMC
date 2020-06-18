package net.labyfy.component.gui;

public class VanillaRenderCancellation {
  private boolean shouldCancel;

  public VanillaRenderCancellation() {
    this.shouldCancel = false;
  }

  public VanillaRenderCancellation(boolean isCancelled) {
    this.shouldCancel = isCancelled;
  }

  public void cancel() {
    this.shouldCancel = true;
  }

  public boolean isCancelled() {
    return shouldCancel;
  }
}
