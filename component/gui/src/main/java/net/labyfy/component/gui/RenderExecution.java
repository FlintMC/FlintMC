package net.labyfy.component.gui;

/**
 * Represents an execution of a rendering cycle.
 */
public class RenderExecution {
  private final int mouseX;
  private final int mouseY;
  private final float partialTicks;
  private final VanillaRenderCancellation cancellation;

  /**
   * Constructs a new {@link RenderExecution} with the given mouse x and y coordinates,
   * the current partial ticks value and an internal cancellation.
   *
   * @param mouseX       The current mouse x coordinate
   * @param mouseY       The current mouse y coordinate
   * @param partialTicks The current partial tick counter
   */
  public RenderExecution(int mouseX, int mouseY, float partialTicks) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation();
  }

  /**
   * Constructs a new {@link RenderExecution} with a boolean determining the the cancellation is canceled already,
   * the given mouse x and y coordinate and the current partial ticks value.
   *
   * @param isCancelled  Wether the execution has been cancelled already
   * @param mouseX       The current mouse x coordinate
   * @param mouseY       The current mouse y coordinate
   * @param partialTicks The current partial tick counter
   */
  public RenderExecution(boolean isCancelled, int mouseX, int mouseY, float partialTicks) {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation(isCancelled);
  }

  /**
   * Retrieves the mouse x coordinate of the execution
   *
   * @return The mouse x coordinate
   */
  public int getMouseX() {
    return mouseX;
  }

  /**
   * Retrieves the mouse y coordinate of the execution
   *
   * @return The mouse y coordinate
   */
  public int getMouseY() {
    return mouseY;
  }

  /**
   * Retrieves the value of partial ticks at the start of the execution
   *
   * @return The partial ticks counter value
   */
  public float getPartialTicks() {
    return partialTicks;
  }

  /**
   * Retrieves the cancellation bound to this execution
   *
   * @return The cancellation bound to this execution
   */
  public VanillaRenderCancellation getCancellation() {
    return cancellation;
  }
}
