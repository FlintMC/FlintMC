package net.labyfy.component.settings.options.dropdown;

import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(value = Enum.class, type = "dropdown")
public @interface EnumSelectSetting {

  // index of enum constants from the return type
  // -1 = default is null
  int defaultValue() default -1;

  SelectMenuType type() default SelectMenuType.DROPDOWN;

}
