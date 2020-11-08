package net.flintmc.mcapi.settings.flint.options.numeric.display;

import net.flintmc.mcapi.chat.annotation.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(RepeatableNumericDisplay.class)
public @interface NumericDisplay {

  Component display();

  int value();

}
