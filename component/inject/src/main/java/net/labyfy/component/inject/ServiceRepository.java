package net.labyfy.component.inject;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Injector;
import net.labyfy.base.structure.annotation.AnnotationCollector;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.identifier.IdentifierParser;
import net.labyfy.base.structure.service.Service;
import net.labyfy.base.structure.service.ServiceHandler;
import net.labyfy.component.inject.assisted.AssistedFactory;
import net.labyfy.component.inject.implement.Implement;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class ServiceRepository {

  private final Collection<Class<? extends ServiceHandler>> pendingServices;
  private final Set<Class<?>> loadedClasses;
  private final IdentifierParser identifierParser;
  private final Multimap<Class<?>, ServiceHandler> serviceHandlers;
  private final AtomicReference<Injector> injectorReference;
  private boolean initialized;

  @Inject
  private ServiceRepository(
      IdentifierParser identifierParser,
      @Named("injectorReference") AtomicReference injectorReference) {
    this.pendingServices = ConcurrentHashMap.newKeySet();
    this.loadedClasses = ConcurrentHashMap.newKeySet();
    this.identifierParser = identifierParser;
    this.serviceHandlers = HashMultimap.create();
    this.injectorReference = injectorReference;
  }

  public synchronized ServiceRepository register(Class<? extends ServiceHandler> handler) {
    this.pendingServices.add(handler);
    if (handler.getDeclaredAnnotation(Service.class).value() == Implement.class
        || handler.getDeclaredAnnotation(Service.class).value() == AssistedFactory.class)
      flush(handler);

    if(initialized){
      flushAll();
    }

    return this;
  }

  public synchronized void flushAll() {
    initialized = true;
    for (Class<? extends ServiceHandler> handler : this.pendingServices) {
      flush(handler);
    }
  }

  private void flush(Class<? extends ServiceHandler> handler) {
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
    this.pendingServices.remove(handler);
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
