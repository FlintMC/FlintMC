package net.labyfy.component.transform.shadow;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a method in a shadow that retrieves the value of a field.
 * Method must return the field type and must not have parameters.
 * <p>
 * Example:
 * {@code private String test;}
 * can be accessed with
 * {@code @FieldGetter String getTest();}
 *
 * @see Shadow
 * @see FieldSetter
 * @see MethodProxy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Identifier(requireParent = true)
@Transitive
public @interface FieldGetter {

  /**
   * @return field name that should be accessed
   */
  String value();

}
