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

package net.flintmc.mcapi.internal.chat.component.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.event.ClickActionExecutor;
import net.flintmc.mcapi.chat.component.event.ClickEvent;

@Implement(ClickEvent.class)
public class DefaultClickEvent implements ClickEvent {

  private final ClickActionExecutor actionExecutor;

  private final Action action;
  private final String value;

  @AssistedInject
  private DefaultClickEvent(
      ClickActionExecutor actionExecutor, @Assisted Action action, @Assisted String value) {
    this.actionExecutor = actionExecutor;
    this.action = action;
    this.value = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Action getAction() {
    return this.action;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getValue() {
    return this.value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() {
    this.actionExecutor.executeEvent(this);
  }

  @AssistedFactory(DefaultClickEvent.class)
  interface InternalFactory {

    DefaultClickEvent create(@Assisted Action action, @Assisted String value);
  }
}
