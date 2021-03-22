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

package net.flintmc.framework.eventbus.event.subscribe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventDetails;
import net.flintmc.metaprogramming.DetectableAnnotation;

/**
 * Marks a class that implements an {@link Event} as such and must be applied on EVERY event that
 * may be fired.
 * <p>
 * If your event is an interface, this annotation has to be on the interface. It can also be on the
 * implementation, but this is optional UNLESS your implementation implements multiple interfaces
 * with this annotation. If this is the case, your implementation also requires this annotation to
 * uniquely identify it.
 *
 * @see Event
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface Subscribable {

  /**
   * Retrieves the phases in which this event can be fired.
   *
   * @return The array of phases that are supported by this event
   * @see EventDetails#getSupportedPhases()
   */
  Subscribe.Phase[] value();

}
