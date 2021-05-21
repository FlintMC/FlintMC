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

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.mcapi.chat.component.event.ClickActionExecutor;
import net.flintmc.mcapi.chat.component.event.ClickEvent;
import net.flintmc.mcapi.chat.component.event.ClickEvent.Action;
import net.flintmc.mcapi.chat.component.event.ClickEventAction;
import net.flintmc.metaprogramming.AnnotationMeta;

@Singleton
@Service(ClickEventAction.class)
@Implement(ClickActionExecutor.class)
public class DefaultClickActionExecutorService
    implements ClickActionExecutor, ServiceHandler<ClickEventAction> {

  private final Map<Action, List<ExecutorContainer>> executors = new HashMap<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<ClickEventAction> meta) {
    ClickEventAction annotation = meta.getAnnotation();
    ClickActionExecutor executor = InjectionHolder
        .getInjectedInstance(CtResolver.get(meta.getClassIdentifier().getLocation()));

    List<ExecutorContainer> containers = this.executors
        .computeIfAbsent(annotation.value(), action -> new ArrayList<>());

    containers.add(new ExecutorContainer(executor, annotation));
    Collections.sort(containers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void executeEvent(ClickEvent event) {
    List<ExecutorContainer> containers =
        this.executors.getOrDefault(event.getAction(), Collections.emptyList());

    for (ExecutorContainer container : containers) {
      container.executor.executeEvent(event);
    }
  }

  private class ExecutorContainer implements Comparable<ExecutorContainer> {

    private final ClickActionExecutor executor;
    private final ClickEventAction annotation;

    private ExecutorContainer(ClickActionExecutor executor,
        ClickEventAction annotation) {
      this.executor = executor;
      this.annotation = annotation;
    }

    @Override
    public int compareTo(ExecutorContainer o) {
      // Reversed
      return Integer.compare(o.annotation.priority(), this.annotation.priority());
    }
  }
}
