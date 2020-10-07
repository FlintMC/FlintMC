package net.labyfy.component.stereotype.service;

import net.labyfy.component.stereotype.identifier.IdentifierMeta;

import java.lang.annotation.Annotation;

public interface ServiceHandler<T extends Annotation> {
  /**
   * Discover a service.
   *
   * @param identifierMeta The property where the identifier was discovered.
   * @throws ServiceNotFoundException If the service could not be discovered.
   */
  void discover(IdentifierMeta<T> identifierMeta) throws ServiceNotFoundException;
}
