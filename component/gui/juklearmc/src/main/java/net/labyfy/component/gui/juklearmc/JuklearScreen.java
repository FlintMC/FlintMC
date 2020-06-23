package net.labyfy.component.gui.juklearmc;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;
import net.labyfy.component.gui.screen.ScreenName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transitive
@Identifier
public @interface JuklearScreen {
  ScreenName.Type type() default ScreenName.Type.FROM_MINECRAFT;
  String value();
}
