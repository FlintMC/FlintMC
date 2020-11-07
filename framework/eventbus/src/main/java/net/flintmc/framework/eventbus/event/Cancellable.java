package net.flintmc.framework.eventbus.event;

/** Represents a cancellable event. */
public interface Cancellable {

  /**
   * Whether the event is cancelled.
   *
   * @return {@code true} if the event has been cancelled, otherwise {@code false}.
   */
  boolean isCancelled();

  /**
   * Changes the cancelled state of the event.
   *
   * @param cancelled {@code true} if the even should be cancelled, otherwise {@code false}.
   */
  void setCancelled(boolean cancelled);
}
