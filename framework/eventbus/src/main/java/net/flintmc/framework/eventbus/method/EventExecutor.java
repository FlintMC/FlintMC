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

import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;

/**
 * An interface that can invoke a defined method on a listener object when an event is fired.
 *
 * @param <E> The type of the event that can be executed by this executor
 */
public interface EventExecutor<E extends Event> {

  /**
   * Invokes the appropriate method on the given listener to handle the event.
   *
   * @param event The event.
   * @param phase The phase in which the given event has been fired
   * @param holderMethod The subscribe method holding this executor
   * @throws Throwable If an exception occurred.
   */
  void invoke(E event, Subscribe.Phase phase, SubscribeMethod holderMethod) throws Throwable;
}
