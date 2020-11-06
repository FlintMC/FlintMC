package net.labyfy.component.settings.options.dropdown;

import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(value = String.class, type = "dropdown")
public @interface CustomSelectSetting {

  // length > 1 (at least two values)
  Selection[] value();

  SelectMenuType type() default SelectMenuType.DROPDOWN;

}
