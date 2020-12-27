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

package net.flintmc.framework.eventbus.event;

import java.util.Collection;
import java.util.List;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.eventbus.method.SubscribeMethod;

/**
 * Represents an event. All other events must implement this Interface and have the {@link
 * Subscribable} annotation.
 *
 * @see Subscribe
 */
public interface Event {

  /**
   * Retrieves a list of all methods that are subscribed to this event. This method shouldn't be
   * implemented by any implementation of this interface because it will be generated
   * automatically.
   *
   * @return An immutable list of methods that are subscribed to this event
   */
  default List<SubscribeMethod> getMethods() {
    throw new UnsupportedOperationException(
        "Missing @Subscribable on " + this.getClass().getName());
  }

  /**
   * Retrieves a list of all phases that are supported by this event that have been declared in the
   * {@link Subscribable} annotation of this event. This method shouldn't be implemented by any
   * implementation of this interface because it will be generated automatically.
   *
   * @return An immutable list of phases that are supported by this event
   */
  default Collection<Phase> getSupportedPhases() {
    throw new UnsupportedOperationException(
        "Missing @Subscribable on " + this.getClass().getName());
  }

}
