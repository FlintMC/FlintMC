package net.labyfy.base.structure.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Injector;
import net.labyfy.base.structure.annotation.AnnotationCollector;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.identifier.IdentifierParser;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class ServiceRepository {

  private final Set<Class<?>> loadedClasses;
  private final IdentifierParser identifierParser;
  private final Multimap<Class<?>, ServiceHandler> serviceHandlers;
  private final AtomicReference<Injector> injectorReference;

  @Inject
  private ServiceRepository(
      IdentifierParser identifierParser,
      @Named("injectorReference") AtomicReference injectorReference) {
    this.loadedClasses = ConcurrentHashMap.newKeySet();
    this.identifierParser = identifierParser;
    this.serviceHandlers = HashMultimap.create();
    this.injectorReference = injectorReference;
  }

  public ServiceRepository register(Class<? extends ServiceHandler> handler) {
    ServiceHandler serviceHandler = injectorReference.get().getInstance(handler);
    this.serviceHandlers.put(handler.getDeclaredAnnotation(Service.class).value(), serviceHandler);

    for (Class<?> loadedClass : this.loadedClasses) {
      Collection<Identifier.Base> identifier = this.identifierParser.parse(loadedClass);
      for (Identifier.Base base : identifier) {
        if (handler
            .getDeclaredAnnotation(Service.class)
            .value()
            .isAssignableFrom(
                base.getProperty().getLocatedIdentifiedAnnotation().getAnnotation().getClass())) {
          serviceHandler.discover(base);
        }
      }
    }
    return this;
  }

  public ServiceRepository notifyClassLoaded(Class<?> clazz) {
    this.loadedClasses.add(clazz);
      Collection<Identifier.Base> identifier = this.identifierParser.parse(clazz);

    if (ServiceHandler.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(Service.class)) {
      this.register(((Class<? extends ServiceHandler>) clazz));
    }

    for (Identifier.Base base : identifier) {
      for (ServiceHandler serviceHandler :
          this.serviceHandlers.get(
              AnnotationCollector.getRealAnnotationClass(
                  base.getProperty().getLocatedIdentifiedAnnotation().getAnnotation()))) {
        serviceHandler.discover(base);
      }
    }
    return this;
  }
}
