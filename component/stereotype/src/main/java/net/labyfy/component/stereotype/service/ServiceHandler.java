package net.labyfy.component.stereotype.service;

import net.labyfy.component.processing.autoload.DetectableAnnotationProvider;

import java.lang.annotation.Annotation;

public interface ServiceHandler<T extends Annotation> {
  /**
   * Discover a service.
   *
   * @param annotationMeta The meta of the discovered annotation.
   * @throws ServiceNotFoundException If the service could not be discovered.
   */
  void discover(DetectableAnnotationProvider.AnnotationMeta<T> annotationMeta) throws ServiceNotFoundException;
}
