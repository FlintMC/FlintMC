package net.flintmc.render.gui;

/** Represents an execution of a rendering cycle. */
public class RenderExecution {
  private final float partialTicks;
  private final VanillaRenderCancellation cancellation;

  /**
   * Constructs a new {@link RenderExecution} with the given mouse x and y coordinates, the current
   * partial ticks value and an internal cancellation.
   *
   * @param partialTicks The current partial tick counter
   */
  public RenderExecution(float partialTicks) {
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation();
  }

  /**
   * Constructs a new {@link RenderExecution} with a boolean determining the the cancellation is
   * canceled already, the given mouse x and y coordinate and the current partial ticks value.
   *
   * @param isCancelled Whether the execution has been cancelled already
   * @param partialTicks The current partial tick counter
   */
  public RenderExecution(boolean isCancelled, float partialTicks) {
    this.partialTicks = partialTicks;
    this.cancellation = new VanillaRenderCancellation(isCancelled);
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
