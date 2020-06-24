package net.labyfy.component.stereotype.identifier;

import java.lang.annotation.Annotation;

/**
 * Representation of an {@link Identifier} located either on a class or method.
 * Fields are currently not supported.
 */
public interface LocatedIdentifiedAnnotation {

  /**
   * @return the {@link Identifier} that created this {@link LocatedIdentifiedAnnotation}
   */
  Identifier getIdentifier();

  /**
   * @return Real class location of this identifier.
   */
  Type getOriginalType();

  /**
   * @return The annotation that represents this identifier.
   */
  <T extends Annotation> T getAnnotation();

  /**
   * @param <T> either {@link Class} or {@link java.lang.reflect.Method}
   * @return the location of getAnnotation()
   */
  <T> T getLocation();

  /**
   * Might differ from getOriginalType when child identifiers are used.
   *
   * @return semantic class location of this identifier.
   */
  Type getType();

  enum Type {
    CLASS,
    METHOD
  }
}
