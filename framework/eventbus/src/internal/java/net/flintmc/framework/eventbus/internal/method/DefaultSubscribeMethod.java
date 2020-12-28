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

package net.flintmc.framework.eventbus.internal.method;

import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.EventExecutor;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

/** A subscribed method in an {@link EventBus}. */
@Implement(SubscribeMethod.class)
public class DefaultSubscribeMethod implements SubscribeMethod {

  private final Class<? extends Event> eventClass;
  private final byte priority;
  private final Subscribe.Phase phase;
  private final EventExecutor executor;

  @AssistedInject
  private DefaultSubscribeMethod(
      @Assisted Class<? extends Event> eventClass,
      @Assisted byte priority,
      @Assisted Subscribe.Phase phase,
      @Assisted EventExecutor<?> executor) {
    this.eventClass = eventClass;
    this.priority = priority;
    this.phase = phase;
    this.executor = executor;
  }

  /** {@inheritDoc} */
  @Override
  public Class<? extends Event> getEventClass() {
    return this.eventClass;
  }

  /** {@inheritDoc} */
  @Override
  public byte getPriority() {
    return this.priority;
  }

  /** {@inheritDoc} */
  @Override
  public Subscribe.Phase getPhase() {
    return this.phase;
  }

  /** {@inheritDoc} */
  @Override
  public void invoke(Event event, Subscribe.Phase phase) throws Throwable {
    this.executor.invoke(event, phase, this);
  }
}
