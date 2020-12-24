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

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * A subscribed method in an {@link EventBus}.
 *
 * @see Subscribe
 */
public interface SubscribeMethod {

  /**
   * Retrieves the class of the event to which this method is subscribed.
   *
   * @return The non-null class of the event to which this method is subscribed
   */
  Class<? extends Event> getEventClass();

  /**
   * Retrieves the priority of the method. The lower the value, the earlier the event will be
   * called.
   *
   * @return The method priority.
   * @see EventPriority
   */
  byte getPriority();

  /**
   * Retrieves the phase of the subscribed method.
   *
   * @return The phase of teh subscribed method.
   */
  Subscribe.Phase getPhase();

  /**
   * Invokes this event subscriber. Called by the bus when a new event is fired to this subscriber.
   *
   * @param event The event that was fired.
   * @throws Throwable Any exception thrown during handling
   */
  void invoke(Event event, Subscribe.Phase phase) throws Throwable;

  /** A factory class for {@link SubscribeMethod}. */
  @AssistedFactory(SubscribeMethod.class)
  interface Factory {

    /**
     * Creates a new {@link SubscribeMethod} with the given parameters.
     *
     * @param eventClass The non-null class of the event to which this method is subscribed
     * @param priority The priority of the subscribed method.
     * @param phase The phase of the subscribed method.
     * @param executor The non-null supplier for the event executor.
     * @return A created subscribed method.
     */
    SubscribeMethod create(
        @Assisted Class<? extends Event> eventClass,
        @Assisted byte priority,
        @Assisted Subscribe.Phase phase,
        @Assisted EventExecutor<?> executor);
  }
}
