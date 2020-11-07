package net.labyfy.component.settings.annotation.ui;

import net.labyfy.chat.annotation.Component;
import net.labyfy.chat.annotation.ComponentAnnotationSerializer;
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
 * If no category with the given {@link #name()} exists, it will be created with the given {@link #displayName()},
 * {@link #description()} and {@link #icon()}. If it already exists, it will be used just like it is done with {@link
 * Category}.
 *
 * @see Config
 * @see Category
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DefineCategory {

  /**
   * Retrieves the name of the category to get/associate the information like the DisplayName and Description from the
   * registry.
   *
   * @return The name of this category
   */
  String name();

  /**
   * Retrieves the display name to be parsed with a {@link ComponentAnnotationSerializer} that should be set for this
   * category if it doesn't exist.
   *
   * @return The display name of this category
   */
  Component[] displayName();

  /**
   * Retrieves the description to be parsed with a {@link ComponentAnnotationSerializer} that should be set for this
   * category if it doesn't exist.
   *
   * @return The description of this category
   */
  Component[] description();

  Icon icon() default @Icon(""); // TODO implement icons

}
