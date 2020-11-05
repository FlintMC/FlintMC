package net.labyfy.component.settings.options.dropdown;

import net.labyfy.component.settings.annotation.ui.Description;
import net.labyfy.component.settings.annotation.ui.DisplayName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Selection {

  String value();

  DisplayName display() default @DisplayName({});

  Description description() default @Description({});

}
