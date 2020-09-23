package net.labyfy.component.eventbus;

import net.labyfy.component.eventbus.event.Subscribe;

import java.util.concurrent.CompletableFuture;

/**
 * Represents an event bus.
 */
public interface EventBus {

  <E> CompletableFuture<E> fire(E event, Subscribe.Phase phase);

}
