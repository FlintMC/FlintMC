package net.labyfy.component.eventbus.event;

/**
 * Represents a cancellable event.
 */
public interface Cancellable {

  /**
   * Whether the event is cancelled.
   *
   * @return {@code true} if the event has been cancelled, otherwise {@code false}.
   */
  boolean cancelled();

  /**
   * Changes the cancelled state of the event.
   *
   * @param cancel {@code true} if the even should be cancelled, otherwise {@code false}.
   */
  void setCancelled(boolean cancel);

}
