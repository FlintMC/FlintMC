package net.flintmc.mcapi.settings.flint.options.dropdown;

import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The same as {@link CustomSelectSetting}, but things like the {@link DisplayName} aren't got from
 * the {@link Selection}, but from the enum constant.
 *
 * @see ApplicableSetting
 * @see CustomSelectSetting
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = Enum.class, name = "dropdown")
public @interface EnumSelectSetting {

  /**
   * Retrieves the type of this menu.
   *
   * @return The type of this menu
   */
  SelectMenuType value() default SelectMenuType.DROPDOWN;
}
