package net.labyfy.component.eventbus;

import net.labyfy.component.eventbus.event.Subscribe;
import net.labyfy.component.transform.hook.Hook;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an event bus.
 */
public interface EventBus {

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E>   The type of the fired event.
   * @return A {@link CompletableFuture} representing the fired event.
   */
  <E> CompletableFuture<E> fire(E event, Subscribe.Phase phase);

  /**
   * Fires the given event to the bus and thrown the result away.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E>   The type of the fired event.
   */
  default <E> void fireEventAndOblivion(E event, Subscribe.Phase phase) {
    this.fire(event, phase);
  }

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param executionTime The execution time of a hooked method.
   * @param <E>   The type of the fired event.
   * @return A {@link CompletableFuture} representing the fired event.
   */
  default <E> CompletableFuture<E> fireEvent(E event, Hook.ExecutionTime executionTime) {
    switch (executionTime) {
      case BEFORE:
        return this.fire(event, Subscribe.Phase.PRE);
      case AFTER:
        return this.fire(event, Subscribe.Phase.POST);
      default:
        throw new IllegalStateException("Unexpected value: " + executionTime);
    }
  }

  /**
   * Fires the given event to the bus and thrown the result away.
   *
   * @param event         The event to fire.
   * @param executionTime The execution time of a hooked method.
   * @param <E>           The type of the fired event.
   */
  default <E> void fireEventAndForgot(E event, Hook.ExecutionTime executionTime) {
    switch (executionTime) {
      case BEFORE:
        this.fire(event, Subscribe.Phase.PRE);
        break;
      case AFTER:
        this.fire(event, Subscribe.Phase.POST);
        break;
    }
  }


}
