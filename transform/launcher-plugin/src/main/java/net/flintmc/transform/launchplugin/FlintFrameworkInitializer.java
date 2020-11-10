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
import net.flintmc.processing.autoload.identifier.ConstructorIdentifier;
import net.flintmc.processing.autoload.identifier.Identifier;
import net.flintmc.processing.autoload.identifier.MethodIdentifier;
import net.flintmc.transform.launchplugin.inject.module.BindConstantModule;
import net.flintmc.util.mappings.ClassMapping;
import net.flintmc.util.mappings.ClassMappingProvider;
import net.flintmc.util.mappings.MethodMapping;
import net.flintmc.util.mappings.utils.MappingUtils;

import java.util.*;

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
          .excludeFromModification("net.flintmc.transform.");

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
      InjectionService service = InjectionHolder.getInjectedInstance(InjectionService.class);
      service.flushImplementation();
      serviceRepository.flushServices(Service.State.AFTER_IMPLEMENT);
      service.flushAssistedFactory();

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
   * on compile time
   */
  private List<AnnotationMeta> getAnnotationMeta() {
    List<AnnotationMeta> annotationMetas = new ArrayList<>();

    Set<DetectableAnnotationProvider> discover =
        ExtendedServiceLoader.get(DetectableAnnotationProvider.class)
            .discover(LaunchController.getInstance().getRootLoader());

    discover.forEach(
        detectableAnnotationProvider -> detectableAnnotationProvider.register(annotationMetas));

    ClassMappingProvider classMappingProvider = InjectionHolder.getInjectedInstance(ClassMappingProvider.class);

    for (AnnotationMeta parent : annotationMetas) {
      for (AnnotationMeta annotationMeta : getAnnotationsRecursively(parent)) {
        if (annotationMeta.getIdentifier() instanceof MethodIdentifier) {
          annotationMeta.getMethodIdentifier()
              .setOwnerConverter((methodIdentifier, owner) -> {
                ClassMapping classMapping = classMappingProvider.get(owner);
                if (classMapping == null) return owner;
                return classMapping.getName();
              })
              .setParametersConverter((methodIdentifier, parameters) -> {
                String[] transformedParameterTypes = new String[parameters.length];
                for (int i = 0; i < transformedParameterTypes.length; i++) {
                  ClassMapping classMapping = classMappingProvider.get(parameters[i]);
                  if (classMapping == null) {
                    transformedParameterTypes[i] = parameters[i];
                    continue;
                  }
                  transformedParameterTypes[i] = classMapping.getName();
                }
                return transformedParameterTypes;
              })
              .setNameConverter((methodIdentifier, name) -> {
                ClassMapping classMapping = classMappingProvider.get(name);
                if (classMapping == null) return name;
                for (MethodMapping methodMapping : classMapping.getDeobfuscatedMethods().values()) {
                  if (methodMapping.getDeobfuscatedName().equals(name) || methodMapping.getObfuscatedName().equals(name)) {
                    return methodMapping.getName();
                  }
                }
                return name;
              });
        }
        if (annotationMeta.getIdentifier() instanceof ClassIdentifier) {
          annotationMeta.getClassIdentifier().setNameConverter(((classIdentifier, name) -> {
            ClassMapping classMapping = classMappingProvider.get(name);
            if (classMapping == null) return name;
            return classMapping.getName();
          }));
        }
        if (annotationMeta.getIdentifier() instanceof ConstructorIdentifier) {
          annotationMeta.getConstructorIdentifier()
              .setOwnerConverter((constructorIdentifier, name) -> {
                ClassMapping classMapping = classMappingProvider.get(name);
                if (classMapping == null) return name;
                return classMapping.getName();
              })
              .setParametersConverter((constructorIdentifier, parameters) -> {
                String[] transformedParameterTypes = new String[parameters.length];
                for (int i = 0; i < transformedParameterTypes.length; i++) {
                  ClassMapping classMapping = classMappingProvider.get(parameters[i]);
                  if (classMapping == null) {
                    transformedParameterTypes[i] = parameters[i];
                    continue;
                  }
                  transformedParameterTypes[i] = classMapping.getName();
                }
                return transformedParameterTypes;
              });
        }
      }
    }

    return annotationMetas;
  }

  private Collection<AnnotationMeta> getAnnotationsRecursively(AnnotationMeta<?> annotationMeta) {
    Collection<AnnotationMeta> result = new HashSet<>();
    for (AnnotationMeta<?> metaDatum : annotationMeta.getMetaData()) {
      result.addAll(this.getAnnotationsRecursively(metaDatum));
    }
    result.add(annotationMeta);
    return result;
  }
}
