package net.labyfy.component.settings.options.text;

import net.labyfy.component.settings.annotation.ApplicableSetting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApplicableSetting(types = String.class, name = "string")
public @interface StringSetting {

  StringRestriction[] value() default {};

  int maxLength() default Integer.MAX_VALUE;

  // may be useful for something like "https://youtube.com/c/ YOUR_NAME"
  String prefix() default "";

  String suffix() default "";

}
