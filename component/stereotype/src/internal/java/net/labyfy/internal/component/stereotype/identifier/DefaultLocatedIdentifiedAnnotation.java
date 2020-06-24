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
   * {@inheritDoc}
   */
  public Identifier getIdentifier() {
    return this.identifier;
  }

  /**
   * {@inheritDoc}
   */
  public Type getOriginalType() {
    return this.originalType;
  }

  /**
   * {@inheritDoc}
   */
  public <T extends Annotation> T getAnnotation() {
    return (T) this.annotation;
  }

  /**
   * {@inheritDoc}
   */
  public <T> T getLocation() {
    return (T) this.location;
  }

  /**
   * {@inheritDoc}
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
