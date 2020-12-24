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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javassist.CtMethod;
import net.flintmc.framework.eventbus.method.EventExecutor;
import net.flintmc.framework.eventbus.method.ExecutorFactory;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.method.MethodInjector;
import net.flintmc.framework.stereotype.service.CtResolver;

/** An executor factory which uses the {@link MethodInjector} to create event executors. */
@Singleton
@Implement(ExecutorFactory.class)
public class InjectingExecutorFactory implements ExecutorFactory {

  private final MethodInjector.Factory injectorFactory;
  private final Map<Integer, EventExecutor<?>> executors;

  @Inject
  private InjectingExecutorFactory(MethodInjector.Factory injectorFactory) {
    this.injectorFactory = injectorFactory;
    this.executors = new ConcurrentHashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public EventExecutor<?> create(CtMethod targetMethod) {
    int hash = CtResolver.hash(targetMethod);
    if (this.executors.containsKey(hash)) {
      return this.executors.get(hash);
    }

    // we need another class because Guice cannot read the dependencies for generic parameter types
    GeneratedEventExecutor executor =
        this.injectorFactory.generate(targetMethod, GeneratedEventExecutor.class);
    EventExecutor<?> eventExecutor = executor::invoke;

    this.executors.put(hash, eventExecutor);

    return eventExecutor;
  }
}
