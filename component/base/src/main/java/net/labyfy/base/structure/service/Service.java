package net.labyfy.base.structure.service;

import net.labyfy.base.structure.annotation.AutoLoad;
import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.base.structure.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoLoad(priority = -100, round = 0)
public @interface Service {
  Class<?>[] value();

  int priority() default 0;
}
