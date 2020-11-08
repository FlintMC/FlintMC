package net.flintmc.mcapi.settings.flint.options.dropdown;

import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = String.class, name = "dropdown")
public @interface CustomSelectSetting {

  // length > 1 (at least two values)
  Selection[] value();

  SelectMenuType type() default SelectMenuType.DROPDOWN;

}
