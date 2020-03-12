package net.labyfy.base.structure.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Injector;
import net.labyfy.base.structure.annotation.AnnotationCollector;
import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.identifier.IdentifierParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class ServiceRepository {

  private final AnnotationCollector annotationCollector;
  private final Injector injector;
  private final IdentifierParser identifierParser;
  private final Multimap<Class<?>, ServiceHandler> serviceHandlers;

  @Inject
  private ServiceRepository(AnnotationCollector annotationCollector, Injector injector, IdentifierParser identifierParser) {
    this.annotationCollector = annotationCollector;
    this.injector = injector;
    this.identifierParser = identifierParser;
    this.serviceHandlers = HashMultimap.create();
  }

  public ServiceRepository register(Class<? extends ServiceHandler> handler) {
    this.serviceHandlers.put(
        handler.getDeclaredAnnotation(Service.class).value(), this.injector.getInstance(handler));
    return this;
  }

  public ServiceRepository classLoaded(Class<?> clazz) {
    Collection<Identifier.Base> identifier = this.identifierParser.parse(clazz);
    for (Identifier.Base base : identifier) {
      for (ServiceHandler serviceHandler :
          this.serviceHandlers.get(
              annotationCollector.getRealAnnotationClass(
                  base.getLocatedIdentifiedAnnotation().getAnnotation()))) {
        serviceHandler.discover(base);
      }
    }
    return this;
  }
}
