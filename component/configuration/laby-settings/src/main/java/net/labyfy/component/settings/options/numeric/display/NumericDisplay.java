package net.labyfy.component.settings.options.numeric.display;

import net.labyfy.chat.annotation.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(RepeatableNumericDisplay.class)
public @interface NumericDisplay {

  Component display();

  int value();

}
