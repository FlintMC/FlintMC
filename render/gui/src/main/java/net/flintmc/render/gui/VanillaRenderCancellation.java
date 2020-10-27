package net.flintmc.render.gui;

/**
 * Represents a cancellable execution of rendering.
 */
public class VanillaRenderCancellation {
  private boolean shouldCancel;

  /**
   * Constructs a new {@link VanillaRenderCancellation} which has not been cancelled.
   */
  public VanillaRenderCancellation() {
    this.shouldCancel = false;
  }

  /**
   * Constructs a new {@link VanillaRenderCancellation} which cancellation depends on the given parameter.
   *
   * @param isCancelled Whether the execution has been cancelled already
   */
  public VanillaRenderCancellation(boolean isCancelled) {
    this.shouldCancel = isCancelled;
  }

  /**
   * Signal the cancellation
   */
  public void cancel() {
    this.shouldCancel = true;
  }

  /**
   * Determines whether the cancellation has been signaled
   *
   * @return {@code true} if the cancellation has been signaled, {@code false} otherwise
   */
  public boolean isCancelled() {
    return shouldCancel;
  }
}
