package net.labyfy.base.structure.annotation;

import net.labyfy.base.structure.identifier.Identifier;

import java.lang.annotation.Annotation;
import java.util.Objects;

public class LocatedIdentifiedAnnotation {

  private final Identifier identifier;
  private final Annotation annotation;
  private final Object location;
  private final Type type;
  private final Type originalType;

  public LocatedIdentifiedAnnotation(
      Identifier identifier, Annotation annotation, Object location, Type type, Type originalType) {
    this.identifier = identifier;
    this.annotation = annotation;
    this.location = location;
    this.type = type;
    this.originalType = originalType;
  }

  public Identifier getIdentifier() {
    return this.identifier;
  }

  public Type getOriginalType() {
    return this.originalType;
  }

  public <T extends Annotation> T getAnnotation() {
    return (T) this.annotation;
  }

  public <T> T getLocation() {
    return (T) this.location;
  }

  public Type getType() {
    return this.type;
  }

  public enum Type {
    CLASS,
    METHOD
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

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LocatedIdentifiedAnnotation that = (LocatedIdentifiedAnnotation) o;
    return Objects.equals(location, that.location)
        && type == that.type
        && originalType == that.originalType;
  }

  public int hashCode() {
    return Objects.hash(location, type, originalType);
  }
}
