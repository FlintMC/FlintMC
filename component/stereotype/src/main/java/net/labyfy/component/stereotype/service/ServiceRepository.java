package net.labyfy.component.stereotype.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import javassist.CtClass;
import javassist.CtMethod;
import net.labyfy.component.commons.util.Pair;
import net.labyfy.component.processing.autoload.AnnotationMeta;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;

@Singleton
public class ServiceRepository {

  private final Multimap<Class<? extends Annotation>, Pair<Service, CtClass>> serviceHandlers;
  private final Multimap<Class<? extends Annotation>, Pair<AnnotationMeta<?>, Object>> annotations;

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

  public void registerClassAnnotation(AnnotationMeta<?> annotationMeta, CtClass ctClass) {
    this.annotations.put(annotationMeta.getAnnotation().annotationType(), new Pair<>(annotationMeta, ctClass));
  }

  public void registerMethodAnnotation(AnnotationMeta<?> annotationMeta, CtMethod ctMethod) {
    this.annotations.put(annotationMeta.getAnnotation().annotationType(), new Pair<>(annotationMeta, ctMethod));
  }

  public Multimap<Class<? extends Annotation>, Pair<AnnotationMeta<?>, Object>> getAnnotations() {
    return HashMultimap.create(annotations);
  }

  public Multimap<Class<? extends Annotation>, Pair<Service, CtClass>> getServiceHandlers() {
    return HashMultimap.create(serviceHandlers);
  }
}
