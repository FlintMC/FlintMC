package net.flintmc.framework.inject.assisted;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an injected parameter or field whose value comes from an argument to a factory method.
 */
@BindingAnnotation
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Assisted {

  /**
   * The unique name for this parameter. This is matched to the `@Assisted` constructor parameter
   * wit hthe same value. Names are not necessary when the parameter types are distinct.
   *
   * @return The unique name for this parameter.
   */
  String value() default "";

}
