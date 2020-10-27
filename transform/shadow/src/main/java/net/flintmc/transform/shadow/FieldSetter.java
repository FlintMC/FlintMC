package net.flintmc.transform.shadow;

import net.flintmc.processing.autoload.DetectableAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a method in a shadow that sets the value of a field. Method must return void and must
 * have exactly one parameter with the type of the field.
 *
 * <p>Example: {@code private String test;} can be modified with {@code @FieldSetter void
 * setTest(String test);}
 *
 * @see Shadow
 * @see FieldGetter
 * @see MethodProxy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@DetectableAnnotation(requiresParent = true)
public @interface FieldSetter {

  String value();

  boolean removeFinal() default false;
}
