package net.flintmc.mcapi.settings.flint.options.text;

import net.flintmc.framework.config.defval.annotation.DefaultString;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.serializer.JsonSettingsSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link ApplicableSetting} to define a string input, the stored type has to be a string.
 *
 * <p>The resulting json for the {@link JsonSettingsSerializer} will contain:
 *
 * <ul>
 *   <li>'value' with the value from the setting, empty if it is {@code null}
 *   <li>'restrictions' (only if {@link #value()} has at least one restriction) with a list of all
 *       restrictions from {@link #value()}
 *   <li>'maxLength' (only if {@link #maxLength()} is not {@link Integer#MAX_VALUE}) with {@link
 *       #maxLength()}
 *   <li>'prefix' (only if {@link #prefix()} is not empty) with {@link #prefix()}
 *   <li>'suffix' (only if {@link #suffix()} is not empty) with {@link #suffix()}
 * </ul>
 *
 * @see ApplicableSetting
 * @see DefaultString
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = String.class, name = "string")
public @interface StringSetting {

  /**
   * Retrieves all restrictions for the text input, if empty, there will be no restriction.
   *
   * @return An array of all restrictions for the input
   */
  StringRestriction[] value() default {};

  /**
   * Retrieves the max number of characters in the input.
   *
   * @return The max number of characters in the input, has to be > 0
   */
  int maxLength() default Integer.MAX_VALUE;

  /**
   * Retrieves the prefix that should be displayed in the text input and which cannot be modified by
   * the user. This may be useful for something like "https://youtube.com/" to let the user type in
   * their channel name.
   *
   * @return The prefix or an empty string to show no prefix
   */
  String prefix() default "";

  /**
   * Retrieves the suffix that should be displayed in the text input and which cannot be modified by
   * the user.
   *
   * @return The suffix or an empty string to show no suffix
   */
  String suffix() default "";
}
