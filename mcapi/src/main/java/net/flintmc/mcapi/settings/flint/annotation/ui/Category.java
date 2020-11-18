package net.flintmc.mcapi.settings.flint.annotation.ui;

import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets a category for a specific {@link ApplicableSetting} in a {@link Config}. It can be used on
 * any method or class that is associated with the given value, see {@link
 * ConfigObjectReference#findLastAnnotation(Class)} for more information.
 *
 * <p>For this to work, a category with the {@link #value() given name} needs to exist, to define
 * one, {@link DefineCategory} may be used.
 *
 * @see Config
 * @see DefineCategory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Category {

  /**
   * Retrieves the name of the category to get the information like the DisplayName and Description
   * from the registry.
   *
   * @return The name of this category
   */
  String value();
}
