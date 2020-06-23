package net.labyfy.component.stereotype.identifier;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.property.Property;

import java.lang.annotation.*;

/** Marks the current class as an identifier. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Transitive
@AutoLoad
public @interface Identifier {

  boolean requireParent() default false;

  Property[] requiredProperties() default {};

  Property[] optionalProperties() default {};

  class Base {
    private final Property.Base property;

    public Base(Property.Base property) {
      this.property = property;
    }

    public Property.Base getProperty() {
      return this.property;
    }
  }
}
