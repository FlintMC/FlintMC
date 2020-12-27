/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.eventbus;

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;
import net.flintmc.transform.hook.Hook;

/**
 * The EventBus manages all methods with the {@link Subscribe} annotation and custom event handlers
 * in the project.
 */
public interface EventBus {

  /**
   * Fires the given event to the bus.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E>   The type of the fired event.
   * @return The input event
   * @throws IllegalArgumentException If the given phase is not supported by the given Event
   * @see Event#getSupportedPhases()
   */
  <E extends Event> E fireEvent(E event, Subscribe.Phase phase);

  /**
   * Fires the given event to the bus.
   *
   * @param event         The event to fire.
   * @param executionTime The execution time of a hooked method.
   * @param <E>           The type of the fired event.
   * @return The input event
   * @throws IllegalArgumentException If the given phase is not supported by the given Event
   * @see Event#getSupportedPhases()
   */
  default <E extends Event> E fireEvent(E event, Hook.ExecutionTime executionTime) {
    switch (executionTime) {
      case BEFORE:
        return this.fireEvent(event, Subscribe.Phase.PRE);
      case AFTER:
        return this.fireEvent(event, Subscribe.Phase.POST);
      default:
        throw new IllegalStateException("Unexpected value: " + executionTime);
    }
  }

  /**
   * Registers a new {@link SubscribeMethod} to this event bus, the executor in this method will be
   * fired whenever {@link #fireEvent(Event, Subscribe.Phase)} is called. A method may be registered
   * multiple times.
   *
   * @param method The non-null method to be registered
   * @see SubscribeMethodBuilder
   * @see #unregisterSubscribeMethod(SubscribeMethod)
   */
  void registerSubscribeMethod(SubscribeMethod method);

  /**
   * Unregisters a {@link SubscribeMethod} from this event bus that has previously been registered
   * via {@link #registerSubscribeMethod(SubscribeMethod)}. If the method is not registered, nothing
   * will happen.
   *
   * @param method The non-null method to be unregistered
   * @see #registerSubscribeMethod(SubscribeMethod)
   */
  void unregisterSubscribeMethod(SubscribeMethod method);

  /**
   * Unregisters all {@link SubscribeMethod}s that are subscribed to the given {@code eventClass}.
   * If there is no method with the event registered, nothing will happen.
   *
   * @param eventClass The non-null event to unregister every method for
   * @see #registerSubscribeMethod(SubscribeMethod)
   */
  void unregisterSubscribeMethods(Class<? extends Event> eventClass);
}
