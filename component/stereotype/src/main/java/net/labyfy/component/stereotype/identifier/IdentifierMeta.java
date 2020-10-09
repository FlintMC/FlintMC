package net.labyfy.component.stereotype.identifier;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.HashSet;

public class IdentifierMeta<T extends Annotation> {

  private final Collection<PropertyMeta> requiredPropertyMeta;
  private final Collection<PropertyMeta> optionalPropertyMeta;
  private final Identifier identifier;
  private final ElementType elementType;
  private final T annotation;
  private final Object target;
  private final Multimap<Class<? extends Annotation>, IdentifierMeta<?>> subProperties;

  public IdentifierMeta(Identifier identifier, ElementType elementType, T annotation, Object target) {
    this.identifier = identifier;
    this.elementType = elementType;
    this.annotation = annotation;
    this.target = target;
    this.subProperties = HashMultimap.create();
    this.requiredPropertyMeta = new HashSet<>();
    this.optionalPropertyMeta = new HashSet<>();

    for (Property property : identifier.requiredProperties()) {
      this.requiredPropertyMeta.add(new PropertyMeta(property.value(), true, property.allowMultiple()));
    }

    for (Property property : identifier.optionalProperties()) {
      this.optionalPropertyMeta.add(new PropertyMeta(property.value(), false, property.allowMultiple()));
    }
  }

  public T getAnnotation() {
    return this.annotation;
  }

  public boolean requireParent() {
    return this.identifier.requireParent();
  }

  public boolean hasProperties() {
    return !this.getProperties().isEmpty();
  }

  public boolean hasProperty(Class<? extends Annotation> clazz) {
    return this.getProperties().containsKey(clazz);
  }

  public ElementType getTargetType() {
    return this.elementType;
  }

  public <K> K getTarget() {
    return (K) this.target;
  }

  public Multimap<Class<? extends Annotation>, IdentifierMeta<?>> getProperties() {
    return this.subProperties;
  }

  public <K extends Annotation> IdentifierMeta<K> getProperty(Class<K> clazz) {
    return ((IdentifierMeta<K>) this.subProperties.get(clazz).toArray()[0]);
  }

  public <K extends Annotation> Collection<IdentifierMeta<K>> getProperties(Class<K> clazz) {
    return (Collection<IdentifierMeta<K>>) (Object) this.subProperties.get(clazz);
  }

  public Collection<PropertyMeta> getRequiredPropertyMeta() {
    return this.requiredPropertyMeta;
  }

  public Collection<PropertyMeta> getOptionalPropertyMeta() {
    return this.optionalPropertyMeta;
  }
}
