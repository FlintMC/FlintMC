package net.labyfy.component.settings.annotation.ui;

import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.config.generator.method.ConfigObjectReference;
import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets a category for a specific {@link ApplicableSetting} in a {@link Config}. It can be used on any method or class
 * that is associated with the given value, see {@link ConfigObjectReference#findLastAnnotation(Class)} for more
 * information.
 * <p>
 * For this to work, a category with the {@link #value() given name} needs to exist, to define one, {@link
 * DefineCategory} may be used.
 *
 * @see Config
 * @see DefineCategory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Category {

  /**
   * Retrieves the name of the category to get the information like the DisplayName and Description from the registry.
   *
   * @return The name of this category
   */
  String value();

}
