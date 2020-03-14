package net.labyfy.base.structure.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Injector;
import net.labyfy.base.structure.annotation.AnnotationCollector;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.identifier.IdentifierParser;
import net.labyfy.inject.assisted.AssistedFactoryService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class ServiceRepository {

  private final Injector injector;
  private final IdentifierParser identifierParser;
  private final Multimap<Class<?>, ServiceHandler> serviceHandlers;

  @Inject
  private ServiceRepository(Injector injector, IdentifierParser identifierParser) {
    this.injector = injector;
    this.identifierParser = identifierParser;
    this.serviceHandlers = HashMultimap.create();
  }

  public ServiceRepository register(Class<? extends ServiceHandler> handler) {
    this.serviceHandlers.put(
        handler.getDeclaredAnnotation(Service.class).value(), this.injector.getInstance(handler));
    System.out.println("Register " + handler);
    return this;
  }

  public ServiceRepository notifyClassLoaded(Class<?> clazz) {
    Collection<Identifier.Base> identifier = this.identifierParser.parse(clazz);
    if (ServiceHandler.class.isAssignableFrom(clazz)) {
      if (clazz.isAnnotationPresent(Service.class)) {
        this.register(((Class<? extends ServiceHandler>) clazz));
      }
    } else {
      for (Identifier.Base base : identifier) {
        for (ServiceHandler serviceHandler :
            this.serviceHandlers.get(
                AnnotationCollector.getRealAnnotationClass(
                    base.getLocatedIdentifiedAnnotation().getAnnotation()))) {
          serviceHandler.discover(base);
        }
      }
    }
    return this;
  }
}
