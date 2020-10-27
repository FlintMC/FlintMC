package net.labyfy.component.transform.launchplugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.ClassPool;
import javassist.NotFoundException;
import net.labyfy.component.inject.InjectionService;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.packages.PackageLoader;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.DetectableAnnotationProvider;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;
import net.labyfy.component.service.ExtendedServiceLoader;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceRepository;
import net.labyfy.component.stereotype.service.Services;
import net.labyfy.component.transform.launchplugin.inject.module.BindConstantModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class LabyfyFrameworkInitializer {

  @Inject
  private LabyfyFrameworkInitializer() {
  }

  /**
   * Initializes all features of the labyfy framework.
   *
   * @param arguments minecraft launch arguments
   */
  public void initialize(Map<String, String> arguments) {
    try {
      LaunchController.getInstance()
          .getRootLoader()
          .excludeFromModification("net.labyfy.internal.component.transform.");

      // create guice constant module
      InjectionHolder.getInstance().addModules(new BindConstantModule(arguments));
      // Apply module and instantiate service repository
      ServiceRepository serviceRepository =
          InjectionHolder.getInjectedInstance(ServiceRepository.class);

      // Find all services on the classpath and register them to the service repository
      this.prepareServices(serviceRepository);
      // Flush all registered services that are defined in the PRE_INIT state. This should only be
      // done if it is really necessary.
      serviceRepository.flushServices(Service.State.PRE_INIT);
      // Apply all Implementations and AssistedFactories
      InjectionHolder.getInjectedInstance(InjectionService.class).flush();
      // Flush all other higher level framework features like Events, Transforms etc.
      serviceRepository.flushServices(Service.State.POST_INIT);

      InjectionHolder.getInjectedInstance(PackageLoader.class).load();
    } catch (NotFoundException e) {
      e.printStackTrace();
    }
  }

  private void prepareServices(ServiceRepository serviceRepository) throws NotFoundException {
    // Discover all annotation meta data
    List<AnnotationMeta> annotations = getAnnotationMeta();
    // Iterate over all annotations
    for (AnnotationMeta annotationMeta : annotations) {
      // check if metadata is a service
      if (annotationMeta.getAnnotation().annotationType().equals(Service.class)) {
        // if yes go ahead and register it
        Service annotation = (Service) annotationMeta.getAnnotation();
        serviceRepository.registerService(
            annotation.value(),
            annotation.priority(),
            annotation.state(),
            ClassPool.getDefault()
                .get(((ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
        // if not check if it might be multiple services at once. the Javapoet framework sadly seems
        // to not support Repeatable annotations yet. Maybe this will change sometime.
      } else if (annotationMeta.getAnnotation().annotationType().equals(Services.class)) {
        // Iterate over all services and register them
        for (Service service : ((Services) annotationMeta.getAnnotation()).value()) {
          serviceRepository.registerService(
              service.value(),
              service.priority(),
              service.state(),
              ClassPool.getDefault()
                  .get(((ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
        }
      }
    }

    // Iterate over all annotations again
    for (AnnotationMeta annotationMeta : annotations) {
      // register the annotation
      serviceRepository.registerAnnotation(annotationMeta);
    }
  }

  /**
   * @return all saved annotation meta that was written to the {@link DetectableAnnotationProvider}
   *     on compile time
   */
  private List<AnnotationMeta> getAnnotationMeta() {
    List<AnnotationMeta> annotationMetas = new ArrayList<>();

    Set<DetectableAnnotationProvider> discover =
        ExtendedServiceLoader.get(DetectableAnnotationProvider.class)
            .discover(LaunchController.getInstance().getRootLoader());
    discover.forEach(
        detectableAnnotationProvider -> detectableAnnotationProvider.register(annotationMetas));
    return annotationMetas;
  }
}
