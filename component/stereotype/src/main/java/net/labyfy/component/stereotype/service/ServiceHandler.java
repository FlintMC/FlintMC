package net.labyfy.component.stereotype.service;

import net.labyfy.component.stereotype.identifier.Identifier;

@FunctionalInterface
public interface ServiceHandler {
  /**
   * Discover a service.
   *
   * @param property a property.
   * @throws ServiceNotFoundException if the service could not be discovered.
   */
  void discover(Identifier.Base property) throws ServiceNotFoundException;
}
