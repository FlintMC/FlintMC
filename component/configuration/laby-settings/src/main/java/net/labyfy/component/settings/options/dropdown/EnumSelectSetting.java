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

  SelectMenuType value() default SelectMenuType.DROPDOWN;

}
