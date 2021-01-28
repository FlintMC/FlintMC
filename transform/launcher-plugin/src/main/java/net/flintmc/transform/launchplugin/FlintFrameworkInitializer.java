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

package net.flintmc.transform.launchplugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.NotFoundException;
import net.flintmc.framework.inject.InjectionService;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.packages.PackageLoader;
import net.flintmc.framework.service.ExtendedServiceLoader;
import net.flintmc.framework.stereotype.service.Service;
import net.flintmc.framework.stereotype.service.ServiceRepository;
import net.flintmc.framework.stereotype.service.Services;
import net.flintmc.launcher.LaunchController;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.DetectableAnnotationProvider;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.flintmc.processing.autoload.identifier.MethodIdentifier;
import net.flintmc.transform.launchplugin.inject.module.BindConstantModule;
import net.flintmc.util.mappings.utils.RemappingMethodLocationResolver;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class FlintFrameworkInitializer {

  @Inject
  private FlintFrameworkInitializer() {
  }

  /**
   * Initializes all features of the Flint framework.
   *
   * @param arguments minecraft launch arguments
   */
  public void initialize(Map<String, String> arguments) {
    try {
      LaunchController.getInstance()
          .getRootLoader()
          .excludeFromModification(
              "net.flintmc.transform.",
              "net.flintmc.framework.config.internal.");

      // create guice constant module
      InjectionHolder.getInstance()
          .addModules(new BindConstantModule(arguments));
      // Apply module and instantiate service repository
      ServiceRepository serviceRepository =
          InjectionHolder.getInjectedInstance(ServiceRepository.class);

      // Find all services on the classpath and register them to the service repository
      this.prepareServices(serviceRepository);
      // Flush all registered services that are defined in the PRE_INIT state. This should only be
      // done if it is really necessary.
      serviceRepository.flushServices(Service.State.PRE_INIT);
      // Apply all Implementations and AssistedFactories
      InjectionService service = InjectionHolder
          .getInjectedInstance(InjectionService.class);
      service.flushImplementation();
      serviceRepository.flushServices(Service.State.AFTER_IMPLEMENT);
      service.flushAssistedFactory();

      // Flush all other higher level framework features like Events, Transforms etc.
      serviceRepository.flushServices(Service.State.POST_INIT);

      try {
        InjectionHolder.getInjectedInstance(PackageLoader.class).load();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (NotFoundException e) {
      e.printStackTrace();
    }
  }

  private void prepareServices(ServiceRepository serviceRepository)
      throws NotFoundException {
    // Discover all annotation meta data
    List<AnnotationMeta> annotations = getAnnotationMeta();
    // Iterate over all annotations
    for (AnnotationMeta<?> annotationMeta : annotations) {
      // check if metadata is a service
      if (annotationMeta.getAnnotation().annotationType()
          .equals(Service.class)) {
        // if yes go ahead and register it
        Service annotation = (Service) annotationMeta.getAnnotation();
        serviceRepository.registerService(
            annotation.value(),
            annotation.priority(),
            annotation.state(),
            ClassPool.getDefault()
                .get(((ClassIdentifier) (annotationMeta.getIdentifier()))
                    .getName()));
        // if not check if it might be multiple services at once. the Javapoet framework sadly seems
        // to not support Repeatable annotations yet. Maybe this will change sometime.
      } else if (annotationMeta.getAnnotation().annotationType()
          .equals(Services.class)) {
        // Iterate over all services and register them
        for (Service service : ((Services) annotationMeta.getAnnotation())
            .value()) {
          serviceRepository.registerService(
              service.value(),
              service.priority(),
              service.state(),
              ClassPool.getDefault()
                  .get(((ClassIdentifier) annotationMeta.getIdentifier())
                      .getName()));
        }
      }
    }

    // Iterate over all annotations again
    for (AnnotationMeta<?> annotationMeta : annotations) {
      // register the annotation
      serviceRepository.registerAnnotation(annotationMeta);
    }
  }

  /**
   * @return all saved annotation meta that was written to the {@link
   * DetectableAnnotationProvider} on compile time
   */
  private List<AnnotationMeta> getAnnotationMeta() {
    List<AnnotationMeta> annotationMetas = new ArrayList<>();

    Set<DetectableAnnotationProvider> discover =
        ExtendedServiceLoader.get(DetectableAnnotationProvider.class)
            .discover(LaunchController.getInstance().getRootLoader());

    discover.forEach(
        detectableAnnotationProvider -> detectableAnnotationProvider
            .register(annotationMetas));

    RemappingMethodLocationResolver remappingMethodLocationResolver
        = new RemappingMethodLocationResolver();
    for (AnnotationMeta annotationMeta : annotationMetas) {
      if (annotationMeta.getElementKind() == ElementKind.METHOD) {
        MethodIdentifier id = annotationMeta.getMethodIdentifier();
        id.setMethodResolver(() -> remappingMethodLocationResolver
            .getLocation(id.getOwner(), id.getName(), id.getParameters()));
      }
    }
    return annotationMetas;
  }
}
