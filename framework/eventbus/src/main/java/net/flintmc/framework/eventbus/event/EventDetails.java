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

import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.eventbus.method.SubscribeMethod;

import java.util.Collection;
import java.util.List;

/**
 * This class will automatically be added to classes/interfaces annotated with {@link Subscribable},
 * it should not be implemented manually.
 *
 * @see Subscribable
 * @see Event
 */
public interface EventDetails {

  /**
   * Retrieves a list of all methods that are subscribed to this event. This method shouldn't be
   * implemented by any implementation of this interface because it will be generated
   * automatically.
   *
   * @return An immutable list of methods that are subscribed to this event
   */
  List<SubscribeMethod> getMethods();

  /**
   * Retrieves a list of all phases that are supported by this event that have been declared in the
   * {@link Subscribable} annotation of this event. This method shouldn't be implemented by any
   * implementation of this interface because it will be generated automatically.
   *
   * @return An immutable list of phases that are supported by this event
   */
  Collection<Phase> getSupportedPhases();

  /**
   * Retrieves the class/interface that is annotated with {@link Subscribable}, this may not be the
   * same as the class of this instance. For example if the annotation is present on an interface
   * and its implementation is the actual event being fired.
   *
   * @return The non-null class
   */
  Class<? extends Event> getEventClass();

}
