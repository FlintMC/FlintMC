package net.flintmc.mcapi.settings.flint.annotation.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

/**
 * Sets a category for a specific {@link ApplicableSetting} in a {@link Config}. It can be used on
 * any method or class that is associated with the given value, see {@link
 * ConfigObjectReference#findLastAnnotation(Class)} for more information.
 *
 * <p>If no category with the given {@link #name()} exists, it will be created with the given
 * {@link
 * #displayName()}, {@link #description()} and {@link #icon()}. If it already exists, it will be
 * used just like it is done with {@link Category}.
 *
 * @see Config
 * @see Category
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DefineCategory {

  /**
   * Retrieves the name of the category to get/associate the information like the DisplayName and
   * Description from the registry.
   *
   * @return The name of this category
   */
  String name();

  /**
   * Retrieves the display name to be parsed with a {@link ComponentAnnotationSerializer} that
   * should be set for this category if it doesn't exist.
   *
   * @return The display name of this category
   */
  Component[] displayName();

  /**
   * Retrieves the description to be parsed with a {@link ComponentAnnotationSerializer} that should
   * be set for this category if it doesn't exist.
   *
   * @return The description of this category
   */
  Component[] description() default {};

  /**
   * Retrieves the icon that should be displayed next to this category.
   *
   * @return The icon of this category
   */
  Icon icon() default @Icon("");
}
