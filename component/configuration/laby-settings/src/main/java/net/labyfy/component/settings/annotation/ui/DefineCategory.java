package net.labyfy.component.settings.annotation.ui;

import net.labyfy.chat.annotation.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DefineCategory {

  String name();

  Component[] displayName();

  Component[] description();

  Icon icon() default @Icon("");

}
