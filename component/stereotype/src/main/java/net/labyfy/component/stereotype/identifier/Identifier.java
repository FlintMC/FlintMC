package net.labyfy.component.stereotype.identifier;

import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.property.Property;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the current class as an identifier.
 * Identifiers are used to set and discover custom values, classes and methods to obtain
 * a very simple structure for event based applications.
 * <p>
 * Identifiers are discovered by a service.
 *
 * @see net.labyfy.component.stereotype.service.Service
 * @see net.labyfy.component.stereotype.service.ServiceHandler
 */
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
