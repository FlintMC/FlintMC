package net.flintmc.mcapi.settings.flint.options.dropdown;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;
import net.flintmc.mcapi.settings.flint.annotation.ui.Icon;

/**
 * Represents a selection entry in a {@link CustomSelectSetting}.
 *
 * @see CustomSelectSetting
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Selection {

  /**
   * Retrieves the unique name per {@link CustomSelectSetting} that identifies this selection entry,
   * it is also used for the stored value in the setting.
   *
   * @return The unique name of this entry
   */
  String value();

  /**
   * Retrieves the optional display name of this selection entry, if it is not provided, the {@link
   * #value()} will be displayed.
   *
   * @return The display name of this entry
   */
  DisplayName display() default @DisplayName({});

  /**
   * Retrieves the optional description of this selection entry.
   *
   * @return The description of this entry
   */
  Description description() default @Description({});

  /**
   * Retrieves the optional icon of this selection entry.
   *
   * @return The icon of this entry
   */
  Icon icon() default @Icon("");
}
