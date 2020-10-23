package net.labyfy.component.config.storage;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Identifier
@Transitive
public @interface LoadStorage {

  // higher = loaded first or lower = loaded first?
  int priority() default 0;

}
