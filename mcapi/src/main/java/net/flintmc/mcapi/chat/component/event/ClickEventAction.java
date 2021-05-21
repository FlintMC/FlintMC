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

package net.flintmc.mcapi.chat.component.event;

import net.flintmc.mcapi.chat.component.event.ClickEvent.Action;
import net.flintmc.metaprogramming.DetectableAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a {@link ClickActionExecutor} to be automatically registered and used by {@link
 * ClickEvent#execute()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@DetectableAnnotation
public @interface ClickEventAction {

  /**
   * Retrieves the action for which the annotated class should be executed.
   *
   * @return The non-null action of the annotated executor
   */
  Action value();

  /**
   * Retrieves the priority of this action executor. Higher priorities will be preferred, but every
   * executor for the specific action will be executed.
   *
   * @return The priority, this can be any integer
   */
  int priority() default 1;
}
