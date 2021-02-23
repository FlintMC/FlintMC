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

package net.flintmc.framework.stereotype.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.ServiceHandlerMeta;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.DetectableAnnotation;
import net.flintmc.util.commons.Pair;

import java.lang.annotation.Annotation;
import java.util.*;

@Singleton
public class ServiceRepository {

  private final Multimap<Class<? extends Annotation>, ServiceHandlerMeta> serviceHandlers;
  private final Multimap<Class<? extends Annotation>, AnnotationMeta<?>> annotations;
  private final Map<CtClass, ServiceHandler> serviceHandlerInstances = new HashMap<>();
  private final Collection<Pair<AnnotationMeta<?>, CtClass>> discoveredMeta = new HashSet<>();

  @Inject
  private ServiceRepository() {
    this.serviceHandlers = HashMultimap.create();
    this.annotations = HashMultimap.create();
  }

  /**
   * Registers a service handler that can pickup {@link DetectableAnnotation}s.
   *
   * @param annotationTypes the annotation types the service should handle
   * @param priority the service priority. Lower priority is called first
   * @param state the initialization state of the service. see also {@link Service.State} for usage
   * @param serviceHandlerClass the service handler class to handle {@link DetectableAnnotation}s
   */
  public void registerService(
      Class<? extends Annotation>[] annotationTypes,
      int priority,
      Service.State state,
      CtClass serviceHandlerClass) {
    for (Class<? extends Annotation> annotationType : annotationTypes) {
      this.registerService(annotationType, priority, state, serviceHandlerClass);
    }
  }

  /**
   * Registers a service handler that can pickup {@link DetectableAnnotation}s.
   *
   * @param annotationType the annotation type the service should handle
   * @param priority the service priority. Lower priority is called first
   * @param state the initialization state of the service. see also {@link Service.State} for usage
   * @param serviceHandlerClass the service handler class to handle {@link DetectableAnnotation}s
   */
  public void registerService(
      Class<? extends Annotation> annotationType,
      int priority,
      Service.State state,
      CtClass serviceHandlerClass) {
    this.serviceHandlers.put(
        annotationType,
        ServiceHandlerMeta.create(annotationType, priority, state, serviceHandlerClass));
  }

  /**
   * Registers a {@link DetectableAnnotation} and provides ot to the service handlers.
   *
   * @param annotationMeta the annotation meta of the {@link DetectableAnnotation} to register.
   */
  public void registerAnnotation(AnnotationMeta<?> annotationMeta) {
    this.annotations.put(annotationMeta.getAnnotation().annotationType(), annotationMeta);
  }

  public void flushServices(Service.State state) {
    List<ServiceHandlerMeta> services = new ArrayList<>();
    for (ServiceHandlerMeta value : this.getServiceHandlers().values()) {
      if (!value.getState().equals(state)) continue;
      services.add(value);
    }

    services.sort(Comparator.comparingInt(ServiceHandlerMeta::getPriority));

    for (ServiceHandlerMeta serviceHandlerMeta : services) {
      CtClass serviceHandlerClass = serviceHandlerMeta.getServiceHandlerClass();
      for (Class<? extends Annotation> annotationType : serviceHandlerMeta.getAnnotationTypes()) {
        for (AnnotationMeta<?> annotationMeta : this.getAnnotations().get(annotationType)) {
          try {
            Pair<AnnotationMeta<?>, CtClass> serviceMetaPair =
                new Pair<>(annotationMeta, serviceHandlerClass);
            if (discoveredMeta.contains(serviceMetaPair)) continue;
            discoveredMeta.add(serviceMetaPair);
            if (!serviceHandlerInstances.containsKey(serviceHandlerClass)) {
              serviceHandlerInstances.put(
                  serviceHandlerClass,
                  InjectionHolder.getInjectedInstance(CtResolver.get(serviceHandlerClass)));
            }
            serviceHandlerInstances.get(serviceHandlerClass).discover(annotationMeta);

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

    for (ServiceHandler handler : serviceHandlerInstances.values()) {
      handler.postDiscover();
    }
  }

  /** @return all registered annotations */
  public Multimap<Class<? extends Annotation>, AnnotationMeta<?>> getAnnotations() {
    return HashMultimap.create(annotations);
  }

  /** @return all registered service handlers */
  public Multimap<Class<? extends Annotation>, ServiceHandlerMeta> getServiceHandlers() {
    return HashMultimap.create(serviceHandlers);
  }
}
