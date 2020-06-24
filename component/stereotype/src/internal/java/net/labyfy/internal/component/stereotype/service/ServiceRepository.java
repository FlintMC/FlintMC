package net.labyfy.internal.component.stereotype.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Injector;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.internal.component.stereotype.annotation.AnnotationCollector;
import net.labyfy.internal.component.stereotype.identifier.IdentifierParser;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class ServiceRepository {

  private final Collection<Class<? extends ServiceHandler>> pendingServices;
  private final Set<Class<?>> loadedClasses;
  private final Multimap<Class<?>, ServiceHandler> serviceHandlers;
  private final AtomicReference<Injector> injectorReference;
  private boolean initialized;

  @Inject
  private ServiceRepository(
      @Named("injectorReference") AtomicReference injectorReference) {
    this.pendingServices = ConcurrentHashMap.newKeySet();
    this.loadedClasses = ConcurrentHashMap.newKeySet();
    this.serviceHandlers = HashMultimap.create();
    this.injectorReference = injectorReference;
  }

  private synchronized ServiceRepository register(Class<? extends ServiceHandler> handler) {
    this.pendingServices.add(handler);

    if (initialized) {
      flushAll();
    }

    return this;
  }

  public synchronized ServiceRepository flushAll() {
    initialized = true;
    for (Class<? extends ServiceHandler> handler : this.pendingServices) {
      flushService(handler);
    }
    return this;
  }

  private void flushService(Class<? extends ServiceHandler> handler) {
    ServiceHandler serviceHandler = injectorReference.get().getInstance(handler);

    for (Class<?> target : handler.getDeclaredAnnotation(Service.class).value()) {
      this.serviceHandlers.put(target, serviceHandler);
    }

    for (Class<?> loadedClass : this.loadedClasses) {
      Collection<Identifier.Base> identifier = IdentifierParser.parse(loadedClass);

      for (Identifier.Base base : identifier) {

        for (Class<?> target : handler
            .getDeclaredAnnotation(Service.class)
            .value()) {
          if (target.isAssignableFrom(
              base.getProperty().getLocatedIdentifiedAnnotation().getAnnotation().getClass())) {
            serviceHandler.discover(base);
            System.out.println("Servicehandler " + serviceHandler.getClass().getName() + " discovered " + base.getProperty().getLocatedIdentifiedAnnotation().<Object>getLocation());
          }
        }
      }
    }
    this.pendingServices.remove(handler);
  }

  public ServiceRepository notifyClassLoaded(Class<?> clazz) {
    this.loadedClasses.add(clazz);
    Collection<Identifier.Base> identifier = IdentifierParser.parse(clazz);

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

  public Multimap<Class<?>, ServiceHandler> getServices() {
    return Multimaps.unmodifiableMultimap(this.serviceHandlers);
  }

}
