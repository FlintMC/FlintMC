package net.labyfy.component.stereotype.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CtClass;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.processing.autoload.AnnotationMeta;

import java.lang.annotation.Annotation;

@Singleton
public class ServiceRepository {

  private final Multimap<Class<? extends Annotation>, Pair<Service, CtClass>> serviceHandlers;
  private final Multimap<Class<? extends Annotation>, AnnotationMeta<?>> annotations;

  @Inject
  private ServiceRepository() {
    this.serviceHandlers = HashMultimap.create();
    this.annotations = HashMultimap.create();
  }

  public void registerService(Service service, CtClass ctClass) {
    for (Class<? extends Annotation> annotation : service.value()) {
      this.registerService(annotation, service, ctClass);
    }
  }

  public void registerService(Class<? extends Annotation> annotation, Service service, CtClass ctClass) {
    this.serviceHandlers.put(annotation, new Pair<>(service, ctClass));
  }

  public void registerAnnotation(AnnotationMeta<?> annotationMeta) {
    this.annotations.put(annotationMeta.getAnnotation().annotationType(), annotationMeta);
  }

  public Multimap<Class<? extends Annotation>, AnnotationMeta<?>> getAnnotations() {
    return HashMultimap.create(annotations);
  }

  public Multimap<Class<? extends Annotation>, Pair<Service, CtClass>> getServiceHandlers() {
    return HashMultimap.create(serviceHandlers);
  }
}
