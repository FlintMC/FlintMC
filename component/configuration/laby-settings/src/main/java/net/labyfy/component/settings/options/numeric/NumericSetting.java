package net.labyfy.component.settings.options.numeric;

import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// see NumericDisplay
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(value = {byte.class, short.class, int.class, long.class, double.class, float.class}, type = "number")
public @interface NumericSetting {

  Range value() default @Range(min = Double.MIN_VALUE, max = Double.MAX_VALUE);

  double defaultValue() default 0;

}
