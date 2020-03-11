package net.labyfy.structure.service;

import net.labyfy.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.structure.identifier.Identifier;
import net.labyfy.structure.property.Property;

@FunctionalInterface
public interface ServiceHandler {
  void discover(Identifier.Base property);
}
