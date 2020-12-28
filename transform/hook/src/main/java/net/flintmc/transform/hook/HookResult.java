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

package net.flintmc.transform.hook;

import net.flintmc.transform.hook.Hook.ExecutionTime;

/**
 * Defines a specific result after a {@link Hook} has been called.
 *
 * @see Hook
 */
public enum HookResult {

  /**
   * Breaks further execution in the annotated method, this only works in {@link
   * ExecutionTime#BEFORE} since in {@link ExecutionTime#AFTER} there is nothing more to be broken.
   */
  BREAK,

  /**
   * Does nothing more after calling the method in the hook. Same result as if {@code null} is
   * returned by the hook and the return type is not {@link HookResult}.
   */
  CONTINUE
}
