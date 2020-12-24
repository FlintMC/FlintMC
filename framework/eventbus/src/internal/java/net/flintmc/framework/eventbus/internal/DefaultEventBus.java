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

package net.flintmc.framework.eventbus.internal;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Default implementation of the {@link EventBus}. */
@Singleton
@Implement(EventBus.class)
public class DefaultEventBus implements EventBus {

  private final Logger logger;
  private final Multimap<Class<? extends Event>, SubscribeMethod> subscribeMethods;

  @Inject
  private DefaultEventBus(@InjectLogger Logger logger) {
    this.logger = logger;
    this.subscribeMethods = HashMultimap.create();
  }

  /** {@inheritDoc} */
  @Override
  public <E extends Event> E fireEvent(E event, Subscribe.Phase phase) {
    if (event == null) {
      throw new NullPointerException("An error is occurred because the event is null");
    }

    this.postEvent(event, phase);
    return event;
  }

  /** {@inheritDoc} */
  @Override
  public Multimap<Class<? extends Event>, SubscribeMethod> getSubscribeMethods() {
    return this.subscribeMethods;
  }

  /** {@inheritDoc} */
  @Override
  public void registerSubscribeMethod(SubscribeMethod method) {
    this.subscribeMethods.put(method.getEventClass(), method);
  }

  /** {@inheritDoc} */
  @Override
  public void unregisterSubscribeMethod(SubscribeMethod method) {
    this.subscribeMethods.remove(method.getEventClass(), method);
  }

  /** {@inheritDoc} */
  @Override
  public void unregisterSubscribeMethods(Class<? extends Event> eventClass) {
    this.subscribeMethods.removeAll(eventClass);
  }

  /**
   * Finds all subscribed method that listen to the given event class.
   *
   * @param eventClass The event class to be searched
   * @return A collection with all subscribed method that listen to the given class.
   */
  @SuppressWarnings("unchecked")
  private List<SubscribeMethod> findMethods(Class<? extends Event> eventClass) {
    List<SubscribeMethod> methods = new ArrayList<>();
    Class<?> currentClass = eventClass;

    do {
      this.searchInterfaces((Class<? extends Event>) currentClass, methods);
    } while (Event.class.isAssignableFrom(currentClass = currentClass.getSuperclass()));

    methods.sort(Comparator.comparingInt(SubscribeMethod::getPriority));

    return methods;
  }

  private void searchInterfaces(
      Class<? extends Event> interfaceClass, List<SubscribeMethod> targetMethods) {
    if (Event.class.isAssignableFrom(interfaceClass)) {
      this.copyMethods(interfaceClass, targetMethods);

      for (Class<?> implemented : interfaceClass.getInterfaces()) {
        if (Event.class.isAssignableFrom(implemented)) {
          @SuppressWarnings("unchecked")
          Class<? extends Event> implementedEvent = (Class<? extends Event>) implemented;
          this.searchInterfaces(implementedEvent, targetMethods);
        }
      }
    }
  }

  private void copyMethods(Class<? extends Event> eventClass, List<SubscribeMethod> targetMethods) {
    for (SubscribeMethod subscribeMethod : this.subscribeMethods.get(eventClass)) {
      if (!targetMethods.contains(subscribeMethod)) {
        targetMethods.add(subscribeMethod);
      }
    }
  }

  /**
   * Fires the given event.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E> The event type.
   */
  private <E extends Event> void postEvent(E event, Subscribe.Phase phase) {
    List<SubscribeMethod> methods = this.findMethods(event.getClass());
    if (methods.isEmpty()) {
      return;
    }

    for (SubscribeMethod method : methods) {
      if (method.getPhase() == Subscribe.Phase.ANY || phase == method.getPhase() || phase == Subscribe.Phase.ANY) {
        this.fireLast(event, phase, method);
      }
    }
  }

  /**
   * Invokes the subscribed method.
   *
   * @param event The fired event.
   * @param method The subscribed method.
   * @param <E> The event type.
   */
  private <E extends Event> void fireLast(E event, Subscribe.Phase phase, SubscribeMethod method) {
    try {
      method.invoke(event, phase);
    } catch (Throwable throwable) {
      this.logger.error("Error while posting event " + event.getClass().getName(), throwable);
    }
  }
}
