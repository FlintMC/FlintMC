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

/**
 * Executor for a {@link ClickEvent}.
 * <p>
 * This interface should be used together with the {@link ClickEventAction} annotation to
 * automatically register it.
 */
public interface ClickActionExecutor {

  /**
   * Executes the given click event as if the user would have clicked on the component in the chat.
   *
   * @param event The non-null event to be executed
   */
  void executeEvent(ClickEvent event);

}
