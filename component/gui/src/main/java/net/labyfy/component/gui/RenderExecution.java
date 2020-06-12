package net.labyfy.component.gui;

public class RenderExecution {
  private final int mouseX;
  private final int mouseY;
  private final float partialTicks;
  private final VanillaRenderCancellation cancellation;

  public RenderExecution(int mouseX, int mouseY, float partialTicks) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation();
  }

  public RenderExecution(boolean isCancelled, int mouseX, int mouseY, float partialTicks) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation(isCancelled);
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }

  public float getPartialTicks() {
    return partialTicks;
  }

  public VanillaRenderCancellation getCancellation() {
    return cancellation;
  }
}
