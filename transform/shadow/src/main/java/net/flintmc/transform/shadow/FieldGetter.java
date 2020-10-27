package net.flintmc.transform.shadow;


import net.flintmc.processing.autoload.DetectableAnnotation;

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
@DetectableAnnotation(requiresParent = true)
public @interface FieldGetter {

  /**
   * @return field name that should be accessed
   */
  String value();

}
