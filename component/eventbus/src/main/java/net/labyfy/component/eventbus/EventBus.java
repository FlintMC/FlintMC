package net.labyfy.component.eventbus;

import com.google.inject.Key;
import net.labyfy.component.eventbus.event.Subscribe;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an event bus.
 */
public interface EventBus {

  <E> CompletableFuture<E> fire(E event, Subscribe.Phase phase);

  <E> CompletableFuture<E> fire(E event, Subscribe.Phase phase, Map<Key<?>, Object> customParameters);


}
