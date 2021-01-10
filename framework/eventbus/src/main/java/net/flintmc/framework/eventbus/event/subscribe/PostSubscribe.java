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

import net.flintmc.framework.eventbus.event.EventPriority;
import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A shortcut to {@code @Subscribe(phase = Subscribe.Phase.POST)}.
 *
 * @see Subscribe
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation
public @interface PostSubscribe {

  /**
   * Retrieves the priority of the subscribed method.
   *
   * @return The priority of this subscribed method.
   * @see Subscribe#priority()
   */
  byte priority() default EventPriority.NORMAL;

  /**
   * Retrieves the minecraft version where this event should be fired, for example "1.15.2". If it
   * is empty, it will work in every version.
   *
   * @return The version where this event should be available
   */
  String version() default "";
}
