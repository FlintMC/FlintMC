package net.labyfy.component.transform.launchplugin;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.inject.InjectionService;
import net.labyfy.component.inject.primitive.InjectionHolder;
import net.labyfy.component.launcher.LaunchController;
import net.labyfy.component.processing.autoload.AnnotationMeta;
import net.labyfy.component.processing.autoload.DetectableAnnotationProvider;
import net.labyfy.component.service.ExtendedServiceLoader;
import net.labyfy.component.stereotype.service.*;
import net.labyfy.component.transform.launchplugin.inject.module.BindConstantModule;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.*;

@Singleton
public class LabyfyFrameworkInitializer {


  private final Map<CtClass, ServiceHandler> serviceHandlerInstances = new HashMap<>();
  private final Collection<Pair<AnnotationMeta<?>, CtClass>> discoveredMeta = new HashSet<>();


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
      LaunchController.getInstance().getRootLoader().excludeFromModification("net.labyfy.internal.component.transform.");

      //create guice constant module
      InjectionHolder.getInstance().addModules(new BindConstantModule(arguments));
      //Apply module and instantiate service repository
      ServiceRepository serviceRepository = InjectionHolder.getInjectedInstance(ServiceRepository.class);

      //Find all services on the classpath and register them to the service repository
      this.prepareServices(serviceRepository);
      //Flush all registered services that are defined in the PRE_INIT state. This should only be done if it is really necessary.
      this.flushServices(Service.State.PRE_INIT, serviceRepository);
      //Apply all Implementations and AssistedFactories
      InjectionHolder.getInjectedInstance(InjectionService.class).flush();
      //Flush all other higher level framework features like Events, Transforms etc.
      this.flushServices(Service.State.POST_INIT, serviceRepository);

    } catch (NotFoundException e) {
      e.printStackTrace();
    }

  }

  private void flushServices(Service.State state, ServiceRepository serviceRepository) {
    List<Pair<Service, CtClass>> services = new ArrayList<>();
    for (Pair<Service, CtClass> value : serviceRepository.getServiceHandlers().values()) {
      if (!value.getFirst().state().equals(state)) continue;
      services.add(value);
    }

    services.sort(Comparator.comparingInt(pair -> pair.getFirst().priority()));

    for (Pair<Service, CtClass> pair : services) {
      Service service = pair.getFirst();
      for (Class<? extends Annotation> annotationType : service.value()) {
        for (AnnotationMeta<?> annotationMeta : serviceRepository.getAnnotations().get(annotationType)) {
          try {
            Pair<AnnotationMeta<?>, CtClass> serviceMetaPair = new Pair<>(annotationMeta, pair.getSecond());
            if (discoveredMeta.contains(serviceMetaPair)) continue;
            discoveredMeta.add(serviceMetaPair);
            if (!serviceHandlerInstances.containsKey(pair.getSecond())) {
              serviceHandlerInstances.put(pair.getSecond(), InjectionHolder.getInjectedInstance(CtResolver.get(pair.getSecond())));
            }
            if(!pair.getSecond().isFrozen()){
              pair.getSecond().freeze();
            }
            serviceHandlerInstances.get(pair.getSecond()).discover(annotationMeta);
            pair.getSecond().defrost();

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

  }

  private void prepareServices(ServiceRepository serviceRepository) throws NotFoundException {
    //Discover all annotation meta data
    List<AnnotationMeta> annotations = getAnnotationMeta();
    //Iterate over all annotations
    for (AnnotationMeta annotationMeta : annotations) {
      //check if metadata is a service
      if (annotationMeta.getAnnotation().annotationType().equals(Service.class)) {
        //if yes go ahead and register it
        serviceRepository.registerService((Service) annotationMeta.getAnnotation(), ClassPool.getDefault().get(((AnnotationMeta.ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
        //if not check if it might be multiple services at once. the Javapoet framework sadly seems to not support Repeatable annotations yet. Maybe this will change sometime.
      } else if (annotationMeta.getAnnotation().annotationType().equals(Services.class)) {
        //Iterate over all services and register them
        for (Service service : ((Services) annotationMeta.getAnnotation()).value()) {
          serviceRepository.registerService(service, ClassPool.getDefault().get(((AnnotationMeta.ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
        }
      }
    }

    //Iterate over all annotations again
    for (AnnotationMeta annotationMeta : annotations) {
      //register the annotation
      serviceRepository.registerAnnotation(
          annotationMeta
      );
    }
  }

  /**
   * @return all saved annotation meta that was written to the {@link DetectableAnnotationProvider} on compile time
   */
  private List<AnnotationMeta> getAnnotationMeta() {
    List<AnnotationMeta> annotationMetas = new ArrayList<>();

    Set<DetectableAnnotationProvider> discover = ExtendedServiceLoader.get(DetectableAnnotationProvider.class).discover(LaunchController.getInstance().getRootLoader());
    discover.forEach(detectableAnnotationProvider -> detectableAnnotationProvider.register(annotationMetas));
    return annotationMetas;
  }

}
