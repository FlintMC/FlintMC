package net.flintmc.mcapi.settings.flint.annotation.ui;

import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.chat.annotation.Component;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used with any {@link ApplicableSetting} to add a displayName to the setting. If it is not set, the {@link
 * ConfigObjectReference#getKey() key} will be used as a plain text.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface DisplayName {

  /**
   * Retrieves the description of the setting.
   *
   * @return The description of the setting
   */
  Component[] value();

}
