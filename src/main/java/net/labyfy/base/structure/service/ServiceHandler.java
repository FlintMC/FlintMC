package net.labyfy.base.structure.service;

import net.labyfy.base.structure.identifier.Identifier;

@FunctionalInterface
public interface ServiceHandler {
  void discover(Identifier.Base property);
}
