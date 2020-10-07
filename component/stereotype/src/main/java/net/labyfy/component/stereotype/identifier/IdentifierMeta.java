package net.labyfy.component.stereotype.identifier;

import com.google.common.collect.Multimap;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Collection;

public interface IdentifierMeta<T extends Annotation> {

  T getAnnotation();

  boolean requireParent();

  boolean hasProperties();

  boolean hasProperty(Class<? extends Annotation> clazz);

  ElementType getTargetType();

  <K> K getTarget();

  Multimap<Class<? extends Annotation>, IdentifierMeta<?>> getProperties();

  <K extends Annotation> IdentifierMeta<K> getProperty(Class<K> clazz);

  <K extends Annotation> Collection<IdentifierMeta<K>> getProperties(Class<K> clazz);

  Collection<PropertyMeta> getRequiredPropertyMeta();

  Collection<PropertyMeta> getOptionalPropertyMeta();

}
