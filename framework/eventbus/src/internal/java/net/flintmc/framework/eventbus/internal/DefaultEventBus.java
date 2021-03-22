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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.Cancellable;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.EventDetails;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe.Phase;
import net.flintmc.framework.eventbus.internal.method.handler.EventMethodRegistry;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.logging.InjectLogger;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Default implementation of the {@link EventBus}.
 */
@Singleton
@Implement(EventBus.class)
public class DefaultEventBus implements EventBus {

  private final Logger logger;
  private final EventMethodRegistry registry;

  @Inject
  private DefaultEventBus(@InjectLogger Logger logger, EventMethodRegistry registry) {
    this.logger = logger;
    this.registry = registry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Event> E fireEventAll(E event, Consumer<E> eventHandler) {
    this.fireEvent(event, Phase.PRE);
    if (event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
      return event;
    }

    eventHandler.accept(event);
    this.fireEvent(event, Phase.POST);

    return event;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Event> E fireEventAll(E event, Runnable handler) {
    return this.fireEventAll(event, e -> handler.run());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <E extends Event> E fireEvent(E event, Phase phase) {
    this.validateEvent(event, phase);

    this.postEvent(event, phase);
    return event;
  }

  private void validateEvent(Event event, Phase phase) {
    if (event == null) {
      throw new NullPointerException("An error is occurred because the event is null");
    }
    if (!(event instanceof EventDetails)) {
      throw new IllegalStateException(
          "Missing @Subscribable on " + event.getClass().getName());
    }

    EventDetails details = (EventDetails) event;

    try {
      details.getSupportedPhases();
    } catch (IncompatibleClassChangeError error) {
      throw new IllegalStateException(
          "You can only fire events of a type that is either annotated with "
              + "@Subscribable or implements/extends EXACTLY ONE interface/class "
              + "that is annotated with @Subscribable",
          error);
    }

    if (phase == Phase.ANY && !details.getSupportedPhases().isEmpty()) {
      return;
    }

    if (!details.getSupportedPhases().contains(phase)
        && !details.getSupportedPhases().contains(Phase.ANY)) {
      throw new IllegalArgumentException(
          String.format("The event %s doesn't support the phase %s, it only supports %s",
              details.getEventClass().getName(), phase.name(),
              details.getSupportedPhases().stream()
                  .map(Enum::name)
                  .collect(Collectors.joining(", "))
          ));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerSubscribeMethod(SubscribeMethod method) {
    if (!method.getEventClass().isAnnotationPresent(Subscribable.class)) {
      throw new IllegalStateException(String.format(
          "Cannot register a SubscribeMethod to an event (%s) that "
              + "doesn't have the @Subscribable annotation",
          method.getEventClass().getName()
      ));
    }

    this.registry.registerForEvents(method);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterSubscribeMethod(SubscribeMethod method) {
    this.registry.forEachEvent((eventClass, methods) -> methods.remove(method));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregisterSubscribeMethods(Class<? extends Event> eventClass) {
    this.registry.getSubscribeMethods(eventClass).clear();
  }

  /**
   * Fires the given event.
   *
   * @param event The event to fire.
   * @param phase The phase when the event is fired.
   * @param <E>   The event type.
   */
  private <E extends Event> void postEvent(E event, Subscribe.Phase phase) {
    List<SubscribeMethod> methods = ((EventDetails) event).getMethods();
    if (methods.isEmpty()) {
      return;
    }

    for (SubscribeMethod method : methods) {
      if (method.getPhase() == Subscribe.Phase.ANY
          || phase == method.getPhase()
          || phase == Subscribe.Phase.ANY) {
        this.invokeMethod(event, phase, method);
      }
    }
  }

  /**
   * Invokes the subscribed method.
   *
   * @param event  The fired event.
   * @param method The subscribed method.
   * @param <E>    The event type.
   */
  private <E extends Event> void invokeMethod(
      E event, Subscribe.Phase phase, SubscribeMethod method) {
    try {
      method.invoke(event, phase);
    } catch (Throwable throwable) {
      this.logger.error(
          "Error while posting event " + ((EventDetails) event).getEventClass().getName(),
          throwable);
    }
  }
}
