package net.labyfy.component.transform.shadow;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a method in a shadow that sets the value of a field.
 * Method must return void and must have exactly one parameter with the type of the field.
 * <p>
 * Example:
 * {@code private String test;}
 * can be modified with
 * {@code @FieldGetter void setTest(String test);}
 *
 * @see Shadow
 * @see FieldGetter
 * @see MethodProxy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Identifier(requireParent = true)
@Transitive
public @interface FieldSetter {

  String value();

}
