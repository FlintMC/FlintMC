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

  public void initialize(Map<String, String> arguments) {
    try {
      InjectionHolder.getInstance().addModules(new BindConstantModule(arguments));
      ServiceRepository serviceRepository = InjectionHolder.getInjectedInstance(ServiceRepository.class);

      this.prepareServices(serviceRepository);
      this.flushServices(Service.State.PRE_INIT, serviceRepository);
      InjectionHolder.getInjectedInstance(InjectionService.class).flush();
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
        for (Pair<AnnotationMeta<?>, Object> annotationMetaObjectPair : serviceRepository.getAnnotations().get(annotationType)) {
          try {
            Pair<AnnotationMeta<?>, CtClass> serviceMetaPair = new Pair<>(annotationMetaObjectPair.getFirst(), pair.getSecond());
            if (discoveredMeta.contains(serviceMetaPair)) continue;
            discoveredMeta.add(serviceMetaPair);
            if (!serviceHandlerInstances.containsKey(pair.getSecond())) {
              serviceHandlerInstances.put(pair.getSecond(), InjectionHolder.getInjectedInstance(CtResolver.get(pair.getSecond())));
            }
            serviceHandlerInstances.get(pair.getSecond()).discover(annotationMetaObjectPair.getFirst());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }

  }

  private void prepareServices(ServiceRepository serviceRepository) throws NotFoundException {
    List<AnnotationMeta> annotations = getAnnotations();
    for (AnnotationMeta annotationMeta : annotations) {
      if (annotationMeta.getAnnotation().annotationType().equals(Service.class)) {
        serviceRepository.registerService((Service) annotationMeta.getAnnotation(), ClassPool.getDefault().get(((AnnotationMeta.ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
      } else if (annotationMeta.getAnnotation().annotationType().equals(Services.class)) {
        for (Service service : ((Services) annotationMeta.getAnnotation()).value()) {
          serviceRepository.registerService(service, ClassPool.getDefault().get(((AnnotationMeta.ClassIdentifier) (annotationMeta.getIdentifier())).getName()));
        }
      }
    }

    for (AnnotationMeta annotationMeta : annotations) {
      if (annotationMeta.getAnnotation().annotationType().equals(Service.class) || annotationMeta.getAnnotation().annotationType().equals(Services.class))
        continue;
      if (annotationMeta.getIdentifier() instanceof AnnotationMeta.ClassIdentifier) {
        serviceRepository.registerClassAnnotation(
            annotationMeta,
            ((AnnotationMeta.ClassIdentifier) (annotationMeta.getIdentifier())).getLocation()
        );
      } else if (annotationMeta.getIdentifier() instanceof AnnotationMeta.MethodIdentifier) {

        AnnotationMeta.MethodIdentifier identifier = (AnnotationMeta.MethodIdentifier) annotationMeta.getIdentifier();

        CtClass[] parameters = new CtClass[identifier.getParameters().length];
        for (int i = 0; i < parameters.length; i++) {
          parameters[i] = ClassPool.getDefault().get(identifier.getParameters()[i]);
        }
        serviceRepository.registerMethodAnnotation(annotationMeta, identifier.getLocation());
      }
    }
  }

  private List<AnnotationMeta> getAnnotations() {
    List<AnnotationMeta> annotationMetas = new ArrayList<>();

    Set<DetectableAnnotationProvider> discover = ExtendedServiceLoader.get(DetectableAnnotationProvider.class).discover(LaunchController.getInstance().getRootLoader());
    discover.forEach(detectableAnnotationProvider -> detectableAnnotationProvider.register(annotationMetas));
    return annotationMetas;
  }

}
