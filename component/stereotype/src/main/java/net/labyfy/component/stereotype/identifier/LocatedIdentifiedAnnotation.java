package net.labyfy.component.stereotype.identifier;

import java.lang.annotation.Annotation;

/**
 * Representation of an {@link Identifier} located either on a class or method.
 * Fields are currently not supported.
 */
public interface LocatedIdentifiedAnnotation {

  /**
   * {@inheritDoc}
   */
  Identifier getIdentifier();

  /**
   * {@inheritDoc}
   */
  Type getOriginalType();

  /**
   * {@inheritDoc}
   */
  <T extends Annotation> T getAnnotation();

  /**
   * {@inheritDoc}
   */
  <T> T getLocation();

  /**
   * {@inheritDoc}
   */
  Type getType();

  enum Type {
    CLASS,
    METHOD
  }
}
