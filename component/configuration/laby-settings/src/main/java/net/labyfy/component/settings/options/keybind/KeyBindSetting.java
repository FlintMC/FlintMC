package net.labyfy.component.settings.options.keybind;

import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(PhysicalKey.class)
public @interface KeyBindSetting {

  PhysicalKey defaultValue() default PhysicalKey.UNKNOWN;

}
