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

import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.processing.autoload.DetectableAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event receiver. The method will then be invoked if the given event has been
 * fired. The method needs to declare at least one parameter which has to be an {@link Event}, the
 * other parameters can be anything from the Injector, {@link Phase} or {@link SubscribeMethod} to
 * get more information about the annotated method in the {@link EventBus}. The {@link Event} in the
 * parameters needs to have the {@link Subscribable} annotation directly (not through any
 * superclasses or interfaces).
 *
 * <p>Subscribe methods should be only used in classes annotated with {@link Singleton} and in
 * classes NOT annotated with {@link Service}.
 *
 * <p>For simpler modification of the {@link #phase()}, {@link PreSubscribe} and {@link
 * PostSubscribe} can also be used.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation
public @interface Subscribe {

  /**
   * Retrieves the priority of the subscribed method.
   *
   * @return The priority of this subscribed method.
   */
  byte priority() default EventPriority.NORMAL;

  /**
   * Retrieves the minecraft version where this event should be fired, for example "1.15.2". If it
   * is empty, it will work in every version.
   *
   * @return The version where this event should be available
   */
  String version() default "";

  /**
   * Retrieves the phase of the subscribed method.
   *
   * @return The subscribed method phase.
   */
  Phase phase() default Phase.PRE;

  /**
   * An enumeration representing all available phases.
   */
  enum Phase {

    /**
     * Subscribed methods with this phase will be fired with the {@link #PRE} and {@link #POST}
     * phases.
     */
    ANY,
    /**
     * Defines the fired event as pre/before.
     */
    PRE,
    /**
     * Defines the fired event as post/after.
     */
    POST
  }
}
