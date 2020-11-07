package net.flintmc.framework.inject;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.flintmc.framework.inject.assisted.AssistedFactory;
import net.flintmc.framework.inject.assisted.factory.AssistedFactoryModuleBuilder;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.service.*;
import net.flintmc.processing.autoload.AnnotationMeta;
import net.flintmc.processing.autoload.identifier.ClassIdentifier;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Singleton
@Services({
        @Service(value = Implement.class, priority = -100000, state = Service.State.PRE_INIT),
        @Service(value = AssistedFactory.class, priority = -10000, state = Service.State.AFTER_IMPLEMENT)
})
public class InjectionService implements ServiceHandler<Annotation> {

  private final Collection<CtClass> implementationsFlushed = new HashSet<>();
  private final Collection<Class> assistedFlushed = new HashSet<>();

  private final Map<Class, CtClass> implementations = Maps.newHashMap();
  private final Map<CtClass, AssistedFactory> assisted = new HashMap<>();
  private final Collection<CtClass> ignore = new HashSet<>();

  private final Map<String, String> launchArguments;

  @Inject
  private InjectionService(@Named("launchArguments") Map launchArguments) {
    this.launchArguments = launchArguments;
  }

  @Override
  public void discover(AnnotationMeta annotationMeta) throws ServiceNotFoundException {
    if (annotationMeta.getAnnotation() instanceof Implement)
      this.handleImplementAnnotation((AnnotationMeta<Implement>) annotationMeta);

    if (annotationMeta.getAnnotation() instanceof AssistedFactory)
      this.handleAssistedFactoryAnnotation((AnnotationMeta<AssistedFactory>) annotationMeta);
  }

  private void handleAssistedFactoryAnnotation(AnnotationMeta<AssistedFactory> annotationMeta) {
    System.out.println(
            "AssistedFactory "
                    + annotationMeta.getAnnotation().value().getName()
                    + " at "
                    + annotationMeta.<ClassIdentifier>getIdentifier().getLocation().getName());
    AssistedFactory annotation = annotationMeta.getAnnotation();
    assisted.put(annotationMeta.<ClassIdentifier>getIdentifier().getLocation(), annotation);
    try {
      ignore.add(ClassPool.getDefault().get(annotation.value().getName()));
    } catch (NotFoundException exception) {
      exception.printStackTrace();
    }
  }

  private void handleImplementAnnotation(AnnotationMeta<Implement> annotationMeta) {
    CtClass location = annotationMeta.<ClassIdentifier>getIdentifier().getLocation();
    Implement annotation = annotationMeta.getAnnotation();

    System.out.println("Implement " + location.getName() + " " + annotation.value().getName());

    if (!(annotation.version().isEmpty()
            || launchArguments.get("--game-version").equals(annotation.version()))) return;

    if (implementations.containsKey(annotation.value()) && !implementations.get(annotation.value()).equals(location)) {
      throw new IllegalStateException(
              String.format("Cannot bind %s. Implementation %s already provided by %s.", annotationMeta.<ClassIdentifier>getIdentifier().getLocation().getName(), annotation.value(), implementations.get(annotation.value()).getName()));
    }
    implementations.put(annotation.value(), location);
  }

  public void flushImplementation() {
    InjectionHolder.getInstance()
            .addModules(new AbstractModule() {
              @Override
              protected void configure() {
                implementations.forEach(
                        (superClass, implementation) -> {
                          try {
                            if (!ignore.contains(ClassPool.getDefault().get(superClass.getName()))
                                    && !ignore.contains(implementation)
                                    && !implementationsFlushed.contains(implementation)) {
                              this.bind(superClass)
                                      .toProvider(
                                              () -> {
                                                Class<?> implementationResolved = CtResolver.get(implementation);

                                                return InjectionHolder.getInjectedInstance(implementationResolved);
                                              }
                                      );
                              implementationsFlushed.add(implementation);
                            }
                          } catch (NotFoundException exception) {
                            exception.printStackTrace();
                          }
                        });
              }
            });
  }

  public void flushAssistedFactory() {
    InjectionHolder.getInstance()
            .addModules(new AbstractModule() {
              @Override
              protected void configure() {
                assisted.forEach(
                        (clazz, factory) -> {
                          Class<?> resolvedClass = CtResolver.get(clazz);
                          if (!assistedFlushed.contains(resolvedClass)) {
                            AssistedFactoryModuleBuilder assistedFactoryModuleBuilder = new AssistedFactoryModuleBuilder();
                            implementations.forEach(
                                    (interfaceClass, implementation) ->
                                            assistedFactoryModuleBuilder.implement(
                                                    interfaceClass, CtResolver.get(implementation)));
                            install(assistedFactoryModuleBuilder.build(resolvedClass));
                            assistedFlushed.add(resolvedClass);
                          }
                        });
              }
            });
  }
}
