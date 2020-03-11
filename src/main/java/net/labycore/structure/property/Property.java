package net.labycore.structure.property;

import net.labycore.structure.annotation.LocatedIdentifiedAnnotation;
import net.labycore.structure.annotation.Transitive;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.stream.Collectors;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
@Transitive
public @interface Property {
  Class<? extends Annotation> value();

  boolean allowMultiple() default false;

  class Base {
    private final LocatedIdentifiedAnnotation locatedIdentifiedAnnotation;
    private final Collection<Property.Base> subProperties;

    public Base(
        LocatedIdentifiedAnnotation locatedIdentifiedAnnotation, Collection<Base> subProperties) {
      this.locatedIdentifiedAnnotation = locatedIdentifiedAnnotation;
      this.subProperties = subProperties;
    }

    public LocatedIdentifiedAnnotation getLocatedIdentifiedAnnotation() {
      return this.locatedIdentifiedAnnotation;
    }

    public Collection<Property.Base> getSubProperties() {
      return this.subProperties;
    }

    public Collection<Property.Base> getSubProperties(Class<?> clazz) {
      return this.subProperties.stream()
          .filter(
              property ->
                  clazz.isInstance(property.getLocatedIdentifiedAnnotation().getAnnotation()))
          .collect(Collectors.toList());
    }
  }
}
