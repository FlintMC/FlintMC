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

package net.flintmc.framework.eventbus.internal.method.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.method.SubscribeMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class EventMethodRegistry {

  private final Logger logger;
  private final Collection<EventMethodHandler> handlers;

  @Inject
  private EventMethodRegistry() {
    // Cannot inject the logger because this is used before the implementations
    // are bound to their interfaces

    this.logger = LogManager.getLogger(super.getClass());
    this.handlers = new ArrayList<>();
  }

  public List<SubscribeMethod> getSubscribeMethods(Class<? extends Event> eventClass) {
    for (EventMethodHandler handler : this.handlers) {
      List<SubscribeMethod> methods = handler.getMethods(eventClass.getName());
      if (methods != null) {
        return methods;
      }
    }

    return Collections.emptyList();
  }

  public void registerHandler(EventMethodHandler handler) {
    this.handlers.add(handler);
  }

  public void forEachEvent(BiConsumer<String, List<SubscribeMethod>> consumer) {
    for (EventMethodHandler handler : this.handlers) {
      handler.forEachEvent(consumer);
    }
  }

  public void registerForEvents(SubscribeMethod method) {
    this.forEachEvent((eventClass, methods) -> {
      try {
        if (method.getEventClass().isAssignableFrom(Class.forName(eventClass))) {
          methods.add(method);

          methods.sort(
              Collections.reverseOrder(Comparator.comparingInt(SubscribeMethod::getPriority)));
        }
      } catch (ClassNotFoundException exception) {
        this.logger
            .error("Class {} not found while registering a subscribe method for {}", eventClass,
                method.getEventClass().getName());
      }
    });
  }

}
