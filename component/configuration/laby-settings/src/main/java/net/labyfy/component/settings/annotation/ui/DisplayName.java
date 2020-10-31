package net.labyfy.component.settings.annotation.ui;

import net.labyfy.chat.annotation.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// can be used with @Setting or any setting to add a description
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface DisplayName {

  Component[] value();

}
