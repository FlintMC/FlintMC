package net.labyfy.component.settings.annotation.ui;

import net.labyfy.chat.annotation.Component;
import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used with any {@link ApplicableSetting} to add a description to the setting. If it is not set, there will be
 * no description.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface Description {

  /**
   * Retrieves the description of the setting.
   *
   * @return The description of the setting
   */
  Component[] value();

}
