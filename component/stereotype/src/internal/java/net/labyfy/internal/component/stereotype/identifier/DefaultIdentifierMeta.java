package net.labyfy.internal.component.stereotype.identifier;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.stereotype.identifier.IdentifierMeta;
import net.labyfy.component.stereotype.identifier.Property;
import net.labyfy.component.stereotype.identifier.PropertyMeta;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Collection;
import java.util.HashSet;

public class DefaultIdentifierMeta<T extends Annotation> implements IdentifierMeta<T> {

  private final Collection<PropertyMeta> requiredPropertyMeta;
  private final Collection<PropertyMeta> optionalPropertyMeta;
  private final Identifier identifier;
  private final ElementType elementType;
  private final T annotation;
  private final Object target;
  private final Multimap<Class<? extends Annotation>, IdentifierMeta<?>> subProperties;

  public DefaultIdentifierMeta(Identifier identifier, ElementType elementType, T annotation, Object target) {
    this.identifier = identifier;
    this.elementType = elementType;
    this.annotation = annotation;
    this.target = target;
    this.subProperties = HashMultimap.create();
    this.requiredPropertyMeta = new HashSet<>();
    this.optionalPropertyMeta = new HashSet<>();

    for (Property property : identifier.requiredProperties()) {
      this.requiredPropertyMeta.add(new DefaultPropertyMeta(property.value(), true, property.allowMultiple()));
    }

    for (Property property : identifier.optionalProperties()) {
      this.optionalPropertyMeta.add(new DefaultPropertyMeta(property.value(), false, property.allowMultiple()));
    }
  }

  @Override
  public T getAnnotation() {
    return this.annotation;
  }

  @Override
  public boolean requireParent() {
    return this.identifier.requireParent();
  }

  @Override
  public boolean hasProperties() {
    return !this.getProperties().isEmpty();
  }

  @Override
  public boolean hasProperty(Class<? extends Annotation> clazz) {
    return this.getProperties().containsKey(clazz);
  }

  @Override
  public ElementType getTargetType() {
    return this.elementType;
  }

  @Override
  public <K> K getTarget() {
    return (K) this.target;
  }

  @Override
  public Multimap<Class<? extends Annotation>, IdentifierMeta<?>> getProperties() {
    return this.subProperties;
  }

  @Override
  public <K extends Annotation> IdentifierMeta<K> getProperty(Class<K> clazz) {
    return ((IdentifierMeta<K>) this.subProperties.get(clazz).toArray()[0]);
  }

  @Override
  public <K extends Annotation> Collection<IdentifierMeta<K>> getProperties(Class<K> clazz) {
    return (Collection<IdentifierMeta<K>>) (Object) this.subProperties.get(clazz);
  }

  @Override
  public Collection<PropertyMeta> getRequiredPropertyMeta() {
    return this.requiredPropertyMeta;
  }

  @Override
  public Collection<PropertyMeta> getOptionalPropertyMeta() {
    return this.optionalPropertyMeta;
  }
}
