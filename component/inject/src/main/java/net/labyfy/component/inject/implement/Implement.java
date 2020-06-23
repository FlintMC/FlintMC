package net.labyfy.component.inject.implement;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static net.labyfy.component.processing.autoload.AutoLoadPriorityConstants.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Identifier
@Transitive
@AutoLoad(priority = IMPLEMENT_PRIORITY, round = IMPLEMENT_ROUND)
public @interface Implement {
  Class<?> value();

  String version() default "";
}
