package net.labyfy.component.settings.annotation.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// class needs to be declared in the class where the "for" part is declared, if not, set declaring()
// sub settings in sub settings don't work
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubSettingsFor {

  String value();

  // if not provided, the class will be the declaring class of the class where the annotation is provided
  Class<?> declaring() default Dummy.class;

  final class Dummy {
    private Dummy() {
    }
  }

}
