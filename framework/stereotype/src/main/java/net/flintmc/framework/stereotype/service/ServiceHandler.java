package net.flintmc.framework.stereotype.service;

import net.flintmc.processing.autoload.AnnotationMeta;

import java.lang.annotation.Annotation;

public interface ServiceHandler<T extends Annotation> {
  /**
   * Discover a service.
   *
   * @param annotationMeta The meta of the discovered annotation.
   * @throws ServiceNotFoundException If the service could not be discovered.
   */
  void discover(AnnotationMeta<T> annotationMeta) throws ServiceNotFoundException;
}
