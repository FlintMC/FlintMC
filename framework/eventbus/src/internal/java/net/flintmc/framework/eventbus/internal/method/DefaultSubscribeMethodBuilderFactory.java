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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;
import net.flintmc.framework.inject.implement.Implement;

@Singleton
@Implement(SubscribeMethodBuilder.Factory.class)
public class DefaultSubscribeMethodBuilderFactory implements SubscribeMethodBuilder.Factory {

  private final EventBus eventBus;
  private final SubscribeMethod.Factory methodFactory;

  @Inject
  private DefaultSubscribeMethodBuilderFactory(EventBus eventBus, SubscribeMethod.Factory methodFactory) {
    this.eventBus = eventBus;
    this.methodFactory = methodFactory;
  }

  /** {@inheritDoc} */
  @Override
  public <E extends Event> SubscribeMethodBuilder<E> newBuilder(Class<E> eventClass) {
    return new DefaultSubscribeMethodBuilder<>(this.methodFactory, this.eventBus, eventClass);
  }
}
