package net.labyfy.component.settings.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

  // parsed with the LegacyComponentSerializer or used as a translationKey if translate() == true
  String value();

  boolean translate() default false;

}
