package net.labyfy.component.settings.options.dropdown;

import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(String.class)
public @interface CustomDropDownSetting {

  // length > 1 (at least two values)
  String[] value();

  // has to be inside of value(), empty = value()[0]
  String defaultValue() default "";

}
