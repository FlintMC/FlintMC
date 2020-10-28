package net.labyfy.component.settings.options.numeric;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Range {

  NumericRestriction[] value() default {};

  double min() default 0;

  double max();

}
