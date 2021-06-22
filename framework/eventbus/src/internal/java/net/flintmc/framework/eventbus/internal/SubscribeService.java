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
import java.lang.annotation.Annotation;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.eventbus.event.Event;
import net.flintmc.framework.eventbus.event.subscribe.PostSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.framework.eventbus.event.subscribe.Subscribable;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.eventbus.method.EventExecutor;
import net.flintmc.framework.eventbus.method.ExecutorFactory;
import net.flintmc.framework.eventbus.method.SubscribeMethodBuilder;
import net.flintmc.util.commons.javassist.CtResolver;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceHandler;
import net.flintmc.framework.stereotype.service.ServiceNotFoundException;
import net.flintmc.metaprogramming.AnnotationMeta;

/**
 * Service for sending events to receivers.
 */
@Singleton
@Service(
    value = {Subscribe.class, PreSubscribe.class, PostSubscribe.class},
    priority = -10000)
public class SubscribeService implements ServiceHandler<Annotation> {

  private final ExecutorFactory factory;
  private final CtClass eventInterface;
  private final SubscribeMethodBuilder.Factory methodBuilderFactory;

  @Inject
  private SubscribeService(
      ExecutorFactory executorFactory,
      ClassPool pool,
      SubscribeMethodBuilder.Factory methodBuilderFactory)
      throws NotFoundException {
    this.methodBuilderFactory = methodBuilderFactory;
    this.eventInterface = pool.get(Event.class.getName());
    this.factory = executorFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void discover(AnnotationMeta<Annotation> meta) throws ServiceNotFoundException {
    Annotation subscribe = meta.getAnnotation();
    CtMethod method = meta.getMethodIdentifier().getLocation();

    CtClass eventClass = null;
    try {
      for (CtClass parameter : method.getParameterTypes()) {
        if (parameter.subtypeOf(this.eventInterface)) {
          eventClass = parameter;
          break;
        }
      }

      if (eventClass == null) {
        throw new ServiceNotFoundException(
            String.format(
                "At least one parameter of an @%s method must implement %s which is missing at %s.%s()",
                meta.getAnnotation().annotationType().getSimpleName(),
                Event.class.getName(),
                method.getDeclaringClass().getName(),
                method.getName()));
      }
    } catch (NotFoundException e) {
      throw new ServiceNotFoundException("Failed to retrieve CtClass of parameter type.", e);
    }

    EventExecutor<?> executor;

    try {
      executor = this.factory.create(method);
    } catch (Throwable throwable) {
      throw new ServiceNotFoundException(
          "Encountered an exception while creating an event subscriber for method \""
              + method
              + "\"!",
          throwable);
    }

    byte priority;
    Subscribe.Phase phase;
    if (subscribe instanceof PreSubscribe) {
      priority = ((PreSubscribe) subscribe).priority();
      phase = Subscribe.Phase.PRE;
    } else if (subscribe instanceof PostSubscribe) {
      priority = ((PostSubscribe) subscribe).priority();
      phase = Subscribe.Phase.POST;
    } else if (subscribe instanceof Subscribe) {
      priority = ((Subscribe) subscribe).priority();
      phase = ((Subscribe) subscribe).value();
    } else {
      throw new ServiceNotFoundException(
          "Unknown subscribe annotation: " + subscribe.annotationType().getName());
    }

    if (!eventClass.hasAnnotation(Subscribable.class)) {
      throw new ServiceNotFoundException(
          String.format("Cannot register %s.%s to an event (%s) that "
                  + "doesn't have the @Subscribable annotation",
              method.getDeclaringClass().getName(), method.getName(), eventClass.getName()
          ));
    }

    SubscribeMethodBuilder builder =
        this.methodBuilderFactory
            .newBuilder(CtResolver.get(eventClass))
            .priority(priority)
            .phaseOnly(phase);

    builder.to(executor).buildAndRegister();
  }
}
