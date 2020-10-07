package net.labyfy.component.transform.shadow;

import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.annotation.Transitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a shadow interface for a target class.
 * <p>
 * A shadow interface is used to access fields and methods in other already compiled classes that cant be accessed without
 * reflections or similar techniques.
 * <p>
 * This is done by bytecode manipulating the target class and adding the - by this annotation - marked class to its interface list.
 * So any instance of the target class can be casted to the interface and the generated or modified methods can be accessed without compile errors.
 *
 * @see FieldGetter
 * @see FieldSetter
 * @see MethodProxy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transitive
@AutoLoad(round = 10)
public @interface Shadow {
  /**
   * @return the target class name that should be modified
   */
  String value();
}
