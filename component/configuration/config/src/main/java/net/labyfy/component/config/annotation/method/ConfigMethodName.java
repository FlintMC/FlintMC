package net.labyfy.component.config.annotation.method;

import net.labyfy.component.config.annotation.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on a method in a config so that the actual method can have any name but it can still be
 * parsed. For example if a method is called 'foo', the {@link ConfigMethodName#value()} could be 'getFoo' so that the
 * method will be detected as a getter.
 *
 * @see Config
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConfigMethodName {

  String value();

}
