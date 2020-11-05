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

  // TODO those should not be strings but rather an array of annotations that contain the registryName (also used for defaultValue() and the value in the config), displayName and maybe a description

  // length > 1 (at least two values)
  String[] value();

  // has to be inside of value(), empty = value()[0]
  String defaultValue() default "";

  SelectMenuType type() default SelectMenuType.DROPDOWN;

}
