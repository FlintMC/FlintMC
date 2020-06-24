package net.labyfy.internal.component.stereotype.identifier;

import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.LocatedIdentifiedAnnotation;

import java.lang.annotation.Annotation;

public class DefaultLocatedIdentifiedAnnotation implements LocatedIdentifiedAnnotation {

  private final Identifier identifier;
  private final Annotation annotation;
  private final Object location;
  private final Type type;
  private final Type originalType;

  public DefaultLocatedIdentifiedAnnotation(
      Identifier identifier, Annotation annotation, Object location, Type type, Type originalType) {
    this.identifier = identifier;
    this.annotation = annotation;
    this.location = location;
    this.type = type;
    this.originalType = originalType;
  }

  /**
   * @return the {@link Identifier} that created this {@link LocatedIdentifiedAnnotation}
   */
  public Identifier getIdentifier() {
    return this.identifier;
  }

  /**
   * @return Real class location of this identifier.
   */
  public Type getOriginalType() {
    return this.originalType;
  }

  /**
   * @return the annotation that represents this identifier.
   */
  public <T extends Annotation> T getAnnotation() {
    return (T) this.annotation;
  }

  /**
   *
   * @param <T> either {@link Class} or {@link java.lang.reflect.Method}
   * @return the location of getAnnotation()
   */
  public <T> T getLocation() {
    return (T) this.location;
  }

  /**
   * Might differ from getOriginalType when child identifiers are used.
   * @return semantic class location of this identifier.
   */
  public Type getType() {
    return this.type;
  }


  public String toString() {
    return "LocatedIdentifiedAnnotation{"
        + "identifier="
        + identifier
        + ", annotation="
        + annotation
        + ", location="
        + location
        + ", type="
        + type
        + '}';
  }
}
