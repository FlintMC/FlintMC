package net.labycore.structure.service;

import net.labycore.structure.annotation.LocatedIdentifiedAnnotation;
import net.labycore.structure.identifier.Identifier;
import net.labycore.structure.property.Property;

@FunctionalInterface
public interface ServiceHandler {
  void discover(Identifier.Base property);
}
