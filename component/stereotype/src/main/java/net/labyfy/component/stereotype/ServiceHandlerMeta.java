package net.labyfy.component.stereotype;

import javassist.CtClass;
import net.labyfy.component.stereotype.service.Service;

import java.lang.annotation.Annotation;

/**
 * Class representation of a {@link Service} annotation.
 */
public class ServiceHandlerMeta {

  private final Class<? extends Annotation>[] annotationTypes;
  private final int priority;
  private final Service.State state;
  private final CtClass serviceHandlerClass;

  private ServiceHandlerMeta(
      Class<? extends Annotation>[] annotationTypes,
      int priority,
      Service.State state,
      CtClass serviceHandlerClass) {
    this.annotationTypes = annotationTypes;
    this.priority = priority;
    this.state = state;
    this.serviceHandlerClass = serviceHandlerClass;
  }

  public static ServiceHandlerMeta create(
      Class<? extends Annotation> annotationType,
      int priority,
      Service.State state,
      CtClass serviceHandlerClass) {
    return new ServiceHandlerMeta(new Class[]{annotationType}, priority, state, serviceHandlerClass);
  }

  public static ServiceHandlerMeta create(
      Class<? extends Annotation>[] annotationTypes,
      int priority,
      Service.State state,
      CtClass serviceHandlerClass) {
    return new ServiceHandlerMeta(annotationTypes, priority, state, serviceHandlerClass);
  }

  public Class<? extends Annotation>[] getAnnotationTypes() {
    return annotationTypes;
  }

  public int getPriority() {
    return priority;
  }

  public CtClass getServiceHandlerClass() {
    return serviceHandlerClass;
  }

  public Service.State getState() {
    return state;
  }
}
