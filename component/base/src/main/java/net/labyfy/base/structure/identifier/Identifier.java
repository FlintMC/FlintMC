package net.labyfy.base.structure.identifier;

import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.property.Property;

import java.lang.annotation.*;

/** Marks the current class as an identifier. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Transitive
public @interface Identifier {

  Class<?>[] parents() default {};

  Property[] requiredProperties() default {};

  Property[] optionalProperties() default {};

  class Base {
    private final LocatedIdentifiedAnnotation locatedIdentifiedAnnotation;
    private final Property.Base property;

    public Base(LocatedIdentifiedAnnotation locatedIdentifiedAnnotation, Property.Base property) {
      this.locatedIdentifiedAnnotation = locatedIdentifiedAnnotation;
      this.property = property;
    }

    public Property.Base getProperty() {
      return this.property;
    }

    public LocatedIdentifiedAnnotation getLocatedIdentifiedAnnotation() {
      return this.locatedIdentifiedAnnotation;
    }
  }
}
