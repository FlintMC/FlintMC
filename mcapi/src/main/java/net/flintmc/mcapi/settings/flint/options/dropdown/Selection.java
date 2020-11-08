package net.flintmc.mcapi.settings.flint.options.dropdown;

import net.flintmc.mcapi.settings.flint.annotation.ui.Description;
import net.flintmc.mcapi.settings.flint.annotation.ui.DisplayName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Selection {

  String value();

  DisplayName display() default @DisplayName({});

  Description description() default @Description({});

}
