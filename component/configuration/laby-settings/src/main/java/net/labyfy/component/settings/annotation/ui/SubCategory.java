package net.labyfy.component.settings.annotation.ui;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.config.annotation.Config;
import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets a sub category for an {@link ApplicableSetting} in a {@link Config}. The categories will be identified by the
 * {@link #value() displayName}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SubCategory {

  /**
   * Retrieves the component as it should be displayed in the frontend.
   *
   * @return The component to be displayed
   */
  Component[] value();

}
