package net.labyfy.component.inject.event;

import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Identifier
@Transitive
public @interface Event {
  Class<?> value();
}
