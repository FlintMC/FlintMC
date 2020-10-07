package net.labyfy.internal.component.stereotype.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Injector;
import javassist.CtClass;
import javassist.NotFoundException;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.service.CtResolver;
import net.labyfy.component.stereotype.service.Service;
import net.labyfy.component.stereotype.service.ServiceHandler;
import net.labyfy.component.stereotype.service.ServiceNotFoundException;
import net.labyfy.internal.component.stereotype.identifier.IdentifierParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class ServiceRepository {

  private final Logger logger;
  private final Collection<CtClass> pendingServices;
  private final Set<CtClass> loadedClasses;
  private final Multimap<Class<?>, ServiceHandler> serviceHandlers;
  private final AtomicReference<Injector> injectorReference;
  private boolean initialized;

  @Inject
  private ServiceRepository(
      @Named("injectorReference") AtomicReference injectorReference) {
    this.logger = LogManager.getLogger(ServiceRepository.class);
    this.pendingServices = ConcurrentHashMap.newKeySet();
    this.loadedClasses = ConcurrentHashMap.newKeySet();
    this.serviceHandlers = HashMultimap.create();
    this.injectorReference = injectorReference;
  }

  private synchronized ServiceRepository register(CtClass ctClass) throws ServiceNotFoundException {
    this.pendingServices.add(ctClass);

    if (initialized) {
      flushAll();
    }

    return this;
  }

  public synchronized ServiceRepository flushAll() throws ServiceNotFoundException {
    initialized = true;
    for (CtClass handler : this.pendingServices) {
      flushService(handler);
    }
    return this;
  }

  private void flushService(CtClass handler) throws ServiceNotFoundException {
    ServiceHandler serviceHandler = (ServiceHandler) injectorReference.get().getInstance(CtResolver.get(handler));
    try {
      Service service = (Service) handler.getAnnotation(Service.class);
      for (Class<?> target : service.value()) {
        this.serviceHandlers.put(target, serviceHandler);
      }

      for (CtClass loadedClass : this.loadedClasses) {
        Collection<IdentifierMeta<?>> identifierMetas = IdentifierParser.collectIdentifiers(loadedClass);
        for (IdentifierMeta<?> identifierMeta : identifierMetas) {
          for (Class<?> annotationClass : service.value()) {
            if (annotationClass.isAssignableFrom(identifierMeta.getAnnotation().annotationType())) {
              serviceHandler.discover(identifierMeta);
              logger.trace("Servicehandler {} discovered {}", serviceHandler.getClass().getName(), identifierMeta.getTarget());
            }
          }
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    this.pendingServices.remove(handler);
  }

  public void notifyClassLoaded(CtClass clazz) throws ServiceNotFoundException {
    this.loadedClasses.add(clazz);
    try {
      Collection<IdentifierMeta<?>> identifierMetas = IdentifierParser.collectIdentifiers(clazz);
      if (clazz.subtypeOf(clazz.getClassPool().get("net.labyfy.component.stereotype.service.ServiceHandler")) && clazz.hasAnnotation(Service.class)) {
        this.register(clazz);
      }

      for (IdentifierMeta<?> identifierMeta : identifierMetas) {
        for (ServiceHandler serviceHandler : this.serviceHandlers.get(identifierMeta.getAnnotation().annotationType())) {
          serviceHandler.discover(identifierMeta);
        }
      }

    } catch (ClassNotFoundException | NotFoundException e) {
      throw new ServiceNotFoundException(e);
    }
//    Collection<IdentifierLegacy.Base> identifier = IdentifierParserLegacy.parse(clazz);
//
//    if (ServiceHandler.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(Service.class)) {
//      this.register(((Class<? extends ServiceHandler>) clazz));
//    }
//
//    for (IdentifierLegacy.Base base : identifier) {
//      for (ServiceHandler serviceHandler :
//          this.serviceHandlers.get(
//              AnnotationCollector.getRealAnnotationClass(
//                  base.getProperty().getLocatedIdentifiedAnnotation().getAnnotation()))) {
//        serviceHandler.discover(base);
//      }
//    }
  }

  public Multimap<Class<?>, ServiceHandler> getServices() {
    return Multimaps.unmodifiableMultimap(this.serviceHandlers);
  }

}
