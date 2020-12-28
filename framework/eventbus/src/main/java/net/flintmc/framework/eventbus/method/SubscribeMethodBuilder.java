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

package net.flintmc.framework.eventbus.method;

import java.util.function.Consumer;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

/**
 * Builder for {@link SubscribeMethod}s.
 *
 * <p>The only necessary value before {@link #build()} can be used is one of the {@code to}
 * methods.
 *
 * @param <E> The type of the event that is handled by the generating method.
 * @see Factory
 */
public interface SubscribeMethodBuilder<E extends Event> {

  /**
   * Sets the phase in which the built method will be fired by the event bus, defaults to {@link
   * Subscribe.Phase#ANY}.
   *
   * @param phase The new non-null phase for the method
   * @return this builder for chaining
   */
  SubscribeMethodBuilder<E> phaseOnly(Subscribe.Phase phase);

  /**
   * Sets the priority of the built method, defaults to {@link EventPriority#NORMAL}.
   *
   * @param priority The priority of the new method
   * @return this builder for chaining
   * @see EventPriority
   */
  SubscribeMethodBuilder<E> priority(byte priority);

  /**
   * Sets the executor that will be invoked when the event is fired. This overrides any other 'to'
   * method in this builder when called.
   *
   * @param executor The non-null executor to invoke
   * @return this builder for chaining
   */
  SubscribeMethodBuilder<E> to(Consumer<E> executor);

  /**
   * Sets the executor that will be invoked when the event is fired. This overrides any other 'to'
   * method in this builder when called.
   *
   * @param executor The non-null executor to invoke
   * @return this builder for chaining
   */
  SubscribeMethodBuilder<E> to(EventExecutor<E> executor);

  /**
   * Builds the method with all values that have been set in this builder.
   *
   * @return The new non-null {@link SubscribeMethod}
   * @throws NullPointerException If no executor with one of the 'to' methods has been set
   */
  SubscribeMethod build() throws NullPointerException;

  /**
   * Builds and registers the method in the {@link EventBus}.
   *
   * @throws NullPointerException If no executor with one of the 'to' methods has been set
   * @see #build()
   * @see EventBus#registerSubscribeMethod(SubscribeMethod)
   */
  void buildAndRegister() throws NullPointerException;

  /**
   * Factory for the {@link SubscribeMethodBuilder}.
   */
  interface Factory {

    /**
     * Creates a new {@link SubscribeMethodBuilder} for the given event.
     *
     * @param eventClass The non-null class of the event which the new builder is for
     * @param <E>        The type of the event which the new builder is for
     * @return The new non-null builder for the given event class
     */
    <E extends Event> SubscribeMethodBuilder<E> newBuilder(Class<E> eventClass);
  }
}
